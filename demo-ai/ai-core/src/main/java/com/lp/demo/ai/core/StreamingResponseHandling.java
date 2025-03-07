package com.lp.demo.ai.core;

public interface StreamingResponseHandling extends AsyncResponseHandling {

	StreamingCompletionHandling onComplete(Runnable streamingCompletionCallback);

}
