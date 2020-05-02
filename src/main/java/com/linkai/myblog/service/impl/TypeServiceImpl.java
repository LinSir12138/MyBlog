package com.linkai.myblog.service.impl;

import com.linkai.myblog.entity.Type;
import com.linkai.myblog.dao.TypeDao;
import com.linkai.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Type)表服务实现类
 *
 * @author 林凯
 * @since 2020-04-03 11:13:28
 */
@Service()
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;

    @Override
    public int insert(Type type) {
        return typeDao.insert(type);
    }

    @Override
    public int deleteById(Long id) {
        return typeDao.deleteById(id);
    }

    @Override
    public int update(Type type) {
        return typeDao.update(type);
    }

    @Override
    public int updateArcileNumberInc(String typeName) {
        return typeDao.updateArcileNumberInc(typeName);
    }

    @Override
    public int updateArcileNumberDec(String typeName) {
        return typeDao.updateArcileNumberDec(typeName);
    }

    @Override
    public int updateArcileNumberDecById(Long typeId) {
        return typeDao.updateArcileNumberDecById(typeId);
    }

    @Override
    public List<Type> queryAll() {
        return typeDao.queryAll();
    }

    @Override
    public Type queryByName(String typename) {
        return typeDao.queryByName(typename);
    }

    @Override
    public Type queryById(Long typeid) {
        return typeDao.queryById(typeid);
    }

    @Override
    public List<Type> queryLike(String typename) {
        return typeDao.queryLike(typename);
    }

    @Override
    public List<Type> queryAllByLimit(int offset, int limit) {
        return typeDao.queryAllByLimit(offset, limit);
    }

    @Override
    public int queryAllNumber() {
        return typeDao.queryAllNumber();
    }
}