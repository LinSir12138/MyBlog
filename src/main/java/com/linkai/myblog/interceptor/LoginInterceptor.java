package com.linkai.myblog.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    // 拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 未登录
        if (request.getSession().getAttribute("user") == null) {
            System.out.println("还未登录");
            // 重定向到登录页面
            response.sendRedirect("/user/login");
            return false;       // 不放心
        }
        System.out.println("已经登录了");
        System.out.println(request.getSession().getAttribute("user"));
        return true;
    }
}
