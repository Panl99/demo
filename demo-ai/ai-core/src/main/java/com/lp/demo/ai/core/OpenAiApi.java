package com.lp.demo.ai.core;

import com.lp.demo.ai.core.chat.ChatCompletionRequest;
import com.lp.demo.ai.core.chat.ChatCompletionResponse;
import com.lp.demo.ai.core.completion.CompletionRequest;
import com.lp.demo.ai.core.completion.CompletionResponse;
import com.lp.demo.ai.core.embedding.EmbeddingRequest;
import com.lp.demo.ai.core.embedding.EmbeddingResponse;
import com.lp.demo.ai.core.image.GenerateImagesRequest;
import com.lp.demo.ai.core.image.GenerateImagesResponse;
import com.lp.demo.ai.core.models.ModelsResponse;
import com.lp.demo.ai.core.moderation.ModerationRequest;
import com.lp.demo.ai.core.moderation.ModerationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

interface OpenAiApi {

	@POST("completions")
	@Headers("Content-Type: application/json")
	Call<CompletionResponse> completions(@Body CompletionRequest request, @Query("api-version") String apiVersion);

	@POST("completions")
	@Headers("Content-Type: application/json")
	Call<CompletionResponse> completions(@HeaderMap Map<String, String> headers, @Body CompletionRequest request,
			@Query("api-version") String apiVersion);

	@POST("chat/completions")
	@Headers("Content-Type: application/json")
	Call<ChatCompletionResponse> chatCompletions(@Body ChatCompletionRequest request,
												 @Query("api-version") String apiVersion);

	@POST("chat/completions")
	@Headers("Content-Type: application/json")
	Call<ChatCompletionResponse> chatCompletions(@HeaderMap Map<String, String> headers,
			@Body ChatCompletionRequest request, @Query("api-version") String apiVersion);

	@POST("moderations")
	@Headers("Content-Type: application/json")
	Call<ModerationResponse> moderations(@Body ModerationRequest request, @Query("api-version") String apiVersion);

	@POST("moderations")
	@Headers("Content-Type: application/json")
	Call<ModerationResponse> moderations(@HeaderMap Map<String, String> headers, @Body ModerationRequest request,
			@Query("api-version") String apiVersion);

	@GET("models")
	@Headers("Content-Type: application/json")
	Call<ModelsResponse> models(@HeaderMap Map<String, String> headers, @Query("api-version") String apiVersion);

	@POST("embeddings")
	@Headers("Content-Type: application/json")
	Call<EmbeddingResponse> embeddings(@Body EmbeddingRequest request, @Query("api-version") String apiVersion);

	@POST("embeddings")
	@Headers("Content-Type: application/json")
	Call<EmbeddingResponse> embeddings(@HeaderMap Map<String, String> headers, @Body EmbeddingRequest request,
			@Query("api-version") String apiVersion);

	@POST("images/generations")
	@Headers({"Content-Type: application/json"})
	Call<GenerateImagesResponse> imagesGenerations(@Body GenerateImagesRequest request, @Query("api-version") String apiVersion
	);

	@POST("images/generations")
	@Headers({"Content-Type: application/json"})
	Call<GenerateImagesResponse> imagesGenerations(@HeaderMap Map<String, String> headers, @Body GenerateImagesRequest request, @Query("api-version") String apiVersion
	);

}
