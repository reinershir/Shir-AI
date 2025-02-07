package io.github.reinershir.boot.dto.res;

import java.util.Collection;

import io.github.reinershir.boot.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserListRespDTO extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6820702940572025306L;

	@Schema(description = "角色id")
	private Collection<Long> roleIds;
	
	@Schema(description="所属角色名称",example = "admin")
	private String roleName;
	
	@Schema(description="所属分组名称",example = "电来来——管理组")
	private String groupName;

	public Collection<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Collection<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
}
