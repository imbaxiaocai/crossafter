package com.example.crossafter.Retailer.service.impl;

import com.example.crossafter.Retailer.bean.Trolley;
import com.example.crossafter.Retailer.dao.TrolleyMapper;
import com.example.crossafter.Retailer.service.TrolleyService;
import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.goods.dao.GoodMapper;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.bean.UserBehavior;
import com.example.crossafter.pub.dao.UserBehaviorMapper;
import com.example.crossafter.pub.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TrolleyServiceImpl implements TrolleyService{
    @Autowired
    private TrolleyMapper trolleyMapper;
    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserBehaviorMapper userBehaviorMapper;
    public RespEntity addToTrolley(Trolley trolley){
        //前端包含 uid gid amount默认为1
        RespEntity respEntity = new RespEntity();
        try{
            if(trolleyMapper.getTroById(trolley)!=null){
                trolleyMapper.plus(trolley);
            }
            else {
                int gid = trolley.getGid();
                Good good =goodMapper.getGoodById(gid);
                trolley.setFname(userMapper.getUnameById(good.getFid()));
                trolley.setSprice(good.getSprice());
                trolley.setGname(good.getGname());
                trolley.setDuration(good.getDuration());
                trolley.setGimg(good.getGimg());
                trolley.setFimg(userMapper.getAvatarById(good.getFid()));
                trolley.setFid(good.getFid());
                System.out.println(trolley.getGname());
                trolleyMapper.addToTrolley(trolley);
                //用户行为
                UserBehavior userBehavior =new UserBehavior();
                userBehavior.setGid(trolley.getGid());
                userBehavior.setUid(trolley.getUid());
                userBehavior.setScore(2);
                if(userBehaviorMapper.getByUidGid(userBehavior)==null){
                    userBehaviorMapper.addBehavior(userBehavior);
                }
                else {
                    userBehaviorMapper.updateBehavior(userBehavior);
                }
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
            HashMap<String,List<Trolley>> data = new HashMap<String,List<Trolley>>();
            for(int i=0;i<trolleys.size();i++){
                Trolley trolley = trolleys.get(i);
                String fname = trolley.getFname();
                if(data.containsKey(fname)){
                    data.get(fname).add(trolley);
                }
                else {
                    List<Trolley> ts = new ArrayList<Trolley>();
                    ts.add(trolley);
                    data.put(fname,ts);
                }
            }
            respEntity.setData(data);
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
