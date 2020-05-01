package com.linkai.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.linkai.myblog.entity.Blog;
import com.linkai.myblog.entity.Blogtag;
import com.linkai.myblog.entity.Type;
import com.linkai.myblog.service.BlogService;
import com.linkai.myblog.service.BlogtagService;
import com.linkai.myblog.service.impl.TypeServiceImpl;
import com.linkai.myblog.util.MyConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 处理与分类相关的 Controller
@Controller
@RequestMapping("/admin")
public class TypeController {

    // Controller 层调用 service 层
    @Autowired
    private TypeServiceImpl typeService;
    @Autowired
    private BlogService blogService;


    /**
    * @Description: 跳转到 “分类管理” 页面，左侧点击 分类管理，或者需要重定向时调用（a标签发送的请求）
    * @Param: [model]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @GetMapping("/Type")
    public String type(Model model) {
        // 跳转时，查询前面10条记录展示出来
        List<Type> types = typeService.queryAllByLimit(0, MyConstant.PAGE_SIZE_TYPE);
        // 同时查询共有多少条记录，为分页做准备
        int typeNumber = typeService.queryAllNumber();
        model.addAttribute("types", types);
        model.addAttribute("typeNumber", typeNumber);
        model.addAttribute("currentPage", 1);       // 表示当前页码为1
        return "admin/type";
    }


    /**
    * @Description: 添加分类（from表单发送的请求）
    * @Param: [typeName]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @PostMapping("/addType")
    public String addType(@RequestParam("typename") String typeName) {
        System.out.println("999999");
        Type type = new Type();
        type.setTypename(typeName);
        type.setArticlenumber(0);
        typeService.insert(type);
        // 重定向，走上面的那个 Controller，再次从数据库中查询数据，并回答分类管理页面
        return "redirect:/admin/Type";
    }


    /**
    * @Description: 删除分类之前，检查该分类是否被占用
    * @Param: [id]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/11
    */
    @PostMapping("/CheckType")
    @ResponseBody
    @ApiOperation("删除分类之前，检查分类是否被占用")
    public String checkTag(@RequestParam("id") String id) {
        System.out.println("TagId = " + id);
        List<Blog> blog = blogService.queryByTypeId(Long.valueOf(id));
        // 判断 List 为空不能用 null，应该用 size() == 0
        if (blog.size() == 0) {
            return "YES";
        } else {
            return "NO";
        }
    }

    /**
    * @Description: 根据 id 删除分类，使用 RestFul 风格（a标签发送的请求）
    * @Param: [id]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @PostMapping("/deleteType")
    public String deleteType(@RequestParam("id") String id) {
        // 删除之前还需要判断该分类是否被占用，没有被占用才能删除，（交由前端发送 Ajax 请求到 CheckType 来完成）
        typeService.deleteById(Long.parseLong(id));
        return "redirect:/admin/Type";      // 重定向，请求上面的那个Controller
    }


    /**
    * @Description: 更新记录（form表单发送的请求）
    * @Param: [updatetypename, typeId, model]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @PostMapping("/updateType")
    public String updateType(@RequestParam("updatetypename") String updatetypename, @RequestParam("typeId") String typeId, Model model) {
        Type type = new Type();
        type.setTypeid(Long.parseLong(typeId));
        type.setTypename(updatetypename);
        System.out.println(type);
        typeService.update(type);
        return "redirect:/admin/Type";      // 重定向，回到分页的第一页
    }



    /**
    * @Description: 搜索时发送 Ajax 请求调用，获得推荐搜索结果
    * @Param: [map]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @PostMapping("/typeGetSearchResult")
    @ResponseBody
    @ApiOperation("后台搜索分类时，获得推荐的搜索结果")
    public String getSearchResult(@RequestBody HashMap<String, String> map) {
        String inputText = map.get("text");
        List<Type> types = typeService.queryLike(inputText);
        System.out.println(types);
        String str = JSON.toJSONString(types);
        return str;
    }


    /**
    * @Description: 根据标签名查询记录（a标签，或者from表单发送请求）
    * @Param: [typename, model]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @GetMapping("/queryByNameType")
    public String getTypeByName(@RequestParam("typenameSearch") String typename, Model model) {
        if (typename == "") {
            return "redirect:/admin/Type";  // 如果输入框为空，直接重定向到分类首页（注意路径大小写）
        }
        Type type = typeService.queryByName(typename);
        ArrayList<Type> types = new ArrayList<>();
        types.add(type);        // 将查询出来的放入 list 集合中
        // 同时查询共有多少条记录，为分页做准备
        int typeNumber = typeService.queryAllNumber();
        model.addAttribute("types", types);
        model.addAttribute("typeNumber", 1);    // 因为是查询一条语句，所以是1
        model.addAttribute("currentPage", 1);       // 表示当前页码为1
        return "admin/type";       // 返回到分类页面
    }



    /**
    * @Description: 分页 （from表单发送的请求）
    * @Param: [currentPage, model]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @PostMapping("/changePageType")
    public String changePage(@RequestParam("currentPage") String currentPage, Model model) {
        int begin = (Integer.parseInt(currentPage) - 1) * MyConstant.PAGE_SIZE_TYPE;
        System.out.println("begin:" + begin);
        List<Type> types = typeService.queryAllByLimit(begin, MyConstant.PAGE_SIZE_TYPE);
        // 同时查询共有多少条记录，为分页做准备
        int typeNumber = typeService.queryAllNumber();
        model.addAttribute("types", types);
        model.addAttribute("typeNumber", typeNumber);
        // 同时将当前页面传递过去
        model.addAttribute("currentPage", currentPage);
        return "admin/type";
    }


}
