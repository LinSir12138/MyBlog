package com.linkai.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.linkai.myblog.entity.Blog;
import com.linkai.myblog.entity.Tag;
import com.linkai.myblog.entity.Type;
import com.linkai.myblog.service.BlogService;
import com.linkai.myblog.service.BlogtagService;
import com.linkai.myblog.service.TagService;
import com.linkai.myblog.service.TypeService;
import com.linkai.myblog.util.MyConstant;
import com.sun.org.apache.xpath.internal.operations.Mod;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping({"/", "/main"})
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

    //  首页  --》  处理分页的点击事件
    @PostMapping("/changePageBlog")
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
    @GetMapping("/article/{id}")
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

/**
 *********************************          前台  ----》  “分类”  页面     ******************************************************************
 * */


    /**
     *      跳转的文章分类页面
     * */
    @GetMapping("/Type")
    public String toType(Model model) {
        List<Type> types = typeService.queryAll();
        model.addAttribute("types", types);
        return "type";
    }

    /**
     *      分类页面中，点击具体的分类跳转展示该分类下面的所有博客
     * */
    @GetMapping("/toSpecificType/{typeid}")
    public String toSpecificType(@PathVariable("typeid") String typeid, Model model) {
        // 1. 类型转换一下
        Long id = Long.valueOf(typeid);

        // 2. 因为需要分页展示，所以这里需要分页查询
        List<Blog> blogs = blogService.queryBlogByLimitAndType(Long.valueOf(typeid), 0, MyConstant.PAGE_SIZE_SHOWBLOG);

        //  3. 根据 id 查询对应的 Type 对象，并放入 Blog 对象中
        Type type = typeService.queryById(id);
        for (Blog b:blogs
             ) {
            b.setType(type);
        }
        model.addAttribute("blogs", blogs);


        // 4. 查询所有的分类
        List<Type> types = typeService.queryAll();
        model.addAttribute("types", types);

        // 5. 同时查询 当前分类下面 共有多少条记录，为分页做准备
        int blogNumber = type.getArticlenumber();
        model.addAttribute("blogNumber", blogNumber);       // 当前页有多少条记录


        model.addAttribute("currentPage", 1);       // 表示当前页码为1

        model.addAttribute("typeid", typeid);      // 同时，将 typeid 传递过去


        return "type";
    }

    /**
     *      分类分页 ---> 选择特定的分类之后，查看该分类下所有博客时的  "分页"  点击事件
     * */
    @PostMapping("/typeChangePageBlog")
    public String typeChangePageBlog(@RequestParam("currentTypeId") String currentTypeId, @RequestParam("currentPage") String currentPage, Model model) {
        int begin = (Integer.parseInt(currentPage) - 1) * MyConstant.PAGE_SIZE_SHOWBLOG;
        List<Blog> blogs = blogService.queryBlogByLimitAndType(Long.valueOf(currentTypeId), begin, MyConstant.PAGE_SIZE_SHOWBLOG);
        Long typeid = blogs.get(0).getBlogtypeid();
        System.out.println("szie = " + blogs.size());

        // 3. 同时查询该条博客记录对应的 type 对象，放入 blog 对象中
        for (Blog tempBlog:blogs
        ) {
            tempBlog.setType(typeService.queryById(tempBlog.getBlogtypeid()));
        }
        model.addAttribute("blogs", blogs);

        // 4. 查询所有的分类
        List<Type> types = typeService.queryAll();
        model.addAttribute("types", types);

        // 5. 同时查询该分类下面 共有多少条记录，为分页做准备
        int blogNumber = typeService.queryById(Long.valueOf(currentTypeId)).getArticlenumber();
        model.addAttribute("blogNumber", blogNumber);


        model.addAttribute("currentPage", currentPage);       // 表示当前页码为1
        model.addAttribute("typeid", typeid);      // 同时，将 typeid 传递过去


        return "type";
    }

