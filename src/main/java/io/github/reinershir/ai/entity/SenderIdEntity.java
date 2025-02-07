package io.github.reinershir.ai.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SenderIdEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5592345457051259406L;
	private String open_id;
    private String union_id;
    private String user_id;

}

