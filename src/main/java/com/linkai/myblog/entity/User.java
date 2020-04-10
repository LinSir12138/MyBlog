package com.linkai.myblog.entity;

import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author 林凯
 * @since 2020-03-29 15:36:56
 */
public class User implements Serializable {
    private static final long serialVersionUID = 610797484720857122L;


    private Integer id;
    
    private String username;
    
    private String userpassword;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

}