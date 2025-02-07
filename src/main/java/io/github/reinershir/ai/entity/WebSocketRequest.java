package io.github.reinershir.ai.entity;

import lombok.Data;

@Data
public class WebSocketRequest {

	private String appKey;
	private String device;
	private String sign;
	private String intention;
	private Object data;
	/**
	 * 校验token（用于判断用户是否有权限访问gpt接口）
	 */
	private String checkToken;


	private String pid;


	private String uid;

	private String refId;


	private String projectId;
}
