package com.tt.ttpictureserver.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 * 
 * @author bianhongbin
 * @date 2026-01-15
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 图片删除队列名称
     */
    public static final String PICTURE_DELETE_QUEUE = "picture-delete-queue";

    /**
     * 创建图片删除队列
     * 
     * @return Queue
     */
    @Bean
    public Queue pictureDeleteQueue() {
        // 参数说明:
        // 1. name: 队列名称
        // 2. durable: true = 持久化,RabbitMQ 重启后队列不会丢失
        // 3. exclusive: false = 不独占,其他连接也可以访问
        // 4. autoDelete: false = 不自动删除,即使没有消费者也保留队列
        return new Queue(PICTURE_DELETE_QUEUE, true, false, false);
    }
}
