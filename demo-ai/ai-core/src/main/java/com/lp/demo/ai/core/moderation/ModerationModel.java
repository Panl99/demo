package com.lp.demo.ai.core.moderation;

public enum ModerationModel {

	TEXT_MODERATION_STABLE("text-moderation-stable"), TEXT_MODERATION_LATEST("text-moderation-latest");

	private final String value;

	ModerationModel(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
