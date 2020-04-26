package com.example.crossafter.chat.service.impl;

import com.example.crossafter.chat.bean.ChatList;
import com.example.crossafter.chat.bean.ChatMessage;
import com.example.crossafter.chat.dao.ChatMapper;
import com.example.crossafter.chat.service.ChatService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;
    //public RespEntity translate()
    public RespEntity getChatLists(int uid){
        RespEntity respEntity = new RespEntity();
        try {
            List<ChatList> chatLists = chatMapper.getChatList(uid);
            respEntity.setData(chatLists);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity getMsgLists(ChatMessage chatMessage){
        RespEntity respEntity = new RespEntity();
        try {
            List<ChatMessage> chatMessages = chatMapper.getChatMsg(chatMessage);
            chatMessage.setStatus(1);
            chatMapper.setMsgStatus(chatMessage);
            ChatList chatList = new ChatList();
            chatList.setUid(chatMessage.getSender());
            chatList.setReceiver(chatMessage.getReceiver());
            chatList.setStatus(1);
            chatMapper.setChatListStatus(chatList);
            respEntity.setData(chatMessages);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
}
