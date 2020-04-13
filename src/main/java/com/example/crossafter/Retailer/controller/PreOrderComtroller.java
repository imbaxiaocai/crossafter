package com.example.crossafter.Retailer.controller;

import com.example.crossafter.Retailer.bean.PreOrder;
import com.example.crossafter.Retailer.service.PreOrderService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.bean.Token;
import com.example.crossafter.pub.utils.CheckJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/preorder")
public class PreOrderComtroller {
    @Autowired
    PreOrderService preOrderService;
    @Autowired
    CheckJson checkJson;
    //库存预定
    @RequestMapping("/addpreorder")
    public void ckt_addPreOrder(@RequestBody Object obj, HttpServletResponse response) throws IOException {
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"preorder")){
            JSONObject tkobj = jsonObject.getJSONObject("token");
            Token tkn = (Token) JSONObject.toBean(tkobj,Token.class);
            JSONArray preOrderArray = jsonObject.getJSONArray("preorder");
            ArrayList<PreOrder> preOrders = new ArrayList<PreOrder>();
            for (int i=0;i<preOrderArray.size();i++){
                PreOrder p = (PreOrder) JSONObject.toBean(preOrderArray.getJSONObject(i),PreOrder.class);
                preOrders.add(p);
            }
            respEntity = preOrderService.addPreOrder(preOrders,tkn.getKey());
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
