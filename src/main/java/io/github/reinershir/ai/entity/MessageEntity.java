package io.github.reinershir.ai.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageEntity implements Serializable {


//    private String app_id;
//    private String msg_type;
//    private String open_message_id;
//    private String open_id; //用户id
//    private String text;

    /**
	 * 
	 */
	private static final long serialVersionUID = 6769024834225207722L;

	private String chat_id;

    private String content;
    private String message_id;

}
