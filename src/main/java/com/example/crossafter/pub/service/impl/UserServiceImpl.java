package com.example.crossafter.pub.service.impl;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.User;
import com.example.crossafter.pub.dao.UserMapper;
import com.example.crossafter.pub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    public RespEntity register(HttpServletRequest request) throws IOException {
        int result;
        User user = new User();
        user.setUname(request.getParameter("uname"));
        user.setUpsw(DigestUtils.md5DigestAsHex(request.getParameter("upsw").getBytes()));
        user.setLocation(request.getParameter("location"));
        user.setPhonenumber(request.getParameter("phonenumber"));
        user.setID(request.getParameter("ID"));
        user.setRname(request.getParameter("rname"));
        try {
            result = userMapper.addUser(user);
        }
        catch (Exception e){
            result = 0;
        }
        String msg;
        if(result==1){
            msg = "注册成功";
        }
        else {
            msg = "注册失败";
        }
        RespEntity respEntity = new RespEntity(result,msg);
        return respEntity;
    }
}
