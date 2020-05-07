package com.example.crossafter.supplier.service;

import com.example.crossafter.Retailer.bean.PreOrder;
import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.pub.bean.RespEntity;

import javax.servlet.http.HttpServletRequest;

public interface SupplierService {
    RespEntity getApply(int fid);
    RespEntity setStatus(PreOrder preOrder);
    RespEntity addGood(HttpServletRequest request, Good good);
    RespEntity getInventoryByFid(int fid);
    RespEntity getGoodsByFid(int fid);
}
