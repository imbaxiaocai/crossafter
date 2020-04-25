package com.example.crossafter.chat.controller;


import com.example.crossafter.chat.bean.ChatUser;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@ServerEndpoint(value = "/chatwebsocket/{id}/{to}")
public class ChatWebSocket {

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
        user.setId(id);
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
        System.out.println(message);
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
            //onlineUser.getSession().getBasicRemote().sendText(message);
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
