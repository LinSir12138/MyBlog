package com.linkai.myblog.service;

import com.linkai.myblog.entity.Blog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Blog)表服务接口
 *
 * @author 林凯
 * @since 2020-04-07 21:06:55
 */
public interface BlogService {

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
     *  分页查询 --》 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Blog> queryAllByLimit(int offset, int limit);

    /**
     * @Description:   分类中进行分页查询    ——》 限定分类的同时，分页查询博客
     * @Param: []
     * @return: java.util.List<com.linkai.myblog.entity.Blog>
     * @Author: 林凯
     * @Date: 2020/4/30
     */
    List<Blog> queryBlogByLimitAndType(@Param("specificTypeId") Long specificTypeId, @Param("offset") int offset, @Param("limit") int limit);


    /**
     * 新增数据
     *
     * @param blog 实例对象
     * @return 实例对象
     */
    Blog insert(Blog blog);

    /**
     * @Description: 根据 title 查询博客，处理博客和标签之间的关系时调用
     * @Param: [btitle]
     * @return: com.linkai.myblog.entity.Blog
     * @Author: 林凯
     * @Date: 2020/4/10
     */
    Blog queryByTitle(String btitle);

    /**
     * 修改数据
     *
     * @param blog 实例对象
     * @return 实例对象
     */
    Blog update(Blog blog);

    /**
     * @Description: 根据博客 id 更新博客的阅读数量，使得阅读数量加一
     * @Param: [bid]
     * @return: int
     * @Author: 林凯
     * @Date: 2020/4/14
     */
    int updateViewsById(Long bid);

    /**
     * 通过主键删除数据
     *
     * @param bid 主键
     * @return 是否成功
     */
    boolean deleteById(Long bid);

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