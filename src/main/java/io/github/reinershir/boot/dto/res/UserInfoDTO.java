package io.github.reinershir.boot.dto.res;

import java.util.List;

import io.github.reinershir.boot.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User info")
public class UserInfoDTO extends User{

	private List<String> roles;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
}
