package com.github.indeednb.taskstatusquery.client.dto.event;

public class RabbitEventConstant {
    private RabbitEventConstant(){}
    public static final String TOPIC_NAME = "task";
    public static final String QUEUE_NAME = "task.queue";
    public static final String EXCHANGE_NAME = "task.exchange";
}
