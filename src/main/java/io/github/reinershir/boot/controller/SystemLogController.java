package io.github.reinershir.boot.controller;

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
import io.github.reinershir.boot.model.SystemLog;
import io.github.reinershir.boot.service.SystemLogService;


 /**
 * SystemLog Controller Generate by Shir-boot
 * @author Shir-Boot
 * @Date 2023年12月7日 下午6:59:47
 * @version 1.0
 *
 */
@RestController
@RequestMapping("systemLog")
@Tag(description = "系统日志管理模块",name = "系统日志")
@PermissionMapping(value="SYSTEMLOG")
public class SystemLogController extends BaseController{
 
	@Autowired
	private SystemLogService systemLogService;
	
	@Operation(summary="系统日志列表", description = "系统日志列表")
	@Parameters({
		@Parameter(name="pageNo",description="Now page",required = true,in = ParameterIn.QUERY),
		@Parameter(name="pageSize",description="Page size",required = true,in = ParameterIn.QUERY),
	})
	@GetMapping
	public Result<IPage<SystemLog>> queryPageList(SystemLog entity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<SystemLog> queryWrapper = QueryHelper.initQueryWrapper(entity);
		Page<SystemLog> page = new Page<SystemLog>(pageNo, pageSize);
		IPage<SystemLog> pageList = systemLogService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 
	@ResponseStatus(code = HttpStatus.CREATED)
	@Permission(name = "保存系统日志",value = OptionType.ADD)
	@Operation(summary = "保存系统日志",description = "保存系统日志")
	@PostMapping
	public Result<SystemLog> saveSystemLog(@Validated(value = ValidateGroups.AddGroup.class) @RequestBody SystemLog entity){
		if(systemLogService.save(entity)) {
			return Result.ok();
		}
		return Result.failed();
	} 
	
	@Permission(name = "修改系统日志",value = OptionType.UPDATE)
	@Operation(summary = "修改系统日志",description = "修改系统日志")
	@PutMapping
	public Result<SystemLog> updateSystemLog(@Validated(value = ValidateGroups.UpdateGroup.class) @RequestBody SystemLog entity){
		if(systemLogService.updateById(entity)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "删除系统日志",value = OptionType.DELETE)
	@Parameter(name = "id",description = "系统日志 ID",required = true)
	@Operation(summary = "删除系统日志",description = "删除系统日志")
	@DeleteMapping("/{id}")
	public Result<SystemLog> delete(@PathVariable("id") Serializable id){
		if(systemLogService.removeById(id)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
}
