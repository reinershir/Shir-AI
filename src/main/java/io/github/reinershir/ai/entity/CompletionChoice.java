package io.github.reinershir.ai.entity;

import lombok.Data;

@Data
public class CompletionChoice {

	private String text;
	private Integer index;
	private float logprobs;
	private String finish_reason;
}
