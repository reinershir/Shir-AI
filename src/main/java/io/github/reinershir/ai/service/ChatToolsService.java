package io.github.reinershir.ai.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.github.reinershir.ai.dto.ChatToolsDTO;

public interface ChatToolsService {

	/**
	* @Function: ChatToolsService.java
	* @Description: 调用AI聊天工具包括RGA问答、谷歌搜索集成等
	* @param: @param req
	* @return：void
	* @version: v1.0.0
	* @author: ReinerShir
	* @date: 2024年7月26日 下午6:58:16 
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2024年7月26日   ReinerShir       v1.0.0              修改原因
	 */
	void openAIChatStrem(ChatToolsDTO req,SseEmitter sse);
}
