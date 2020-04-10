package com.linkai.myblog.service;

import com.linkai.myblog.entity.User;
import java.util.List;

/**
 * (User)表服务接口
 *
 * @author 林凯
 * @since 2020-03-29 15:36:57
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Integer id);

    String queryPasswordByUsername(String userpassword);

}