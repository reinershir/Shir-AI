package io.github.reinershir.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.github.reinershir.ai.model.ChatHistory;
import io.github.reinershir.ai.service.ChatHistoryService;
import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.boot.common.BaseController;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.core.query.QueryHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;


 /**
 * ChatHistory Controller Generate by Shir-boot
 * @author Shir-Boot
 * @Date 2024年4月17日 上午10:07:25
 * @version 1.0
 *
 */
@RestController
@RequestMapping("chatHistory")
@Tag(description = "gpt chat history",name = "gpt chat history")
@PermissionMapping(value="CHATHISTORY")
public class ChatHistoryController extends BaseController{
 
	@Autowired
	private ChatHistoryService chatHistoryService;
	
	@Permission(name = "gpt chat history list",value = OptionType.LIST)
	@Operation(summary="gpt chat history list", description = "gpt chat history list")
	@Parameters({
		@Parameter(name="pageNo",description="Now page",required = true,in = ParameterIn.QUERY),
		@Parameter(name="pageSize",description="Page size",required = true,in = ParameterIn.QUERY),
	})
	@GetMapping
	public Result<IPage<ChatHistory>> queryPageList(ChatHistory entity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<ChatHistory> queryWrapper = QueryHelper.initQueryWrapper(entity);
		if(StringUtils.hasText(entity.getSessionId())) {
			queryWrapper.orderByAsc("CREATE_TIME");
		}else {
			queryWrapper.orderByDesc("CREATE_TIME");
		}
		Page<ChatHistory> page = new Page<ChatHistory>(pageNo, pageSize);
		IPage<ChatHistory> pageList = chatHistoryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
}
