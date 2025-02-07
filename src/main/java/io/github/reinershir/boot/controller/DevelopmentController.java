package io.github.reinershir.boot.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.core.easygenerator.generator.EasyAutoModule;
import io.github.reinershir.boot.core.easygenerator.generator.MicroSMSCodeGenerator;
import io.github.reinershir.boot.core.easygenerator.model.FieldInfo;
import io.github.reinershir.boot.core.easygenerator.model.GenerateInfo;
import io.github.reinershir.boot.core.international.IMessager;
import io.github.reinershir.boot.dto.req.CodeGenerateDTO;
import io.github.reinershir.boot.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("development")
@RestController
@Tag(description = "Development management",name = "Development management")
@PermissionMapping(value="DEVELOPMENT")
public class DevelopmentController {
	
	@Autowired
	MicroSMSCodeGenerator codeGenerator;
	
	@Permission(name = "Get Table columns",value = OptionType.LIST)
	@Operation(summary = "Get Table columns",description = "Get Table columns")
	@Parameter(name = "tableName",required = false,description = "Parent menu id")
	@GetMapping("/codeGenerate/{tableName}/columns")
	public Result<List<FieldInfo>> getColumns(@PathVariable(value="tableName",required = true) String tableName){
		try {
			return Result.ok(codeGenerator.getTableFields(tableName));
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return Result.failed(IMessager.getMessageByCode("message.error"));
		}                                                   
	}

	@Permission(name = "Code generate",value = OptionType.LIST)
	@Operation(summary = "Code generate",description = "Generate front-end and back-end codes")
	@PostMapping("/codeGenerate/codes") 
	public void generate(@RequestBody @Validated CodeGenerateDTO dto,HttpServletResponse response){
		GenerateInfo generateInfo = new GenerateInfo(dto.getTableName(),dto.getModelName(),dto.getModelDescription());
		generateInfo.setFieldInfos(dto.getFieldInfos());
		try {
			// 将生成好的代码打包成ZIP并下载
			String zipPath = codeGenerator.generateCodeToZip(dto.getPackageName(), dto.getPackageName(), EasyAutoModule.values(),generateInfo);
			InputStream inputStream = new FileInputStream(zipPath);
	        OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=codes.zip");
            
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(IMessager.getMessageByCode("message.error"));
		}
	}
}
