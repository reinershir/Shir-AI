package io.github.reinershir.ai.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class HeaderEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8854064380576139428L;
	private String event_id;
    private String token;



}
