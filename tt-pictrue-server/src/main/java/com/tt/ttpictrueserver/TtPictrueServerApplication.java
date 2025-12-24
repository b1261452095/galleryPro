package com.tt.ttpictrueserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tt.ttpictrueserver.mapper")
public class TtPictrueServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtPictrueServerApplication.class, args);
    }

}
