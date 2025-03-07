package com.lp.demo.ai.core.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ToolChoiceMode {

	@JsonProperty("none")
	NONE, @JsonProperty("auto")
	AUTO, @JsonProperty("required")
	REQUIRED

}
