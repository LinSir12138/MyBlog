package com.linkai.myblog.entity;

import java.io.Serializable;

/**
 * (Blogtag)实体类
 *
 * @author 林凯
 * @since 2020-04-10 12:04:20
 */
public class Blogtag implements Serializable {
    private static final long serialVersionUID = 393599913988719011L;
    
    private Integer id;
    /**
    * 对应博客表的id

    */
    private Long bid;
    /**
    * 对应标签表的id
    */
    private Long tagid;

    public Blogtag() {
    }

    public Blogtag(Integer id, Long bid, Long tagid) {
        this.id = id;
        this.bid = bid;
        this.tagid = tagid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getTagid() {
        return tagid;
    }

    public void setTagid(Long tagid) {
        this.tagid = tagid;
    }

}