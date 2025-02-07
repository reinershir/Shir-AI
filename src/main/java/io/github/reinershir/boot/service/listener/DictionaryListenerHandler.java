package io.github.reinershir.boot.service.listener;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.github.reinershir.boot.contract.DictionaryCodes;
import io.github.reinershir.boot.model.Dictionary;

@Component
public class DictionaryListenerHandler {

	private Map<String,DictionaryListener> listeners = new LinkedHashMap<>();
	
	public void runListeners(Dictionary dic) {
		listeners.keySet().forEach(k->{
			if(DictionaryCodes.OPENAI_API_KEY.name().equals(k)) {
				listeners.get(k).callback(dic);
			}
		});
	}
	
	public void addListener(String code,DictionaryListener listener) {
		listeners.put(code, listener);
	}
}
