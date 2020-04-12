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
    //从购物车删除
    @RequestMapping("/del")
    public void delFromTrolley(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject json = JSONObject.fromObject(obj);
        if(checkJson.isEffective(json,"Trolley")){
            Trolley trolley = (Trolley) JSONObject.toBean(json.getJSONObject("Trolley"),Trolley.class);
            respEntity = trolleyService.delFromTrolley(trolley);
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //数量+1
    @RequestMapping("/plus")
    public void pluGood(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject json = JSONObject.fromObject(obj);
        if(checkJson.isEffective(json,"Trolley")){
            Trolley trolley = (Trolley) JSONObject.toBean(json.getJSONObject("Trolley"),Trolley.class);
            respEntity = trolleyService.pluGood(trolley);
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //数量-1
    @RequestMapping("/sub")
    public void subGood(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject json = JSONObject.fromObject(obj);
        if(checkJson.isEffective(json,"Trolley")){
            Trolley trolley = (Trolley) JSONObject.toBean(json.getJSONObject("Trolley"),Trolley.class);
            respEntity = trolleyService.subGood(trolley);
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //获取购物车
    @RequestMapping("/gettro")
    public void getTrolleyById(@RequestBody Object obj,HttpServletResponse response)throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"uid")){
            int uid = jsonObject.getInt("uid");
            respEntity = trolleyService.getTrolley(uid);
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //设置商品数量
    @RequestMapping("/setamount")
    public void setAmount(@RequestBody Object obj,HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"Trolley")){
            Trolley trolley = (Trolley) JSONObject.toBean(jsonObject.getJSONObject("Trolley"),Trolley.class);
            respEntity = trolleyService.setAmount(trolley);
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
