package io.github.reinershir.ai.service.impl;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.reinershir.ai.mapper.ChatSessionMapper;
import io.github.reinershir.ai.model.ChatSession;
import io.github.reinershir.ai.service.ChatSessionService;

@Service("chatSessionService")
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper,ChatSession> implements ChatSessionService{


}
