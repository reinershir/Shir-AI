//package io.github.reinershir.ai.websocket;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson2.JSON;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.json.JSONUtil;
//import io.github.reinershir.ai.entity.WebSocketRequest;
//import io.github.reinershir.ai.websocket.forward.WebSocketClient;
//import jakarta.websocket.OnClose;
//import jakarta.websocket.OnError;
//import jakarta.websocket.OnMessage;
//import jakarta.websocket.OnOpen;
//import jakarta.websocket.Session;
//import jakarta.websocket.server.PathParam;
//import jakarta.websocket.server.ServerEndpoint;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Slf4j
//@Service
//@ServerEndpoint("/websocket/gpt/py/{uid}")
//@Data
//public class ForwardPythonWebSocketServer {
//    //静态变量，用来记录当前在线连接数。 
//    private static volatile Integer onlineCount = 0;
//    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//    private static CopyOnWriteArraySet<ForwardPythonWebSocketServer> webSocketSet = new CopyOnWriteArraySet<ForwardPythonWebSocketServer>();
//
//    //与某个客户端的连接会话，需要通过它来给客户端发送数据
//    public Session session;
//
//    //设置人设是否需要返回信息
//    private boolean isReturnMsg = true;
//
//    //用户ID
//    private String uid;
//
//    //人设ID
//    private String pid;
//
//    private String refId;
//
//    private String bindId;
//
//
//
//    WebSocketClient webSocketClient;
//
//
//    private static ApplicationContext applicationContext;
//
//
//    public static void setWebSocketServerToApplicationContext(ApplicationContext applicationContext){
//        ForwardPythonWebSocketServer.applicationContext = applicationContext;
//    }
//
//
//
//    /**
//     * 连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("uid") String uid) {
//        log.info("获取到的sessionId为：{}",session.getId());
//        this.session = session;
//        this.uid = uid;
//        addOnlineCount();
//		webSocketSet.add(this);     //加入set中
//        try {
//            webSocketClient = applicationContext.getBean(WebSocketClient.class);
//            if(ObjectUtil.isEmpty(webSocketClient.getSession()) || !webSocketClient.getSession().isOpen()){
//                webSocketClient.init();
//                webSocketClient.getServerMap().put(webSocketClient.getSession().getId(),this);
//            }
//            sendMessage(JSONUtil.toJsonStr(WebSocketGeneralResponse.success("open")));
//            log.info("有新窗口开始监听:" + uid + ",当前在线人数为:" + getOnlineCount()+" ");
//        } catch (IOException e) {
//            log.error("websocket IO Exception,{}",e.getMessage(),e);
//        }
//        
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose() {
//    	try {
//    		if(session.isOpen()) {
//    			session.close();
//    		}
//            if(ObjectUtil.isNotEmpty(webSocketClient.getSession()) && webSocketClient.getSession().isOpen()){
//                webSocketClient.close();
//            }
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        webSocketSet.remove(this);  //从set中删除
//        subOnlineCount();           //在线数减1
//
//
//        //断开连接情况下，更新主板占用情况为释放
//        log.info("释放的uid为："+uid);
//        //这里写你 释放的时候，要处理的业务
//        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
//
//    }
//    
//
//    /**
//     * 收到客户端消息后调用的方法
//     * @ Param message 客户端发送过来的消息
//     */
//    @OnMessage
//    public void onMessage(String message) {
//        log.info("收到来自请求py窗口" + uid + "的信息:" + message);
//
//        try {
//
//            WebSocketRequest req = JSON.parseObject(message,WebSocketRequest.class);
//            Handler handler = applicationContext.getBean(Handler.class);
//            if(ObjectUtil.isEmpty(webSocketClient.getSession()) || !webSocketClient.getSession().isOpen()){
//                this.isReturnMsg = false;
//                webSocketClient.init();
//                webSocketClient.getServerMap().put(webSocketClient.getSession().getId(),this);
//            }
//            handler.pyHandleRequest(webSocketClient,req,sessionPersonalityMap);
//        	
//		} catch (Exception e) {
//			log.error("解析消息内容时出错！uid:{},内容：{}",uid,message);
//			log.error("解析消息内容时出错！异常信息：{}",e.getMessage(),e);
//			try {
//				sendMessage("无效的消息格式！");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//				onClose();
//			}
//		}
//        
//    }
//
//    /**
//     * @ Param session
//     * @ Param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("发生错误:{}",error.getMessage(),error);
//        error.printStackTrace();
//        try {
//    		if(session.isOpen()) {
//    			sendMessage("ERROR");
//    			session.close();
//    			subOnlineCount();           //在线数减1
//    		}
//    		webSocketSet.remove(this);  //从set中删除
//	        
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//       
//    }
//
//    /**
//     * 实现服务器主动推送
//     */
//    public void sendMessage(String message) throws IOException {
//        this.session.getBasicRemote().sendText(message);
//    }
//
//
//
//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
//
//    public static synchronized void addOnlineCount() {
//        ForwardPythonWebSocketServer.onlineCount++;
//    }
//
//    public static synchronized void subOnlineCount() {
//        ForwardPythonWebSocketServer.onlineCount--;
//    }
//
//    public static CopyOnWriteArraySet<ForwardPythonWebSocketServer> getWebSocketSet() {
//        return webSocketSet;
//    }
//
//
//
//    
//    
//}