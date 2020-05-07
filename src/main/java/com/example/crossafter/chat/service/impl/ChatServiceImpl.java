package com.example.crossafter.chat.service.impl;

import com.example.crossafter.chat.bean.ChatList;
import com.example.crossafter.chat.bean.ChatMessage;
import com.example.crossafter.chat.dao.ChatMapper;
import com.example.crossafter.chat.service.ChatService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;
    public RespEntity translate(String message){
        RespEntity respEntity = new RespEntity();
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/x-www-form-urlencoded"));
            MultiValueMap<String,String> map = new LinkedMultiValueMap<String, String>();
            map.add("q",message);
            map.add("from","auto");
            map.add("to","zh");
            map.add("appid","20200413000418451");
            //生成4为随机数
            Random r = new Random();
            String salt = ""+ r.nextInt(9000)+1000;
            map.add("salt",salt);
            String sign = "20200413000418451"+ message + salt + "Xnq02Ab0ZeOdtRlSfuQY";
            sign = DigestUtils.md5DigestAsHex(sign.getBytes());
            map.add("sign",sign);
            //发送请求
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<Object> response = restTemplate.postForEntity( url, request,Object.class);
            JSONObject jsonObject = JSONObject.fromObject(response);
            jsonObject = jsonObject.getJSONObject("body");
            JSONArray jsonArray = jsonObject.getJSONArray("trans_result");
            jsonObject = jsonArray.getJSONObject(0);
            String result = jsonObject.getString("dst");
            respEntity.setData(result);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity getChatLists(int uid){
        RespEntity respEntity = new RespEntity();
        try {
            List<ChatList> chatLists = chatMapper.getChatList(uid);
            respEntity.setData(chatLists);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity getMsgLists(ChatMessage chatMessage){
        RespEntity respEntity = new RespEntity();
        try {
            List<ChatMessage> chatMessages = chatMapper.getChatMsg(chatMessage);
            chatMessage.setStatus(1);
            chatMapper.setMsgStatus(chatMessage);
            ChatList chatList = new ChatList();
            chatList.setUid(chatMessage.getSender());
            chatList.setReceiver(chatMessage.getReceiver());
            chatList.setStatus(1);
            chatMapper.setChatListStatus(chatList);
            respEntity.setData(chatMessages);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
}
