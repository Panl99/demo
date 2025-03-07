package com.lp.demo.ai.core.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ResponseFormatType {

	@JsonProperty("text")
	TEXT, @JsonProperty("json_object")
	JSON_OBJECT, @JsonProperty("json_schema")
	JSON_SCHEMA

}
