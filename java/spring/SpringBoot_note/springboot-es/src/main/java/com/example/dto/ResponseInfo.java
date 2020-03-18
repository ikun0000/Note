package com.example.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseInfo {
    private Integer code;
    private String message;
    private Object resultData;
}
