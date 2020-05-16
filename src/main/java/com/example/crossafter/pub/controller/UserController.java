package com.example.crossafter.pub.controller;

import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.bean.User;
import com.example.crossafter.pub.service.UserService;
import com.example.crossafter.pub.utils.CheckJson;
import com.example.crossafter.pub.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CheckJson checkJson;
    //用户注册
    @RequestMapping(value="/register",produces = "application/json;charset=UTF-8")
    public void register(@RequestBody User user, HttpServletResponse response) throws IOException{
        //用户唯一校验
        RespEntity respEntity = userService.register(user);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //用户登录
    @RequestMapping(value="/login",produces = "application/json;charset=UTF-8")
    public void login (@RequestBody User user, HttpServletResponse response) throws IOException{
        RespEntity respEntity = userService.login(user);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //aop_test
    @RequestMapping(value="/aop",produces = "application/json;charset=UTF-8")
    public void ckt_aop(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        response.getWriter().write("controller method");
        response.getWriter().close();
    }
    //设置头像
    @RequestMapping("/setavatar")
    public void setAvatar(HttpServletRequest request,HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        //token验证
        if(request.getParameter("key")!=null&&request.getParameter("value")!=null){
            String sysval = redisUtils.getToken(request.getParameter("key"));
            //token通过
            if(sysval!=""&&sysval!=null&&sysval.equals(request.getParameter("value"))){
                User user = new User();
                user.setUid(Integer.parseInt(request.getParameter("uid")));
                respEntity = userService.setAvatar(request,user);
            }
            else{
                respEntity.setHead(RespHead.TOKEN_ERROR);
            }
        }
        else {
            respEntity.setHead(RespHead.TOKEN_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //获取用户信息
    @RequestMapping("/getuserinfo")
    public void ckt_getUserInfo(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"uid")){
            respEntity = userService.getUserInfo(jsonObject.getInt("uid"));
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
