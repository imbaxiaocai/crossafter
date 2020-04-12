package com.example.crossafter.Retailer.service.impl;

import com.example.crossafter.Retailer.bean.Trolley;
import com.example.crossafter.Retailer.dao.TrolleyMapper;
import com.example.crossafter.Retailer.service.TrolleyService;
import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.goods.dao.GoodMapper;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrolleyServiceImpl implements TrolleyService{
    @Autowired
    private TrolleyMapper trolleyMapper;
    @Autowired
    private GoodMapper goodMapper;
    public RespEntity addToTrolley(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        try{
            if(trolleyMapper.getTroById(trolley)!=null){
                trolleyMapper.plus(trolley);
            }
            else {
                trolleyMapper.addToTrolley(trolley);
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
    public RespEntity getTrolley(int uid){
        RespEntity respEntity = new RespEntity();
        try{
            List<Trolley> trolleys = trolleyMapper.getTroByUid(uid);
            List<Good> goods = new ArrayList<Good>();
            for(int i=0;i<trolleys.size();i++){
                Good good = goodMapper.getGoodById(trolleys.get(i).getGid());
                goods.add(good);
            }
            respEntity.setData(goods);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity setAmount(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        try{
            trolleyMapper.setAmount(trolley);
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
