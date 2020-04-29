package com.linkai.myblog.service.impl;

import com.linkai.myblog.entity.Blog;
import com.linkai.myblog.dao.BlogDao;
import com.linkai.myblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 通过ID查询单条数据，使用了 缓存
     *
     * @param bid 主键
     * @return 实例对象
     */
    @Override
    @Cacheable(value = "blog")      // 缓存组件的名称为 blog ，key 默认为参数名称，即博客的 id
    public Blog queryById(Long bid) {
        System.out.println("查询了数据库， id = " + bid);
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
    @Cacheable(value = "blog")      // key 默认为Blog的 title
    public Blog queryByTitle(String btitle) {
        return blogDao.queryByTitle(btitle);
    }

    /**
     * 修改数据
     * 特别注意：使用 Redis 之后，是将方法的返回值存入的 Redis 中，所以这里的 update 返回值应该是 Blog 类型
     *          然而调用 Dao 层的方法返回值却应该是 int 类型
     *
     * @param blog 实例对象
     * @return 实例对象
     */
    @Override
    @CachePut(value = "blog", keyGenerator = "mykeyGenerator")      // 返回值应为 Blog 类型
    public Blog update(Blog blog) {
        System.out.println("更新了blog");
        int update = blogDao.update(blog);
        return blog;
    }

    @Override
    public int updateViewsById(Long bid) {
        return blogDao.updateViewsById(bid);
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

    @Override
    public List<Blog> queryByNameLike(String btitle) {
        return blogDao.queryByNameLike(btitle);
    }
}