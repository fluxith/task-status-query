package com.github.fluxith.taskstatusquery.infrastructure.event;

import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.cola.exception.Assert;
import com.github.fluxith.taskstatusquery.client.dto.co.ErrorCode;
import com.github.fluxith.taskstatusquery.client.dto.event.RabbitEventConstant;
import com.github.fluxith.taskstatusquery.client.dto.event.TaskProcessEvent;

import jakarta.annotation.Resource;

@Component
public class TaskProcessEventPublisher {
    @Resource
    private RabbitTemplate rabbitTemplate;
    public void publish(TaskProcessEvent taskProcessEvent) {
        Assert.isTrue(
            taskProcessEvent.getTaskId() != null,
            ErrorCode.S_TASK_ID_IS_EMPTY.getErrCode(),
            ErrorCode.S_TASK_ID_IS_EMPTY.getErrDesc()
        );
        // 发送到 task.exchange，路由键格式： task.queue.业务类型
        rabbitTemplate.convertAndSend(
            RabbitEventConstant.EXCHANGE_NAME,
            RabbitEventConstant.TOPIC_NAME + ".process", // 任务处理消息
            taskProcessEvent,
            new CorrelationData(UUID.randomUUID().toString())
        );
    }
}
