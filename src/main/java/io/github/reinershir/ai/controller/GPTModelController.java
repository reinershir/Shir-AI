package io.github.reinershir.ai.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.unfbx.chatgpt.entity.common.DeleteResponse;
import com.unfbx.chatgpt.entity.files.File;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.fineTune.FineTuneDeleteResponse;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobListResponse;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobResponse;

import io.github.reinershir.ai.service.ChatGPTService;
import io.github.reinershir.auth.annotation.OptionType;
import io.github.reinershir.auth.annotation.Permission;
import io.github.reinershir.auth.annotation.PermissionMapping;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@RestController
@Schema(description =  "ChatGPT Model请求接口")
@RequestMapping({"/gpt/models"})
@Slf4j
@PermissionMapping("GPTMODEL")
public class GPTModelController {

	@Autowired
	ChatGPTService chatGPTService;
	
	@Operation(summary = "获取上传的文件列表", description = "获取上传的文件列表")
	@GetMapping("files")
	public Result<List<File>> getModelFileList() {
		return Result.ok(chatGPTService.getModelFileList());
	}

	@Permission(value=OptionType.LOGIN)
	@GetMapping
	@Operation(summary = "获取模型列表", description = "获取模型列表")
	public Result<List<com.unfbx.chatgpt.entity.models.Model>> getModelList() {
		return Result.ok(chatGPTService.getModelList());
	}

	@PostMapping("/fineTuning")
	@Operation(summary = "创建微调作业", description = "创建微调作业")
	public Result<FineTuneJobResponse> createFineTuning(@RequestParam("fileId")String fileId) {
		return Result.ok(chatGPTService.createFineTuning(fileId));
	}

	@PatchMapping("fineTuneId")
	@Operation(summary = "取消微调作业", description = "取消微调作业")
	public Result<FineTuneJobResponse> cancelFineTuning(@RequestParam("fineTuneId")String fineTuneId) {
		return Result.ok(chatGPTService.cancelFineTuning(fineTuneId));
	}

	@PostMapping("uploadFile")
	@Operation(summary = "上传微调数据", description = "上传微调数据")
	public Result<UploadFileResponse> uploadFile(@RequestPart @RequestParam("file") MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename();
		if(!fileName.toLowerCase().endsWith("jsonl")) {
			throw new BusinessException("only jsonl");
		}
		java.io.File f = new java.io.File(System.getProperty("user.dir")+"/file/"+new Date().getTime()+"/"+file.getOriginalFilename());
		try {
			if(!f.exists()) {
				f.mkdirs();
			}
			file.transferTo(f);
			return Result.ok(chatGPTService.uploadFile(f));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("文件上传失败！",e);
			throw e;
		}
		
	}
	
	@DeleteMapping("file")
	@Operation(summary = "删除微调数据", description = "删除微调数据")
	public Result<DeleteResponse> deleteFile(@RequestParam("fileId") String fileId) {
		return Result.ok(chatGPTService.deleteFile(fileId));
	}
	
	@GetMapping("/fineTunings")
	@Operation(summary = "获取微调作业列表", description = "获取微调作业列表")
	public Result<FineTuneJobListResponse<FineTuneJobResponse>> fineTunings() {
		return Result.ok(chatGPTService.fineTunings());
	}
	
	/**
	* @Function: GPTModelController.java
	* @Description: 免费账号的KEY无法使用此API
	* @param: @param fileId
	* @param: @return
	* @return：Result<String>
	* @version: v1.0.0
	* @author: ReinerShir
	* @date: 2024年3月9日 下午3:41:15 
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2024年3月9日   ReinerShir       v1.0.0              修改原因
	 */
	@GetMapping("/files/{fileId}/content")
	@Operation(summary = "获取文件内容", description = "获取文件内容")
	public Result<String> getFileContent(@PathVariable("fileId") String fileId) {
		try {
			Result<String> r = Result.ok(chatGPTService.getFileContent(fileId));
			r.setMessage(null);
			return r;
		} catch (IOException e) {
			e.printStackTrace();
			log.error("获取文件内容出错！",e);
			return Result.failed("获取文件内容出错："+e.getMessage());
		}
	}
	
	@Operation(summary = "删除微调模型", description = "删除微调模型,仅限自创模型")
	@DeleteMapping("fineTuning")
	public Result<FineTuneDeleteResponse> deleteFineTune(@RequestParam("model") String model){
		return Result.ok(chatGPTService.deleteFineTuneModel(model));
	}
}
