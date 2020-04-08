package com.example.crossafter.Retailer.service.impl;

import com.example.crossafter.Retailer.bean.Trolley;
import com.example.crossafter.Retailer.service.TrolleyService;
import com.example.crossafter.pub.bean.RespEntity;
import org.springframework.stereotype.Service;

@Service
public class TrolleyServiceImpl implements TrolleyService{
    public RespEntity addToTrolley(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        return respEntity;
    }
    public RespEntity delFromTrolley(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        return respEntity;
    }
    public RespEntity pluGood(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        return respEntity;
    }
    public RespEntity subGood(Trolley trolley){
        RespEntity respEntity = new RespEntity();
        return respEntity;
    }

}
