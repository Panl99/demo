package com.lp.demo.openfeign.consumer.fallback;

import com.lp.demo.openfeign.consumer.client.ProviderClient;
import com.lp.demo.openfeign.consumer.dto.RequestDto;
import com.lp.demo.openfeign.consumer.dto.ResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProviderClientFallback implements ProviderClient {

    @Override
    public ResponseDto processRequest(RequestDto requestDto) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("fallback-providerService");
        return responseDto;
    }
}