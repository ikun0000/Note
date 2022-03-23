package com.example.demo.service;

import common.api.Echo;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@DubboService(methods = {@Method(name = "echo", retries = 3, timeout = 2000)},
        version = "v2",
//        delay = 500,
        token = "true")
public class EchoServiceImpl implements Echo {

    private static final Logger logger = LoggerFactory.getLogger(EchoServiceImpl.class);

    @Override
    public String echo(String data) {
        logger.info("remote invoke");
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "[echo v2]: " + data;
    }
}
