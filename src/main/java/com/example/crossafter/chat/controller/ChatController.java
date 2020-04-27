package com.example.crossafter.chat.controller;

import com.example.crossafter.chat.bean.ChatMessage;
import com.example.crossafter.chat.service.ChatService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.utils.CheckJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private CheckJson checkJson;
    //翻译
    @RequestMapping("/translate")
    public void ckt_translate(@RequestBody Object obj, HttpServletResponse response) throws IOException {
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"message")){
            respEntity = chatService.translate(jsonObject.getString("message"));
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //获取聊天对象列表（前端依旧需要在没与某人聊天时进行新消息的轮询获取）
    @RequestMapping("/getlist")
    public void ckt_getChatList(@RequestBody Object obj, HttpServletResponse response) throws IOException {
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"uid")){
            respEntity = chatService.getChatLists(jsonObject.getInt("uid"));
        }
         else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //获取聊天记录（同时设置消息为已读）
    @RequestMapping("/getmsg")
    public void ckt_getMessages(@RequestBody Object obj, HttpServletResponse response) throws IOException {
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"message")){
            ChatMessage chatMessage = (ChatMessage)JSONObject.toBean(jsonObject.getJSONObject("message"),ChatMessage.class);
            respEntity = chatService.getMsgLists(chatMessage);
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
