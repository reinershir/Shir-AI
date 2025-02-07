package io.github.reinershir.boot.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.core.international.IMessager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@RestController
@Schema(description =  "Files Controller")
@RequestMapping({"files"})
@Slf4j
public class FileUploadController {
	
	@Value("${spring.profiles.active}") 
	String profiles;

	@PostMapping("upload")
	@Operation(summary = "Upload single file", description = "Upload single file")
	public Result<String> uploadFile(@RequestPart @RequestParam("file") MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename();
		String realName = new Date().getTime()+fileName;
		// 非本地启动统一使用/root/shir 路径保存上传文件
		String path = (!profiles.equals("dev")?"/root/shir/":System.getProperty("user.dir"))+"/file/"+
		((fileName.toLowerCase().endsWith(".jpg")||fileName.toLowerCase().endsWith(".png"))?"images/":"")+realName;
		java.io.File f = new java.io.File(path);
		try {
			if(!f.exists()) {
				f.mkdirs();
			}
			file.transferTo(f);
			return Result.ok(IMessager.getMessageByCode("message.success"),realName);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Upload error！",e);
			throw e;
		}
		
	}
	
	@PostMapping("upload/multiply")
	@Operation(summary = "Upload multiply file", description = "Upload multiply file")
	public Result<String> uploadFile(@RequestPart @RequestParam("file") MultipartFile[] files,String requestId) throws Exception {
		if(files == null || files.length<1) {
			return Result.failed();
		}
		String path = (profiles.equals("test")?"/root/shir/":System.getProperty("user.dir"))+"/file/"+(StringUtils.hasText(requestId)?requestId:new Date().getTime())+"/";
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			String filePath = path + fileName;
			java.io.File f = new java.io.File(filePath);
			try {
				if(!f.exists()) {
					f.mkdirs();
				}
				file.transferTo(f);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Upload error！",e);
				throw e;
			}
		}
		return Result.ok(path);
		
	}
}
