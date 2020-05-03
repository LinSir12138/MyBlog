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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @GetMapping("/Blog")
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
    @PostMapping("/changePageBlog")
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
    @GetMapping("/toAddBlog")
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
    @ResponseBody()
    @ApiOperation("获得所有的标签，返回JSON数组 (新增博客时，供用户选择对应的标签)")
    public String getTags() {
        List<Tag> tags = tagService.queryAll();
        String tagStr = JSON.toJSONString(tags);
        return tagStr;
    }

    // 执行博客添加操作-》 发布该博客
    @PostMapping("/addBlog")
    public String addBlog(@RequestParam("title") String title,
                          @RequestParam("my-editormd-markdown-doc") String bcontent,
                          @RequestParam("type") String typeName,
                          @RequestParam("original") String orginal,
                          @RequestParam("ifComment") String ifComment,
                          @RequestParam("tag") String[] tags,
                          @RequestParam("published") String published) {

        System.out.println("发布博客");
        /**
         *      1. 新增（insert） 一条博客记录
         * */
        Blog blog = new Blog();
        blog.setBtitle(title);
        blog.setBcontent(bcontent);
        blog.setViews(1);
        blog.setCommentabled(Integer.valueOf(ifComment));
        blog.setOriginal(Integer.valueOf(orginal));
        blog.setPublished(Integer.valueOf(published));
        blog.setCreatetime(new Date());
        blog.setUpdatetime(new Date());

        /**
         *      2. 处理博客对应的分类  --》  改变2个地方
         *              (1) blog 里面的 typeID
         *              (2)Type  表里面的分类下博客的数量
         * */
        /**
         *          2.1 设置 blog 里面的 typeID
         * */
        Type type = typeService.queryByName(typeName);
        blog.setType(type);
        blog.setBlogtypeid(type.getTypeid());
        /**
         *          2.2 增加 Type 表里面分类下面的博客数量
         * */
        System.out.println("typeName = " + typeName);
        int i1 = typeService.updateArcileNumberInc(typeName);// 该分类下面的博客数量加 1
        System.out.println("i1 = " + i1);


        // 首先要想插入本条博客的记录，有了 id 之后，再继续对本条博客的标签进行处理
        Blog insertBlog = blogService.insert(blog);


        /**
         *      3. 处理博客对应的 “标签”
         * */
        /**
         *          3.1 处理 blogtag 中间表（多对多）
         * */
        Blog myBlog = blogService.queryByTitle(title);
        for (int i = 0; i < tags.length; i++) {
            Tag tag = tagService.queryByName(tags[i]);  // 变量标签数组，根据标签名称查询对应标签
            // 对 blog 和 tag 直接关联的表执行插入操作
            blogtagService.insert(new Blogtag(null, myBlog.getBid(), tag.getTagid()));
        }

        /**
         *          3.2  更新 tag 表中标签下面博客的数量
         * */
        List<Tag> tags2 = tagService.queryAll();
        int count;
        for (Tag t:tags2
        ) {
            count = blogtagService.queryCouontBlogByTagId(t.getTagid());
            t.setAriticlenumber(count);     // 设置 Type 类的 articleNumber
            tagService.update(t);       // 设置完之后在更新
        }


        return "redirect:/admin/Blog";
    }


    // 执行删除博客操作  ->   特别注意删除的先后顺序
    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable("id") String id) {

        Blog deleteBlog = blogService.queryById(Long.valueOf(id));


        /**
         *      1. 处理博客的 “分类”
         * */
        // 2. 同时要删除 博客和标签 管理表中的记录
        /**
         *      1.1 将对应分类下面的博客数量减 1
         * */
        typeService.updateArcileNumberDecById(deleteBlog.getBlogtypeid());

        /**
         *      2. 处理博客的 “标签”
         * */
        /**
         *          2.1 对应标签下面的博客数量减 1
         * */
        List<Tag> tags = blogtagService.queryTagsByBlogId(Long.valueOf(id));
        System.out.println("length + " + tags.size());
        for (Tag t:tags
             ) {
            System.out.println("t1" + t);
            Integer ariticlenumber = t.getAriticlenumber();
            t.setAriticlenumber(ariticlenumber - 1);
            System.out.println("t2" + t);
            tagService.update(t);
        }
        /**
         *          2.2 删除 blogtag 中间表中的记录  --》  要先将博客的数量减 1 ，然后再执行删除操作
         * */
        blogtagService.deleteBlog(Long.valueOf(id));

        /**
         *      3. 最后再来根据 id 删除博客记录
         * */
        blogService.deleteById(Long.valueOf(id));



        return "redirect:/admin/Blog";
    }


    // 跳转到 博客编辑 页面
    @PostMapping("/EditBlog")
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
    @PostMapping("/updateBlog")
    @ResponseBody
    @ApiOperation("临时保存博客草稿")
    public String updateBlog(@RequestParam("blogid") String bid,
                             @RequestParam("title") String title,
                             @RequestParam("my-editormd-markdown-doc") String bcontent,
                             @RequestParam("type") String typeName,
                             @RequestParam("original") String orginal,
                             @RequestParam("ifComment") String ifComment,
                             @RequestParam("tag") String[] tags,
                             @RequestParam("published") String published) {



        /**
         *      if  里面的执行 insert 操作，对博客只会执行一次 insert 操作，其他都是 update
         * */
        if ("TempSave".equals(bid)) {        // 字符串值相等，用equals，执行 insert 操作

            /**
             *      1. 新增（insert） 一条博客记录
             * */
            Blog blog = new Blog();                 // （由于字符串传递过来会报错，所以传递了一个特殊的字符串表示是添加博客时点击第一次暂存）
            blog.setBtitle(title);      // 标题
            blog.setBcontent(bcontent);     // 内容
            blog.setViews(1);       // 阅读量
            blog.setCommentabled(Integer.valueOf(ifComment));       // 是否开启评论
            blog.setOriginal(Integer.valueOf(orginal));     // 是否原创
            blog.setPublished(Integer.valueOf(published));      // 是否发布
            blog.setCreatetime(new Date());     // 创建时间
            blog.setUpdatetime(new Date());     // 最近修改时间

            /**
             *      2. 处理博客对应的分类  --》  改变2个地方
             *              (1) Type  表里面的分类下博客的数量
             *              (2)blog 里面的 typeID
             *              注意先后顺序
             * */
            /**
             *          2.1  设置 blog 里面的 typeId
             * */
            typeService.updateArcileNumberInc(typeName);        // 该分类下面的博客数量加 1
            /**
             *          2.2  设置 Type 表中分类下博客的数量
             * */
            Type type = typeService.queryByName(typeName);      // 查出来 Type 对象，放入 Blog 对象中
            blog.setType(type);
            blog.setBlogtypeid(type.getTypeid());
            Blog insertBlog = blogService.insert(blog);         //新增一条博客记录


            /**
             *      3. 处理博客对应的 “标签”  ---》 改 2 个地方
             *                  （1） 设置 blogtag 表（博客和标签的中间表--多对多）
             *                  （2）设置 tag 表中标签下面的博客的数量
             * */
            /**
             *          3.1 设置 blogtag 表（博客和标签的中间表--多对多）
             * */
            for (int j = 0; j < tags.length; j++) {
                Tag tag = tagService.queryByName(tags[j]);  // 变量标签数组，根据标签名称查询对应标签
                blogtagService.insert(new Blogtag(null, blog.getBid(), tag.getTagid()));
            }
            /**
             *          3.2  设置 tag 表中标签下面博客的数量
             * */
            List<Tag> tags2 = tagService.queryAll();
            int count;
            for (Tag t:tags2
            ) {
                count = blogtagService.queryCouontBlogByTagId(t.getTagid());
                t.setAriticlenumber(count);     // 设置 Type 类的 articleNumber
                tagService.update(t);       // 设置完之后在更新
            }

            // 需要返回该博客对应的 id，并以字符串形式返回
            Blog queryBlog = blogService.queryByTitle(title);       // 获得刚才插入的博客，进而获取该博客 id
            return String.valueOf(queryBlog.getBid());
        }


        /**
         *      1. 对 Blog 执行 update 操作
         * */
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



        /**
         *      2. 处理博客对应的分类  --》  改变2个地方
         *              (1) blog 里面的 typeID
         *              (2)Type  表里面的分类下博客的数量
         * */
        /**
         *          2.2 更新Type  表里面的分类下博客的数量
         * */
        Long oldTypeId = blogService.queryById(Long.valueOf(bid)).getBlogtypeid();
        Type oldType = typeService.queryById(oldTypeId);
        String oldTypeName = oldType.getTypename(); //看博客分类是否改变，如果改变了，就得将原来分类下博客数量减 1， 新的分类下博客数量加 1

        if (oldTypeName != typeName) {
            // 不相等，说明博客的分类发生了变化，得进行修改
            typeService.updateArcileNumberInc(typeName);
            typeService.updateArcileNumberDec(oldTypeName);
        }

        /**
         *          2.3 更新该条博客记录  --> 因为需要判断分类是否改变，所以更新操作应该放在  2.1 步操作后面执行
         * */
        Type type = typeService.queryByName(typeName);
        blog.setType(type);
        blog.setBlogtypeid(type.getTypeid());
        Blog updateBlog = blogService.update(blog);     //  更新该条博客记录



        /**
         *      3. 处理博客对应的 “标签”  ---》 改 2 个地方
         *                  （1） 更新 blogtag 表（博客和标签的中间表--多对多）
         *                  （2）更新 tag 表中标签下面的博客的数量
         * */
        /**
         *        3.1 更新 blogtag 中间表
         * */
        int i = blogtagService.deleteBlog(blog.getBid());    // 更新 博客和标签关联的那张表 （先删除原来的记录，再插入新纪录）
        for (int j = 0; j < tags.length; j++) {
            Tag tag = tagService.queryByName(tags[j]);  // 变量标签数组，根据标签名称查询对应标签
            blogtagService.insert(new Blogtag(null, blog.getBid(), tag.getTagid()));
        }
        /**
         *          3.2  更新 tag 表中标签下面博客的数量
         * */
        List<Tag> tags2 = tagService.queryAll();
        int count;
        for (Tag t:tags2
        ) {
            count = blogtagService.queryCouontBlogByTagId(t.getTagid());
            t.setAriticlenumber(count);     // 设置 Type 类的 articleNumber
            tagService.update(t);       // 设置完之后在更新
        }

        return "admin/editblog";
    }


    // 前端发送 Ajax 请求，获得推荐的搜索结果
    @PostMapping("/blogGetSearchResult")
    @ResponseBody
    @ApiOperation("获得搜索的推荐结果")
    public String blogGetSearchResult(@ApiParam("博客标题") @RequestParam("text") String btitle) {
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
    @GetMapping("/queryByNameBlog")
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