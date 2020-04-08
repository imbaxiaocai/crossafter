package com.example.crossafter.Retailer.service;

import com.example.crossafter.Retailer.bean.Trolley;
import com.example.crossafter.pub.bean.RespEntity;

public interface TrolleyService {
    RespEntity addToTrolley(Trolley trolley);
    RespEntity delFromTrolley(Trolley trolley);
    RespEntity pluGood(Trolley trolley);
    RespEntity subGood(Trolley trolley);
}
