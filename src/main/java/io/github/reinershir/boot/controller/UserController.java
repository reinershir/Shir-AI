package io.github.reinershir.boot.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.auth.core.model.Menu;
import io.github.reinershir.auth.core.model.Role;
import io.github.reinershir.auth.core.support.AuthorizeManager;
import io.github.reinershir.auth.entity.TokenInfo;
import io.github.reinershir.boot.common.BaseController;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.common.ValidateGroups;
import io.github.reinershir.boot.contract.ShirBootContracts;
import io.github.reinershir.boot.core.international.IMessager;
import io.github.reinershir.boot.core.query.QueryHelper;
import io.github.reinershir.boot.dto.req.LoginDTO;
import io.github.reinershir.boot.dto.req.ResetPasswordDTO;
import io.github.reinershir.boot.dto.req.UpdatePasswordDTO;
import io.github.reinershir.boot.dto.req.UserReqDTO;
import io.github.reinershir.boot.dto.res.LoginRespDTO;
import io.github.reinershir.boot.dto.res.UserInfoDTO;
import io.github.reinershir.boot.model.User;
import io.github.reinershir.boot.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("user")
@RestController
@Tag(description = "user management",name = "user management")
@PermissionMapping(value="USER")
public class UserController extends BaseController{
	
	
	@Autowired
	UserServiceImpl userService;
	@Autowired(required = false)
	AuthorizeManager authorizeManager;
	@Value(value="${lui-auth.authrizationConfig.administratorId}")
	String administratorId;
	
	@PostMapping("token")
	@Permission(value=OptionType.SKIP,name="Login ")
	@Operation(summary = "Login ",description = "Login")
	public Result<LoginRespDTO> login(@Validated @RequestBody LoginDTO loginDTO) throws Exception{
		User user = userService.login(loginDTO.getLoginName(), loginDTO.getPassword());
		if(user==null) {
			return Result.failed(IMessager.getInstance().getMessage("message.notmatch"));
		}
		LoginRespDTO resp = new LoginRespDTO();
		resp.setAccessToken(user.getPassword());
		resp.setId(user.getId());
		resp.setLoginName(user.getLoginName());
		resp.setNickName(user.getNickName());
		//resp.setMenus(authorizeManager.getMenusByUser(user.getId().toString()));
		return Result.ok(resp);
	}
	
	@Operation(summary="User list", description = "User list")
	@Parameters({
		@Parameter(name="pageNo",description="pageNo",required = true,in = ParameterIn.QUERY),
		@Parameter(name="pageSize",description="pageSize",required = true,in = ParameterIn.QUERY),
	})
	@GetMapping(value = "/list")
	public Result<IPage<User>> queryPageList(User entity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<User> queryWrapper = QueryHelper.initQueryWrapper(entity);
		Page<User> page = new Page<User>(pageNo, pageSize);
		IPage<User> pageList = userService.page(page, queryWrapper);
		pageList.getRecords().forEach(u->u.setPassword(null));
		return Result.ok(pageList);
	}
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@Permission(name = "Insert user",value = OptionType.ADD)
	@Operation(summary = "Insert user",description = "Insert user")
	@PostMapping
	public Result<User> addUser(@Validated(value = ValidateGroups.AddGroup.class) @RequestBody UserReqDTO user){
		User result = userService.insert(user);
		if(result!=null) {
			return Result.ok(user);
		}
		return Result.failed();
	}
	
	@Permission(name = "Update user",value = OptionType.UPDATE)
	@Operation(summary = "Update user",description = "Update user")
	@PutMapping
	public Result<User> updateUser(@Validated(value = ValidateGroups.UpdateGroup.class) @RequestBody UserReqDTO user){
		if(userService.updateUser(user)>0) {
			return Result.ok(user);
		}
		return Result.failed();
	}
	
