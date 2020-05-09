package com.example.crossafter.recommend.dao;

import com.example.crossafter.goods.bean.EvalDetail;
import com.example.crossafter.goods.bean.Evaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@Mapper
public interface RecommendMapper {
    //所有商品编号和评价均分
    List<Evaluation> getAll();
    //更新商品的权重值
    int updateWR(@Param("weight") double weight, @Param("gid") int gid);
    //用户X对商品的评价数据集
    List<EvalDetail> getAllEvaluationByUid(@Param("uid") int uid);
    
    //List<Integer> getUidByGid(@Param("gid") int gid, @Param("eva") double eva);
    //商品i的评价数据集
    List<EvalDetail> getAllEvaluationByGid(@Param("gid") int gid);
    
    List<Integer> getUidByGid(@Param("gid") int gid);
    
    List<Integer> getGidByUid(@Param("uid") int uid, @Param("eva") double eva);
    
    List<Integer> getTopEvaluation();
    
    int getCountByUid(@Param("uid")int uid);
    
    int getweightByGid(@Param("gid")int gid);
}
