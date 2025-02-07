package io.github.reinershir.ai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.callback.DingTalkStreamTopics;
import com.dingtalk.open.app.api.security.AuthClientCredential;

@Configuration
public class DingDingOpenAPIConfig {
	
	@Value("${thirdParty.dingding.clientId}")
	String clientId;
	@Value("${thirdParty.dingding.clientSecret}")
	String clientSecret;

//	@Bean
//	public OpenDingTalkClient initDingDingBuilder(@Autowired ChatbotCallbackListener chatbotCallbackListener) throws Exception {
//		OpenDingTalkClient client = OpenDingTalkStreamClientBuilder
//        .custom()
//        .credential(new AuthClientCredential(clientId,clientSecret))
//        //注册机器人监听器
//        .registerCallbackListener(DingTalkStreamTopics.BOT_MESSAGE_TOPIC, chatbotCallbackListener).build();
//		client.start();
//		return client;
//	}
}
