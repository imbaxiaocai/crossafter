package com.example.crossafter.pub.bean;

public enum RespHead {
    //
    SYS_ERROE(500,"系统错误"),
    SUCCESS(200,"成功"),
    FAILED(100,"失败"),
    TOKEN_ERROR(102,"非法Token"),
    REQ_ERROR(101,"非法请求"),
    LACK_OF_BALANCE(103,"余额不足");
    private int code;
    private String msg;

    RespHead(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
