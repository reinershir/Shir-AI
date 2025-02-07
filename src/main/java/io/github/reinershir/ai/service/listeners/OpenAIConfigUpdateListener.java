package io.github.reinershir.ai.service.listeners;

import io.github.reinershir.ai.service.ChatGPTService;
import io.github.reinershir.boot.model.Dictionary;
import io.github.reinershir.boot.service.listener.DictionaryListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenAIConfigUpdateListener implements DictionaryListener{
	
	private ChatGPTService chatGPTService;

	public OpenAIConfigUpdateListener(ChatGPTService chatGPTService) {
		this.chatGPTService = chatGPTService;
	}
	@Override
	public void callback(Dictionary dic) {
		log.info("api config update");
		chatGPTService.updateApiConfigs();
	}

}
