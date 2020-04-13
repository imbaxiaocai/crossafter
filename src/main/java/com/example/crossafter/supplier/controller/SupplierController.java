package com.example.crossafter.supplier.controller;


import com.example.crossafter.Retailer.bean.PreOrder;
import com.example.crossafter.Retailer.dao.PreOrderMapper;
import com.example.crossafter.Retailer.service.PreOrderService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.utils.CheckJson;
import com.example.crossafter.supplier.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    private CheckJson checkJson;
    @Autowired
    private SupplierService supplierService;
    //上架商品
    //下架商品
    //获取所有库存申请
    @RequestMapping("/getapply")
    public void ckt_getApply(@RequestBody Object obj, HttpServletResponse response)throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"fid")){
            int fid = jsonObject.getInt("fid");
            respEntity = supplierService.getApply(fid);
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //库存申请处理
    @RequestMapping("/setstatus")
    public void ckt_setStatus(@RequestBody Object obj,HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"preorder")){
            jsonObject = jsonObject.getJSONObject("preorder");
            PreOrder preOrder = (PreOrder) JSONObject.toBean(jsonObject,PreOrder.class);
            respEntity = supplierService.setStatus(preOrder);
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //查看商品库存
    //修改商品库存
    //查看零售商库存
    //查看实际订单
    //发货
    //显示厂商商品

    //supplier 的bean
}
