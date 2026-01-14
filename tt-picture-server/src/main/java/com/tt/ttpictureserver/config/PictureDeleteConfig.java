package com.tt.ttpictureserver.config;

import com.tt.ttpictureserver.enums.DeleteStrategyEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 图片删除配置
 * 
 * @author NoBug
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "picture.delete")
public class PictureDeleteConfig {

    /**
     * 删除策略: IMMEDIATE(立即删除), SOFT_DELETE(软删除), ASYNC(异步删除)
     * 默认使用软删除策略
     */
    private DeleteStrategyEnum strategy = DeleteStrategyEnum.SOFT_DELETE;

    /**
     * 软删除保留天数（仅在 SOFT_DELETE 策略下生效）
     * 默认7天
     */
    private Integer retentionDays = 7;

    /**
     * 定时清理任务 cron 表达式
     * 默认每天凌晨3点执行
     */
    private String cleanupCron = "0 0 3 * * ?";

    /**
     * 是否启用异步删除（仅在 ASYNC 策略下生效）
     */
    private Boolean asyncEnabled = false;
}
