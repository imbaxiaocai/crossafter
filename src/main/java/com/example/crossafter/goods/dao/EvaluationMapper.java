package com.example.crossafter.goods.dao;

import com.example.crossafter.goods.bean.Evaluation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
@Mapper
public interface EvaluationMapper {
    int evalInit(Evaluation evaluation);
}
