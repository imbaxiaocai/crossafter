package com.example.crossafter.chat.bean;

public class ChatList {
    private int uid;//用户id
    private int receiver;//聊天对象
    private int lastmsg;//最后一次消息id
    private int status;//是否有未读消息0-否1-是
    private String msg;//最后一条消息
    private String rname;//聊天对象用户名

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }
}
