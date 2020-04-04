package com.example.crossafter.pub.service.impl;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.User;
import com.example.crossafter.pub.dao.UserMapper;
import com.example.crossafter.pub.service.UserService;
import com.example.crossafter.pub.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
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
    public RespEntity login(HttpServletRequest request) throws IOException{
        String msg;
        int result;
        User user = new User();
        //非空校验
        if("".equals(request.getParameter("uname"))||"".equals(request.getParameter("upsw"))){
            msg = "登录失败";
            result = 0;
        }
        else{
            user.setUpsw(DigestUtils.md5DigestAsHex(request.getParameter("upsw").getBytes()));
            user.setUname(request.getParameter("uname"));
            User u = userMapper.userLogin(user);
            if(user.equals(u)){
                msg = "登录成功";
                result = 1;
                //生成token

                stringRedisTemplate.opsForValue().set(request.getParameter("uname"),"testtoken",2*24*60*60, TimeUnit.SECONDS);
            }
            else{
                msg = "登录失败";
                result = 0;
            }
        }
        RespEntity respEntity = new RespEntity(result,msg);
        return respEntity;
    }
}
