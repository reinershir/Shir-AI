package io.github.reinershir.boot.dto.req;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UpdatePasswordDTO {

	@Schema(description = "原密码",  required = true, example = "zhangsan")
	@NotBlank(message = "原密码不能为空！")
	private String password;
	
	@Schema(description = "新密码",  required = true, example = "zhangsanNo2")
	@NotBlank(message = "新密码不能为空！")
	private String newPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
