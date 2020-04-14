package com.linkai.myblog.service.impl;

import com.linkai.myblog.entity.Blogtag;
import com.linkai.myblog.dao.BlogtagDao;
import com.linkai.myblog.entity.Tag;
import com.linkai.myblog.service.BlogtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Blogtag)表服务实现类
 *
 * @author 林凯
 * @since 2020-04-10 11:50:30
 */
@Service()
public class BlogtagServiceImpl implements BlogtagService {
    @Autowired
    private BlogtagDao blogtagDao;

    @Override
    public int insert(Blogtag blogtag) {
        return blogtagDao.insert(blogtag);
    }

    @Override
    public int deleteBlog(Long bid) {
        return blogtagDao.deleteBlog(bid);
    }

    @Override
    public List<Blogtag> queryByTagId(Long tagid) {
        return blogtagDao.queryByTagId(tagid);
    }

    @Override
    public List<Tag> queryTagsByBlogId(Long bid) {
        return blogtagDao.queryTagsByBlogId(bid);
    }


}