package io.github.reinershir.ai.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.github.reinershir.boot.common.ValidateGroups;
import io.github.reinershir.boot.core.query.annotation.QueryRule;
import io.github.reinershir.boot.core.query.annotation.QueryRuleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@TableName("CHAT_SESSION")
public class ChatSession {
	/**
	 * 
	 */
	@TableId(type=IdType.AUTO)
	@Schema(description = "ChatHistory ID")
	private String sessionId;
	
	/**
	 * 
	 */
	@QueryRule(QueryRuleEnum.LIKE)
	@Schema(description = "session Name")
	@NotBlank(message = "sessionName不能为空",groups = {ValidateGroups.AddGroup.class,ValidateGroups.UpdateGroup.class})
	private String sessionName;
	
	/**
	 * 
	 */
	@Schema(description = "user Id")
	private Long userId;
	
	/**
	 * 
	 */
	@TableField("MASK")
	@Schema(description = "mask")
	private String mask;
	
	/**
	 * 
	 */
	@Schema(description = "mask Id")
	private Long maskId;
	
	/**
	 * 
	 */
	@Schema(description = "llm model")
	private String model;
	
	/**
	 * 
	 */
	@TableField("CREATE_TIME")
	@Schema(description = "createTime")
	private Date createTime;
	
}
