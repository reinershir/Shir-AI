package io.github.reinershir.ai.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.unfbx.chatgpt.entity.chat.Message;

import io.github.reinershir.ai.dto.ChatToolsDTO;
import io.github.reinershir.ai.service.ChatHistoryService;
import io.github.reinershir.ai.service.ChatToolsService;
import io.github.reinershir.ai.websocket.MessageHepler;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

@Slf4j
@Service
public class ChatToolsServiceImpl implements ChatToolsService{
	
	@Value("${thirdParty.chatToolsService.url}")
	private String chatToolUrl;

	@Autowired
	MessageHepler messageHepler;
	
	@Autowired
	ChatHistoryService chatHistoryService;
	@Override
	public void openAIChatStrem(ChatToolsDTO req,SseEmitter sse) {
		// 设置请求参数

		if(!StringUtils.hasText(req.getModel())) {
			req.setModel("gpt-3.5-turbo-0613");
		}
		// 创建请求体
		RequestBody requestBody = new FormBody.Builder()
			      .add("question", req.getQuestion())
			      .add("model", req.getModel())
			      .add("method", req.getMethod())
			      .add("lang", req.getLang())
			      .add("sessionId", req.getSessionId())
			      .build();

		// 创建请求对象
		Request request = new Request.Builder()
		        .url(chatToolUrl+"/chatStream")
		        .post(requestBody) // 请求体
		        .addHeader("Authorization", "Bearer {token}")
		        .addHeader("Accept", "text/event-stream")
		        .build();

		// 开启 Http 客户端
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
		        .connectTimeout(10, TimeUnit.SECONDS)   // 建立连接的超时时间
		        .readTimeout(30, TimeUnit.MINUTES)  // 建立连接后读取数据的超时时间
		        .build();
		
		StringBuffer message = new StringBuffer();
		// 实例化EventSource，注册EventSource监听器 -- 创建一个用于处理服务器发送事件的实例，并定义处理事件的回调逻辑
		RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {

		    @Override
		    public void onEvent(EventSource eventSource, String id, String type, String data) {
		        System.out.println(data);   // 请求到的数据
		        message.append(data);
		        try {
		        	if ("[DONE]".equals(data)) {
		        		sse.send(SseEmitter.event()
		                        .id("[DONE]")
		                        .data("[DONE]")
		                        .reconnectTime(3000));
			        	sse.complete();
			        	chatHistoryService.saveChatHistory(req.getSessionId(),req.getModel(),
			        			req.getQuestion(),"rga", message,req.getUserId() , new ArrayList<>());
			        	//TODO python模块暂未支持历史记录
//			        	if(req.getEnableContext()) {
//			        		//cache 
//			        		messageHepler.cacheContext(message.toString(), req.getQuestion(), req.getSessionId(),req.getMethod());
//			        	}
			        }else {
			        	Message sendMessage = new Message();
			        	sendMessage.setContent(data);
						sse.send(SseEmitter.event()
			                    .id(id)
			                    .data(sendMessage)
			                    .reconnectTime(3000));
			        }
		        	
				} catch (IOException e) {
					log.error("send sse data error :"+e.getMessage(),e);
					sse.completeWithError(e);
				}
		        
		    }

			@Override
			public void onClosed(EventSource eventSource) {
				System.out.println("sse on closed "+eventSource.request().url());
				sse.complete();
				super.onClosed(eventSource);
			}

			@Override
			public void onFailure(EventSource eventSource, Throwable t, Response response) {
				log.error("sse request failed :"+response.message(),t);
				
				try {
					sse.send("Opps ! It looks like the robot has gone to Mars...");
					sse.send("[DONE]");
				} catch (IOException e) {
					e.printStackTrace();
				}
				sse.completeWithError(t);
				super.onFailure(eventSource, t, response);
			}
		    
		    

		});

		// 与服务器建立连接
		realEventSource.connect(okHttpClient);
		// realEventSource.request();
	}

}
