package io.github.reinershir.boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.auth.core.integrate.access.MenuAccess;
import io.github.reinershir.auth.core.integrate.vo.MenuVO;
import io.github.reinershir.auth.core.model.Menu;
import io.github.reinershir.auth.core.support.AuthorizeManager;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.core.international.IMessager;
import io.github.reinershir.boot.dto.req.MenuMoveDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("menus")
@RestController
@Tag(description = "Menu management",name = "menu management")
@PermissionMapping(value="MENU")
public class MenuController {
	
	MenuAccess MenuAccess;
	
	public MenuController(@Autowired(required = false) AuthorizeManager authorizeManager) {
		if(authorizeManager!=null) {
			this.MenuAccess=authorizeManager.getMenuAccess();
		}
	}

	@Permission(name = "Menu list",value = OptionType.LIST)
	@Operation(summary = "Menu list",description = "Convert modified preorder tree traversal into the list of menus")
	@Parameter(name = "parentId",required = false,description = "Parent menu id")
	@GetMapping("list")
	public Result<List<Menu>> list(@RequestParam(value="parentId",required = false) Long parentId){
		return Result.ok(MenuAccess.qureyList(parentId));                                                   
	}
	
	@Permission(name = "Insert menu",value = OptionType.ADD)
	@Operation(summary = "Insert menu",description = "Insert menu")
	@Parameter(name = "parentId",description = "parent menu id",required = false)
	@PostMapping
	public Result<Menu> addMenu(@Validated @RequestBody MenuVO menu){
		if(!StringUtils.hasText(menu.getName())) {
			return Result.failed(IMessager.getMessageByCode("message.menu.nameNotNull"));
		}
		if(MenuAccess.insertMenu(menu)>0) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "Update menu",value = OptionType.UPDATE)
	@Operation(summary = "Update menu",description = "Update menu")
	@PutMapping
	public Result<Menu> updateMenu( @RequestBody MenuVO MenuDTO){
		if(!StringUtils.hasText(MenuDTO.getName())) {
			return Result.failed(IMessager.getMessageByCode("message.menu.nameNotNull"));
		}
		if(MenuAccess.updateById(MenuDTO)>0) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "Delete menu",value = OptionType.DELETE)
	@Parameter(name = "id",description = "Menu ID",required = true)
	@Operation(summary = "Delete menu",description = "Delete menu")
	@DeleteMapping("/{id}")
	public Result<Object> delete(@PathVariable("id") Long id){
		if(MenuAccess.deleteById(id)>0) {
			return Result.ok();
		}
		return Result.failed();
	}

	@Permission(name = "Get menu details",value = OptionType.LIST)
	@Parameter(name = "id",description = "Menu ID")
	@Operation(summary = "Get menu details",description = "Get menu details")
	@GetMapping("/{id}")
	public Result<Menu> detail(@PathVariable("id") Long id){
		return Result.ok(MenuAccess.selectById(id));
	}
	
	@Permission(name = "Move menu",value = OptionType.UPDATE)
	@Operation(summary = "Move menu",description = "Move menu")
	@PatchMapping("/position")
	public Result<Object> updateMenu(@RequestBody @Validated  MenuMoveDTO dto){
		boolean flag = false;
		Long moveId = dto.getMoveId();
		Long targetId = dto.getTargetId();
		switch(dto.getPosition()) {
		case 1:
			flag = MenuAccess.moveNodeBefore(moveId, targetId)>0?true:false;
			break;
		case 2:
			flag = MenuAccess.moveNodeAfter(moveId, targetId)>0?true:false;
			break;
		case 3:
			flag = MenuAccess.moveNodeByParentAsLastChild(moveId, targetId)>0?true:false;
			break;
		}
		if(flag) {
			return Result.ok();
		}
		return Result.failed();
	}
}
