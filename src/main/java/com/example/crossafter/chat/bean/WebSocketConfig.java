package com.example.crossafter.chat.bean;

import com.example.crossafter.chat.controller.ChatWebSocket;
import com.example.crossafter.chat.dao.ChatMapper;
import com.example.crossafter.pub.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


@Configuration
public class WebSocketConfig {
    //这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    @Autowired
    public void setChatMapper(ChatMapper chatMapper){
        ChatWebSocket.chatMapper = chatMapper;
    }
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        ChatWebSocket.userMapper = userMapper;
    }
}