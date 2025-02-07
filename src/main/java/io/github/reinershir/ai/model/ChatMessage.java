package io.github.reinershir.ai.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Chat Message")
@TableName(value = "CHAT_MESSAGE")
public class ChatMessage {


    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "message")
    private String message;

    @Schema(description = "chatType ：0-ask，1-answer")
    private Integer chatType;

    @Schema(description = "senderId")
    private String senderId;

    @Schema(description = "recipientId")
    private String recipientId;

    @Schema(description = "")
    private String refId;

    private LocalDateTime createTime;

}
