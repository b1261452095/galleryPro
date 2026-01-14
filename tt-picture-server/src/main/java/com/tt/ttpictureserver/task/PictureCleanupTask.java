package com.tt.ttpictureserver.task;

import com.tt.ttpictureserver.config.PictureDeleteConfig;
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
     * 使用配置文件中的 cron 表达式
     */
    @Scheduled(cron = "${picture.delete.cleanup-cron:0 0 3 * * ?}")
    public void cleanupExpiredPictures() {
        log.info("========== 开始执行图片清理定时任务 ==========");
        log.info("当前删除策略: {}", pictureDeleteConfig.getStrategy().getDescription());
        log.info("保留天数: {} 天", pictureDeleteConfig.getRetentionDays());

        try {
            pictureService.cleanupExpiredPictures();
            log.info("========== 图片清理定时任务执行完成 ==========");
        } catch (Exception e) {
            log.error("========== 图片清理定时任务执行失败 ==========", e);
        }
    }
}
