package com.example.crossafter.Retailer.controller;

import com.example.crossafter.Retailer.bean.Trolley;
import com.example.crossafter.Retailer.service.TrolleyService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.utils.CheckJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/trolley")
public class TrolleyControler {
    @Autowired
    private TrolleyService trolleyService;
    @Autowired
    private CheckJson checkJson;
    //添加到购物车
    @RequestMapping("/add")
    public void addToTrolley(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject json = JSONObject.fromObject(obj);
        if(checkJson.isEffective(json,"Trolley")){
            Trolley trolley = (Trolley) JSONObject.toBean(json.getJSONObject("Trolley"),Trolley.class);
            respEntity = trolleyService.addToTrolley(trolley);
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //从购物车删除s
    //数量+1
    //数量-1
}
