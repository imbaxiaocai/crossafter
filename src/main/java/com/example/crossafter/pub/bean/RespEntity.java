package com.example.crossafter.pub.bean;

public class RespEntity {
    private int code;
    private String msg;
    private Object data;
    public RespEntity(){

    }
    public RespEntity(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RespEntity(int code,String msg,Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public RespEntity(RespHead rh){
        this.code = rh.getCode();
        this.msg = rh.getMsg();
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public void setHead(RespHead rh){
        this.code = rh.getCode();
        this.msg = rh.getMsg();
    }
}
