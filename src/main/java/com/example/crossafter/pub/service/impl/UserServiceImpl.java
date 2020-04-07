package com.example.crossafter.pub.service.impl;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
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
        RespEntity respEntity = new RespEntity();
        try {
            user.setUpsw(DigestUtils.md5DigestAsHex(user.getUpsw().getBytes()));
            result = userMapper.addUser(user);
            if(result==1){
                respEntity.setHead(RespHead.SUCCESS);
            }
            else {
                respEntity.setHead(RespHead.FAILED);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity login(User user) throws IOException{
        RespEntity respEntity = new RespEntity();
        //非空校验
        if("".equals(user.getUname())||"".equals(user.getUpsw())){
            respEntity.setHead(RespHead.FAILED);
        }
        else{
            //登录验证
            try {
                user.setUpsw(DigestUtils.md5DigestAsHex(user.getUpsw().getBytes()));
                User u = userMapper.userLogin(user);
                if (user.equals(u)) {
                    respEntity.setHead(RespHead.SUCCESS);
                    //生成token
                    Token token = new Token();
                    token.setKey(user.getUname());
                    String tkn = token.createToken(user.getUname());
                    token.setValue(tkn);
                    redisUtils.setToken(user.getUname(), tkn);
                    respEntity.setData(token);
                } else {
                    respEntity.setHead(RespHead.FAILED);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                respEntity.setHead(RespHead.SYS_ERROE);
                return  respEntity;
            }
        }
        return respEntity;
    }
}
