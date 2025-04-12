package com.lp.demo.openfeign.provider.controller;

import com.lp.demo.openfeign.provider.dto.RequestDto;
import com.lp.demo.openfeign.provider.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider")
@Slf4j
public class ProviderController {

    /**
     * 处理POST请求的方法
     *
     * @param requestDto 请求体中的数据
     * @return处理后的响应对象
     */
    @PostMapping("/process")
    public ResponseDto processRequest(@RequestBody RequestDto requestDto) {
        log.info("[ProviderController] Received request with name: {} and age: {}", requestDto.getName(), requestDto.getAge());

        // 构建响应对象
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("[ProviderController] Processed request for " + requestDto.getName() + " who is " + requestDto.getAge() + " years old.");
        responseDto.setName(requestDto.getName());
        responseDto.setAge(requestDto.getAge());
        return responseDto;
    }
}