package com.example.crossafter.goods.controller;

import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.goods.service.GoodService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Goods")
public class GoodController {
    @Autowired
    private GoodService goodService;
    @Autowired
    private CheckJson checkJson;
    //获取所有商品信息
    @RequestMapping("/getAll")
    public void ckt_getAll(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = goodService.getAllGoods();
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //获取商品详情
    @RequestMapping("/getGoodDetail")
    public void ckt_getGoodDetails(@RequestBody Object obj, HttpServletResponse response)throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"gid")){
            respEntity = goodService.getGoodDetail(jsonObject.getInt("uid"),jsonObject.getInt("gid"));
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //搜索商品
    @RequestMapping("/searchgoods")
    public void ckt_searchGoods(@RequestBody Object obj, HttpServletResponse response)throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"gname")){
            respEntity = goodService.searchGoods(jsonObject.getString("gname"));
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //下架商品
    @RequestMapping("/removegood")
    public void ckt_removeGood(@RequestBody Object obj, HttpServletResponse response)throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"gid")){
            respEntity = goodService.removeGood(jsonObject.getInt("gid"));
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //获取前5销量
    @RequestMapping("/gettop5")
    public void ckt_getTop5(@RequestBody Object obj, HttpServletResponse response)throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"fid")){
            respEntity = goodService.getTop5(jsonObject.getInt("fid"));
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
