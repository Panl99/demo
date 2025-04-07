package com.lp.demo.ai.core.chat;

import lombok.Getter;

public enum ChatCompletionModel {

	/**
	 * deepseek v3
	 */
	DEEPSEEK_CHAT("deepseek-chat"),
	/**
	 * deepseek r1
	 */
	DEEPSEEK_REASONER("deepseek-reasoner"),

	/**
	 * local deployment
	 */
	DEEPSEEK_R1_7B("deepseek-r1:7b"),

	QWEN_2_5_7B("qwen2.5:7b"),
	;

	@Getter
	private final String value;

	ChatCompletionModel(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
