package com.linkai.myblog;

import com.linkai.myblog.entity.Type;
import com.linkai.myblog.service.TypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyblogApplicationTests {

    @Autowired
    private TypeService typeService;

    @Test
    void contextLoads() {
        List<Type> test = typeService.queryLike("test");
    }

    @Test
    void test1() {
        typeService.updateArcileNumberDecById(Long.valueOf(2));

    }

}
