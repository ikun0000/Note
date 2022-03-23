package com.example.demo.controller;

import org.apache.dubbo.remoting.TimeoutException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RPCTimeOutHandler {

    @ExceptionHandler({TimeoutException.class})
    @ResponseBody
    public String timeOut() {
        return "Timeout!";
    }
}
