package io.github.reinershir.boot.dto.req;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "系统日志参数对象")
public class SystemLogDTO extends PageReqDTO{

	@Schema(description = "用户ID",  required = false, example = "1")
	private Long userId;
	@Schema(description = "开始时间",  required = false, example = "2077-1-18 10:00:00")
	private Date startTime;
	
	@Schema(description = "结束时间",  required = false, example = "2177-1-18 10:00:00")
	private Date endTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
