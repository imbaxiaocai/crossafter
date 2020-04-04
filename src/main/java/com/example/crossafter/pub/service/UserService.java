package com.example.crossafter.pub.service;

import com.example.crossafter.pub.bean.RespEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {
    RespEntity register(HttpServletRequest request) throws IOException;
    RespEntity login(HttpServletRequest request) throws IOException;
}
