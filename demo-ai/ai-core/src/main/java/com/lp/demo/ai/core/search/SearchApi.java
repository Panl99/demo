package com.lp.demo.ai.core.search;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SearchApi {

	@POST("web-search")
	@Headers("Content-Type: application/json")
	Call<SearchResponse> webSearch(@Body SearchRequest request);

}
