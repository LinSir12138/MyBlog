package com.linkai.myblog.service.impl;

import com.linkai.myblog.entity.Tag;
import com.linkai.myblog.dao.TagDao;
import com.linkai.myblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Tag)表服务实现类
 *
 * @author 林凯
 * @since 2020-04-05 15:35:32
 */

@Service()
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public int insert(Tag tag) {
        return tagDao.insert(tag);
    }

    @Override
    public int deleteById(Long id) {
        return tagDao.deleteById(id);
    }

    @Override
    public int update(Tag tag) {
        return tagDao.update(tag);
    }

    @Override
    public List<Tag> queryAll() {
        return tagDao.queryAll();
    }

    @Override
    public Tag queryByName(String tagName) {
        return tagDao.queryByName(tagName);
    }

    @Override
    public List<Tag> queryLike(String tagName) {
        return tagDao.queryLike(tagName);
    }

    @Override
    public List<Tag> queryAllByLimit(int offset, int limit) {
        return tagDao.queryAllByLimit(offset,limit);
    }

    @Override
    public int queryAllNumber() {
        return tagDao.queryAllNumber();
    }
}