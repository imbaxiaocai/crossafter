package com.example.crossafter.pub.service.impl;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.Token;
import com.example.crossafter.pub.bean.User;
import com.example.crossafter.pub.dao.UserMapper;
import com.example.crossafter.pub.service.UserService;
import com.example.crossafter.pub.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;
    public RespEntity register(User user) throws IOException {
        int result;
        user.setUpsw(DigestUtils.md5DigestAsHex(user.getUpsw().getBytes()));
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
    public RespEntity login(User user) throws IOException{
        String msg;
        int result;
        RespEntity respEntity = new RespEntity();
        //非空校验
        if("".equals(user.getUname())||"".equals(user.getUpsw())){
            msg = "登录失败";
            result = 0;
        }
        else{
            //登录验证
            user.setUpsw(DigestUtils.md5DigestAsHex(user.getUpsw().getBytes()));
            User u = userMapper.userLogin(user);
            if(user.equals(u)){
                msg = "登录成功";
                result = 1;
                //生成token
                Token token = new Token();
                token.setKey(user.getUname());
                String tkn = token.createToken(user.getUname());
                token.setValue(tkn);
                redisUtils.setToken(user.getUname(),tkn);
                respEntity.setData(token);
            }
            else{
                msg = "登录失败";
                result = 0;
            }
        }
        respEntity.setCode(result);
        respEntity.setMsg(msg);
        return respEntity;
    }
}
