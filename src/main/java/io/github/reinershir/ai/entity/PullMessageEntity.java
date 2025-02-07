package io.github.reinershir.ai.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class PullMessageEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9069264258325639435L;
	private String uuid;
    private String challenge = "challenge";
    private String encrypt;
    private HeaderEntity header;
    private EventEntity event;

}