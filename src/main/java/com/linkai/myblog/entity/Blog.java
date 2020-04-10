package com.linkai.myblog.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Blog)实体类
 *
 * @author 林凯
 * @since 2020-04-07 21:06:53
 */
public class Blog implements Serializable {
    private static final long serialVersionUID = 899130782399389132L;
    /**
    * 文章id
    */
    private Long bid;
    /**
    * 文章标题
    */
    private String btitle;
    /**
    * 文章内容,text 可以存储64kb文本数据
    */
    private String bcontent;
    /**
    * 文章浏览量
    */
    private Integer views;
    /**
    * 是否开启评论，1代表开启评论，0代表不开启评论
    */
    private Integer commentabled;
    /**
    * 是否原创，1代表是原创，0代表转载
    */
    private Integer original;
    /**
    * 是否发布，1代表发布，0代表存为草稿
    */
    private Integer published;
    /**
    * 文章发布时间
    */
    private Date createtime;
    /**
    * 文章最近修改时间

    */
    private Date updatetime;
    /**
    * 对应type的id
    */
    private Long blogtypeid;

    private Type type;

    @Override
    public String toString() {
        return "Blog{" +
                "bid=" + bid +
                ", btitle='" + btitle + '\'' +
                ", bcontent='" + bcontent + '\'' +
                ", views=" + views +
                ", commentabled=" + commentabled +
                ", original=" + original +
                ", published=" + published +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", blogtypeid=" + blogtypeid +
                ", type=" + type +
                '}';
    }

    public Long getBid() {
        return bid;
    }

    /*    返回 bid 对应的 String 类型*/
    public String getBidToString() {return String.valueOf(bid);}

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getBtitle() {
        return btitle;
    }

    public void setBtitle(String btitle) {
        this.btitle = btitle;
    }

    public String getBcontent() {
        return bcontent;
    }

    public void setBcontent(String bcontent) {
        this.bcontent = bcontent;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getCommentabled() {
        return commentabled;
    }

    public void setCommentabled(Integer commentabled) {
        this.commentabled = commentabled;
    }

    public Integer getOriginal() {
        return original;
    }

    public void setOriginal(Integer original) {
        this.original = original;
    }

    public Integer getPublished() {
        return published;
    }

    public void setPublished(Integer published) {
        this.published = published;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Long getBlogtypeid() {
        return blogtypeid;
    }

    public void setBlogtypeid(Long blogtypeid) {
        this.blogtypeid = blogtypeid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}