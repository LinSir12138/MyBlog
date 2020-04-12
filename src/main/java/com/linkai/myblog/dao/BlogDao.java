package com.linkai.myblog.dao;

import com.linkai.myblog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Blog)表数据库访问层
 *
 * @author 林凯
 * @since 2020-04-07 21:06:54
 */
@Mapper
@Repository
public interface BlogDao {



    /**
     * 新增数据
     *
     * @param blog 实例对象
     * @return 影响行数
     */
    int insert(Blog blog);

    /**
     * 通过主键删除数据
     *
     * @param bid 主键
     * @return 影响行数
     */
    int deleteById(Long bid);

    /**
     * 修改数据
     *
     * @param blog 实例对象
     * @return 影响行数
     */
    int update(Blog blog);


    /**
    * @Description: 查询所有的博客信息
    * @Param: []
    * @return: java.util.List<com.linkai.myblog.entity.Blog>
    * @Author: 林凯
    * @Date: 2020/4/7
    */
    List<Blog> queryAll();

    /**
     * 通过ID查询单条数据
     *
     * @param bid 主键
     * @return 实例对象
     */
    Blog queryById(Long bid);


    /**
    * @Description: 根据 typeid 查询博客记录，删除分类时调用，查看分类是否被占用
    * @Param: [typeid]
    * @return: com.linkai.myblog.entity.Blog
    * @Author: 林凯
    * @Date: 2020/4/11
    */
    List<Blog> queryByTypeId(Long typeid);


    /**
    * @Description: 根据 title 查询博客，处理博客和标签之间的关系时调用
    * @Param: [btitle]
    * @return: com.linkai.myblog.entity.Blog
    * @Author: 林凯
    * @Date: 2020/4/10
    */
    Blog queryByTitle(String btitle);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Blog> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * @Description: 查询共有多少条记录，为分页做准备
     * @Param: []
     * @return: int
     * @Author: 林凯
     * @Date: 2020/4/7
     */
    int queryAllNumber();


    /**
    * @Description: 根据博客名字模糊查询，后台管理进行博客搜索时用到
    * @Param: []
    * @return: java.util.List<com.linkai.myblog.entity.Blog>
    * @Author: 林凯
    * @Date: 2020/4/12
    */
    List<Blog> queryByNameLike(String btitle);

}