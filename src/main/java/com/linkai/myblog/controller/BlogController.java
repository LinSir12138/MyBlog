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
import com.linkai.myblog.util.MyConstant;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

        return "admin/blog";
    }

    // 博客分页
    @RequestMapping("/changePageBlog")
    public String changepageBLog(@RequestParam("currentPage") String currentPage, Model model) {
        int begin = (Integer.parseInt(currentPage) - 1) * MyConstant.PAGE_SIZE_BLOG;
        List<Blog> blogs = blogService.queryAllByLimit(begin, MyConstant.PAGE_SIZE_BLOG);
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

        return "admin/blog";
    }

    // 跳转到添加博客页面
    @RequestMapping("/toAddBlog")
    public String toAddBlog(Model model) {
        // 查询所有的分类，标签信息。供用户添加博客时选择
        List<Type> types = typeService.queryAll();
        List<Tag> tags = tagService.queryAll();
        model.addAttribute("types", types);
        model.addAttribute("tags", tags);
        return "admin/addblog";
    }

    // 获得所有的标签，返回JSON数组 (新增博客时，供用户选择对应的标签)
    @PostMapping("/Blog/GetTags")
    @ResponseBody
    public String getTags() {
        List<Tag> tags = tagService.queryAll();
        String tagStr = JSON.toJSONString(tags);
        return tagStr;
    }

    // 执行博客添加操作-》 发布该博客
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
        List<Tag> selectedTags = blogtagService.queryTagsByBlogId(blog.getBid());
        model.addAttribute("selectedTags", selectedTags);
        return "admin/editblog";       // 跳转到编辑博客的界面
    }

    /**
    * @Description:  自动保存为草稿（暂时不发布），前端发送的是 Ajax 请求
     *              需要判断： 新增博客时，根据id判断是否已经存在，如果不存在：insert    如果存在：update
     *                          编辑博客时，直接 update
    * @Param: [bid, title, bcontent, typeName, orginal, ifComment, tags, published]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/12
    */
    @RequestMapping("/updateBlog")
    @ResponseBody
    public String updateBlog(@RequestParam("blogid") String bid,
                             @RequestParam("title") String title,
                             @RequestParam("my-editormd-markdown-doc") String bcontent,
                             @RequestParam("type") String typeName,
                             @RequestParam("original") String orginal,
                             @RequestParam("ifComment") String ifComment,
                             @RequestParam("tag") String[] tags,
                             @RequestParam("published") String published) {



        // 如果相等，所以是第一次保存，得执行插入操作
        // （由于字符串传递过来会报错，所以传递了一个特殊的字符串表示是添加博客时点击第一次暂存）
        if ("TempSave".equals(bid)) {        // 字符串值相等，用equals，执行 insert 操作
            System.out.println("相等");
            Blog blog = new Blog();
            blog.setBtitle(title);      // 标题
            blog.setBcontent(bcontent);     // 内容
            blog.setViews(1);       // 阅读量
            blog.setCommentabled(Integer.valueOf(ifComment));       // 是否开启评论
            blog.setOriginal(Integer.valueOf(orginal));     // 是否原创
            blog.setPublished(Integer.valueOf(published));      // 是否发布
            blog.setCreatetime(new Date());     // 创建时间
            blog.setUpdatetime(new Date());     // 最近修改时间

            /*  处理博客对应的分类   */
            typeService.updateArcileNumberInc(typeName);                       // 该分类下面的博客数量加 1
            Type type = typeService.queryByName(typeName);      // 查出才 Type 对象，放入 Blog 对象中
            blog.setType(type);
            blog.setBlogtypeid(type.getTypeid());

            Blog insertBlog = blogService.insert(blog);         //新增一条博客记录

            // 还要更新 博客和标签关联的那张表
            for (int j = 0; j < tags.length; j++) {
                Tag tag = tagService.queryByName(tags[j]);  // 变量标签数组，根据标签名称查询对应标签
                blogtagService.insert(new Blogtag(null, blog.getBid(), tag.getTagid()));
            }

            // 需要返回该博客对应的 id，并以字符串形式返回
            Blog queryBlog = blogService.queryByTitle(title);       // 获得刚才插入的博客，进而获取该博客 id
            return String.valueOf(queryBlog.getBid());
        }

        System.out.println("controller 更新一条博客");


        // 下面执行的是 update 操作
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
        // 1. 看博客分类是否改变，如果改变了，就得将原来分类下博客数量减 1， 新的分类下博客数量加 1
        Long oldTypeId = blogService.queryById(Long.valueOf(bid)).getBlogtypeid();
        Type oldType = typeService.queryById(oldTypeId);
        String oldTypeName = oldType.getTypename();
        if (oldTypeName != typeName) {
            // 不相等，说明博客的分类发生了变化，得进行修改
            typeService.updateArcileNumberInc(typeName);
            typeService.updateArcileNumberDec(oldTypeName);
        }

        Type type = typeService.queryByName(typeName);
        blog.setType(type);
        blog.setBlogtypeid(type.getTypeid());



        //  更新该条博客记录
        Blog updateBlog = blogService.update(blog);

        // 还要更新 博客和标签关联的那张表 （先删除原来的记录，再插入新纪录）
        int i = blogtagService.deleteBlog(blog.getBid());       // 删除原来的记录
        for (int j = 0; j < tags.length; j++) {
            Tag tag = tagService.queryByName(tags[j]);  // 变量标签数组，根据标签名称查询对应标签
            blogtagService.insert(new Blogtag(null, blog.getBid(), tag.getTagid()));
        }

        return "admin/editblog";
    }


    // 前端发送 Ajax 请求，获得推荐的搜索结果
    @RequestMapping("/blogGetSearchResult")
    @ResponseBody
    public String blogGetSearchResult(@RequestParam("text") String btitle) {
        /**
         *      注意，因为前端传递的是 json 对象，可以看到地址栏就是用 ？ 拼接参数的，所以可以使用 @RequestParam
         *      如果前端传递的是 json 字符串，则直接是使用 @RequestBody 配合对象，或者 Map 接收参数
         * */

        List<Blog> blogs = blogService.queryByNameLike(btitle);
        for (Blog b:blogs
             ) {
            System.out.println("查询结果为：~~~~~~" + b);
        }
        String blogsStr = JSON.toJSONString(blogs);
        System.out.println(blogsStr);
        return blogsStr;
    }

    // 根据 title 查询对应的 Blog，用户点击搜索推荐的条目之后调用。
    @RequestMapping("/queryByNameBlog")
    public String queryByBlogTitle(@RequestParam("blogtitleSearch") String btitle, Model model) {
        if (btitle == "") {
            return "redirect:/admin/Blog";  // 如果输入框为空，直接重定向到博客首页（注意路径大小写）
        }

        Blog blog = blogService.queryByTitle(btitle);
        // 同时查询该条博客记录对应的 type 对象，放入 blog 对象中
        blog.setType(typeService.queryById(blog.getBlogtypeid()));
        ArrayList<Blog> blogs = new ArrayList<>();
        blogs.add(blog);        // 只需要显示1条记录
        // 同时查询共有多少条记录
        int typeNumber = typeService.queryAllNumber();
        model.addAttribute("blogs", blogs);
        model.addAttribute("typeNumber", 1);    // 因为是查询一条语句，所以是1
        model.addAttribute("currentPage", 1);       // 表示当前页码为1

        return "admin/blog";
    }

}