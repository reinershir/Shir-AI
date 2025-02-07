package io.github.reinershir.ai.controller;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.github.reinershir.ai.model.Mask;
import io.github.reinershir.ai.service.MaskService;
import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.boot.common.BaseController;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.common.ValidateGroups;
import io.github.reinershir.boot.core.query.QueryHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;


 /**
 * Mask Controller Generate by Shir-boot
 * @author Shir-Boot
 * @Date 2024年4月17日 上午10:40:16
 * @version 1.0
 *
 */
@RestController
@RequestMapping("mask")
@Tag(description = "mask管理模块",name = "mask")
@PermissionMapping(value="MASK")
public class MaskController extends BaseController{
 
	@Autowired
	private MaskService maskService;
	
	@Permission(name = "mask列表",value = OptionType.LIST)
	@Operation(summary="mask列表", description = "mask列表")
	@Parameters({
		@Parameter(name="pageNo",description="Now page",required = true,in = ParameterIn.QUERY),
		@Parameter(name="pageSize",description="Page size",required = true,in = ParameterIn.QUERY),
	})
	@GetMapping
	public Result<IPage<Mask>> queryPageList(Mask entity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<Mask> queryWrapper = QueryHelper.initQueryWrapper(entity);
		Page<Mask> page = new Page<Mask>(pageNo, pageSize);
		IPage<Mask> pageList = maskService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 
	@ResponseStatus(code = HttpStatus.CREATED)
	@Permission(name = "保存mask",value = OptionType.ADD)
	@Operation(summary = "保存mask",description = "保存mask")
	@PostMapping
	public Result<Mask> saveMask(@Validated(value = ValidateGroups.AddGroup.class) @RequestBody Mask entity){
		entity.setCreateTime(new Date());
		if(maskService.save(entity)) {
			return Result.ok();
		}
		return Result.failed();
	} 
	
	@Permission(name = "修改mask",value = OptionType.UPDATE)
	@Operation(summary = "修改mask",description = "修改mask")
	@PutMapping
	public Result<Mask> updateMask(@Validated(value = ValidateGroups.UpdateGroup.class) @RequestBody Mask entity){
		if(maskService.updateById(entity)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "删除mask",value = OptionType.DELETE)
	@Parameter(name = "id",description = "mask ID",required = true)
	@Operation(summary = "删除mask",description = "删除mask")
	@DeleteMapping("/{id}")
	public Result<Mask> delete(@PathVariable("id") Serializable id){
		if(maskService.removeById(id)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
}
