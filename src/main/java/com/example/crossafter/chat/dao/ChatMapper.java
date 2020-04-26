package com.example.crossafter.chat.dao;

import com.example.crossafter.chat.bean.ChatList;
import com.example.crossafter.chat.bean.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@Mapper
public interface ChatMapper {
    int addMsg(ChatMessage chatMessage);//添加聊天记录
    int setMsgStatus(ChatMessage chatMessage);//聊天记录设置为已读
    int isInChatList(ChatList chatList);//有过聊天记录
    int getMsgid(ChatMessage chatMessage);//获得聊天记录id
    int addChatList(ChatList chatList);//添加聊天对象
    int updateChatList(ChatList chatList);//更新聊天对象
    int setChatListStatus(ChatList chatList);//设置对象信息已读
    List<ChatList> getChatList(int uid);//获取聊天对象
    List<ChatMessage> getChatMsg(ChatMessage chatMessage);//获取聊天记录
}
