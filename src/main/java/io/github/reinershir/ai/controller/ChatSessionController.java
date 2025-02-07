package io.github.reinershir.ai.controller;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.github.reinershir.ai.model.ChatSession;
import io.github.reinershir.ai.model.Mask;
import io.github.reinershir.ai.service.ChatSessionService;
import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.auth.core.support.AuthorizeManager;
import io.github.reinershir.boot.common.BaseController;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.common.ValidateGroups;
import io.github.reinershir.boot.common.ValidateGroups.AddGroup;
import io.github.reinershir.boot.utils.IdUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;


 /**
 * ChatSession Controller Generate by Shir-boot
 * @author Shir-Boot
 * @Date 2024年4月17日 上午10:07:25
 * @version 1.0
 *
 */
@RestController
@RequestMapping("chatSession")
@Tag(description = "gpt chat history",name = "gpt chat history")
@PermissionMapping(value="ChatSession")
public class ChatSessionController extends BaseController{
 
	@Autowired
	private ChatSessionService chatSessionService;
	@Autowired(required = false)
	AuthorizeManager authorizeManager;
	
	@Permission(name = "gpt chat session list",value = OptionType.LOGIN)
	@Operation(summary="gpt chat session list", description = "gpt chat history list")
	@Parameters({
		@Parameter(name="pageNo",description="Now page",required = true,in = ParameterIn.QUERY),
		@Parameter(name="pageSize",description="Page size",required = true,in = ParameterIn.QUERY),
	})
	@GetMapping
	public Result<IPage<ChatSession>> queryPageList(
								   HttpServletRequest request,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<ChatSession> queryWrapper = new QueryWrapper<>();
		String userId = authorizeManager.getTokenInfo(request).getUserId();
		queryWrapper.eq("USER_ID", userId);
		queryWrapper.orderByAsc("CREATE_TIME");
		Page<ChatSession> page = new Page<ChatSession>(pageNo, pageSize);
		IPage<ChatSession> pageList = chatSessionService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	@Permission(name = "create chat session",value = OptionType.LOGIN)
	@Operation(summary="create chat session", description = "create chat session")
	@PostMapping
	public Result<ChatSession> create(@RequestBody @Validated(AddGroup.class) ChatSession entity,HttpServletRequest request){
		String userId = authorizeManager.getTokenInfo(request).getUserId();
		entity.setCreateTime(new Date());
		entity.setSessionId(IdUtils.getRandomIdByUUID());
		entity.setUserId(Long.parseLong(userId));
		if(chatSessionService.save(entity)) {
			return Result.ok(entity);
		}
		return Result.failed();
	}
	
	@Permission(name = "update chat session",value = OptionType.LOGIN)
	@Operation(summary = "update chat session",description = "update chat session")
	@PutMapping
	public Result<Mask> updateMask(@Validated(value = ValidateGroups.UpdateGroup.class) @RequestBody ChatSession entity,HttpServletRequest request){
		String userId = authorizeManager.getTokenInfo(request).getUserId();
		ChatSession chatSession = chatSessionService.getById(entity.getSessionId());
		if(chatSession == null || !chatSession.getUserId().toString().equals(userId)) {
			return Result.failed();
		}
		chatSession.setModel(entity.getModel());
		chatSession.setSessionName(entity.getSessionName());
		if(chatSessionService.updateById(chatSession)) {
			return Result.ok();
		}
		return Result.failed();
	}
	
	@Permission(name = "delete chat session",value = OptionType.LOGIN)
	@Parameter(name = "id",description = "chat session ID",required = true)
	@Operation(summary = "delete chat session",description = "delete chat session")
	@DeleteMapping("/{id}")
	public Result<Mask> delete(@PathVariable("id") Serializable id,HttpServletRequest request){
		String userId = authorizeManager.getTokenInfo(request).getUserId();
		long count = chatSessionService.count(new LambdaQueryWrapper<ChatSession>()
				.eq(ChatSession::getUserId, userId).eq(ChatSession::getSessionId,id));
		if(count<1) {
			return Result.failed();
		}
		if(chatSessionService.removeById(id)) {
			return Result.ok();
		}
		return Result.failed();
	}
}
