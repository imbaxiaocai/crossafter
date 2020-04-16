package com.example.crossafter.goods.service.impl;

import com.example.crossafter.goods.bean.EvalDetail;
import com.example.crossafter.goods.bean.Evaluation;
import com.example.crossafter.goods.dao.EvalDetailMapper;
import com.example.crossafter.goods.dao.EvaluationMapper;
import com.example.crossafter.goods.service.EvalService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.dao.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvalServiceImpl implements EvalService{
    @Autowired
    private EvalDetailMapper evalDetailMapper;
    @Autowired
    private EvaluationMapper evaluationMapper;
    @Autowired
    private OrderMapper orderMapper;
    public RespEntity addEvaluation(EvalDetail evaluation){
        RespEntity respEntity = new RespEntity();
        try {
            //添加评价
            evalDetailMapper.addEvaluation(evaluation);
            //修改状态为已评价
            orderMapper.evalOrder(evaluation.getOid());
            //重新计算评价分
            Evaluation eval = evaluationMapper.getEvalByGid(evaluation.getGid());
            int amount = eval.getAmount();
            double avg = eval.getEvaluation();
            eval.setAmount(amount+1);
            avg = (avg*amount+evaluation.getEvaluation())/(amount+1);
            eval.setEvaluation(avg);
            evaluationMapper.updateEval(eval);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
}
