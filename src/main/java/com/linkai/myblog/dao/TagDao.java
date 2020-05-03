package com.linkai.myblog.dao;

import com.linkai.myblog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Tag)表数据库访问层
 *
 * @author 林凯
 * @since 2020-04-05 15:35:32
 */
@Mapper
@Repository
public interface TagDao {

    /**
     * 新增数据
     *
     * @param tag 实例对象
     * @return 影响行数
     */
    int insert(Tag tag);

    /** 
    * @Description: 根据 id 删除数据 
    * @Param: [id] 
    * @return: int 
    * @Author: 林凯
    * @Date: 2020/4/5 
    */ 
    int deleteById(Long id);

    /**
     * 修改数据
     *
     * @param tag 实例对象
     * @return 影响行数
     */
    int update(Tag tag);


    /**
    * @Description: 查询所有的标签记录，添加博客时调用
    * @Param: []
    * @return: java.util.List<com.linkai.myblog.entity.Tag>
    * @Author: 林凯
    * @Date: 2020/4/7
    */
    List<Tag> queryAll();

    /**
    * @Description: 根据标签名称查询数据
    * @Param: [tagName]
    * @return: java.util.List<com.linkai.myblog.entity.Tag>
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    Tag queryByName(String tagName);

    /**
    * @Description:  根据 id 查询数据
    * @Param: [tagid]
    * @return: com.linkai.myblog.entity.Tag
    * @Author: 林凯
    * @Date: 2020/5/2
    */
    Tag queryById(Long tagid);

    /**
    * @Description: 根据标签名称模糊查询
    * @Param:
    * @return:
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    List<Tag> queryLike(String tagName);


    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Tag> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
    * @Description: 查询共有多少条记录
    * @Param: []
    * @return: int
    * @Author: 林凯
    * @Date: 2020/4/5
    */
    int queryAllNumber();

}