package com.example.crossafter.pub.service;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {
    RespEntity register(User user) throws IOException;
    RespEntity login(User user) throws IOException;
    RespEntity setAvatar(HttpServletRequest request,User user);
    RespEntity getUserInfo(int uid);
}
