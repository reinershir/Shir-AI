package io.github.reinershir.boot.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description =  "Move menu DTO")
public class MenuMoveDTO {

	@NotNull
	@Schema(description = "ID of the menu you want to move",  nullable = false, example = "1")
	private Long moveId;
	
	@NotNull
	@Schema(description = "target menu id",nullable = false, example = "11")
	private Long targetId;
	
	@NotNull
	@Schema(description = "Move to the position of the target menu,1=in front of the target, 2=behind the target, 3=last of the target children.", nullable = false, example = "1")
	private int position;

	public Long getMoveId() {
		return moveId;
	}

	public void setMoveId(Long moveId) {
		this.moveId = moveId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	

	
	
	
}
