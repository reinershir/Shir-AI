package io.github.reinershir.ai.entity;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OpenAiAmount {

	@Schema(description = "用户余额（美元）")
	private Double totalGranted;
	@Schema(description = "总使用金额（美元）")
	private BigDecimal totalUsed;
	private Double totalAvailable;
}
