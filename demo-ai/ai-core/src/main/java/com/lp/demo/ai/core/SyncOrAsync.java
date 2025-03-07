package com.lp.demo.ai.core;

import java.util.function.Consumer;

public interface SyncOrAsync<ResponseContent> {

	ResponseContent execute();

	void onResponse(Consumer<ResponseContent> responseHandler, Consumer<Throwable> errorHandler);

}
