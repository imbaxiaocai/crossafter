package com.example.crossafter.pub.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public boolean setToken(String key,String value){
        try{
            stringRedisTemplate.opsForValue().set(key,value,60*60*24*2, TimeUnit.SECONDS);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public String getToken(String key){
        String value = "";
        try{
            value = stringRedisTemplate.opsForValue().get(key);
            return value;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }
}
