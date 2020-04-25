package com.example.crossafter.recommend.service.impl;

import com.example.crossafter.goods.bean.Evaluation;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.recommend.dao.RecommendMapper;
import com.example.crossafter.recommend.service.RecommendService;
import com.example.crossafter.recommend.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService{
    @Autowired
    RecommendMapper recommendMapper;
    //计算商品的权重
    @Override
    public RespEntity updateWR() {
        // TODO Auto-generated method stub
        RespEntity respEntity = new RespEntity();
        try {
            List<Evaluation> list = recommendMapper.getAll();
            double sum = 0;
            for (Evaluation e : list) {
                sum += e.getEvaluation();
            }
            double C = sum / list.size();

            for (Evaluation e : list) {
                double WR = new Utils().getWR(e.getEvaluation(), C, e.getAmount());
                recommendMapper.updateWR(WR, e.getGid());
            }
        }catch(Exception e) {
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
}
