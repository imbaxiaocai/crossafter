package com.example.crossafter.recommend.controller;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.utils.CheckJson;
import com.example.crossafter.pub.utils.RedisUtils;
import com.example.crossafter.recommend.service.RecommendService;
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
@RequestMapping("/recommend")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private CheckJson checkJson;
    //获取推荐商品
    @RequestMapping("/getgoods")
    public void ckt_getRecommendGoods(@RequestBody Object obj, HttpServletResponse response) throws Exception {
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"uid")&&checkJson.isEffective(jsonObject,"recounts")){
            respEntity = recommendService.updateRecommend(jsonObject.getInt("uid"),jsonObject.getInt("recounts"));
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
}
