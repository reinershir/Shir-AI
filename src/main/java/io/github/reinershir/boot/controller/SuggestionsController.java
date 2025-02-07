package io.github.reinershir.boot.controller;

import java.io.Serializable;

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

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.boot.common.BaseController;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.common.ValidateGroups;
import io.github.reinershir.boot.core.query.QueryHelper;
import io.github.reinershir.boot.model.Suggestions;
import io.github.reinershir.boot.service.SuggestionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;


 /**
 * Suggestions Controller Generate by Shir-boot
 * @author Shir-Boot
 * @Date 2023年8月19日 下午5:44:42
 * @version 1.0
 *
 */
@RestController
@RequestMapping("suggestions")
@Tag(description = "投诉建议管理模块",name = "投诉建议")
@PermissionMapping(value="SUGGESTIONS")
public class SuggestionsController extends BaseController{
 
	@Autowired
	private SuggestionsService suggestionsService;
	
	@Operation(summary="投诉建议列表", description = "投诉建议列表")
	@Parameters({
		@Parameter(name="pageNo",description="Now page",required = true,in = ParameterIn.QUERY),
		@Parameter(name="pageSize",description="Page size",required = true,in = ParameterIn.QUERY),
	})
	@GetMapping
	public Result<IPage<Suggestions>> queryPageList(Suggestions entity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<Suggestions> queryWrapper = QueryHelper.initQueryWrapper(entity);
		Page<Suggestions> page = new Page<Suggestions>(pageNo, pageSize);
		IPage<Suggestions> pageList = suggestionsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 
	@ResponseStatus(code = HttpStatus.CREATED)
	@Permission(name = "保存投诉建议",value = OptionType.ADD)
	@Operation(summary = "保存投诉建议",description = "保存投诉建议")
	@PostMapping
	public Result<Suggestions> saveSuggestions(@Validated(value = ValidateGroups.AddGroup.class) @RequestBody Suggestions entity){
		if(suggestionsService.save(entity)) {
			return Result.ok();
		}
		return Result.failed();
	} 
	
	@Permission(name = "修改投诉建议",value = OptionType.UPDATE)
	@Operation(summary = "修改投诉建议",description = "修改投诉建议")
	@PutMapping
	public Result<Suggestions> updateSuggestions(@Validated(value = ValidateGroups.UpdateGroup.class) @RequestBody Suggestions entity){
		if(suggestionsService.updateById(entity)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "删除投诉建议",value = OptionType.DELETE)
	@Parameter(name = "id",description = "投诉建议 ID",required = true)
	@Operation(summary = "删除投诉建议",description = "删除投诉建议")
	@DeleteMapping("/{id}")
	public Result<Suggestions> delete(@PathVariable("id") Serializable id){
		if(suggestionsService.removeById(id)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
}
