package com.linkai.myblog.dao;

import com.linkai.myblog.entity.Blogtag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Blogtag)表数据库访问层
 *
 * @author 林凯
 * @since 2020-04-10 11:50:30
 */
@Mapper
@Repository
public interface BlogtagDao {

    /**
     * 新增数据
     *
     * @param blogtag 实例对象
     * @return 影响行数
     */
    int insert(Blogtag blogtag);


    /**
    * @Description: 删除记录（根据博客的id）
    * @Param: []
    * @return: int
    * @Author: 林凯
    * @Date: 2020/4/10
    */
    int deleteBlog(Long bid);


    /**
    * @Description: 根据标签的id查询记录，删除标签时调用，查看是否存在某篇博客占用该标签
    * @Param: []
    * @return: java.util.List<com.linkai.myblog.entity.Blogtag>
    * @Author: 林凯
    * @Date: 2020/4/10
    */
    List<Blogtag> queryByTagId(Long tagid);

    /**
    * @Description: 根据博客的 id 查询记录，编辑博客时调用，获得该博客说对应的标签的名称，返回 List 集合, 直接一个连表查询
    * @Param: []
    * @return: java.util.List<com.linkai.myblog.entity.Blogtag>
    * @Author: 林凯
    * @Date: 2020/4/11
    */
    List<String> queryTagNameByBlogId(Long bid);

}