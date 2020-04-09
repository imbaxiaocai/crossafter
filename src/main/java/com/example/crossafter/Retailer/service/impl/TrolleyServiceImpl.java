package com.example.crossafter.Retailer.service.impl;

import com.example.crossafter.Retailer.bean.Trolley;
import com.example.crossafter.Retailer.dao.TrolleyMapper;
import com.example.crossafter.Retailer.service.TrolleyService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrolleyServiceImpl implements TrolleyService{
    @Autowired
    private TrolleyMapper trolleyMapper;
    public RespEntity addToTrolley(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        try{
            if(trolleyMapper.getTroById(trolley)!=null){
                trolleyMapper.plus(trolley);
            }
            else {
                trolleyMapper.addTotrolley(trolley);
            }
            respEntity.setHead(RespHead.SUCCESS);
            return respEntity;
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
    }
    public RespEntity delFromTrolley(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        try{
            trolleyMapper.deleteFromTro(trolley);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity pluGood(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        try{
            trolleyMapper.plus(trolley);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity subGood(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        try{
            trolleyMapper.sub(trolley);
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
