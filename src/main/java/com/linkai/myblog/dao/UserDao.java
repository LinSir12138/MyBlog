package com.linkai.myblog.dao;
//
import com.linkai.myblog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author 林凯
 * @since 2020-03-29 15:36:57
 */
@Mapper
@Repository
public interface UserDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Integer id);

    /**
    * @Description: 根据用户名查询密码
    * @Param: [password]
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2020/3/29
    */
    String queryPasswordByUsername(String userpassword);


}