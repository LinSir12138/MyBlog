package com.linkai.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.linkai.myblog.entity.Type;
import com.linkai.myblog.service.impl.TypeServiceImpl;
import com.linkai.myblog.util.MyConstant;
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


    /**
    * @Description: 跳转到 “分类管理” 页面，左侧点击 分类管理，或者需要重定向时调用（a标签发送的请求）
    * @Param: [model]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @RequestMapping("/Type")
    public String type(Model model) {
        // 跳转时，查询前面10条记录展示出来
        List<Type> types = typeService.queryAllByLimit(0, MyConstant.PAGE_SIZE_TYPE);
        // 同时查询共有多少条记录，为分页做准备
        int typeNumber = typeService.queryAllNumber();
        model.addAttribute("types", types);
        model.addAttribute("typeNumber", typeNumber);
        model.addAttribute("currentPage", 1);       // 表示当前页码为1
        return "/admin/type";
    }


    /**
    * @Description: 添加分类（from表单发送的请求）
    * @Param: [typeName]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @RequestMapping("/addType")
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
    * @Description: 根据 id 删除分类，使用 RestFul 风格（a标签发送的请求）
    * @Param: [id]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @RequestMapping("/deleteType/{id}")
    public String deleteType(@PathVariable("id") String id) {
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
    @RequestMapping("/updateType")
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
    @RequestMapping("/typeGetSearchResult")
    @ResponseBody
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
    @RequestMapping("/queryByNameType")
    public String getTypeByName(@RequestParam("typenameSearch") String typename, Model model) {
        System.out.println("5201314");
        if (typename == "") {
            return "redirect:/admin/Type";  // 如果输入框为空，直接重定向到分类首页（注意路径大小写）
        }
        Type type = typeService.queryByName(typename);
        ArrayList<Type> types = new ArrayList<>();
        System.out.println("获得的type =" + types);
        // 同时查询共有多少条记录，为分页做准备
        int typeNumber = typeService.queryAllNumber();
        model.addAttribute("types", types);
        model.addAttribute("typeNumber", 1);    // 因为是查询一条语句，所以是1
        model.addAttribute("currentPage", 1);       // 表示当前页码为1
        return "/admin/type";       // 返回到分类页面
    }



    /**
    * @Description: 分页 （from表单发送的请求）
    * @Param: [currentPage, model]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    @RequestMapping("/changePageType")
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
        return "/admin/type";
    }

    /*  下面的代码是用 Ajax 实现的分页，太麻烦了，因为数据量太大，不方便展示，所以替换成立一个 from 表单*/
//    @RequestMapping("/changePage")
//    @ResponseBody       // 返回字符串
//    /**
//     *      注意，因为前端传递的是 json 对象，可以看到地址栏就是用 ？ 拼接参数的，所以可以使用 @RequestParam
//     *      如果前端传递的是 json 字符串，则直接是使用 @RequestBody 配合对象，或者 Map 接收参数
//     * */
//    public String pageChange(@RequestParam("currentPage") String currentPage ) {
//        int begin = (Integer.parseInt(currentPage) - 1) * 10;       // 根据当前页面计算实际需要分页的下标
//        List<Type> types = typeService.queryAllByLimit(begin, 10);
//        // 将 list 转换为 json 字符串
//        String typesJson = JSON.toJSONString(types);
//        return typesJson;
//    }
}
