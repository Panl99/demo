package com.lp.demo.ai.core;

import java.util.function.Consumer;

public interface SyncOrAsyncOrStreaming<ResponseContent> extends SyncOrAsync<ResponseContent> {

	StreamingResponseHandling onPartialResponse(Consumer<ResponseContent> partialResponseHandler);

}
