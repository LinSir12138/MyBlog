package com.linkai.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.linkai.myblog.entity.Blog;
import com.linkai.myblog.entity.Blogtag;
import com.linkai.myblog.entity.Tag;
import com.linkai.myblog.entity.Type;
import com.linkai.myblog.service.BlogService;
import com.linkai.myblog.service.BlogtagService;
import com.linkai.myblog.service.TagService;
import com.linkai.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * (Blog)表控制层
 *
 * @author 林凯
 * @since 2020-03-29 18:49:15
 */
@Controller
@RequestMapping("/admin")       // 所有操作博客相关的，都需要添加 admin,这样拦截器就可以进行拦截
public class BlogController {
    /**
     * 服务对象
     */
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogtagService blogtagService;


    // 跳转到博客管理界面
    @RequestMapping("/Blog")
    public String blog(Model model) {
        // 分页查询后7条博客记录(这个查询是连表查询，可以查询从对应博客的分类)
        List<Blog> blogs = blogService.queryAllByLimit(0, 7);
        // 同时查询该条博客记录对应的 type 对象，放入 blog 对象中
        for (Blog tempBlog:blogs
             ) {
            tempBlog.setType(typeService.queryById(tempBlog.getBlogtypeid()));
        }
        for (int i = 0; i < blogs.size(); i++) {
            blogs.get(i).setType(typeService.queryById(blogs.get(i).getBlogtypeid()));
        }
        model.addAttribute("blogs", blogs);

        // 同时查询共有多少条记录，为分页做准备
        int blogNumber = blogService.queryAllNumber();
        model.addAttribute("blogNumber", blogNumber);
        model.addAttribute("currentPage", 1);       // 表示当前页码为1

        return "/admin/blog";
    }

    // 跳转到添加博客页面
    @RequestMapping("/toAddBlog")
    public String toAddBlog(Model model) {
        // 查询所有的分类，标签信息。供用户添加博客时选择
        List<Type> types = typeService.queryAll();
        List<Tag> tags = tagService.queryAll();
        model.addAttribute("types", types);
        model.addAttribute("tags", tags);
        return "/admin/addblog";
    }

    // 获得所有的标签，返回JSON数组 (新增博客时，供用户选择对应的标签)
    @PostMapping("/Blog/GetTags")
    @ResponseBody
    public String getTags() {
        List<Tag> tags = tagService.queryAll();
        String tagStr = JSON.toJSONString(tags);
        return tagStr;
    }

    // 执行博客添加操作
    @RequestMapping("/addBlog")
    public String addBlog(@RequestParam("title") String title,
                          @RequestParam("my-editormd-markdown-doc") String bcontent,
                          @RequestParam("type") String typeName,
                          @RequestParam("original") String orginal,
                          @RequestParam("ifComment") String ifComment,
                          @RequestParam("tag") String[] tags,
                          @RequestParam("published") String published) {

        Blog blog = new Blog();
        blog.setBtitle(title);
        blog.setBcontent(bcontent);
        blog.setViews(1);
        blog.setCommentabled(Integer.valueOf(ifComment));
        blog.setOriginal(Integer.valueOf(orginal));
        blog.setPublished(Integer.valueOf(published));
        blog.setCreatetime(new Date());
        blog.setUpdatetime(new Date());

        /*  处理博客对应的分类   */
        Type type = typeService.queryByName(typeName);
        blog.setType(type);
        blog.setBlogtypeid(type.getTypeid());

        // 首先要想插入本条博客的记录，有了 id 之后，再继续对本条博客的标签进行处理
        Blog insertBlog = blogService.insert(blog);

        // 处理博客对应的 标签
        Blog myBlog = blogService.queryByTitle(title);
        for (int i = 0; i < tags.length; i++) {
            Tag tag = tagService.queryByName(tags[i]);  // 变量标签数组，根据标签名称查询对应标签
            // 对 blog 和 tag 直接关联的表执行插入操作
            blogtagService.insert(new Blogtag(null, myBlog.getBid(), tag.getTagid()));
        }

        return "redirect:/admin/Blog";
    }


    // 执行删除博客操作
    @RequestMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable("id") String id) {
        // 根据 id 删除博客记录
        blogService.deleteById(Long.valueOf(id));
        // 同时要删除 博客和标签 管理表中的记录
        blogtagService.deleteBlog(Long.valueOf(id));

        return "redirect:/admin/Blog";
    }


    // 跳转到 博客编辑 页面
    @RequestMapping("/EditBlog")
    public String editBlog(@RequestParam("bid") String bid, Model model) {
        // 查询该博客的信息
        Blog blog = blogService.queryById(Long.valueOf(bid));
        model.addAttribute("blog", blog);
        // 查询所有的分类，标签信息。供用户添加博客时选择
        List<Type> types = typeService.queryAll();
        List<Tag> tags = tagService.queryAll();
        model.addAttribute("types", types);
        model.addAttribute("tags", tags);
        // 查询该条博客记录对应的 标签
        List<String> selectedTags = blogtagService.queryTagNameByBlogId(blog.getBid());
        model.addAttribute("selectedTags", selectedTags);
        return "/admin/editblog";       // 跳转到编辑博客的界面
    }

    // 执行 博客编辑之后的 保存
    @RequestMapping("/updateBlog")
    public String updateBlog(@RequestParam("bid") String bid,
                             @RequestParam("title") String title,
                             @RequestParam("my-editormd-markdown-doc") String bcontent,
                             @RequestParam("type") String typeName,
                             @RequestParam("original") String orginal,
                             @RequestParam("ifComment") String ifComment,
                             @RequestParam("tag") String[] tags,
                             @RequestParam("published") String published) {

        // 创建一个 blog 对象并赋值 (由于是更新操作，所以需要 id )
        Blog blog = new Blog();
        blog.setBid(Long.valueOf(bid));
        blog.setBtitle(title);
        blog.setBcontent(bcontent);
//        blog.setViews(1);     // 不需要设置 view，保持不变
        blog.setCommentabled(Integer.valueOf(ifComment));
        blog.setOriginal(Integer.valueOf(orginal));
        blog.setPublished(Integer.valueOf(published));
//        blog.setCreatetime(new Date());       // 创建时间保持不变
        blog.setUpdatetime(new Date());

        /*  处理博客对应的分类   */
        Type type = typeService.queryByName(typeName);
        blog.setType(type);
        blog.setBlogtypeid(type.getTypeid());

        //  更新该条博客记录
        int updateBlog = blogService.update(blog);

        // 还要更新 博客和标签关联的那张表 （先删除原来的记录，再插入新纪录）
        int i = blogtagService.deleteBlog(blog.getBid());       // 删除原来的记录
        for (int j = 0; j < tags.length; j++) {
            Tag tag = tagService.queryByName(tags[j]);  // 变量标签数组，根据标签名称查询对应标签
            blogtagService.insert(new Blogtag(null, blog.getBid(), tag.getTagid()));
        }

        return "redirect:/admin/Blog";
    }

}