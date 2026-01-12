package com.tt.ttpictureserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.tt.ttpictureserver.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) // 代理
public class TtPictrueServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtPictrueServerApplication.class, args);
    }

}
