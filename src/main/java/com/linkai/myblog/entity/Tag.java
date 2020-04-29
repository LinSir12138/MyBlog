package com.linkai.myblog.entity;

import java.io.Serializable;

/**
 * (Tag)实体类
 *
 * @author 林凯
 * @since 2020-04-07 19:20:13
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = -89461776418344488L;
    /**
    * 标签id
    */
    private Long tagid;
    /**
    * 标签名称
    */
    private String tagname;
    /**
    * 标签下对应的文章数量
    */
    private Integer ariticlenumber;


    public Long getTagid() {
        return tagid;
    }

    // 返回 id 转换成为的 String 字符串，在 js 中插入搜索推荐标签是使用 RestFul 风格是用到
    public String idToString() {
        return String.valueOf(this.tagid);
    }


    public void setTagid(Long tagid) {
        this.tagid = tagid;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public Integer getAriticlenumber() {
        return ariticlenumber;
    }

    public void setAriticlenumber(Integer ariticlenumber) {
        this.ariticlenumber = ariticlenumber;
    }

}