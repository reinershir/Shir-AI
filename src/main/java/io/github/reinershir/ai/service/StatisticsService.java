package io.github.reinershir.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;

import io.github.reinershir.ai.model.ChatHistory;

public interface StatisticsService extends IService<ChatHistory> {
	public double selectToday();

	public double selectTotal();
	
	public int selectChatCountToday();
}
