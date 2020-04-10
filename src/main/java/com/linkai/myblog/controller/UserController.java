package com.linkai.myblog.controller;

import com.linkai.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
* @Description: 处理后台管理员登录相关的 Controller
* @Param:
* @return:
* @Author: 林凯
* @Date: 2020/3/29
*/
@Controller
public class UserController {
    /**
     * 服务对象
     */
    @Autowired
    private UserService userService;


    // 跳转到后台登录页面
//    @RequestMapping("/toLogin")
    @RequestMapping("/user/login")
    public String toLogin() {
        System.out.println("来到登录页面");
        return "/login";
    }

    // 执行登录，检测用户名密码
    @RequestMapping("/admin/adminMain")
    public String login(@RequestParam("username") String username, @RequestParam("userpassword") String userpassword, HttpSession session, Model model) {
        String userpasswordInDb = userService.queryPasswordByUsername(username);
        System.out.println(userpasswordInDb);
        if (userpassword.equals(userpasswordInDb)) {
            // 登录成功，将用户名放入 Session 中
            session.setAttribute("user", username);
            System.out.println("Session中获取" + session.getAttribute("user"));
            System.out.println("登录成功");
            return "redirect:/admin/Blog";      // 重定向，经过Controller，从数据库中查询数据
        } else {
            model.addAttribute("error", "用户名或密码错误");
            System.out.println("用户名密码错误");
            return "login";
        }
    }


    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        // 将用户从 session 中移除
        session.removeAttribute("user");
        return "login";
    }

}