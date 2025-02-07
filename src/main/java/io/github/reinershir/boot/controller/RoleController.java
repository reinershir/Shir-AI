package io.github.reinershir.boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.auth.core.integrate.access.RoleAccess;
import io.github.reinershir.auth.core.model.Role;
import io.github.reinershir.auth.core.model.RolePermission;
import io.github.reinershir.auth.core.support.AuthorizeManager;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.common.ValidateGroups;
import io.github.reinershir.boot.dto.req.RoleDTO;
import io.github.reinershir.boot.dto.req.RoleListReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("roles")
@RestController
@Tag(description = "Role management",name = "Role management")
@PermissionMapping(value="ROLE")
public class RoleController {
	
	RoleAccess roleAccess;
	public RoleController(@Autowired(required = false) AuthorizeManager authorizeManager) {
		if(authorizeManager!=null) {
			this.roleAccess=authorizeManager.getRoleAccess();
		}
	}

	@Permission(name = "Role list",value = OptionType.LIST)
	@Operation(summary = "Role list ",description = "Role list")
	@GetMapping("list")
	public Result<Page<Role>> list(@Validated RoleListReqDTO reqDTO){
		List<io.github.reinershir.auth.core.model.Role> list = roleAccess.selectList(reqDTO.getPage(), reqDTO.getPageSize(),reqDTO.getRoleName());
		Long count = roleAccess.selectCount(reqDTO.getRoleName());
		Page<Role> page = new Page<Role>(reqDTO.getPage(),reqDTO.getPageSize(),count);
		page.setRecords(list);
		return Result.ok(page);
	}
	
	@Permission(name = "Insert Role",value = OptionType.ADD)
	@Operation(summary = "Insert Role ",description = "Insert Role")
	@PostMapping
	public Result<Role> addRole(@Validated @RequestBody RoleDTO dto){
		if(roleAccess.insert(dto,dto.getMenuIds())>0) {
			return Result.ok(dto);
		}
		return Result.failed();
	}
	
	@Permission(name = "Update role",value = OptionType.UPDATE)
	@Operation(summary = "Update Role ",description = "Update Role")
	@PutMapping
	public Result<Role> updateUser(@Validated(value = ValidateGroups.UpdateGroup.class) @RequestBody RoleDTO roleDTO){
		if(roleAccess.updateById(roleDTO, roleDTO.getMenuIds())>0) {
			return Result.ok(roleDTO);
		}
		return Result.failed();
	}
	
	@Permission(name = "Delete role",value = OptionType.DELETE)
	@Operation(summary = "Delete role ",description = "Delete role")
	@DeleteMapping("/{id}")
	public Result<String> delete(@PathVariable("id") Long id){
		if(roleAccess.deleteById(id)>0) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "Query menu permissions",value = OptionType.LIST)
	@Operation(summary = "Query menu permissions ",description = "Query the menu permissions bound to this role")
	@GetMapping("/{roleId}/rolePermissions")
	public Result<List<RolePermission>> getRolePermissionsById(@PathVariable("roleId") Long roleId){
		return Result.ok(roleAccess.selectRolePermissionByRole(roleId));
	}
	
}
