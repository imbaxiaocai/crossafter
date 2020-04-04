package com.example.crossafter.pub.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class RedisUtils {
    private RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    public boolean setToken(String key,String value){
        try{
            redisTemplate.opsForValue().set(key,value,60*60*24*2);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean getToken(String key){
        try{

        }
        catch (Exception e){

        }
        return false;
    }
}
