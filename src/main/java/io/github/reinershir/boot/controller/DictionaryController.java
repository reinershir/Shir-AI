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
import io.github.reinershir.boot.model.Dictionary;
import io.github.reinershir.boot.service.DictionaryService;
import io.github.reinershir.boot.service.listener.DictionaryListenerHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;


 /**
 * Dictionary Controller Generate by Shir-boot
 * @author Shir-Boot
 * @Date 2024年1月20日 下午4:56:15
 * @version 1.0
 *
 */
@RestController
@RequestMapping("dictionary")
@Tag(description = "dictionary management",name = "dictionary")
@PermissionMapping(value="DICTIONARY")
public class DictionaryController extends BaseController{
 
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	DictionaryListenerHandler dictionaryListenerHandler;
	
	@Permission(name = "dictionary list",value = OptionType.LIST)
	@Operation(summary="dictionary list", description = "dictionary list")
	@Parameters({
		@Parameter(name="pageNo",description="Now page",required = true,in = ParameterIn.QUERY),
		@Parameter(name="pageSize",description="Page size",required = true,in = ParameterIn.QUERY),
	})
	@GetMapping
	public Result<IPage<Dictionary>> queryPageList(Dictionary entity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<Dictionary> queryWrapper = QueryHelper.initQueryWrapper(entity);
		Page<Dictionary> page = new Page<Dictionary>(pageNo, pageSize);
		IPage<Dictionary> pageList = dictionaryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 
	@ResponseStatus(code = HttpStatus.CREATED)
	@Permission(name = "save dictionary",value = OptionType.ADD)
	@Operation(summary = "savedictionary",description = "保存dictionary")
	@PostMapping
	public Result<Dictionary> saveDictionary(@Validated(value = ValidateGroups.AddGroup.class) @RequestBody Dictionary entity){
		if(dictionaryService.save(entity)) {
			return Result.ok();
		}
		return Result.failed();
	} 
	
	@Permission(name = "update dictionary",value = OptionType.UPDATE)
	@Operation(summary = "update dictionary",description = "update dictionary")
	@PutMapping
	public Result<Dictionary> updateDictionary(@Validated(value = ValidateGroups.UpdateGroup.class) @RequestBody Dictionary entity){
		if(dictionaryService.updateById(entity)) {
			if("SYSTEM".equals(entity.getType())){
				dictionaryListenerHandler.runListeners(entity);
			}
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "delete dictionary",value = OptionType.DELETE)
	@Parameter(name = "id",description = "dictionary ID",required = true)
	@Operation(summary = "delete dictionary",description = "delete dictionary")
	@DeleteMapping("/{id}")
	public Result<Dictionary> delete(@PathVariable("id") Serializable id){
		if(dictionaryService.removeById(id)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
}
