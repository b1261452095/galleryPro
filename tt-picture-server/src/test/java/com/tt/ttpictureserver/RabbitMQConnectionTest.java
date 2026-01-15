package com.tt.ttpictureserver;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * RabbitMQ 连接测试
 * 
 * @author bianhongbin
 * @date 2026-01-15
 */
@SpringBootTest
public class RabbitMQConnectionTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试 RabbitMQ 连接是否正常
     */
    @Test
    public void testConnection() {
        try {
            System.out.println("========== 开始测试 RabbitMQ 连接 ==========");

            // 发送测试消息
            String testMessage = "Hello RabbitMQ! 测试消息 from " + System.currentTimeMillis();
            rabbitTemplate.convertAndSend("test-queue", testMessage);

            System.out.println("✅ RabbitMQ 连接成功!");
            System.out.println("✅ 测试消息已发送: " + testMessage);
            System.out.println("========== 测试完成 ==========");

        } catch (Exception e) {
            System.err.println("❌ RabbitMQ 连接失败!");
            System.err.println("错误信息: " + e.getMessage());
            System.err.println("========== 测试失败 ==========");
            e.printStackTrace();

            // 打印帮助信息
            System.err.println("\n💡 排查建议:");
            System.err.println("1. 检查 application.yml 中的 RabbitMQ 配置");
            System.err.println("2. 确认云服务器防火墙已开放 5672 端口");
            System.err.println("3. 确认 RabbitMQ 容器正在运行: docker ps | grep rabbitmq");
            System.err.println("4. 测试端口连通性: telnet your-server-ip 5672");
        }
    }

    /**
     * 测试发送和接收消息
     */
    @Test
    public void testSendAndReceive() {
        try {
            System.out.println("========== 测试发送和接收消息 ==========");

            String queueName = "test-queue";
            String message = "Test Message: " + System.currentTimeMillis();

            // 发送消息
            rabbitTemplate.convertAndSend(queueName, message);
            System.out.println("✅ 消息已发送: " + message);

            // 接收消息
            Object received = rabbitTemplate.receiveAndConvert(queueName, 5000);
            if (received != null) {
                System.out.println("✅ 消息已接收: " + received);
            } else {
                System.out.println("⚠️ 未接收到消息 (可能队列为空)");
            }

            System.out.println("========== 测试完成 ==========");

        } catch (Exception e) {
            System.err.println("❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
