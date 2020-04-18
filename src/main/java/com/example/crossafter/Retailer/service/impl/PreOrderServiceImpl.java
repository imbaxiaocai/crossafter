package com.example.crossafter.Retailer.service.impl;

import com.example.crossafter.Retailer.bean.PreOrder;
import com.example.crossafter.Retailer.bean.Trolley;
import com.example.crossafter.Retailer.dao.PreOrderMapper;
import com.example.crossafter.Retailer.dao.TrolleyMapper;
import com.example.crossafter.Retailer.service.PreOrderService;
import com.example.crossafter.goods.dao.GoodMapper;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.bean.User;
import com.example.crossafter.pub.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

@Service
public class PreOrderServiceImpl implements PreOrderService {
    @Autowired
    private PreOrderMapper preOrderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private TrolleyMapper trolleyMapper;
    public RespEntity addPreOrder(ArrayList<PreOrder> preOrders,String uname){
        RespEntity respEntity = new RespEntity();
        try {
            //计算总保证金
            double sum = 0;
            for(int j=0;j<preOrders.size();j++){
                double sprice = goodMapper.getSprice(preOrders.get(j).getGid())*preOrders.get(j).getAmount();
                int duration = goodMapper.getDuration(preOrders.get(j).getGid());
                String gname = goodMapper.getGname(preOrders.get(j).getGid());
                preOrders.get(j).setSsprice(sprice);
                preOrders.get(j).setDuration(duration);
                preOrders.get(j).setGname(gname);
                sum = sum + sprice;
            }
            //暂扣保证金
            double wallet = userMapper.getWallet(uname);
            if(wallet<sum){
                respEntity.setHead(RespHead.LACK_OF_BALANCE);
                return respEntity;
            }
            else {
                User user = new User();
                user.setUname(uname);
                user.setWallet(wallet-sum);
                userMapper.setWallet(user);
            }
            //将已预定从购物车中除去
            for (int k=0;k<preOrders.size();k++){
                Trolley trolley = new Trolley();
                trolley.setGid(preOrders.get(k).getGid());
                trolley.setUid(preOrders.get(k).getRid());
                trolleyMapper.deleteFromTro(trolley);
            }
            //添加预订单
            for (int i=0;i<preOrders.size();i++){
                preOrderMapper.addPreOrder(preOrders.get(i));
            }
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