	@Permission(name = "Remove user",value = OptionType.DELETE)
	@Parameter(name = "id",description = "User ID",required = true)
	@Operation(summary = "Remove user",description = "Remove user")
	@DeleteMapping("/{id}")
	public Result<Object> delete(@PathVariable("id") Long id){
		if(userService.logicDeleteUser(id)>0) {
			try {
				authorizeManager.logout(id.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return Result.failed("删除失败！");
			}
			return Result.ok("删除成功！");
		}
		return Result.failed("删除失败！");
	}
	
	@Permission(name = "获取用户所绑定的角色ID",value = OptionType.LOGIN)
	@Parameter(name = "userId",description = "用户ID",required = true)
	@Operation(summary = "获取用户所绑定的角色ID",description = "获取用户所绑定的角色")
	@GetMapping("/{userId}/roleIds")
	public Result<List<Long>> getRoleIdByUser(@PathVariable("userId") Long userId){
		return Result.ok(userService.getRoleByUser(userId+""));
	}
	
	@Permission(name = "Update password",value = OptionType.LOGIN)
	@Operation(summary = "Update password",description = "Update password")
	@PatchMapping("password")
	public Result<Object> updatePassword(@Validated @RequestBody UpdatePasswordDTO dto,HttpServletRequest request){
		return userService.updatePassword(request, dto.getPassword(),dto.getNewPassword());
	}

	@Permission(name = "Rest password",value = OptionType.CUSTOM,customPermissionCode = "RESETPASSWORD")
	@Operation(summary = "Rest password",description = "Rest password")
	@PatchMapping("/password/reset")
	public Result<Object> resetPassword(@RequestBody ResetPasswordDTO dto){
		if(userService.resetPassword(dto.getUser(),dto.getNewPassword())) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@DeleteMapping("token")
	@Permission(name = "用户登出",value = OptionType.LOGIN)
	@Operation(summary = "用户登出",description = "登出接口")
	public Result<Object> logout(HttpServletRequest request){
		userService.logout(request);
		return Result.ok();
	}

	@Permission(name = "Get user info",value = OptionType.LIST)
	@Parameter(name = "id",description = "User ID",required = true)
	@Operation(summary = "Get user info",description = "Get user info")
	@GetMapping("/{id}")
	public Result<User> detail(@PathVariable("id") Long id){
		User user = userService.getById(id);
		if(user!=null) {
			user.setPassword(null);
			return Result.ok(user);
		}
		return Result.failed("参数错误!");
	}
	
	@Permission(name = "Get user info by token",value = OptionType.LOGIN)
	@Operation(summary = "Get user info by token",description = "Get user info by token")
	@GetMapping("/info")
	public Result<UserInfoDTO> getUserInfo(HttpServletRequest request){
		TokenInfo tokenInfo = authorizeManager.getTokenInfo(request);
		User user = userService.getById(tokenInfo.getUserId());
		if(user!=null) {
			user.setPassword(null);
			UserInfoDTO resp = new UserInfoDTO();
			BeanUtils.copyProperties(user, resp);
			if(tokenInfo.getUserId().equals(administratorId)&&administratorId!=null) {
				resp.setRoles(Arrays.asList("admin"));
			}else {
				List<Role> roles = authorizeManager.getRoleAccess().selectRoleByUser(tokenInfo.getUserId());
				List<String> roleNames = new LinkedList<>();
				if(!CollectionUtils.isEmpty(roles)) {
					roles.forEach(r->roleNames.add(r.getRoleName()));
				}
				resp.setRoles(roleNames);
			}
			return Result.ok(resp);
		}
		return Result.failed("paramemter error!");
	}
	
	@Permission(name = "Get user info by token",value = OptionType.LOGIN)
	@Operation(summary = "Get user info by token",description = "Get user info by token")
	@GetMapping("/menus")
	public Result<List<Menu>> getUserMenus(HttpServletRequest request){
		return Result.ok(authorizeManager.getMenusByUser(authorizeManager.getTokenInfo(request).getUserId()));
	} 
	
	@Permission(name = "disable user",value = OptionType.DISABLE)
	@Parameter(name = "id",description = "User ID",required = true)
	@Operation(summary = "disable user",description = "disable user")
	@PatchMapping("/{id}/disable")
	public Result<Long> disable(@PathVariable("id") Long id){
		if(userService.disbleOrEnable(id, ShirBootContracts.STATUS_DISABLE)) {
			return Result.ok(id);
		}
		return Result.failed();
	}
	
	@Permission(name = "enable user",value = OptionType.ENABLE)
	@Parameter(name = "id",description = "User ID",required = true)
	@Operation(summary = "enable user",description = "enable user")
	@PatchMapping("/{id}/enable")
	public Result<Long> enable(@PathVariable("id") Long id){
		if(userService.disbleOrEnable(id, ShirBootContracts.STATUS_ENABLE)) {
			return Result.ok(id);
		}
		return Result.failed();
	}
	

}
