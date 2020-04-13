package com.example.crossafter.supplier.service;

import com.example.crossafter.Retailer.bean.PreOrder;
import com.example.crossafter.pub.bean.RespEntity;

public interface SupplierService {
    RespEntity getApply(int fid);
    RespEntity setStatus(PreOrder preOrder);
}
