package com.tt.ttpictureserver.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description: cos客户端配置类
 * @date 2026-01-09 11:34
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "cos.client")
@Data
public class CosClientConfig {

    /**
     * cos 域名
     */
    private String host;

    /**
     * secretId
     */
    private String secretId;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 地区
     */
    private String region;

    /**
     * bucket 名称
     */
    private String bucket;



    /**
     * 创建 COSClient Bean
     */
    @Bean
    public COSClient cosClient() {
        log.info("初始化cos客户端");
        log.info("secretId: {}, region: {}, bucket: {}", secretId, region, bucket);

        // 1 初始化用户信息
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端
        return new COSClient(cred, clientConfig);

    }
}
