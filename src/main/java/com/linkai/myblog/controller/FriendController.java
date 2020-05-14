package com.linkai.myblog.controller;

import com.linkai.myblog.entity.Friend;
import com.linkai.myblog.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (Friend)表控制层
 *
 * @author 林凯
 * @since 2020-05-05 21:25:38
 */
@Controller
@RequestMapping("/admin")       // 所有操作博客相关的，都需要添加 admin,这样拦截器就可以进行拦截
public class FriendController {
    /**
     * 服务对象
     */
    @Autowired
    private FriendService friendService;


    /**
     *      后台跳转到 “友链管理”  界面
     * */
    @GetMapping("/Friend")
    public String toFriend(Model model) {
        List<Friend> friends = friendService.queryAll();
        model.addAttribute("friends", friends);

        return "admin/friend";      // 回到 friend 页面
    }


    @PostMapping("/addFriend")
    public String addFriend(Friend friend) {
        friend.setFTime(new Date());
        friend.setFFlag(1);
        friendService.insert(friend);

        return "redirect:/admin/Friend";
    }

    @PostMapping("/deleteFriend")
    public String deleteFriend(@RequestParam("fid") Integer fid) {
        friendService.deleteById(fid);
        return "redirect:/admin/Friend";
    }

}