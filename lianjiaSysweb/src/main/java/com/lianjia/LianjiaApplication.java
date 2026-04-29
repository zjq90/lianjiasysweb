package com.lianjia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lianjia.mapper")
public class LianjiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LianjiaApplication.class, args);
    }

}
