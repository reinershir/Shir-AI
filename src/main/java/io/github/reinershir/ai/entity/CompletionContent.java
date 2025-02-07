package io.github.reinershir.ai.entity;

import java.util.List;

import lombok.Data;

@Data
public class CompletionContent {
	private String id;
	private String object;
	private String model;
	
	private List<CompletionChoice> choices;
}
