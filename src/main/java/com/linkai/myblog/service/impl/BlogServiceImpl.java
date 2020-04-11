package com.linkai.myblog.service.impl;

import com.linkai.myblog.entity.Blog;
import com.linkai.myblog.dao.BlogDao;
import com.linkai.myblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Blog)表服务实现类
 *
 * @author 林凯
 * @since 2020-04-07 21:06:55
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogDao blogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param bid 主键
     * @return 实例对象
     */
    @Override
    public Blog queryById(Long bid) {
        return this.blogDao.queryById(bid);
    }

    @Override
    public List<Blog> queryByTypeId(Long typeid) {
        return blogDao.queryByTypeId(typeid);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Blog> queryAllByLimit(int offset, int limit) {
        return this.blogDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param blog 实例对象
     * @return 实例对象
     */
    @Override
    public Blog insert(Blog blog) {
        this.blogDao.insert(blog);
        return blog;
    }

    /**
     * @Description: 根据 title 查询博客，处理博客和标签之间的关系时调用
     * @Param: [btitle]
     * @return: com.linkai.myblog.entity.Blog
     * @Author: 林凯
     * @Date: 2020/4/10
     */
    @Override
    public Blog queryByTitle(String btitle) {
        return blogDao.queryByTitle(btitle);
    }

    /**
     * 修改数据
     *
     * @param blog 实例对象
     * @return 实例对象
     */
    @Override
    public int update(Blog blog) {
        return this.blogDao.update(blog);
    }

    /**
     * 通过主键删除数据
     *
     * @param bid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long bid) {
        return this.blogDao.deleteById(bid) > 0;
    }

    /**
    * @Description: 查询共有多少条博客记录，为分页做准备
    * @Param: []
    * @return: int
    * @Author: 林凯
    * @Date: 2020/4/7
    */
    @Override
    public int queryAllNumber() {
        return blogDao.queryAllNumber();
    }
}