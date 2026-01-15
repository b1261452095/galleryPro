package com.tt.ttpictureserver.message.consumer;

import com.tt.ttpictureserver.manager.CosManager;
import com.tt.ttpictureserver.mapper.PictureMapper;
import com.tt.ttpictureserver.message.dto.DeleteMessage;
import com.tt.ttpictureserver.model.picture.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * 图片删除消息消费者
 * 负责监听队列并处理删除任务
 * 
 * @author bianhongbin
 * @date 2026-01-15
 */
@Slf4j
@Component
public class PictureDeleteConsumer {

    @Resource
    private CosManager cosManager;

    @Resource
    private PictureMapper pictureMapper;

    @Resource
    private PictureService pictureService;

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRY_COUNT = 3;

    /**
     * 监听删除队列,处理删除消息
     * 
     * @param message 删除消息
     */
    @RabbitListener(queues = "picture-delete-queue")
    public void handleDeleteMessage(DeleteMessage message) {
        log.info("========== 开始处理删除消息 ==========");
        log.info("图片ID: {}", message.getPictureId());
        log.info("图片URL: {}", message.getUrl());
        log.info("重试次数: {}", message.getRetryCount());

        try {
            // 1. 检查重试次数
            if (message.getRetryCount() >= MAX_RETRY_COUNT) {
                log.error("❌ 重试次数超过限制,放弃处理: pictureId={}, retryCount={}",
                        message.getPictureId(), message.getRetryCount());
                // 不抛异常,消息会被确认并删除
                return;
            }

            // 2. 删除 OSS 文件
            String fileKey = pictureService.extractKeyFromUrl(message.getUrl());
            if (fileKey != null && !fileKey.isEmpty()) {
                log.info("准备删除 OSS 文件: {}", fileKey);
                cosManager.deleteObject(fileKey);
                log.info("✅ OSS 文件删除成功: {}", fileKey);
            } else {
                log.warn("⚠️ 文件 key 为空,跳过 OSS 删除");
            }

            // 3. 物理删除数据库记录
            log.info("准备删除数据库记录: {}", message.getPictureId());
            pictureMapper.deletePictureByIds(Collections.singletonList(message.getPictureId()));
            log.info("✅ 数据库记录删除成功: {}", message.getPictureId());

            log.info("========== 删除消息处理完成 ==========");

            // 方法正常结束,Spring 自动 ACK,消息从队列删除

        } catch (Exception e) {
            // 增加重试次数
            message.setRetryCount(message.getRetryCount() + 1);

            log.error("❌ 删除失败,第 {} 次重试: pictureId={}",
                    message.getRetryCount(), message.getPictureId(), e);

            // 抛出异常,Spring 自动 NACK,消息重新入队
            throw new RuntimeException("删除失败,将重试", e);
        }
    }
}
