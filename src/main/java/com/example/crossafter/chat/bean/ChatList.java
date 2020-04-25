package com.example.crossafter.chat.bean;

public class ChatList {
    private int uid;//用户id
    private int receiver;//聊天对象
    private int lastmsg;//最后一次消息id

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(int lastmsg) {
        this.lastmsg = lastmsg;
    }
}
