package io.github.reinershir.ai.websocket.listener;
import java.util.Objects;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

@Slf4j
public class OpenAISSEEventSourceListener extends EventSourceListener {

    private long tokens;

    private SseEmitter sseEmitter;
    private StringBuffer sb;
    private OpenAISSECallback callback;

    public OpenAISSEEventSourceListener(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
        sb = new StringBuffer();
    }
    
    public OpenAISSEEventSourceListener(SseEmitter sseEmitter,OpenAISSECallback callback) {
        this.sseEmitter = sseEmitter;
        sb = new StringBuffer();
        this.callback = callback; 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("OpenAI sse connected...");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        // log.info("OpenAI repond：{}", data);
        tokens += 1;
        if (data.equals("[DONE]")) {
            log.info("response done ：{}",sb.toString());
            sseEmitter.send(SseEmitter.event()
                    .id("[TOKENS]")
                    .data("<br/><br/>tokens：" + tokens())
                    .reconnectTime(3000));
            sseEmitter.send(SseEmitter.event()
                    .id("[DONE]")
                    .data("[DONE]")
                    .reconnectTime(3000));
            // 传输完成后自动关闭sse
            //sseEmitter.complete();   //可视情况关闭
            if(callback!=null) {
            	//run callback to save messages
            	this.callback.sseCompleteCallback(sb);
            }
            sb=null;
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ChatCompletionResponse completionResponse = mapper.readValue(data, ChatCompletionResponse.class); // 读取Json
        try {
        	String content = completionResponse.getChoices().get(0).getDelta().getContent();
        	if(content!=null) {
        		sb.append(content);
        	}
            sseEmitter.send(SseEmitter.event()
                    .id(completionResponse.getId())
                    .data(completionResponse.getChoices().get(0).getDelta())
                    .reconnectTime(3000));
        } catch (Exception e) {
            log.error("sse send falied！");
            eventSource.cancel();
            e.printStackTrace();
        }
    }


    @Override
    public void onClosed(EventSource eventSource) {
        log.info("流式输出返回值总共{}tokens", tokens() - 2);
        log.info("OpenAI关闭sse连接...");
        sseEmitter.complete();
    }


    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if (Objects.isNull(response)) {
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
        }
        sseEmitter.completeWithError(t);
        eventSource.cancel();
    }

    /**
     * tokens
     * @return
     */
    public long tokens() {
        return tokens;
    }
}