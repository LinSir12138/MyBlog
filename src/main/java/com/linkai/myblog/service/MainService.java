package com.linkai.myblog.service;

import com.linkai.myblog.entity.Friend;
import org.springframework.stereotype.Service;

public interface MainService {

    void becomeFriend(String blogTitle, String blogAddress, String imageAddress, String emailAddress);

    public void inserFirend(Friend friend);
}
