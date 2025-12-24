package com.tt.ttpictrueserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j 配置类
 */
@Slf4j
@Configuration
public class Knife4jConfig implements ApplicationRunner {

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("图片服务 API 文档")
                        .description("TT Picture Server API 接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("TT")
                                .email("your-email@example.com")));
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("========================================");
        log.info("Knife4j 文档地址: http://localhost:{}{}/doc.html", port, contextPath);
        log.info("========================================");
    }
}
