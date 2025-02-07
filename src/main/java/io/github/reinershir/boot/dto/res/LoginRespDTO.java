package io.github.reinershir.boot.dto.res;

import java.util.List;

import io.github.reinershir.auth.core.model.Menu;
import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRespDTO {

	@Schema(description = "User ID",  required = true, example = "1")
	private Long id;
	@Schema(description = "nick name",  required = true, example = "zhangsan")
	private String nickName;
	@Schema(description = "login name",  required = true, example = "zhangsan")
	private String loginName;
	@Schema(description = "request token",  required = true, example = "10-asd1e5w4fsd54ew814fsd651")
	private String accessToken;
	
	@Schema(description = "menus", name="Menu permissions for the role the user belongs to, used to construct the left-hand menu",  required = false, example = "[{}]")
	private List<Menu> menus;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	
	
	
}
