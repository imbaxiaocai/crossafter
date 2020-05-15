package com.example.crossafter.chat.service;

import com.example.crossafter.chat.bean.ChatList;
import com.example.crossafter.chat.bean.ChatMessage;
import com.example.crossafter.pub.bean.RespEntity;
import org.springframework.beans.factory.annotation.Autowired;

public interface ChatService {
    RespEntity translate(String message,String lang);
    RespEntity getChatLists(int uid);
    RespEntity getMsgLists(ChatMessage chatMessage);
}
