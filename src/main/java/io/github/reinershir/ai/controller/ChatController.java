package io.github.reinershir.ai.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.im.v1.enums.MsgTypeEnum;
import com.lark.oapi.service.im.v1.enums.ReceiveIdTypeEnum;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import com.lark.oapi.service.im.v1.model.ext.MessageText;
import com.unfbx.chatgpt.entity.chat.Message;

import cn.hutool.core.util.XmlUtil;
import io.github.reinershir.ai.dto.ChatToolsDTO;
import io.github.reinershir.ai.entity.EventEntity;
import io.github.reinershir.ai.entity.PullMessageEntity;
import io.github.reinershir.ai.entity.Rs;
import io.github.reinershir.ai.entity.WebRequestMessage;
import io.github.reinershir.ai.service.ChatGPTService;
import io.github.reinershir.ai.service.ChatToolsService;
import io.github.reinershir.ai.tools.Decrypt;
import io.github.reinershir.ai.tools.wechat.AesException;
import io.github.reinershir.ai.tools.wechat.WXBizMsgCrypt;
import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.auth.core.support.AuthorizeManager;
import io.github.reinershir.auth.entity.TokenInfo;
import io.github.reinershir.boot.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Schema(description =  "ChatGPT请求接口")
@RequestMapping({"/gpt/chat"})
@Slf4j
@PermissionMapping("AICHAT")
public class ChatController {

	@Autowired
	ChatGPTService chatGPTService;
	@Autowired
	ChatToolsService chatToolsService;
	@Autowired(required = false)
	AuthorizeManager authorizeManager;

	private Cache<String,SseEmitter> cache;
	
	ExecutorService executorService = Executors.newFixedThreadPool(48);
	
	public ChatController() {
		cache = CacheBuilder.newBuilder()
        .expireAfterWrite(240, TimeUnit.SECONDS)
        .build();
	}
	
	@Value("${thirdParty.feishu.appId}")
	private String appId;
	@Value("${thirdParty.feishu.appSecret}")
	private String appSecret;
	@Value("${thirdParty.feishu.encryptKey}")
	private String encryptKey;
	
	@Value("${thirdParty.wechat.secret}")
	private String wechatSecret;
	@Value("${thirdParty.wechat.agentId}")
	private String wechatAgentId;
	@Value("${thirdParty.wechat.token}")
	private String wechatToken;
	@Value("${thirdParty.wechat.encodingAESKey}")
	private String encodingAESKey;
	@Value("${thirdParty.wechat.corpid}")
	private String corpid;
	
	@Permission(value = OptionType.SKIP)
	@ResponseBody
	@Operation(summary = "请求chatGPT对话（单次）", description = "请求chatGPT对话（单次）")
	@GetMapping("")
	public Result<String> askOne(@RequestParam("question") String question,HttpServletRequest request){
		return Result.ok(chatGPTService.chat(question, request.getSession().getId(),null));
	}
	
