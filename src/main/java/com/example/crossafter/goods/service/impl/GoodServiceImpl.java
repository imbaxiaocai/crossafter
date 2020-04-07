package com.example.crossafter.goods.service.impl;

import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.goods.dao.GoodMapper;
import com.example.crossafter.goods.service.GoodService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodServiceImpl implements GoodService{
    @Autowired
    GoodMapper goodMapper;
    public RespEntity getAllGoods(){
        RespEntity respEntity = new RespEntity();
        try {
            List<Good> goods = goodMapper.getAllGoods();
            respEntity.setData(goods);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return  respEntity;
        }
        return respEntity;
    }
}
