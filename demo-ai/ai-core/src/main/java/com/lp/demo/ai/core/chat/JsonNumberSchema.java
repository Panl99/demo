package com.lp.demo.ai.core.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = JsonNumberSchema.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JsonNumberSchema extends JsonSchemaElement {

	@JsonProperty
	private final String description;

	public JsonNumberSchema(Builder builder) {
		super("number");
		this.description = builder.description;
	}

	@Override
	public boolean equals(Object another) {
		if (this == another)
			return true;
		return another instanceof JsonNumberSchema && equalTo((JsonNumberSchema) another);
	}

	private boolean equalTo(JsonNumberSchema another) {
		return Objects.equals(description, another.description);
	}

	@Override
	public int hashCode() {
		int h = 5381;
		h += (h << 5) + Objects.hashCode(description);
		return h;
	}

	@Override
	public String toString() {
		return "JsonNumberSchema{" + "description=" + description + "}";
	}

	public static Builder builder() {
		return new Builder();
	}

	@JsonPOJOBuilder(withPrefix = "")
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Builder {

		private String description;

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public JsonNumberSchema build() {
			return new JsonNumberSchema(this);
		}

	}

}
