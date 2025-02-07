package io.github.reinershir.ai.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;

import cn.hutool.json.JSONUtil;
import io.github.reinershir.ai.entity.WebRequestMessage;
import io.github.reinershir.ai.service.ChatGPTService;
import io.github.reinershir.auth.contract.AuthContract;
import io.github.reinershir.auth.core.support.AuthorizeManager;
import io.github.reinershir.boot.common.Result;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Service
@ServerEndpoint("/websocket/chatgpt/{token}")
public class WebSocketServer {
    private static volatile Integer onlineCount = 0;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    private Session session;

    private String token = null;

    private static ApplicationContext applicationContext;
    private AuthorizeManager authorizeManager;


    public static void setWebSocketServerToApplicationContext(ApplicationContext applicationContext){
        WebSocketServer.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        log.info("get session Id：{}",session.getId());
        this.session = session;
        this.token = token;
        addOnlineCount();
        if(authorizeManager==null) {
        	this.authorizeManager = applicationContext.getBean(AuthorizeManager.class);
        }
        // check token
        Boolean isPassAuth = authorizeManager.validateTokenAndRenewal(token)==AuthContract.AUTHORIZATION_STATUS_SUCCESS?true:false;

        if(token==null|| !isPassAuth) {
    		try {
				sendMessage(JSONUtil.toJsonStr(Result.failed("invalid token")));
                this.onClose();
			} catch (IOException e) {
				log.info("send message falied ！{}",e.getMessage(),e);
			}
    	}else {
    		webSocketSet.add(this);   
            try {
            	sendMessage(JSONUtil.toJsonStr(Result.ok()));
                log.info("client connected :" + token + ",online count:" + getOnlineCount());
            } catch (IOException e) {
                log.error("websocket IO Exception,{}",e.getMessage(),e);
            }
    	}
        
    }

    @OnClose
    public void onClose() {
    	try {
    		if(session.isOpen()) {
    			session.close();
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
        webSocketSet.remove(this);  
        subOnlineCount();           

    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("receive message from : " + token + ",message :" + message);
        WebRequestMessage req = JSON.parseObject(message,WebRequestMessage.class);
        ChatGPTService chatGPTService = applicationContext.getBean(ChatGPTService.class);
        chatGPTService.chatCompletions(req, session.getId(),null,session,"");
    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket exception:{}",error.getMessage(),error);
        error.printStackTrace();
        try {
    		if(session.isOpen()) {
    			sendMessage("ERROR");
    			session.close();
    			subOnlineCount();     
    		}
    		webSocketSet.remove(this); 
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
       
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("token") String token) throws IOException {
        log.info("推送消息到窗口" + token + "，推送内容:" + message);

        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个token的，为null则全部推送
                if (token == null) {
                    item.sendMessage(message);
                } else if (item.token.equals(token)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }

    
}