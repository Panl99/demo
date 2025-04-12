package com.lp.demo.openfeign.consumer.client;

import com.lp.demo.openfeign.consumer.config.FeignConfig;
import com.lp.demo.openfeign.consumer.dto.RequestDto;
import com.lp.demo.openfeign.consumer.dto.ResponseDto;
import com.lp.demo.openfeign.consumer.fallback.ProviderClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign客户端接口，用于调用Provider Service的API
 */
@FeignClient(name = "demo-openfeign-provider",
        url = "http://localhost:8081",
        configuration = FeignConfig.class,
        fallback = ProviderClientFallback.class)
public interface ProviderClient {

    /**
     * 调用Provider Service的/process接口
     *
     * @param requestDto 请求体中的数据
     * @return处理后的响应对象
     */
    @PostMapping("/api/provider/process")
    ResponseDto processRequest(@RequestBody RequestDto requestDto);
}