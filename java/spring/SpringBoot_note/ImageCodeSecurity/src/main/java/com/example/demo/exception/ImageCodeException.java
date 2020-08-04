package com.example.demo.exception;

import org.springframework.security.core.AuthenticationException;

public class ImageCodeException extends AuthenticationException {
    public ImageCodeException(String explanation) {
        super(explanation);
    }
}
