package com.example.crossafter.recommend.service;

import com.example.crossafter.pub.bean.RespEntity;

public interface RecommendService {
    //更新商品的权重值
    RespEntity updateWR();
    
    /*
     * 商品推荐
     * @param
     * int uid 用户编号
     * int reCounts 推荐数量
     * @retrun RespEntity
     */
    
    RespEntity updateRecommend(int uid, int reCounts);
}
