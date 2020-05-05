package com.linkai.myblog.service;

import com.linkai.myblog.entity.Friend;
import java.util.List;

/**
 * (Friend)表服务接口
 *
 * @author 林凯
 * @since 2020-05-05 21:24:49
 */
public interface FriendService {

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Friend> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param friend 实例对象
     * @return 实例对象
     */
    Friend insert(Friend friend);


    /**
     * 通过主键删除数据
     *
     * @param fId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer fId);

    /**
    * @Description: 查询所有友链记录
    * @Param: []
    * @return: java.util.List<com.linkai.myblog.entity.Friend>
    * @Author: 林凯
    * @Date: 2020/5/5
    */
    List<Friend> queryAll();

}