	@Permission(value = OptionType.LOGIN)
	@ResponseBody
	@Operation(summary = "connect chatGPT server", description = "connect chatGPT server")
	@GetMapping(value="connect",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connect( HttpServletRequest request) {
		String token = authorizeManager.getTokenInfo(request).getUserId();
		//String token = request.getHeader("Access-Token");
		//默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0l);
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.info("[{}]连接异常,{}", request.getSession().getId(), throwable.toString());
                        sseEmitter.send(SseEmitter.event()
                                .id(token)
                                .name("error！")
                                .data(Message.builder().content("error！").build())
                                .reconnectTime(8000));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(8000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(cache.getIfPresent(token) !=null) {
        	cache.invalidate(token);
        }
        cache.put(token, sseEmitter);
        log.info("[{}]创建sse连接成功！", token);
        return sseEmitter;
	}
	
	@Permission(value = OptionType.ALL)
	@ResponseBody
	@Operation(summary = "OPENAI chatGPT stream", description = "OPENAI chatGPT stream")
	@PostMapping
	public Result<String> chat(@RequestBody WebRequestMessage requestDTO,HttpServletRequest request){
		String token = authorizeManager.getTokenInfo(request).getUserId();
		//String token = request.getHeader("Access-Token");
		SseEmitter sseEmitter = cache.getIfPresent(token);
		log.info("sse =======================> {}",sseEmitter);
		if(sseEmitter==null) {
			log.warn("无法获取到sse对象，TOKEN:{}",token);
			return Result.failed("connect falied");
		}
		TokenInfo tokenInfo = authorizeManager.getTokenInfo(request);
		Long userId = null;
		if(tokenInfo!=null) {
			userId = Long.parseLong(tokenInfo.getUserId());
		}else {
			userId= 0l;
		}
		if(!StringUtils.hasText( requestDTO.getSessionId())) {
			requestDTO.setSessionId(request.getSession().getId());
		}
		// request chat tools
		if(requestDTO.getEnableRgaContext()||requestDTO.getEnableGoogleSearch()) {
			ChatToolsDTO dto = new ChatToolsDTO();
			dto.setEnableContext(requestDTO.getEnableContext());
			dto.setMethod(requestDTO.getEnableGoogleSearch()?"google":"");
			dto.setModel(requestDTO.getModel());
			dto.setQuestion(requestDTO.getPrompt());
			dto.setSessionId(requestDTO.getSessionId());
			dto.setUserId(userId);
			dto.setLang(requestDTO.getLang());
			chatToolsService.openAIChatStrem(dto, sseEmitter);
		}else if(StringUtils.hasText(requestDTO.getImageUrl())){
			chatGPTService.chatWithPicture(requestDTO, 
					StringUtils.hasText(requestDTO.getSessionId())?requestDTO.getSessionId():request.getSession().getId(), sseEmitter,userId);
		}
		else {
			chatGPTService.chatCompletions(requestDTO, 
					StringUtils.hasText(requestDTO.getSessionId())?requestDTO.getSessionId():request.getSession().getId(), sseEmitter,userId);
		}
		return Result.ok();
	}
	
	
	
	/**
     * 功能描述: 用户提问OpenAi，返回答案
     */
	@Permission(value = OptionType.SKIP)
	@ResponseBody
    @Operation(summary = "feishu提问接口", description = "feishu提问接口")
    @RequestMapping(path = "/pull/message", method = RequestMethod.POST)
    public Rs<Object> openAiChat(@RequestBody PullMessageEntity pullMessage) {
        log.info("pull message --> " + com.alibaba.fastjson2.JSON.toJSONString(pullMessage));
        //第一次需要验证加密
        if(StringUtils.hasText(pullMessage.getEncrypt())) {
        	Decrypt d = new Decrypt(encryptKey);
        	try {
        		//返回解密后的内容
        		JSONObject json = JSONObject.parse(d.decrypt(pullMessage.getEncrypt()));
        		log.info("解密后参数：{}",json);
        		if(StringUtils.hasText(json.getString("challenge"))) {
        			pullMessage.setChallenge(json.getString("challenge"));
        		}
        		EventEntity event = json.getJSONObject("event")!=null?json.getJSONObject("event").to(EventEntity.class):null;
        		//将解密后的参数转换为实体类
        		if(event!=null) {
        			pullMessage.setEvent(event);
        		}
			} catch (Exception e) {
				e.printStackTrace();
				 pullErrorMessage(pullMessage, e.getMessage());
	             log.error(e.getMessage());
			} 
        	
        }
        executorService.submit(() -> {
            log.info("异步线程 =====> 开始 =====> " + new Date());
            try {
                getRs(pullMessage);
            } catch (Exception e) {
            	e.printStackTrace();
                pullErrorMessage(pullMessage, e.getMessage());
                log.error(e.getMessage());
            }
            log.info("异步线程 =====> 结束 =====> " + new Date());
        });
        return new Rs<>(200, "成功", null, pullMessage.getChallenge());
    }

    private void pullErrorMessage(PullMessageEntity pullMessage, String errorMessage) {
        String chatId = pullMessage.getEvent().getMessage().getChat_id(); //聊天Id
        String replyContentFormat = errorMessage.replaceAll("\n", " ")
                .replaceAll("\"", "");
        System.out.println(replyContentFormat);
        // 构建client
        Client client = Client.newBuilder(appId, appSecret).build();

        // 使用Builder模式构建请求对象
        CreateMessageReq req = CreateMessageReq.newBuilder()
                .receiveIdType(ReceiveIdTypeEnum.CHAT_ID.getValue())
                .createMessageReqBody(CreateMessageReqBody.newBuilder()
                        .receiveId(chatId)
                        .msgType(MsgTypeEnum.MSG_TYPE_TEXT.getValue())
                        .content(MessageText.newBuilder()
                                .text("ChatGPT机器人摆烂了，请稍后再试～!  ")
                                .build())
                        .build())
                .build();

        try {
            // 发起请求，并处理结果
            CreateMessageResp resp = client.im().message().create(req);
            if (resp.getCode() != 0) {
                System.out.println(String.format("code:%d,msg:%s,err:%s"
                        , resp.getCode(), resp.getMsg(), Jsons.DEFAULT.toJson(resp.getError())));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Nullable
    private void getRs(PullMessageEntity pullMessage) throws Exception {
        EventEntity event = pullMessage.getEvent();

        String textContent = JSONObject.parseObject(event.getMessage().getContent()).getString("text");

        String openId = event.getSender().getSender_id().getOpen_id(); //用户Id
        log.info("获取到feishu用户提问：{}",textContent);
        String replyContent = chatGPTService.chat(textContent, openId,null);
        log.info("AI返回飞书用户内容：{}",replyContent);
        String chatId = event.getMessage().getChat_id(); //聊天Id

        String replyContentFormat = replyContent.replaceAll("\n", "\\\\n")
                .replaceAll("\"", "");
        // 构建client
        Client client = Client.newBuilder(appId, appSecret).build();

        // 使用Builder模式构建请求对象
        CreateMessageReq req = CreateMessageReq.newBuilder()
                .receiveIdType(ReceiveIdTypeEnum.CHAT_ID.getValue())
                .createMessageReqBody(CreateMessageReqBody.newBuilder()
                        .receiveId(chatId)
                        .msgType(MsgTypeEnum.MSG_TYPE_TEXT.getValue())
                        .content(MessageText.newBuilder()
                                .atUser(openId, "chat-gpt")
                                .text(replyContentFormat)
                                .build())
                        .build())
                .build(); 

        // 发起请求，并处理结果
        CreateMessageResp resp = client.im().message().create(req);
        if (resp.getCode() != 0) {
            log.error(String.format("code:%d,msg:%s,err:%s"
                    , resp.getCode(), resp.getMsg(), Jsons.DEFAULT.toJson(resp.getError())));
        }
    }
    
    /**
    * @Function: ChatController.java
    * @Description: 企业微信对话回调验证方法
    * @param: @param msg_signature
    * @param: @param timestamp
    * @param: @param nonce
    * @param: @param echostr
    * @param: @return
    * @return：String
    * @version: v1.0.0
    * @author: ReinerShir
    * @date: 2024年4月30日 下午5:55:35 
    * Modification History:
    * Date         Author          Version            Description
    *---------------------------------------------------------*
    * 2024年4月30日   ReinerShir       v1.0.0              修改原因
     */
    //@GetMapping("wechat")
    private String validateWechat(String msg_signature,String timestamp,
    		String nonce,String echostr) {
    	WXBizMsgCrypt wxcpt;
		try {
			wxcpt = new WXBizMsgCrypt(wechatToken, encodingAESKey, corpid);
		} catch (AesException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		try {
			String sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp,
					nonce, echostr);
			log.info("verifyurl echostr: " + sEchoStr);
			// 验证URL成功，将sEchoStr返回
			return sEchoStr;
		} catch (Exception e) {
			//验证URL失败，错误原因请查看异常
			e.printStackTrace();
			return e.getMessage();
		}
    }
    
    /**
    * @Function: ChatController.java
    * @Description: 企业微信接收消息接口
    * @param: @param xml
    * @param: @param msg_signature
    * @param: @param timestamp
    * @param: @param nonce
    * @param: @return
    * @return：String
    * @version: v1.0.0
    * @author: ReinerShir
     * @throws AesException 
    * @date: 2024年4月30日 下午6:03:49 
    * Modification History:
    * Date         Author          Version            Description
    *---------------------------------------------------------*
    * 2024年4月30日   ReinerShir       v1.0.0              修改原因
     */
    @Permission(value = OptionType.SKIP)
    @ResponseBody
    @RequestMapping(value="wechat",produces = MediaType.TEXT_XML_VALUE)
    public String wechat(@RequestBody(required = false) String xml, @RequestParam("msg_signature") String msg_signature,@RequestParam("timestamp") String timestamp,
    		@RequestParam("nonce") String nonce,@RequestParam(value="echostr",required = false) String echostr) throws Exception {
    	WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(wechatToken, encodingAESKey, corpid);
    	//首次需要验证
    	if(!StringUtils.hasText(xml)) {
    		return validateWechat(msg_signature, timestamp, nonce, echostr);
    	}
    	log.debug("企业微信提问接口接收加密前参数：{}",xml);
    	try {
			String sMsg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, xml);
			// 解析出明文xml标签的内容进行处理
			log.debug("企业微信提交解析后的明文：{}",sMsg);
			// For example:
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(sMsg);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName("Content");
			String Content = nodelist1.item(0).getTextContent();
			String fromUserName = root.getElementsByTagName("FromUserName").item(0).getTextContent();
			log.info("接收到企业微信提交请求，Content：{}",Content);
			String replyContent = chatGPTService.chat(Content, fromUserName,null);
 
	        String replyContentFormat = replyContent.replaceAll("\n", "\\\\n")
	                .replaceAll("\"", "");
	        //String sRespData = "<xml><ToUserName><![CDATA[mycreate]]></ToUserName><FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content><MsgId>1234567890123456</MsgId><AgentID>128</AgentID></xml>";
			
	        Map<String,String> map = new HashMap<String, String>();
	        //String timeStr = new Date().getTime()+"";
	        map.put("ToUserName", fromUserName);
	        map.put("FromUserName", corpid);
	        map.put("CreateTime", timestamp);
	        map.put("MsgType", "text");
	        map.put("Content", replyContentFormat);
	        String responseXml = XmlUtil.mapToXmlStr(map,true);
	        //加密返回内容
	        String sEncryptMsg = wxcpt.EncryptMsg(responseXml, timestamp, nonce);
	        log.debug("最终返回加密内容：{}",sEncryptMsg);
	        return sEncryptMsg;
    	} catch (Exception e) {
			log.error("处理企业微信提交接口出错：{}",e.getMessage(),e);
			throw e;
		}
    }
    
}