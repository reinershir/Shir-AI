package io.github.reinershir.ai.entity;

import java.io.Serializable;

import lombok.Data;
@Data
public class EventEntity implements Serializable {


//    private String app_id;
//    private String msg_type;
//    private String open_message_id;
//    private String open_id; //用户id
//    private String text;


    /**
	 * 
	 */
	private static final long serialVersionUID = -870810128612064188L;
	private MessageEntity message;
    private SenderEntity sender;


  
}
