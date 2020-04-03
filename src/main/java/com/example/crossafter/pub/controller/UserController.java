package com.example.crossafter.pub.controller;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.User;
import com.example.crossafter.pub.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.DigestUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/register")
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //用户唯一校验
        RespEntity respEntity = userService.register(request);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
