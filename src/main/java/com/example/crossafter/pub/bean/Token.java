package com.example.crossafter.pub.bean;

import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Token {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String createToken(String uname){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String t = "" + uname; //username
        t = t.concat(simpleDateFormat.format(date));//time
        Random r = new Random();
        t = t + "" + (r.nextInt(9000)+1000);//4 random number
        return DigestUtils.md5DigestAsHex(t.getBytes());
    }
}
