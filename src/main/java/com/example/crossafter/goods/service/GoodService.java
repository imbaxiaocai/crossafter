package com.example.crossafter.goods.service;

import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.pub.bean.RespEntity;

import java.util.List;

public interface GoodService {
    RespEntity getAllGoods();
    RespEntity getGoodDetail(int uid,int gid);
    RespEntity searchGoods(String gname);
    RespEntity removeGood(int gid);
    RespEntity getTop5(int fid);
}
