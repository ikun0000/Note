package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TTLQueueConfig {
    public static final String QA = "QA";
    public static final String QB = "QB";
    public static final String QD = "QD";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String YD = "YD";
    public static final String XA = "XA";
    public static final String XB = "XB";

    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X, true, false);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y, true, false);
    }

    @Bean("qaQueue")
    public Queue qaQueue() {
        return QueueBuilder.durable(QA)
                .ttl(10000)
                .deadLetterExchange(Y)
                .deadLetterRoutingKey(YD)
                .build();
    }

    @Bean("qbQueue")
    public Queue qbQueue() {
        return QueueBuilder.durable(QB)
                .ttl(40000)
                .deadLetterExchange(Y)
                .deadLetterRoutingKey(YD)
                .build();
    }

    @Bean("qdQueue")
    public Queue qdQueue() {
        return QueueBuilder.durable(QD).build();
    }

    @Bean("qaBindX")
    public Binding qaBindX(@Qualifier("qaQueue") Queue queueA,
                           @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with(XA);
    }

    @Bean("qbBindX")
    public Binding qbBindX(@Qualifier("qbQueue") Queue queueB,
                           @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with(XB);
    }

    @Bean("qdBindY")
    public Binding qdBindY(@Qualifier("qdQueue") Queue queueD,
                           @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with(YD);
    }
}
