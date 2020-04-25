package com.example.crossafter.recommend.dao;

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
    int updateWR(@Param("weight") double weight, @Param("gid")int gid);
}
