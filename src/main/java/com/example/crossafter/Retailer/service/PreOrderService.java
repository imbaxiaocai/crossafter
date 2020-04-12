package com.example.crossafter.Retailer.service;

import com.example.crossafter.Retailer.bean.PreOrder;
import com.example.crossafter.pub.bean.RespEntity;

import java.util.ArrayList;

public interface PreOrderService {
    RespEntity addPreOrder(ArrayList<PreOrder> preOrders,String uname);
}
