package com.linkai.myblog.service.impl;

import com.linkai.myblog.dao.FriendDao;
import com.linkai.myblog.entity.Friend;
import com.linkai.myblog.service.MainService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class MainServiceImpl implements MainService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    FriendDao friendDao;


    @Autowired
    JavaMailSenderImpl javaMailSender;

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

        System.out.println("监听成功！");
        //  将 Map 里面的
        Friend friend = new Friend();
        friend.setFBlogtitle(map.get("blogTitle"));
        friend.setFBlogaddress(map.get("blogAddress"));
        friend.setFImageaddress(map.get("imageAddress"));
        friend.setFEmail(map.get("emailAddress"));
        friend.setFTime(new Date());
        friend.setFFlag(0);

        // 执行插入操作
        friendDao.insert(friend);

        // 发送邮件，通知等待 审核
        try {
            sendEmail(map.get("emailAddress"));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    void sendEmail(String emailAddress) throws MessagingException {

        // 一个复杂的邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // 组装，看源码，开启多文件设置一下true
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

        mimeMessageHelper.setSubject("欢迎申请友链!");
        //true 表示会支持html格式转换
        mimeMessageHelper.setText("<p>请耐心等待，审核通过之后您将受到邮件！</p>",true);

        mimeMessageHelper.setTo(emailAddress);
        mimeMessageHelper.setFrom("1670822659@qq.com");

        javaMailSender.send(mimeMessage);

    }


}
