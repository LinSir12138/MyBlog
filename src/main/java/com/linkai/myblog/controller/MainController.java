package com.linkai.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.linkai.myblog.entity.Blog;
import com.linkai.myblog.entity.Tag;
import com.linkai.myblog.service.BlogService;
import com.linkai.myblog.service.BlogtagService;
import com.linkai.myblog.service.TagService;
import com.linkai.myblog.service.TypeService;
import com.linkai.myblog.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Autowired
    TagService tagService;
    @Autowired
    BlogtagService blogtagService;

    // 跳转到 main  页面
    @RequestMapping({"/", "/main"})
    public String toMain(Model model) {
        List<Blog> blogs = blogService.queryAllByLimit(0, MyConstant.PAGE_SIZE_SHOWBLOG);
        // 从数据库中查询该篇博客对应的 type 对象，放入该 blog 对象中
        for (Blog b:blogs
             ) {
            b.setType(typeService.queryById(b.getBlogtypeid()));
        }
        model.addAttribute("blogs", blogs);

        // 同时查询共有多少条记录，为分页做准备
        int blogNumber = blogService.queryAllNumber();
        model.addAttribute("blogNumber", blogNumber);
        model.addAttribute("currentPage", 1);       // 表示当前页码为1


        return "main";
    }

    // 处理分页的点击事件
    @RequestMapping("/changePageBlog")
    public String changepageBLog(@RequestParam("currentPage") String currentPage, Model model) {
        int begin = (Integer.parseInt(currentPage) - 1) * MyConstant.PAGE_SIZE_SHOWBLOG;
        List<Blog> blogs = blogService.queryAllByLimit(begin, MyConstant.PAGE_SIZE_SHOWBLOG);
        // 同时查询该条博客记录对应的 type 对象，放入 blog 对象中
        for (Blog tempBlog:blogs
        ) {
            tempBlog.setType(typeService.queryById(tempBlog.getBlogtypeid()));
        }
        model.addAttribute("blogs", blogs);

        // 同时查询共有多少条记录，为分页做准备
        int blogNumber = blogService.queryAllNumber();
        model.addAttribute("blogNumber", blogNumber);
        model.addAttribute("currentPage", currentPage);       // 表示当前页码为1

        return "main";
    }

    // 跳转到文章详情页面
    @RequestMapping("/article/{id}")
    public String showArticle(@PathVariable("id") String id, Model model) {
        // 首先，更新该博客的 “阅读数量”
        blogService.updateViewsById(Long.valueOf(id));
        // 更新完之后再查询该博客
        Blog blog = blogService.queryById(Long.valueOf(id));
        blog.setType(typeService.queryById(blog.getBlogtypeid()));      // 给 blog 中的 type 对象赋值
        model.addAttribute("blog", blog);

        // 还有查询该博客对应的标签 (需要使用连表查询)
        List<Tag> tags = blogtagService.queryTagsByBlogId(blog.getBid());      // 获得标签名称
        model.addAttribute("tags", tags);

        return "article";
    }

    // 跳转的文章分类页面
    @RequestMapping("/Type")
    public String toType() {
        return "type";
    }

    @RequestMapping("/Statistic")
    @ResponseBody
    public String statistic() {
        int blogNumbers = blogService.queryAllNumber();     // 总博客数量
        int tagNumbers = tagService.queryAllNumber();      // 总标签数量
        int typeNumbers = typeService.queryAllNumber();     // 中分类数量

        Map<String, Integer> datamap = new HashMap<>();
        datamap.put("blogNumbers", blogNumbers);
        datamap.put("tagNumbers", tagNumbers);
        datamap.put("typeNumbers", typeNumbers);
        String str = JSON.toJSONString(datamap);
        return str;
    }
}
