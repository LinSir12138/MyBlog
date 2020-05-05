package com.linkai.myblog.service.impl;

import com.linkai.myblog.dao.FriendDao;
import com.linkai.myblog.entity.Friend;
import com.linkai.myblog.service.MainService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class MainServiceImpl implements MainService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    FriendDao friendDao;

    @Override
    public void becomeFriend(String blogTitle, String blogAddress, String imageAddress, String emailAddress) {
        Map<String, String> map = new HashMap<>();
        map.put("blogTitle", blogTitle);
        map.put("blogAddress", blogAddress);
        map.put("imageAddress", imageAddress);
        map.put("emailAddress", emailAddress);

        // 将消息发送到 “消息队列” 中
        rabbitTemplate.convertAndSend("email.send", "email", map);
        System.out.println("执行成功！！！");

    }

    /**
    * @Description: 监听消息队列，一旦有消息进入，就自动执行该方法   注 ---》
     *              （1）被监听的消息队列一定要存在，如何不存在，就会报错
     *              （2）尝试过参数是实体类，但是 id 一开始为空，导致监听识别，所以使用 Map
    * @Param:
    * @return: void
    * @Author: 林凯
    * @Date: 2020/5/5
    */
    @RabbitListener(queues = "email")
    public void receive2(HashMap<String, String> map) {
        /**
         *      当消息队列里有消息时，执行插入操作
         * */

        //  将 Map 里面的
        Friend friend = new Friend();
        friend.setFBlogtitle(map.get("blogTitle"));
        friend.setFBlogaddress(map.get("blogAddress"));
        friend.setFImageaddress(map.get("imageAddress"));
        friend.setFEmail(map.get("emailAddress"));
        friend.setFTime(new Date());
        friend.setFFlag(0);

        inserFirend(friend);

    }



    @Override
    public void inserFirend(Friend friend) {
        friendDao.insert(friend);
    }
}
