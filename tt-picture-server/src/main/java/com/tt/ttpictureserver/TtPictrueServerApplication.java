package com.tt.ttpictureserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.tt.ttpictureserver.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) // 代理
@EnableScheduling // 启用定时任务
public class TtPictrueServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtPictrueServerApplication.class, args);
    }

}
