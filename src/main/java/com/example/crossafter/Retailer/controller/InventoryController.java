package com.example.crossafter.Retailer.controller;

import com.example.crossafter.Retailer.service.InventoryService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.utils.CheckJson;
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
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private CheckJson checkJson;
    @Autowired
    private InventoryService inventoryService;
    @RequestMapping("/getall")
    public void ckt_getAllInventory(@RequestBody Object obj, HttpServletResponse response) throws IOException {
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"uid")){
            respEntity = inventoryService.getInventory(jsonObject.getInt("uid"));
            respEntity.setHead(RespHead.SUCCESS);
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
    }
}
