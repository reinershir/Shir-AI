package io.github.reinershir.ai.controller;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.reinershir.ai.model.ChatHistory;
import io.github.reinershir.ai.service.ChatHistoryService;
import io.github.reinershir.auth.core.support.AuthorizeManager;
import io.github.reinershir.auth.entity.TokenInfo;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.core.international.IMessager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Schema(description =  "Chat Tools Controller")
@RequestMapping({"chatTools"})
@Slf4j
public class ChatToolsController {
	
	@Value("${spring.profiles.active}") 
	String profiles;
	
	@Value("${thirdParty.chatToolsService.rgaFilePath}")
	String uploadFilePath;
	
	@Autowired(required = false)
	AuthorizeManager authorizeManager;
	
	@Autowired
	ChatHistoryService chatHistoryService;

	@PostMapping("/rga/upload")
	@Operation(summary = "Upload rga file", description = "Upload rga file")
	public Result<String> uploadFile(@RequestPart @RequestParam("file") MultipartFile file,@RequestParam("sessionId") String sessionId
			,HttpServletRequest request) throws Exception {
		TokenInfo tokenInfo = authorizeManager.getTokenInfo(request);
		String fileName = file.getOriginalFilename();
		String filePath = uploadFilePath+sessionId+"/"+new Date().getTime()+fileName;
		java.io.File f = new java.io.File(filePath);
		Long userId = null;
		if(tokenInfo!=null) {
			userId = Long.parseLong(tokenInfo.getUserId());
		}else {
			userId= 0l;
		}
		try {
			// create folder
			File checkDirs = new File(uploadFilePath+sessionId+"/");
			if(!checkDirs.exists()) {
				checkDirs.mkdirs();
			}
			file.transferTo(f);
			ChatHistory chatHistory = new ChatHistory("<span><i v-else class='el-icon-document file-uploader-icon' style='height:140px;' ></i></span><span>"+fileName+"</span>", "", "", userId,0f, sessionId, new Date());
			chatHistory.setAttach("file:"+fileName);
			chatHistoryService.save(chatHistory);
			return Result.ok(IMessager.getMessageByCode("message.success"),fileName);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Upload error！",e);
			throw e;
		}
		
	}
	
}
