package com.lp.demo.ai.core;

import retrofit2.Call;

import java.io.IOException;
import java.util.function.Function;

class SyncRequestExecutor<Response, ResponseContent> {

	private final Call<Response> call;

	private final Function<Response, ResponseContent> responseContentExtractor;

	SyncRequestExecutor(Call<Response> call, Function<Response, ResponseContent> responseContentExtractor) {
		this.call = call;
		this.responseContentExtractor = responseContentExtractor;
	}

	ResponseContent execute() {
		try {
			retrofit2.Response<Response> retrofitResponse = call.execute();
			if (retrofitResponse.isSuccessful()) {
				Response response = retrofitResponse.body();
				return responseContentExtractor.apply(response);
			}
			else {
				throw Utils.toException(retrofitResponse);
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
