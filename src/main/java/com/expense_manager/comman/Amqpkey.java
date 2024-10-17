package com.expense_manager.comman;

public enum Amqpkey {
    DirectExchange("myExchange"),
    Queue("myQueue"),
    RoutingKey("routingKey");

    private final String value;

    Amqpkey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
