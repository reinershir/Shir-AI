package io.github.reinershir.ai.mapper;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.github.reinershir.ai.model.ChatHistory;
 @Repository
public interface StatisticsMapper extends BaseMapper<ChatHistory> {

	 public double selectToday();
	 
	 public double selectTotal();
	 
	 public int selectChatCountToday();
}
