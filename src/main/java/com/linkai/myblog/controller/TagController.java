package com.linkai.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.linkai.myblog.entity.Blogtag;
import com.linkai.myblog.entity.Tag;
import com.linkai.myblog.service.BlogtagService;
import com.linkai.myblog.service.impl.TagServiceImpl;
import com.linkai.myblog.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Long.parseLong;


/**
 * @Description: 处理与 “标签” 相关的 Controller
 * @Param:
 * @return:
 * @Author: 林凯
 * @Date: 2020/4/5
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    // Controller 层调用 Service 层
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private BlogtagService blogtagService;


    /**
     * @Description: 跳转到 “标签管理” 页面，左侧点击 分类管理，或者需要重定向时调用（a标签发送的请求）
     * @Param: [model]
     * @return: java.lang.String
     * @Author: 林凯
     * @Date: 2020/4/5
     */
    @RequestMapping("/Tag")
    public String tag(Model model) {
        // 跳转时，查询前面10条记录展示出来
        List<Tag> tags = tagService.queryAllByLimit(0, MyConstant.PAGE_SIZE_TAG);
        // 同时查询共有多少条记录，为分页做准备
        int tagNumber = tagService.queryAllNumber();
        model.addAttribute("tags", tags);
        model.addAttribute("tagNumber", tagNumber);
        model.addAttribute("currentPage", 1);       // 表示当前页码为1
        return "/admin/tag";
    }


    /**
     * @Description: 添加标签（from表单发送的请求）
     * @Param: [tagName]
     * @return: java.lang.String
     * @Author: 林凯
     * @Date: 2020/4/5
     */
    @RequestMapping("/addTag")
    public String addTag(@RequestParam("tagname") String tagName) {
        System.out.println("999999");
        Tag tag = new Tag();
        tag.setTagname(tagName);
        tag.setAriticlenumber(0);
        tagService.insert(tag);
        // 重定向，走上面的那个 Controller，再次从数据库中查询数据，并回答分类管理页面
        return "redirect:/admin/Tag";
    }


    @RequestMapping("/CheckTag")
    @ResponseBody
    public String checkTag(@RequestParam("id") String id) {
        System.out.println("TagId = " + id);
        List<Blogtag> blogtags = blogtagService.queryByTagId(Long.valueOf(id));
        // 判断 List 为空不能用 null，应该用 size() == 0
        if (blogtags.size() == 0) {
            return "YES";
        } else {
            return "NO";
        }
    }

    /**
     * @Description: 根据 id 删除标签，使用 RestFul 风格（a标签发送的请求）
     * @Param: [id]
     * @return: java.lang.String
     * @Author: 林凯
     * @Date: 2020/4/5
     */
    @RequestMapping("/deleteTag")
    public String deleteTag(@RequestParam("id") String id) {
        // 首先查看该标签是否被某篇博客所使用的，如果有，则删除失败  (这一步在前端利用 Ajax 已经完成了)
        System.out.println("删除~~~~~~~~~~~~~~~~~~~~~~~~~");
        tagService.deleteById(parseLong(id));
        return "redirect:/admin/Tag";      // 重定向，请求上面的那个Controller
    }


    /**
     * @Description: 更新记录（form表单发送的请求）
     * @Param: [updatetagname, tagId, model]
     * @return: java.lang.String
     * @Author: 林凯
     * @Date: 2020/4/5
     */
    @RequestMapping("/updateTag")
    public String updateTag(@RequestParam("updatetagname") String updatetagname, @RequestParam("tagId") String tagId, Model model) {
        Tag tag = new Tag();
        tag.setTagid(parseLong(tagId));
        tag.setTagname(updatetagname);

        System.out.println(tag);
        tagService.update(tag);
        return "redirect:/admin/Tag";      // 重定向，回到分页的第一页
    }


    /**
     * @Description: 搜索时发送 Ajax 请求调用，获得推荐搜索结果
     * @Param: [map]
     * @return: java.lang.String
     * @Author: 林凯
     * @Date: 2020/4/5
     */
    @RequestMapping("/tagGetSearchResult")
    @ResponseBody
    public String getSearchResult(@RequestBody HashMap<String, String> map) {
        String inputText = map.get("text");
        List<Tag> tags = tagService.queryLike(inputText);
        System.out.println(true);
        String str = JSON.toJSONString(tags);
        return str;
    }


    /**
     * @Description: 根据标签名查询记录（a标签，或者from表单发送请求）
     * @Param: [tagname, model]
     * @return: java.lang.String
     * @Author: 林凯
     * @Date: 2020/4/5
     */
    @RequestMapping("/queryByNameTag")
    public String getTagByName(@RequestParam("tagnameSearch") String tagname, Model model) {
        System.out.println("5201314");
        if (tagname == "") {
            return "redirect:/admin/Tag";  // 如果输入框为空，直接重定向到分类首页（注意路径大小写）
        }
        Tag tag = tagService.queryByName(tagname);
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(tag);
        System.out.println("获得的tag =" + tags);
        // 同时查询共有多少条记录，为分页做准备
        int tagNumber = tagService.queryAllNumber();
        model.addAttribute("tags", tags);
        model.addAttribute("tagNumber", 1);    // 因为是查询一条语句，所以是1
        model.addAttribute("currentPage", 1);       // 表示当前页码为1
        return "/admin/tag";       // 返回到分类页面
    }


    /**
     * @Description: 分页 （from表单发送的请求）
     * @Param: [currentPage, model]
     * @return: java.lang.String
     * @Author: 林凯
     * @Date: 2020/4/5
     */
    @RequestMapping("/changePageTag")
    public String changePage(@RequestParam("currentPage") String currentPage, Model model) {
        int begin = (Integer.parseInt(currentPage) - 1) * MyConstant.PAGE_SIZE_TYPE;
        System.out.println("begin:" + begin);
        List<Tag> tags = tagService.queryAllByLimit(begin, MyConstant.PAGE_SIZE_TYPE);
        // 同时查询共有多少条记录，为分页做准备
        int typeNumber = tagService.queryAllNumber();
        model.addAttribute("tags", tags);
        model.addAttribute("tagNumber", typeNumber);
        // 同时将当前页面传递过去
        model.addAttribute("currentPage", currentPage);
        return "/admin/tag";
    }


}
