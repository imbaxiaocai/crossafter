package com.example.crossafter.Retailer.controller;

import com.example.crossafter.pub.bean.Order;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.service.OrderService;
import com.example.crossafter.pub.utils.CheckJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSON;
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
@RequestMapping("/rorder")
public class OrderController {
    @Autowired
    private CheckJson checkJson;
    @Autowired
    private OrderService orderService;
    //零售商下单
    @RequestMapping("/addorder")
    public void ckt_addOrder(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"order")){
            Order order = (Order)JSONObject.toBean(jsonObject.getJSONObject("order"),Order.class);
            respEntity = orderService.addOrder(order);
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
