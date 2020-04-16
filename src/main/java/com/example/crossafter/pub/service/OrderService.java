package com.example.crossafter.pub.service;

import com.example.crossafter.pub.bean.Order;
import com.example.crossafter.pub.bean.RespEntity;

public interface OrderService {
    RespEntity addOrder(Order order);
    RespEntity getUnshippedOrder(int fid);
    RespEntity shipOrder(Order order);
    RespEntity confirmOrder(int id);
}
