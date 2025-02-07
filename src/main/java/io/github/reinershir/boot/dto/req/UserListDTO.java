package io.github.reinershir.boot.dto.req;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserListDTO extends PageReqDTO{

	@Schema(description = "登陆用户名",  required = false, example = "zhangsan")
	private String loginName;
	@Schema(description = "用户名",  required = false, example = "zhangsan")
	private String nickName;
	@Schema(description = "电话",  required = false, example = "1343434438")
	private String phoneNumber;
	@Schema(description = "状态",name="0=正常，1=禁用",  required = false, example = "1")
	private Integer status;
	@Schema(description = "所属分组ID",  required = false, example = "1")
	private Long groupId;
	
	@Schema(description = "创建开始时间",  required = false, example = "2077-7-7")
	private Date startDate;
	
	@Schema(description = "创建结束时间",  required = false, example = "2077-7-7")
	private Date endDate; 
	
	@Schema(description = "角色ID",  required = false, example = "1")
	private Long roleId;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
	
}
