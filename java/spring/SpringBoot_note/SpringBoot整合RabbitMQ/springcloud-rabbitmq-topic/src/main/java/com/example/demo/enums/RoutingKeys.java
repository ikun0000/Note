package com.example.demo.enums;

public enum RoutingKeys {
    USER_INFO("user.log.info"),
    PRODUCT_INFO("prod.log.info"),
    ORDER_INFO("order.log.info"),
    USER_ERROR("user.log.error"),
    PRODUCT_ERROR("prod.log.error"),
    ORDER_ERROR("order.log.error");

    private String routingKey;

    RoutingKeys(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
