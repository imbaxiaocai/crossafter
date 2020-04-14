package com.example.crossafter.Retailer.service.impl;

import com.example.crossafter.Retailer.bean.RetailerInventory;
import com.example.crossafter.Retailer.dao.RetailerInventoryMapper;
import com.example.crossafter.Retailer.service.InventoryService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService{
    @Autowired
    private RetailerInventoryMapper retailerInventoryMapper;
    public RespEntity getInventory(int uid){
        RespEntity respEntity = new RespEntity();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String now = sdf.format(date);
            List<RetailerInventory> retailerInventories= retailerInventoryMapper.getInventory(uid,now);
            respEntity.setData(retailerInventories);
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
