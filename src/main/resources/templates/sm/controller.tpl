package ${commonPath}.controller;

import java.io.Serializable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import io.github.reinershir.boot.common.ValidateGroups;
import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;

import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.common.BaseController;
import io.github.reinershir.boot.core.query.QueryHelper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${modelPackage}.${ClassName};
import ${commonPath}.service.${ClassName}Service;


 /**
 * ${ClassName} Controller Generate by Shir-boot
 * @author Shir-Boot
 * @Date ${.now}
 * @version 1.0
 *
 */
@RestController
@RequestMapping("${ClassName?uncap_first}")
@Tag(description = "${modelDescription}<@messages code='doc.api.description.management'/>",name = "${modelDescription}")
@PermissionMapping(value="${ClassName?upper_case}")
public class ${ClassName}Controller extends BaseController{
 
	@Autowired
	private ${ClassName}Service ${ClassName?uncap_first}Service;
	
	@Permission(name = "${modelDescription}<@messages code='doc.api.description.list'/>",value = OptionType.LIST)
	@Operation(summary="${modelDescription}<@messages code='doc.api.description.list'/>", description = "${modelDescription}<@messages code='doc.api.description.list'/>")
	@Parameters({
		@Parameter(name="pageNo",description="Now page",required = true,in = ParameterIn.QUERY),
		@Parameter(name="pageSize",description="Page size",required = true,in = ParameterIn.QUERY),
	})
	@GetMapping
	public Result<IPage<${ClassName}>> queryPageList(${ClassName} entity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<${ClassName}> queryWrapper = QueryHelper.initQueryWrapper(entity);
		Page<${ClassName}> page = new Page<${ClassName}>(pageNo, pageSize);
		IPage<${ClassName}> pageList = ${ClassName?uncap_first}Service.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 
	@ResponseStatus(code = HttpStatus.CREATED)
	@Permission(name = "<@messages code='doc.api.description.save'/>${modelDescription}",value = OptionType.ADD)
	@Operation(summary = "<@messages code='doc.api.description.save'/>${modelDescription}",description = "<@messages code='doc.api.description.save'/>${modelDescription}")
	@PostMapping
	public Result<${ClassName}> save${ClassName}(@Validated(value = ValidateGroups.AddGroup.class) @RequestBody ${ClassName} entity){
		if(${ClassName?uncap_first}Service.save(entity)) {
			return Result.ok();
		}
		return Result.failed();
	} 
	
	@Permission(name = "<@messages code='doc.api.description.update'/>${modelDescription}",value = OptionType.UPDATE)
	@Operation(summary = "<@messages code='doc.api.description.update'/>${modelDescription}",description = "<@messages code='doc.api.description.update'/>${modelDescription}")
	@PutMapping
	public Result<${ClassName}> update${ClassName}(@Validated(value = ValidateGroups.UpdateGroup.class) @RequestBody ${ClassName} entity){
		if(${ClassName?uncap_first}Service.updateById(entity)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "<@messages code='doc.api.description.delete'/>${modelDescription}",value = OptionType.DELETE)
	@Parameter(name = "id",description = "${modelDescription} ID",required = true)
	@Operation(summary = "<@messages code='doc.api.description.delete'/>${modelDescription}",description = "<@messages code='doc.api.description.delete'/>${modelDescription}")
	@DeleteMapping("/{id}")
	public Result<${ClassName}> delete(@PathVariable("id") Serializable id){
		if(${ClassName?uncap_first}Service.removeById(id)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
}
