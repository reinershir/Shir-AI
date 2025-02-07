package io.github.reinershir.boot.dto.req;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@ApiResponse(description = "reset password DTO")
public class ResetPasswordDTO {

	private Long user;
	private String newPassword;
	public Long getUser() {
		return user;
	}
	public void setUser(Long user) {
		this.user = user;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
