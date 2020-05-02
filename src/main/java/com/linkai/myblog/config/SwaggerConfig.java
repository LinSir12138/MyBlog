package com.linkai.myblog.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
* @Description: Swagger2 的配置类
* @Param:  
* @return:  
* @Author: 林凯
* @Date: 2020/5/1 
*/ 
@Configuration
@EnableSwagger2     // 开启 Swagger2
public class SwaggerConfig {

    // 配置了 SwaggerBean 实例
    @Bean       // 注册一个bean
    public Docket docket(Environment environment) {

        // 获取项目的环境；dev， pro
        Profiles profiles = Profiles.of("dev");
        // 判断是否在 dev 环境中,只在开发环境使用 Swagger
        boolean flag = environment.acceptsProfiles(profiles);

        // 看源码，传递参数
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("JackLin")
                // 是否启用 Swagger
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.linkai.myblog.controller"))
                .build();       // build -》 工厂模式
    }


    // 配置 Swagger 信息 apiInfo， 按照 ApiInfo 类里面的方法来填写
    private ApiInfo apiInfo() {
        // 作者信息
        Contact contact = new Contact("JackLin", "http://linkaiblog.top", "jacklin.it@qq.com");

        // 点进 ApiInfo 里面去看源码，才知道如何传递参数
        return new ApiInfo(
                "JackLin 的SwaggerAPI文档",
                "talk is cheap,show me the code!",
                "1.0",
                "http://linkaiblog.top",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList());
    }


}
