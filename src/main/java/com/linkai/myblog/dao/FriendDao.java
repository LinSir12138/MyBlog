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
 * @since 2020-05-05 21:24:48
 */
@Mapper
@Repository
public interface FriendDao {


    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Friend> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param
     * @return 对象列表
     */
    List<Friend> queryAll();

    /**
     * 新增数据
     *
     * @param friend 实例对象
     * @return 影响行数
     */
    int insert(Friend friend);

    /**
     * 修改数据
     *
     * @param friend 实例对象
     * @return 影响行数
     */
    int update(Friend friend);

    /**
     * 通过主键删除数据
     *
     * @param fId 主键
     * @return 影响行数
     */
    int deleteById(Integer fId);

}