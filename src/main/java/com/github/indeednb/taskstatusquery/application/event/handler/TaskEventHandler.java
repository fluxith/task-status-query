package com.github.indeednb.taskstatusquery.application.event.handler;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.github.indeednb.taskstatusquery.client.dto.event.RabbitEventConstant;
import com.github.indeednb.taskstatusquery.client.dto.event.TaskProcessEvent;
import com.github.indeednb.taskstatusquery.domain.taskprocess.TaskProcess;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaskEventHandler {
    @RabbitListener(queues = RabbitEventConstant.QUEUE_NAME, concurrency = "2-5")
    public void handlerTaskProsessEvent(TaskProcessEvent taskProcessEvent, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{
        log.info("收到任务处理事件：{}", taskProcessEvent);
        try {
            TaskProcess taskProcess = TaskProcess.valueOf(taskProcessEvent.getTaskId());
            taskProcess.process();
        } catch (Exception e) {
            log.error("任务处理事件处理失败：{}", taskProcessEvent, e);
            e.printStackTrace();
        }
        log.info("手动确认");
        channel.basicAck(tag, false);
    }
}
