package io.github.reinershir.ai.service.dingding;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkrobot_1_0.Client;
import com.aliyun.dingtalkrobot_1_0.models.OrgGroupSendHeaders;
import com.aliyun.dingtalkrobot_1_0.models.OrgGroupSendRequest;
import com.aliyun.dingtalkrobot_1_0.models.OrgGroupSendResponse;
import com.aliyun.tea.TeaException;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zeymo
 */
@Slf4j
@Service
public class RobotGroupMessagesService {
    private Client robotClient;
    private final AccessTokenService accessTokenService;

    @Value("${thirdParty.dingding.robotCode}")
    private String robotCode;
    
    @Autowired
    public RobotGroupMessagesService(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @PostConstruct
    public void init() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        robotClient = new Client(config);
    }

    /**
     * send message to group with openConversationId
     *
     * @param openConversationId conversationId
     * @return messageId
     * @throws Exception e
     */
    public String send(String openConversationId, String text) throws Exception {
        OrgGroupSendHeaders orgGroupSendHeaders = new OrgGroupSendHeaders();
        if(accessTokenService.isTokenNearlyExpired()) {
        	log.error("dingtalk access token is expired!");
        }
        orgGroupSendHeaders.setXAcsDingtalkAccessToken(accessTokenService.getAccessToken());

        OrgGroupSendRequest orgGroupSendRequest = new OrgGroupSendRequest();
        orgGroupSendRequest.setMsgKey("sampleText");
        // orgGroupSendRequest.setMsgKey("sampleMarkdown");
        orgGroupSendRequest.setRobotCode(robotCode);
        orgGroupSendRequest.setOpenConversationId(openConversationId);

        JSONObject msgParam = new JSONObject();
        //msgParam.put("title", "AI回复");
        msgParam.put("content", text);
        String json = msgParam.toJSONString();
        log.info("send message to dingtalk:{}",json);
        orgGroupSendRequest.setMsgParam(json);

        try {
            OrgGroupSendResponse orgGroupSendResponse = robotClient.orgGroupSendWithOptions(orgGroupSendRequest,
                    orgGroupSendHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            if (Objects.isNull(orgGroupSendResponse) || Objects.isNull(orgGroupSendResponse.getBody())) {
                log.error("RobotGroupMessagesService_send orgGroupSendWithOptions return error, response={}",
                        orgGroupSendResponse);
                return null;
            }
            return orgGroupSendResponse.getBody().getProcessQueryKey();
        } catch (TeaException e) {
            log.error("RobotGroupMessagesService_send orgGroupSendWithOptions throw TeaException, errCode={}, " +
                    "errorMessage={}", e.getCode(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("RobotGroupMessagesService_send orgGroupSendWithOptions throw Exception", e);
            throw e;
        }
    }
}