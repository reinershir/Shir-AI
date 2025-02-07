package io.github.reinershir.ai.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.unfbx.chatgpt.entity.common.DeleteResponse;
import com.unfbx.chatgpt.entity.files.File;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.fineTune.FineTuneDeleteResponse;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobListResponse;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobResponse;
import com.unfbx.chatgpt.entity.images.Item;

import io.github.reinershir.ai.entity.WebRequestMessage;
import jakarta.websocket.Session;


public interface ChatGPTService {

    String chat( String prompt,String openId,@Nullable String model);
    
    void chatCompletions(String prompt, String openId,Session session, @Nullable String model);
    
    public void chatCompletions(WebRequestMessage request, String openId,@Nullable String model,Session session, String chatPerson);
    
    /**
    * @Function: ChatGPTService.java
    * @Description: request openai chatGPT response http json
    * @param: @param request request params
    * @param: @param openId session id
    * @param: @param model gpt model
    * @param: @return
    * @version: v1.0.0
    * @author: ReinerShir
    * @date: 2024年2月25日 下午7:21:09 
    * Modification History:
    * Date         Author          Version            Description
    *---------------------------------------------------------*
    * 2024年2月25日   ReinerShir       v1.0.0             
     */
    public void chatCompletions(WebRequestMessage request, String openId,SseEmitter sseEmitter,Long userId);
    
    /**
    * @Function: chat with picture
    * @Description: 
    * @param: @param request
    * @param: @param openId
    * @param: @param sseEmitter
    * @param: @param userId
    * @return：void
    * @version: v1.0.0
    * @author: ReinerShir
    * @date: 2024年7月28日 下午3:18:49 
    * Modification History:
    * Date         Author          Version            Description
    *---------------------------------------------------------*
    * 2024年7月28日   ReinerShir       v1.0.0              修改原因
     */
    public void chatWithPicture(WebRequestMessage request, String openId,SseEmitter sseEmitter,Long userId);
    
    public void completions(String question,@Nullable String model);
    
    public List<Item> generateImage(String prompt);

    public List<File> getModelFileList();

    public List<com.unfbx.chatgpt.entity.models.Model> getModelList();
    
    public FineTuneJobResponse createFineTuning(String fileId);

    public FineTuneJobResponse cancelFineTuning(String fineTuneId);
    
    public UploadFileResponse uploadFile(java.io.File file);
    
    public DeleteResponse deleteFile(String fileId);
    
    public FineTuneDeleteResponse deleteFineTuning(String fineTuneId);
    
    public FineTuneJobListResponse<FineTuneJobResponse> fineTunings();
    
    public String getFileContent(String fileId) throws IOException ;
    
    public FineTuneDeleteResponse deleteFineTuneModel(String model);
    
    public void updateApiConfigs();
}
