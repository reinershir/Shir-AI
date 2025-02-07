package io.github.reinershir.ai.websocket;

import java.io.IOException;
import java.util.Objects;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;

import io.github.reinershir.ai.entity.CompletionContent;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;

@Slf4j
public class OnCompletionEvebntListener extends ConsoleEventSourceListener{

	private Session session;
	private StringBuffer sb;
    
	public OnCompletionEvebntListener(Session session) {
		this.session=session;
		sb = new StringBuffer();
	}
	
	 @Override
	    public void onEvent(EventSource eventSource, String id, String type, String data) {
	        log.debug("receive OpenAI reponse ：{}", data);
	        try {
				if (data.equals("[DONE]")) {
					session.getBasicRemote().sendText(data);
		            log.info("response done ：{}",sb.toString());
		            sb=null;
		            sb = new StringBuffer();
		            return;
		        }else {
		        	CompletionContent json = JSON.parseObject(data,CompletionContent.class);
		        	session.getBasicRemote().sendText(data);
		        	String content = json.getChoices().get(0).getText();
		        	if(content!=null) {
		        		sb.append(content);
		        	}
		        }
			} catch (IOException e) {
				log.error("websocket error！msg:{}",e.getMessage(),e);
			}catch(JSONException e2) {
				log.error("json parse error ！ {}",e2.getMessage(),e2);
			}
	        
	    }
	 
	 @Override
	    public void onFailure(EventSource eventSource, Throwable t, Response response) {
	        super.onFailure(eventSource, t, response);
	        try {
	        	if(session.isOpen()) {
	        		session.getBasicRemote().sendText("TIMEOUT");
	        		//session.close();
	        		//WebSocketServer.subOnlineCount();
	        		return;
	        	}
	        	ResponseBody body = response.body();
	            if (Objects.nonNull(body)) {
	            	JSONObject error = JSONObject.parseObject(body.toString());
	                if(null!=error.get("error")) {
	                	JSONObject err = error.getJSONObject("error");
	                	String code = err.getString("code");
	                	if(code!=null&&"account_deactivated".equals(code)) {
	                		log.error("account deactivated！");
	                	}
	                }
	            }
			} catch (IOException e) {
				log.error("websocket exception {}",e.getMessage(),e);
			}catch (JSONException e) {
				log.error("can not parse json {}",e.getMessage(),e);
			}
	        
	    }
}
