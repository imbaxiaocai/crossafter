package com.example.crossafter.pub.utils;

import com.example.crossafter.pub.bean.RespHead;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class CheckJson {
    public boolean isEffective( JSONObject json,String key){
        if(json==null){
            return false;
        }
        else{
            if(json.containsKey(key)){
                return true;
            }
            else{
                return false;
            }
        }
    }
}
