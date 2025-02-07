package io.github.reinershir.ai.entity;

import java.util.List;

import com.unfbx.chatgpt.entity.chat.Message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "ai chat request body")
public class WebRequestMessage {

	@Schema(description = "send message")
	private String prompt;
	
	private List<Message> messages;
	
	@Schema(description = "gpt model",nullable = true)
	private String model;
	
	@Schema(description = "mask",nullable = true)
	private String mask;
	
	@Schema(description = "chat session id")
	private String sessionId;
	
	@Schema(description = "enable chat history",nullable = true)
	private Boolean enableContext=true;
	
	@Schema(description = "enable RGA Q&A",nullable = true)
	private Boolean enableRgaContext=false;
	
	@Schema(description = "enable google search chat",nullable = true)
	private Boolean enableGoogleSearch=false;
	
	@Schema(description = "chat with picture if this parameter is specified",nullable = true)
	private String imageUrl;
	
	@Schema(description = "language",nullable = true)
	private String lang;
	
}