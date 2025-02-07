package io.github.reinershir.ai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import com.dingtalk.open.app.api.models.bot.ChatbotMessage;
import com.dingtalk.open.app.api.models.bot.MessageContent;

import io.github.reinershir.ai.service.ChatGPTService;
import io.github.reinershir.ai.service.dingding.RobotGroupMessagesService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatbotCallbackListener implements OpenDingTalkCallbackListener<ChatbotMessage, JSONObject>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1828925804135049395L;
	private RobotGroupMessagesService robotGroupMessagesService;
	@Autowired
    ChatGPTService chatGPTService;

    @Autowired
    public ChatbotCallbackListener(RobotGroupMessagesService robotGroupMessagesService) {
        this.robotGroupMessagesService = robotGroupMessagesService;
    }
    
	@Override
	public JSONObject execute(ChatbotMessage message) {
		try {
            MessageContent text = message.getText();
            if (text != null) {
                String msg = text.getContent();
                log.info("receive bot message from user={}, msg={}", message.getSenderId(), msg);
                String openConversationId = message.getConversationId();
                try {
                	String responseText = chatGPTService.chat(msg, message.getSenderId(), null);
                    //发送机器人消息
                    robotGroupMessagesService.send(openConversationId, responseText);
                } catch (Exception e) {
                    log.error("send group message by robot error:" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("receive group message by robot error:" + e.getMessage(), e);
        }
        return new JSONObject();
	}

}
