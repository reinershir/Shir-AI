package io.github.reinershir.ai.service.impl;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.reinershir.ai.mapper.StatisticsMapper;
import io.github.reinershir.ai.model.ChatHistory;
import io.github.reinershir.ai.service.StatisticsService;

@Service("statisticsService")
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper,ChatHistory> implements StatisticsService{

	@Override
	public double selectToday() {
		Double value = baseMapper.selectToday();
		return value==null?0d:value;
	}

	@Override
	public double selectTotal() {
		return baseMapper.selectTotal();
	}

	@Override
	public int selectChatCountToday() {
		return baseMapper.selectChatCountToday();
	}


}
