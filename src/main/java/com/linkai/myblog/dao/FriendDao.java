package com.linkai.myblog.dao;

import com.linkai.myblog.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Friend)表数据库访问层
 *
 * @author 林凯
 * @since 2020-05-05 19:44:24
 */
@Mapper
@Repository
public interface FriendDao {


    /**
     * 新增数据
     *
     * @param friend 实例对象
     * @return 影响行数
     */
    int insert(Friend friend);


}