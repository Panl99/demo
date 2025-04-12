package com.lp.demo.openfeign.consumer.controller;

import com.lp.demo.openfeign.consumer.client.ProviderClient;
import com.lp.demo.openfeign.consumer.dto.RequestDto;
import com.lp.demo.openfeign.consumer.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者服务的REST控制器
 */
@RestController
@RequestMapping("/api/consumer")
@Slf4j
public class ConsumerController {

    @Autowired
    ProviderClient providerClient;

    /**
     * 处理POST请求的方法
     *
     * @param requestDto 请求体中的数据
     * @return处理后的响应对象
     */
    @PostMapping("/process")
    public ResponseDto processRequest(@RequestBody RequestDto requestDto) {
        ResponseDto responseDto = providerClient.processRequest(requestDto);
        log.info("[ConsumerController] Received response from Provider Service: {}", responseDto.getMessage());
        return responseDto;
    }
}