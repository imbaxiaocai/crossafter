package com.example.crossafter.goods.service;

import com.example.crossafter.goods.bean.EvalDetail;
import com.example.crossafter.goods.bean.Evaluation;
import com.example.crossafter.pub.bean.RespEntity;

public interface EvalService {
    RespEntity addEvaluation(EvalDetail evaluation);
    RespEntity getAllEvalDetail(int gid);
}
