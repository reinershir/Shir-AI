package io.github.reinershir.boot.dto.req;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import io.github.reinershir.boot.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserReqDTO extends User{

	@TableField(exist = false)
	@Schema(description = "The role ID to which this user is to be bound",  nullable = true, example = "[1,2,10]")
	private List<Long> roleIds;
	public List<Long> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	
	
	
}
