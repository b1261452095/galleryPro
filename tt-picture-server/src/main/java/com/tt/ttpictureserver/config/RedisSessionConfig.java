// @AI-Begin Q8RT3 20260511 @@Qoder
package com.tt.ttpictureserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Redis Session 配置类
 *
 * 启用 Redis 存储 Session，重启服务后登录状态不丢失
 */
@Slf4j
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {

    public RedisSessionConfig() {
        log.info("✅ Redis Session 配置成功，登录状态已持久化到 Redis（30分钟无操作过期）");
    }
}
// @AI-End Q8RT3 20260511 @@Qoder

