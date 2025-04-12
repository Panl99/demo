package com.lp.demo.openfeign.consumer.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String message;
    private String name;
    private int age;
}