package io.github.reinershir.ai.mapper;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.github.reinershir.ai.model.ChatSession;

@Repository
public interface ChatSessionMapper extends BaseMapper<ChatSession> {

}
