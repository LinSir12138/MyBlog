package com.linkai.myblog;

import com.google.gson.Gson;
import com.linkai.myblog.dao.BlogDao;
import com.linkai.myblog.dao.FriendDao;
import com.linkai.myblog.entity.Friend;
import com.linkai.myblog.entity.Type;
import com.linkai.myblog.service.TypeService;
import com.linkai.myblog.service.impl.MainServiceImpl;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MyblogApplicationTests {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private MainServiceImpl mainService;

    @Autowired
    JavaMailSenderImpl mailSender;

    @Test
    void contextLoads() {
        List<Type> test = typeService.queryLike("test");
    }

    @Test
    void testTimeLineWithCount() {
        List<Map<String, Object>> list = blogDao.queryTimeLingWithCount();
        for (Map<String, Object> map:list
             ) {
            System.out.println(map.get("year"));
            System.out.println(map.get("month"));
            System.out.println(map.get("count"));
        }

    }

    @Test
    void testTimeLineWithOutCount() {
        List<Map<String, Object>> list = blogDao.queryTimeLingWithOutCount();
        for (Map<String, Object> map:list
        ) {
            System.out.println(map.get("year"));
            System.out.println(map.get("month"));
            System.out.println(map.get("time"));
            System.out.println(map.get("title"));
        }
    }

    @Test
    void testInsert() {
        List<Friend> friends = friendDao.queryAll();
        for (Friend f:friends
             ) {
            System.out.println(f);
        }
    }

    @Test
    void sendEmail() throws MessagingException {

        // 一个复杂的邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 组装，看源码，开启多文件设置一下true
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

        mimeMessageHelper.setSubject("欢迎申请友链");
        //true 表示会支持html格式转换
        mimeMessageHelper.setText("<p></p>",true);

        // 附件
//        mimeMessageHelper.addAttachment("1.jpg", new File());
//        mimeMessageHelper.addAttachment("2.jpg", new File());

        mimeMessageHelper.setTo("1670822659@qq.com");
        mimeMessageHelper.setFrom("1670822659@qq.com");

        mailSender.send(mimeMessage);

    }


    @Test
    void testMQ() {
        mainService.becomeFriend("666的博客", "https://blog.csdn.net/guanmao4322/article/details/88388199", "https://blog.csdn.net/guanmao4322/article/details/88388199", "1670822659@qq.com");

    }

    @Test
    void testUpload() {
        // 注意是在 ：com.qiniu.storage 包下面的
        Configuration cfg = new Configuration(Region.region2());        // 由于我们选择的是华南的机房，所以这里调用 region2()

        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "";
        String secretKey = "";
        String bucket = "jacklin-blog";     // 填写我们的存储空间的名称

        String localFilePath = "D:\\temp\\test.png";
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        // com.qiniu.http.Response;
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException e) {
            Response r = e.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex) {
                ex.printStackTrace();
            }
        }

    }

}
