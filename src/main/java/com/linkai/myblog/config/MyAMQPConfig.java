package com.linkai.myblog.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAMQPConfig {

    //  MessageConverter 是一个接口，我们选择它的实现类 Jackson2JsonMessageConverter 返回
    @Bean
    public MessageConverter messageConverter() {
        // org.springframework.amqp.support.converter.MessageConverter; 包下的
        return new Jackson2JsonMessageConverter();
    }
}
