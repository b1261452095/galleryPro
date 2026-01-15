package com.tt.ttpictureserver.task;

import com.tt.ttpictureserver.config.PictureDeleteConfig;
import com.tt.ttpictureserver.enums.DeleteStrategyEnum;
import com.tt.ttpictureserver.model.picture.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 图片清理定时任务
 * 
 * @author NoBug
 */
@Slf4j
@Component
public class PictureCleanupTask {

    @Resource
    private PictureService pictureService;

    @Resource
    private PictureDeleteConfig pictureDeleteConfig;

    /**
     * 定时清理过期的软删除图片
     * 注意: 仅在 SOFT_DELETE 策略下执行
     * ASYNC 策略由 RabbitMQ 消费者处理,不需要定时任务
     */
    @Scheduled(cron = "${picture.delete.cleanup-cron:0 0 3 * * ?}")
    public void cleanupExpiredPictures() {
        // 检查删除策略
        DeleteStrategyEnum strategy = pictureDeleteConfig.getStrategy();

        // 只有软删除策略才执行定时清理
        if (strategy != DeleteStrategyEnum.SOFT_DELETE) {
            log.debug("当前策略为 {}, 跳过定时清理任务", strategy.getDescription());
            return;
        }

        log.info("========== 开始执行图片清理定时任务 ==========");
        log.info("当前删除策略: {}", strategy.getDescription());
        log.info("保留天数: {} 天", pictureDeleteConfig.getRetentionDays());

        try {
            pictureService.cleanupExpiredPictures();
            log.info("========== 图片清理定时任务执行完成 ==========");
        } catch (Exception e) {
            log.error("========== 图片清理定时任务执行失败 ==========", e);
        }
    }
}
