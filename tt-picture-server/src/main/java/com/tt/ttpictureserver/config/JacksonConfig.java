package com.tt.ttpictureserver.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 全局配置
 * 解决 Long 类型精度丢失问题
 */
@Configuration
public class JacksonConfig {

    /**
     * 配置 Jackson 序列化规则
     * 将所有 Long 类型序列化为字符串,避免前端 JavaScript 精度丢失
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 将 Long 类型序列化为字符串
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            // 将基本类型 long 也序列化为字符串
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        };
    }
}
