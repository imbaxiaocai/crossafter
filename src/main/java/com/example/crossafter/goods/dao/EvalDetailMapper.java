package com.example.crossafter.goods.dao;

import com.example.crossafter.goods.bean.EvalDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
@Mapper
public interface EvalDetailMapper {
    int addEvaluation(EvalDetail evalDetail);
}
