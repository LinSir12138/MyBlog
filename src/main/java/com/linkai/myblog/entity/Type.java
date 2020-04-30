package com.linkai.myblog.entity;

import java.io.Serializable;

/**
 * (Type)实体类
 *
 * @author 林凯
 * @since 2020-04-07 19:20:56
 */
public class Type implements Serializable {
    private static final long serialVersionUID = 713754012700581657L;
    
    private Long typeid;
    /**
    * 分类名称
    */
    private String typename;
    /**
    * 该分类下对应的文章数量
    */
    private Integer articlenumber;

    @Override
    public String toString() {
        return "Type{" +
                "typeid=" + typeid +
                ", typename='" + typename + '\'' +
                ", articlenumber=" + articlenumber +
                '}';
    }

    public Long getTypeid() {
        return typeid;
    }

    // 返回 id 转换成为的 String 字符串，在 js 中插入搜索推荐标签是使用 RestFul 风格是用到
    public String idToString() {
        return String.valueOf(this.typeid);
    }


    public void setTypeid(Long typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    /**
     *      将 articlenumber 转换为 String 类型
     * */
    public String articleNumberToString() {
        return String.valueOf(this.articlenumber);
    }

    public Integer getArticlenumber() {
        return articlenumber;
    }

    public void setArticlenumber(Integer articlenumber) {
        this.articlenumber = articlenumber;
    }

}