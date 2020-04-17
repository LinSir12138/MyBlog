package com.linkai.myblog.config;

import com.linkai.myblog.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 记得还要加注解
@Configuration      // Java 配置类 （JavaConfig，这个类奇偶相当于原来的 xml 配置文件）
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    // 自定义视图控制器
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("main");
    }


    // 配置拦截器，过滤器相关
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对 admin 下面的所有请求进行拦截；
        /**
         *      首先，将我们自定义的拦截器添加过来
         *      对 admin 下面的所有请求进行拦截；
         *      放行下面2个：
         *      ① 提交form表单时要放行   /admin/adminMain
         * */
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/adminMain");
    }
}
