package com.linkai.myblog.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Friend)实体类
 *
 * @author 林凯
 * @since 2020-05-05 21:24:47
 */
public class Friend implements Serializable {
    private static final long serialVersionUID = -81951042846740251L;
    
    private Integer fId;
    /**
    * 博客名称
    */
    private String fBlogtitle;
    /**
    * 博客地址
    */
    private String fBlogaddress;
    /**
    * 头像图片地址
    */
    private String fImageaddress;
    /**
    * 邮箱
    */
    private String fEmail;
    /**
    * 创建时间
    */
    private Date fTime;
    /**
    * 是否通过审核，初值为0，表示为通过审核
    */
    private Integer fFlag;

    @Override
    public String toString() {
        return "Friend{" +
                "fId=" + fId +
                ", fBlogtitle='" + fBlogtitle + '\'' +
                ", fBlogaddress='" + fBlogaddress + '\'' +
                ", fImageaddress='" + fImageaddress + '\'' +
                ", fEmail='" + fEmail + '\'' +
                ", fTime=" + fTime +
                ", fFlag=" + fFlag +
                '}';
    }

    public Integer getFId() {
        return fId;
    }

    public void setFId(Integer fId) {
        this.fId = fId;
    }

    public String getFBlogtitle() {
        return fBlogtitle;
    }

    public void setFBlogtitle(String fBlogtitle) {
        this.fBlogtitle = fBlogtitle;
    }

    public String getFBlogaddress() {
        return fBlogaddress;
    }

    public void setFBlogaddress(String fBlogaddress) {
        this.fBlogaddress = fBlogaddress;
    }

    public String getFImageaddress() {
        return fImageaddress;
    }

    public void setFImageaddress(String fImageaddress) {
        this.fImageaddress = fImageaddress;
    }

    public String getFEmail() {
        return fEmail;
    }

    public void setFEmail(String fEmail) {
        this.fEmail = fEmail;
    }

    public Date getFTime() {
        return fTime;
    }

    public void setFTime(Date fTime) {
        this.fTime = fTime;
    }

    public Integer getFFlag() {
        return fFlag;
    }

    public void setFFlag(Integer fFlag) {
        this.fFlag = fFlag;
    }

}