package io.github.reinershir.boot.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

	@Schema(description = "登陆用户名（手机号）",  required = true, example = "155765489654")
	@NotBlank(message = "登陆名不能为空！")
	private String loginName;
	@Schema(description = "登陆密码",name = "前端需要MD5再传输", required = true, example = "e10adc3949ba59abbe56e057f20f883e")
	@NotBlank(message = "密码不能为空！")
	private String password;
//	@Schema(description = "登陆验证码",  required = true, example = "2g3d")
//	@NotBlank(message = "验证码不能为空！")
//	private String code;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
