package com.lp.demo.openfeign.provider.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String message;
    private String name;
    private int age;
}