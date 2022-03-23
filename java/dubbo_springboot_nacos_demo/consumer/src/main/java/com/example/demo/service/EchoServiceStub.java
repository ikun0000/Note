package com.example.demo.service;

import common.api.Echo;
import org.springframework.stereotype.Service;

@Service
public class EchoServiceStub implements Echo {

    private final Echo echo;

    public EchoServiceStub(Echo echo) {
        this.echo = echo;
    }

    @Override
    public String echo(String data) {
        try {
            return echo.echo(data);
        } catch (Exception e) {
            return "[echo stub]";
        }
    }
}
