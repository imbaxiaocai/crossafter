package com.example.crossafter.chat.controller;


import com.example.crossafter.chat.bean.ChatList;
import com.example.crossafter.chat.bean.ChatMessage;
import com.example.crossafter.chat.bean.ChatUser;
import com.example.crossafter.chat.dao.ChatMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@ServerEndpoint(value = "/chatwebsocket/{id}/{to}")
public class ChatWebSocket{

    public static ChatMapper chatMapper;
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    //private static CopyOnWriteArraySet<WebSocketServer >  onlineUserSet = new CopyOnWriteArraySet<WebSocketServer >();
    private static Map<Integer, ChatUser> onlineUserMap = new ConcurrentHashMap<Integer, ChatUser>();

    //原来的构造方法要保留
    public ChatWebSocket(){

    }

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") int id, @PathParam("to") int to) {
        ChatUser user = new ChatUser();
        user.setId(to);
        user.setSession(session);
        onlineUserMap.put(id,user);
        // 将当前用户存到在线用户列表中
        addOnlineCount(); // 在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }



    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("id") int id, @PathParam("to") int to){
        // 移除的用户信息
        onlineUserMap.remove(id);
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }



    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(Session session, @PathParam("id") int id, @PathParam("to") int to, String message) {
        try {
            ChatList sender = new ChatList();
            ChatList receiver = new ChatList();
            ChatMessage msg = new ChatMessage();
            msg.setSender(id);
            msg.setReceiver(to);
            msg.setContent(message);
            //日期获取
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String now = sdf.format(date);
            msg.setSendtime(now);

            sender.setUid(id);
            sender.setReceiver(to);
            sender.setMsg(message);
            receiver.setUid(to);
            receiver.setReceiver(id);
            receiver.setMsg(message);
            //双方都在线
            if (onlineUserMap.containsKey(to)) {
                singleSend(message, onlineUserMap.get(to).getSession());
                sender.setStatus(1);
                receiver.setStatus(1);
                //添加已读msg
                msg.setStatus(1);
                chatMapper.addMsg(msg);
                int msgid = chatMapper.getMsgid(msg);
                sender.setLastmsg(msgid);
                receiver.setLastmsg(msgid);


            }
            //接收方未连接
            else {
                sender.setStatus(1);
                receiver.setStatus(0);
                //添加未读msg
                msg.setStatus(0);
                chatMapper.addMsg(msg);
                int msgid = chatMapper.getMsgid(msg);
                sender.setLastmsg(msgid);
                receiver.setLastmsg(msgid);

            }
            //存在聊天记录
            if (chatMapper.isInChatList(sender) > 0) {
                //发送方对话列表更新
                chatMapper.updateChatList(sender);
                //接受方对话列表更新
                chatMapper.updateChatList(receiver);
            }
            //不存在聊天记录
            else {
                //发送方对话列表添加用户
                chatMapper.addChatList(sender);
                //接受方对话列表添加用户
                chatMapper.addChatList(receiver);
            }
            System.out.println(message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。对特定用户发送消息
     * @param message
     * @throws IOException
     */
    public static void singleSend(String message,Session session){
        try {
            session.getAsyncRemote().sendText(message);
            //getAsyncRemote()和getBasicRemote()是异步与同步的区别，大部分情况下，推荐使用getAsyncRemote()异步
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }


    public static synchronized void addOnlineCount() {
        ChatWebSocket.onlineCount++;
    }


    public static synchronized void subOnlineCount() {
        ChatWebSocket.onlineCount--;
    }
}
