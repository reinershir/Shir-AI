package io.github.reinershir.ai.websocket.listener;

public interface OpenAISSECallback {

	public void sseCompleteCallback(StringBuffer messages);
}
