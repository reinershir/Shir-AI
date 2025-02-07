package io.github.reinershir.ai.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SenderEntity implements Serializable {


//    private String app_id;
//    private String msg_type;
//    private String open_message_id;
//    private String open_id; //用户id
//    private String text;

    /**
	 * 
	 */
	private static final long serialVersionUID = 3027942874873836809L;
	private SenderIdEntity sender_id;
}