/**
 *********************************          前台  ----》  “标签”  页面     ******************************************************************
 * */

    /**
     *      跳转到 “标签”  页面
     **/
    @GetMapping("/Tag")
    public String toTag(Model model) {

        /**
         *      1. 查询所有的标签, 标签下博客的数量已经存在 Tag 表中，可以直接获取
         * */
        List<Tag> tags = tagService.queryAll();
        model.addAttribute("tags", tags);

        return "tag";
    }


    /**
     *      标签页面中，点击具体的标签跳转展示该标签下面的所有博客
     * */
    @GetMapping("/toSpecificTag/{tagid}")
    public String toSpecificTag(@PathVariable("tagid") String tagid, Model model) {
        /**
         *      1. 类型转换一下
         * */
        Long id = Long.valueOf(tagid);

        /**
         *      2. 因为需要分页展示，所以这里需要分页查询
         * */
        List<Blog> blogs = blogService.queryBlogByLimitAndTag(id, 0, MyConstant.PAGE_SIZE_SHOWBLOG);

        /**
         *        3. 因为前台要展示该博客所属分类，所以也还有查询分类放入 Blog 对象中
         * */
        Type type = null;       // 遍历 blogs 列表，通过 blgotypeid 查询 Type 对象并放入 Blog 对象中
        for (Blog b:blogs
             ) {
            type = typeService.queryById(b.getBlogtypeid());
            b.setType(type);
        }
        model.addAttribute("blogs", blogs);

        /**
         *      4. 查询所有的标签
         * */
        List<Tag> tags = tagService.queryAll();
        model.addAttribute("tags", tags);


        /**
         *      5. 同时查询该标签下面共有多少条记录，为分页做准备
         * */
        Tag tag = tagService.queryById(Long.valueOf(tagid));
        int blogNumber = tag.getAriticlenumber();
        model.addAttribute("blogNumber", blogNumber);

        /**
         *      6. 将 currentpage(当前页) 和 tagid 传入
         * */
        model.addAttribute("currentPage", 1);
        model.addAttribute("tagid", tagid);

        return "tag";
    }


    /**
    * @Description: 标签分页  ---》  选择特定的标签之后，查看该标签下所有博客时的  "分页"  点击事件
    * @Param: []
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/5/2
    */
    @PostMapping("/tagChangePageBlog")
    public String tagChangePageBlog(@RequestParam("currentTagId") String currentTagId, @RequestParam("currentPage") String currentPage, Model model) {
        /**
         *      1. 根据规则查询对应的博客集合
         * */
        int begin = (Integer.parseInt(currentPage) - 1) * MyConstant.PAGE_SIZE_SHOWBLOG;
        List<Blog> blogs = blogService.queryBlogByLimitAndTag(Long.valueOf(currentTagId), begin, MyConstant.PAGE_SIZE_SHOWBLOG);

        /**
         *      2. 查询该条博客记录对应的 type 对象，并放入 blog 对象中
         * */
        for (Blog tempBlog:blogs
        ) {
            tempBlog.setType(typeService.queryById(tempBlog.getBlogtypeid()));
        }
        model.addAttribute("blogs", blogs);


        /**
         *      3. 查询所有标签
         * */
        List<Tag> tags = tagService.queryAll();
        model.addAttribute("tags",tags);


        /**
         *      4. 同时查询该标签下面共有多少条记录，为分页做准备
         * */
        Tag tag = tagService.queryById(Long.valueOf(currentTagId));
        int blogNumber = tag.getAriticlenumber();
        model.addAttribute("blogNumber", blogNumber);

        /**
         *     5. 将 currentpage(当前页) 和 tagid 传入
         * */
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("tagid", currentTagId);

        return "tag";
    }


/**
 *********************************          前台  ----》  “时间轴”  页面     ******************************************************************
 * */


    /** 
    * @Description: 跳转到 “时间轴”  页面
    * @Param: [] 
    * @return: java.lang.String 
    * @Author: 林凯
    * @Date: 2020/5/2 
    */ 
    @GetMapping("/Timeline")
    public String toTimeline(Model model) {
        List<Map<String, Object>> listWithCount = blogService.queryTimeLingWithCount();
        System.out.println("listWithCount size = " + listWithCount.size());
        List<Map<String, Object>> listWithOutCount = blogService.queryTimeLingWithOutCount();
        System.out.println("listWithOutCount size = " + listWithOutCount.size());

        model.addAttribute("listWithCount", listWithCount);
        model.addAttribute("listWithOutCount", listWithOutCount);

        return "timeline";
    }




    @GetMapping("/Statistic")
    @ResponseBody
    @ApiOperation("统计博客数据")
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
