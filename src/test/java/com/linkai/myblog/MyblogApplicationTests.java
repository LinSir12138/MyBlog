package com.linkai.myblog;

import com.linkai.myblog.dao.BlogDao;
import com.linkai.myblog.dao.FriendDao;
import com.linkai.myblog.entity.Friend;
import com.linkai.myblog.entity.Type;
import com.linkai.myblog.service.TypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.FacesRequestAttributes;

import java.util.Date;
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

}
