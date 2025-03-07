package com.lp.demo.ai.core.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ContentType {

	@JsonProperty("text")
	TEXT, @JsonProperty("image_url")
	IMAGE_URL, @JsonProperty("input_audio")
	AUDIO

}
