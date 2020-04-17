package com.example.crossafter.pub.service.impl;

import com.example.crossafter.Retailer.bean.RetailerInventory;
import com.example.crossafter.Retailer.dao.RetailerInventoryMapper;
import com.example.crossafter.goods.dao.GoodMapper;
import com.example.crossafter.pub.bean.Order;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.bean.User;
import com.example.crossafter.pub.dao.OrderMapper;
import com.example.crossafter.pub.dao.UserMapper;
import com.example.crossafter.pub.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RetailerInventoryMapper retailerInventoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodMapper goodMapper;
    public RespEntity addOrder(Order order){
        RespEntity respEntity = new RespEntity();
        try{
            int ramount = retailerInventoryMapper.getAmountByPoid(order.getPoid());
            if(ramount>=order.getAmount()){
                RetailerInventory retailerInventory = new RetailerInventory();
                retailerInventory.setAmount(ramount-order.getAmount());
                retailerInventory.setPoid(order.getPoid());
                retailerInventoryMapper.subInventory(retailerInventory);
                orderMapper.addOrder(order);
                respEntity.setHead(RespHead.SUCCESS);
            }
            else{
                respEntity.setHead(RespHead.REQ_ERROR);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return  respEntity;
    }
    public RespEntity getUnshippedOrder(int fid){
        RespEntity respEntity = new RespEntity();
        try{
            List<Order> orders = orderMapper.getUnshippedOrder(fid);
            respEntity.setData(orders);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return  respEntity;
    }
    public RespEntity shipOrder(Order order){
        RespEntity respEntity = new RespEntity();
        try{
            orderMapper.shipOrder(order);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return  respEntity;
    }
    public RespEntity confirmOrder(int id){
        RespEntity respEntity = new RespEntity();
        try{
            Order order = orderMapper.getOrderById(id);
            //确认收货
            orderMapper.confirmOrder(id);
            //厂商加钱
            String uname = userMapper.getUnameById(order.getFid());
            double wallet = userMapper.getWallet(uname);
            int gid = order.getGid();
            wallet = wallet + order.getAmount()*(goodMapper.getGprice(gid)-goodMapper.getSprice(gid));
            User user = new User();
            user.setWallet(wallet);
            user.setUname(uname);
            userMapper.setWallet(user);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return  respEntity;
    }
}
