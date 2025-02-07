package io.github.reinershir.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.reinershir.ai.service.StatisticsService;
import io.github.reinershir.boot.common.Result;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Schema(description =  "Token 使用量统计")
@RequestMapping({"/gpt/statistics"})
public class StatisticsController {
	@Autowired
	StatisticsService statisticsService;

	@Schema(description = "get today tokens usage")
	@GetMapping("token/usage/today")
	public Result<Double> today(){
		return Result.ok(statisticsService.selectToday());
	}
	
	@Schema(description = "get total tokens usage")
	@GetMapping("token/usage/total")
	public Result<Double> total(){
		return Result.ok(statisticsService.selectTotal());
	}
	
	@Schema(description = "get chat count")
	@GetMapping("token/count/today")
	public Result<Integer> chatCount(){
		return Result.ok(statisticsService.selectChatCountToday());
	}
	
}
