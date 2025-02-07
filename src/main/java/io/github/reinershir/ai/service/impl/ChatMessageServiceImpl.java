package io.github.reinershir.ai.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.reinershir.ai.mapper.ChatMessageMapper;
import io.github.reinershir.ai.model.ChatMessage;
import io.github.reinershir.ai.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {



}
