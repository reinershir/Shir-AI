package io.github.reinershir.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class ChatToolsDTO {

	private String question;
	
	private String model;
	
	private String method;
	
	private String sessionId;
	
	private Boolean enableContext;
	
	private Long userId;
	
	private String lang;
}
