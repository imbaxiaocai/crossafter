package com.example.crossafter.recommend.service;

import com.example.crossafter.pub.bean.RespEntity;

public interface RecommendService {
    //更新商品的权重值
    RespEntity updateWR();
    
    //更新用户的推荐商品列表
    RespEntity updateRecommend(int uid);
}
