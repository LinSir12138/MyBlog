package com.linkai.myblog.service.impl;

import com.linkai.myblog.entity.User;
import com.linkai.myblog.dao.UserDao;
import com.linkai.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author 林凯
 * @since 2020-03-29 15:36:58
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Integer id) {
        User user = userDao.queryById(id);
        System.out.println("service 层：" + user);
        return user;
    }

    /**
    * @Description: 根据用户名查询密码
    * @Param: [userpassword]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/3/29
    */
    @Override
    public String queryPasswordByUsername(String userpassword) {
        return userDao.queryPasswordByUsername(userpassword);
    }
}