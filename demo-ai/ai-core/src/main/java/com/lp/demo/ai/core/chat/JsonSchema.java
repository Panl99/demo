package com.lp.demo.ai.core.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = JsonSchema.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JsonSchema {

	@JsonProperty
	private final String name;

	@JsonProperty
	private final Boolean strict;

	@JsonProperty
	private final JsonObjectSchema schema;

	public JsonSchema(Builder builder) {
		this.name = builder.name;
		this.strict = builder.strict;
		this.schema = builder.schema;
	}

	@Override
	public boolean equals(Object another) {
		if (this == another)
			return true;
		return another instanceof JsonSchema && equalTo((JsonSchema) another);
	}

	private boolean equalTo(JsonSchema another) {
		return Objects.equals(name, another.name) && Objects.equals(strict, another.strict)
				&& Objects.equals(schema, another.schema);
	}

	@Override
	public int hashCode() {
		int h = 5381;
		h += (h << 5) + Objects.hashCode(name);
		h += (h << 5) + Objects.hashCode(strict);
		h += (h << 5) + Objects.hashCode(schema);
		return h;
	}

	@Override
	public String toString() {
		return "JsonSchema{" + "name=" + name + ", strict=" + strict + ", schema=" + schema + "}";
	}

	public static Builder builder() {
		return new Builder();
	}

	@JsonPOJOBuilder(withPrefix = "")
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Builder {

		private String name;

		private Boolean strict;

		private JsonObjectSchema schema;

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder strict(Boolean strict) {
			this.strict = strict;
			return this;
		}

		public Builder schema(JsonObjectSchema schema) {
			this.schema = schema;
			return this;
		}

		public JsonSchema build() {
			return new JsonSchema(this);
		}

	}

}
