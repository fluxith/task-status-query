package com.github.fluxith.taskstatusquery.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.fluxith.taskstatusquery.client.dto.event.RabbitEventConstant;

import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class RabbitMQConfig {
    /**
     * 【核心队列定义】 
     * durable=true 保证队列持久化（服务器重启后队列不丢失）
     * 小项目推荐至少配置一个主业务队列
     */
    @Bean
    public Queue taskQueue() {
        return new Queue(RabbitEventConstant.QUEUE_NAME, true);
    }

    /**
     * 【交换机定义】
     * 使用 Topic 类型交换机（可用通配符路由）
     * 其他类型：Direct（精确匹配）、Fanout（广播）
     */
    @Bean
    public TopicExchange taskExchange() {
        return new TopicExchange(RabbitEventConstant.EXCHANGE_NAME);
    }

    /**
     * 【队列与交换机绑定】
     * 将 demo.queue 绑定到 demo.exchange
     * "#" 表示匹配多个单词，"*" 匹配一个单词
     */
    @Bean
    public Binding demoBinding() {
        return BindingBuilder.bind(taskQueue())
                .to(taskExchange())
                .with(RabbitEventConstant.TOPIC_NAME + ".#");
    }

    /**
     * 【消息序列化配置（必须）】
     * 默认的Java序列化有安全问题且不可读
     * 使用JSON格式便于调试和跨语言交互
     */
    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
