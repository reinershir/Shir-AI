package io.github.reinershir.boot.dto.req;

import java.util.List;

import io.github.reinershir.boot.core.easygenerator.model.FieldInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Code generate DTO")
public class CodeGenerateDTO {

	@NotBlank(message = "package name can not be null")
	@Schema(description="package path name")
	private String packageName;
	
	@NotBlank(message = "model name can not be null")
	private String modelName;
	
	@NotBlank(message = "table name can not be null")
	@Schema(description="table name")
	private String tableName;
	
	@Schema(description="fields")
	private List<FieldInfo> fieldInfos;
	
	@NotBlank(message = "model description can not be null")
	@Schema(description="model description")
	private String modelDescription;
}
