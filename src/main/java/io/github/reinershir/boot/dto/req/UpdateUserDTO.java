package io.github.reinershir.boot.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateUserDTO{

	@Schema(description="用户名称")
	private String userName;
	
	@Schema(description="头像")
	private String profile;
	
	private String unionId;
	
	@Schema(description="电话(忘记密码时必填)")
	private String phoneNumber;
	
//	@Schema("微信用户加密的数据")
//	private String encryptedData;
	
	@Schema(description="手机验证码(忘记密码时必填)")
	private String code;
	
	private String iv;
	
	@Schema(description="新密码",required = true)
	private String password;
	@Schema(description="原密码")
	private String oldPassword;
	
}
