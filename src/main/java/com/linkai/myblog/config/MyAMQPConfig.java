package com.linkai.myblog.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAMQPConfig {

    /**
     *      更换服务器之后，需要配置交换器和队列
     *      交换器：   email.send
     *      队列：     email
     *      绑定：     email
     * */
    //  MessageConverter 是一个接口，我们选择它的实现类 Jackson2JsonMessageConverter 返回
    @Bean
    public MessageConverter messageConverter() {
        // org.springframework.amqp.support.converter.MessageConverter; 包下的
        return new Jackson2JsonMessageConverter();
    }
}
