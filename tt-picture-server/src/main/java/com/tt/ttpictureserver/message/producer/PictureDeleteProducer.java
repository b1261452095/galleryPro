package com.tt.ttpictureserver.message.producer;

import com.tt.ttpictureserver.message.dto.DeleteMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 图片删除消息生产者
 * 负责发送删除消息到队列
 * 
 * @author bianhongbin
 * @date 2026-01-15
 */
@Slf4j
@Component
public class PictureDeleteProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "picture-delete-queue";

    /**
     * 发送删除消息
     * 
     * @param pictureId 图片ID
     * @param url       图片URL
     */
    public void sendDeleteMessage(Long pictureId, String url) {
        try {
            // 创建消息对象
            DeleteMessage message = new DeleteMessage(pictureId, url);

            // 发送消息到队列
            rabbitTemplate.convertAndSend(QUEUE_NAME, message);

            log.info("✅ 发送删除消息成功: pictureId={}, url={}", pictureId, url);

        } catch (Exception e) {
            log.error("❌ 发送删除消息失败: pictureId={}", pictureId, e);
            throw new RuntimeException("发送删除消息失败", e);
        }
    }
}
