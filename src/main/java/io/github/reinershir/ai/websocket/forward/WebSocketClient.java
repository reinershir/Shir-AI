//package io.github.reinershir.ai.websocket.forward;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.jeecg.modules.chatgpt.websocket.entity.SessionPersonality;
//import org.jeecg.modules.chatgpt.websocket.entity.WebSocketGeneralResponse;
//import org.jeecg.modules.chatgpt.websocket.entity.WebSocketResponse;
//import org.jeecg.modules.chatgpt.websocket.server.ForwardPythonWebSocketServer;
//import org.jeecg.modules.chatsession.entity.ProjectUserPersonalityRef;
//import org.jeecg.modules.chatsession.enums.ChatTypeEnum;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson2.JSON;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.text.UnicodeUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import io.github.reinershir.ai.model.ChatMessage;
//import io.github.reinershir.ai.service.ChatMessageService;
//import jakarta.websocket.ClientEndpoint;
//import jakarta.websocket.ContainerProvider;
//import jakarta.websocket.DeploymentException;
//import jakarta.websocket.OnClose;
//import jakarta.websocket.OnError;
//import jakarta.websocket.OnMessage;
//import jakarta.websocket.OnOpen;
//import jakarta.websocket.Session;
//import jakarta.websocket.WebSocketContainer;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@ClientEndpoint
//@Slf4j
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//public class WebSocketClient {
//
//    @Value("${websocket.server.url:ws://8.219.76.162:9960}")
//    private String serverUrl;
//
//
//    private Session session;
//
//
//    public Session getSession(){
//        return this.session;
//    }
//
//    static Map<String,ForwardPythonWebSocketServer> serverMap = new HashMap<>();
//
//    public Map<String, ForwardPythonWebSocketServer> getServerMap() {
//        return this.serverMap;
//    }
//
//    static ChatMessageService chatMessageService;
//
//    @Autowired
//    public void setChatMessageService(ChatMessageService chatMessageService){
//        WebSocketClient.chatMessageService = chatMessageService;
//    }
//
//    private  StringBuffer stringBuffer = new StringBuffer();
//
//    private final  String endStr = "[DONE]";
//
//
//    String[] array = new String[]{"[","]","{","}","]{","}["};
//
//    private StringBuffer emotion = new StringBuffer();
//
//    private StringBuffer emotionalWeight = new StringBuffer();
//
//
//    private boolean isSaveEmotion;
//
//    private boolean isSaveEmotionWeight;
//
//
//    public void init() {
//        try {
//            // 连接python 的websocket接口服务
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            String wsUrl =  serverUrl;
//            URI uri = URI.create(wsUrl);
//            session = container.connectToServer(WebSocketClient.class, uri);
//            System.out.println("打开时间为："+DateUtil.now());
//
//        } catch (DeploymentException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 打开连接
//     * @param session
//     */
//    @OnOpen
//    public void onOpen(Session session) {
//        log.info("打开,py的sessionId为{}",session.getId());
//        this.session = session;
//    }
//
//    /**
//     * 接收消息
//     * @param text
//     */
//    @OnMessage
//    public void onMessage(String text,Session session) {
//
//        try {
//
//            ForwardPythonWebSocketServer server = serverMap.get(session.getId());
//            //将获取到的python websocket数据进行返回
//            String str = UnicodeUtil.toString(text);
//            log.info("python返回结果：{}",str);
//            WebSocketGeneralResponse webSocketGeneralResponse = JSON.parseObject(str,WebSocketGeneralResponse.class);
//            if(webSocketGeneralResponse.getStat().equals("FAIL")){
//                log.info("python异常信息{}",str);
//                server.sendMessage("系统繁忙，请稍后再试");
//                return;
//            }
//            if(StrUtil.isNotBlank(webSocketGeneralResponse.getIntention()) && "setPersonality".equals(webSocketGeneralResponse.getIntention())){
//                if(server.isReturnMsg()){
//                    server.sendMessage(str);
//                }
//                server.setReturnMsg(true);
//                return;
//            }
//
//            String message = webSocketGeneralResponse.getMessage();
//
//            if(!endStr.equals(message)){
//
//                String trimStr = StrUtil.trim(message);
//                switch (trimStr){
//                    case "[": isSaveEmotion = true; break;
//                    case "]{":isSaveEmotion =false; isSaveEmotionWeight = true; break;
//                    case "}":isSaveEmotionWeight =false; break;
//                    case "]":isSaveEmotion =false;break;
//                    case "{": isSaveEmotionWeight = true;break;
//                    case "}[":isSaveEmotion =true; isSaveEmotionWeight = false; break;
//                    default: break;
//                }
//                if(Arrays.asList(array).contains(trimStr)){
//                    return;
//                }
//                if(isSaveEmotion){
//                    emotion.append(message);
//                    return;
//                }
//                if(isSaveEmotionWeight){
//                    emotionalWeight.append(message);
//                    return;
//                }
//
//                stringBuffer.append(message);
//                WebSocketResponse webSocketResponse = new WebSocketResponse();
//                webSocketResponse.setMessage(message);
//                webSocketResponse.setEmotion(emotion.toString());
//                webSocketResponse.setEmotionalWeight(emotionalWeight.toString());
//                server.sendMessage(JSON.toJSONString(webSocketResponse));
//            }else {
//
//
//                //保存聊天信息
//                ChatMessage chatMessage = new ChatMessage();
//                chatMessage.setMessage(stringBuffer.toString());
//                chatMessage.setChatType(ChatTypeEnum.ANSWER.getType());
//                chatMessage.setSenderId(server.getPid());
//                chatMessage.setRecipientId(server.getUid());
//                chatMessage.setRefId(server.getRefId());
//                chatMessage.setBindId(server.getBindId());
//                chatMessageService.save(chatMessage);
//
//                ProjectUserPersonalityRef ref = new ProjectUserPersonalityRef();
//                ref.setId(server.getRefId());
//                chatMessageService.updateRef(ref);
//                log.info("接收到python返回的数据：{}",stringBuffer);
//                stringBuffer = new StringBuffer();
//                server.sendMessage(endStr);
//
//                //将情绪信息置空
//                emotion = new StringBuffer();
//                emotionalWeight = new StringBuffer();
//            }
//
//        } catch (IOException e) {
//            log.error("接收python websocket数据异常，请检查python websocket服务");
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 异常处理
//     * @param throwable
//     */
//    @OnError
//    public void onError(Throwable throwable) {
//        log.error("发生错误:{}",throwable.getMessage(),throwable);
//        throwable.printStackTrace();
//    }
//
//    /**
//     * 关闭连接
//     */
//    @OnClose
//    public void onClosing(Session session) throws IOException {
//        log.info("关闭和python Websocket服务的连接，sessionId为:{}",session.getId());
//        session.close();
//        this.close();
//    }
//
//    /**
//     * 主动发送消息
//     */
//    public void send(String message) throws IOException{
//
//        this.session.getBasicRemote().sendText(message);
//
//    }
//    public void close() throws IOException{
//
//        log.info("当前关闭的sessionId为：{}",this.session.getId());
//        ForwardPythonWebSocketServer server = serverMap.get(session.getId());
//        SessionPersonality sessionPersonality = server.getSessionPersonalityMap().get(server.getSession().getId());
//        if(ObjectUtil.isNotEmpty(sessionPersonality)){
//            sessionPersonality.setIsSet(false);
//        }
//        serverMap.remove(session.getId());
//        this.session.close();
//
//    }
//
//
//}
