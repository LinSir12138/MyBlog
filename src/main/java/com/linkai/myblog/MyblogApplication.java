package com.linkai.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching          // 开启基于注解的缓存 @EnableCaching  放在主启动类上
public class MyblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }

}
