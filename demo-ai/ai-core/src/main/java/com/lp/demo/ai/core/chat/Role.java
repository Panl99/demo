package com.lp.demo.ai.core.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {

	@JsonProperty("system")
	SYSTEM, @JsonProperty("user")
	USER, @JsonProperty("assistant")
	ASSISTANT, @JsonProperty("tool")
	TOOL, @JsonProperty("function")
	@Deprecated
	FUNCTION

}
