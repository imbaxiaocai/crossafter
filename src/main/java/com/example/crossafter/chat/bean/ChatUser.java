package com.example.crossafter.chat.bean;

import javax.websocket.Session;

public class ChatUser {
    private Session session;
    private int id;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
