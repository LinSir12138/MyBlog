package com.linkai.myblog.service.impl;

import com.linkai.myblog.entity.Friend;
import com.linkai.myblog.dao.FriendDao;
import com.linkai.myblog.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Friend)表服务实现类
 *
 * @author 林凯
 * @since 2020-05-05 21:24:49
 */
@Service("friendService")
public class FriendServiceImpl implements FriendService {


    @Autowired
    private FriendDao friendDao;





    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Friend> queryAllByLimit(int offset, int limit) {
        return this.friendDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param friend 实例对象
     * @return 实例对象
     */
    @Override
    public Friend insert(Friend friend) {
        this.friendDao.insert(friend);
        return friend;
    }


    /**
     * 通过主键删除数据
     *
     * @param fId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer fId) {
        return this.friendDao.deleteById(fId) > 0;
    }

    @Override
    public List<Friend> queryAll() {
        return friendDao.queryAll();
    }
}