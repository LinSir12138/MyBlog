package com.linkai.myblog.service;

import com.linkai.myblog.entity.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Type)表服务接口
 *
 * @author 林凯
 * @since 2020-04-03 11:13:28
 */
public interface TypeService {

    /**
     * 新增数据
     *
     * @param type 实例对象
     * @return 影响行数
     */
    int insert(Type type);

    /**
     * 通过分类名称删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * @Description: 根据数据
     * @Param: [type]
     * @return: int
     * @Author: 林凯
     * @Date: 2020/4/3
     */
    int update(Type type);

    /**
     * @Description: 根据分类名称，将该分类下的博客数量加 1
     * @Param: [typeName]
     * @return: int
     * @Author: 林凯
     * @Date: 2020/4/30
     */
    int updateArcileNumberInc(String typeName);

    /**
     * @Description: 根据分类名称，将该分类下的博客数量减 1
     * @Param: [typeName]
     * @return: int
     * @Author: 林凯
     * @Date: 2020/4/30
     */
    int updateArcileNumberDec(String typeName);

    /**
     * @Description: 根据分类 ID，将该分类下的博客数量减 1，删除博客时调用
     * @Param: [typeId]
     * @return: int
     * @Author: 林凯
     * @Date: 2020/5/1
     */
    int updateArcileNumberDecById(Long typeId);

    /**
     * @Description: 查询所有的分类记录，添加博客时调用
     * @Param: []
     * @return: java.util.List<com.linkai.myblog.entity.Type>
     * @Author: 林凯
     * @Date: 2020/4/7
     */
    List<Type> queryAll();

    /**
     * @Description: 根据分类名称查询 --> 搜索时，点击搜索选项时调用
     * @Param: []
     * @return: java.util.List<com.linkai.myblog.entity.Type>
     * @Author: 林凯
     * @Date: 2020/4/5
     */
    Type queryByName(String typename);

    /**
     * @Description: 根据 id 查询
     * @Param: [typeid]
     * @return: com.linkai.myblog.entity.Type
     * @Author: 林凯
     * @Date: 2020/4/10
     */
    Type queryById(Long typeid);

    /**
    * @Description: 根据 分类名称 模糊查询
    * @Param: [typename]
    * @return: java.util.List<com.linkai.myblog.entity.Type>
    * @Author: 林凯
    * @Date: 2020/4/4
    */
    List<Type> queryLike(String typename);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Type> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * @Description: 查询共有多少条记录
     * @Param: []
     * @return: int
     * @Author: 林凯
     * @Date: 2020/4/3
     */
    int queryAllNumber();
}