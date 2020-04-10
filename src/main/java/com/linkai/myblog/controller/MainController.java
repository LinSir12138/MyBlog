package com.linkai.myblog.controller;

import com.linkai.myblog.entity.Blog;
import com.linkai.myblog.service.BlogService;
import com.linkai.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
* @Description: 处理主页面相关的Controller
* @Param:
* @return:
* @Author: 林凯
* @Date: 2020/3/29
*/
@Controller
public class MainController {

    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;

    // 跳转到 main  页面
    @RequestMapping({"/", "/main"})
    public String toMain(Model model) {
        List<Blog> blogs = blogService.queryAllByLimit(0, 7);
        // 从数据库中查询该篇博客对应的 type 对象，放入该 blog 对象中
        for (Blog b:blogs
             ) {
            b.setType(typeService.queryById(b.getBlogtypeid()));
        }
        model.addAttribute("blogs", blogs);
        return "main";
    }

    // 跳转到文章详情页面
    @RequestMapping("/article/{id}")
    public String showArticle(@PathVariable("id") String id, Model model) {
//        Blog blog = blogService.queryById(id);
//        model.addAttribute("blog", blog);

        Blog blog = blogService.queryById(Long.valueOf(id));
        model.addAttribute("blog", blog);
        return "article";
    }

    // 跳转的文章分类页面
    @RequestMapping("/Type")
    public String toType() {
        return "type";
    }
}
