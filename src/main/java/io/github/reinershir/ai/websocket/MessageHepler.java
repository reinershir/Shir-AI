package io.github.reinershir.ai.websocket;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.unfbx.chatgpt.entity.chat.BaseMessage.Role;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.utils.TikTokensUtil;

import io.github.reinershir.ai.model.ChatHistory;
import io.github.reinershir.ai.service.ChatHistoryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Data
@Slf4j
public class MessageHepler {

	Map<String, LinkedList<Message>> historyMsg = new LinkedHashMap<>();
	private Map<String, Message> personalityMap = new LinkedHashMap<>();

	@Autowired
	ChatHistoryService chatHistoryService;

	private int getTokenLimitByModel(String model) {
		if (model.toLowerCase().startsWith("gpt-4") || model.toLowerCase().equals("gpt-3.5-turbo-16k")) {
			return 160000;
		}
		return 4000;
	}

	public void cacheContext(String replyContent, String prompt, String openId, String model) {
		LinkedList<Message> message = historyMsg.get(openId);
		String modelName = model.startsWith("gpt-4o") ? ChatCompletion.Model.GPT_4_0613.getName() : model;
		// TODO 暂不支持gpt-4o计算
		int tokens = CollectionUtils.isEmpty(message) ? 0 : TikTokensUtil.tokens(modelName, message);
		int nextTokens = TikTokensUtil.tokens(model, prompt);
		int limit = getTokenLimitByModel(model);
		// 记录历史会话
		if (StringUtils.hasText(replyContent)) {
			if (message == null) {
				message = new LinkedList<>();
			} else if ((tokens + nextTokens) >= limit) {
				log.warn("记录到达上限:{},openid:{}", (tokens += nextTokens), openId);
				// 如果超过了，去掉一部分
//        		List<Message> subList = new ArrayList<>();
//        		int currentTokens = 0;
//        		//计算tokens避免超出
//        		for (int i = message.size()-1;i>=0; i--) {
//        			Message msg = message.get(i);
//        			currentTokens += TikTokensUtil.tokens(modelName, msg.getContent());
//        	   		if(currentTokens<limit) {
//        	   			subList.add(msg);
//        	   		}else {
//        	   			break;
//        	   		}
//        	   	}
//        		message = subList;
				// message = message.subList(message.size()>=15?message.size()-15:0,
				// message.size());
			}
			Message userMsg = new Message();
			userMsg.setRole(Message.Role.USER.getName());
			userMsg.setContent(prompt);
			Message systemMsg = new Message();
			systemMsg.setRole(Message.Role.SYSTEM.getName());
			systemMsg.setContent(replyContent);
			message.add(userMsg);
			message.add(systemMsg);
			historyMsg.put(openId, message);
		}
	}

	public List<Message> getCacheContextByOpenId(String openId, String model, @Nullable String sessionId) {
		// 当前工具不支持gpt-4o计算，因此先换成gpt-4计算token
		String modelName = model.startsWith("gpt-4o") ? ChatCompletion.Model.GPT_4_0613.getName() : model;
		LinkedList<Message> message = historyMsg.get(openId);
		LinkedList<Message> allMessage = new LinkedList<>();
		Message personality = personalityMap.get(openId);
		if (personality != null) {
			allMessage.add(personality);
		}
		int limit = getTokenLimitByModel(model);
		if (!CollectionUtils.isEmpty(message)) {
			// 该工具暂不支持gpt-4o
			int tokens = TikTokensUtil.tokens(modelName, message);
			// 如果超出上下文上限，则重新组装历史记录
			if (tokens > limit) {
				allMessage.clear();
				int nextTokens = 0;
				for (int i = message.size() - 1; i >= 0; i--) {
					Message msg = message.get(i);
					nextTokens += TikTokensUtil.tokens(model, msg.getContent());
					if (nextTokens < limit) {
						allMessage.addFirst(msg);
					} else {
						break;
					}
				}
				if (personality != null) {
					allMessage.addFirst(personality);
				}
				//更新缓存 
				historyMsg.put(openId, allMessage);
			} else {
				// 未超出直接返回缓存
				return message;
			}

		} else if (StringUtils.hasText(sessionId)) {
			log.info("无上下文记录，开始从数据库中获取对话历史上下文... session id:{} ,open id:{}", sessionId, openId);
			List<ChatHistory> historys = chatHistoryService.list(new LambdaQueryWrapper<ChatHistory>()
					.eq(ChatHistory::getSessionId, sessionId).orderByDesc(ChatHistory::getCreateTime).last("limit 15"));
			int tokens = 0;
			for (int i = historys.size() - 1; i >= 0; i--) {
				ChatHistory h = historys.get(i);
				Message userMsg = Message.builder().content(h.getQuestion()).role(Role.USER).build();
				Message systemMsg = Message.builder().content(h.getAnswer()).role(Role.SYSTEM).build();
				allMessage.add(userMsg);
				allMessage.add(systemMsg);
				tokens += TikTokensUtil.tokens(modelName, h.getQuestion());
				tokens += TikTokensUtil.tokens(modelName, h.getAnswer());
				if (tokens < limit) {
					allMessage.add(userMsg);
					allMessage.add(systemMsg);
				} else {
					break;
				}
			}
			//更新缓存
			historyMsg.put(openId, allMessage);
		}
		return allMessage;
	}

	public void setMask(String mask, String openId) {
		if (StringUtils.hasText(mask)) {
			Message userMsg = new Message();
			userMsg.setRole(Message.Role.USER.getName());
			userMsg.setContent(mask);
			log.info("session id:{}, ,mask prompt ：{}", openId, mask);
			personalityMap.put(openId, userMsg);
			if (personalityMap.size() > 50000) {
				log.warn("人设缓存到达限制，将自动清理.");
				personalityMap.clear();
			}

		}
	}
}
