package io.github.reinershir.boot.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Role")
public class RoleListReqDTO extends PageReqDTO{

	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
