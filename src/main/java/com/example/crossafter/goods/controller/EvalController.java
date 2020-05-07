package com.example.crossafter.goods.controller;

import com.example.crossafter.goods.bean.EvalDetail;
import com.example.crossafter.goods.bean.Evaluation;
import com.example.crossafter.goods.service.EvalService;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.utils.CheckJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/evaluation")
public class EvalController {
    @Autowired
    private EvalService evalService;
    @Autowired
    private CheckJson checkJson;
    //用户添加评价
    @RequestMapping("/addeval")
    public void ckt_addEvaluation(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"evaluation")){
            EvalDetail evaluation = (EvalDetail) JSONObject.toBean(jsonObject.getJSONObject("evaluation"),EvalDetail.class);
            respEntity = evalService.addEvaluation(evaluation);
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //获取某商品所有评价
    @RequestMapping("/getalldetails")
    public void ckt_getAllEvalDetails(@RequestBody Object obj, HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"gid")){
            respEntity = evalService.getAllEvalDetail(jsonObject.getInt("gid"));
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //获取某一商品总评
    @RequestMapping("/getevaluation")
    public void  ckt_getEvaluationByGid(@RequestBody Object obj,HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"gid")){
            respEntity = evalService.getEvaluationByGid(jsonObject.getInt("gid"));
        }
        else {
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
