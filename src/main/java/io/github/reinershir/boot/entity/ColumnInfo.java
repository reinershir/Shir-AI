package io.github.reinershir.boot.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ColumnInfo {

	@Schema(description="column name")
	private String columnName;
	
	@Schema(description="query type")
	private String queryType;
}
