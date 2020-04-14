package com.example.crossafter.supplier.controller;


import com.example.crossafter.Retailer.bean.PreOrder;
import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.utils.CheckJson;
import com.example.crossafter.pub.utils.RedisUtils;
import com.example.crossafter.supplier.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    private CheckJson checkJson;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    RedisUtils redisUtils;
    //上架商品
    @RequestMapping("/addgood")
    public void addGood(HttpServletRequest request,HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        //token验证
        if(request.getParameter("key")!=null&&request.getParameter("value")!=null){
            String sysval = redisUtils.getToken(request.getParameter("key"));
            //token通过
            if(sysval.equals(request.getParameter("value"))){
                Good good = new Good();
                good.setContent(Integer.parseInt(request.getParameter("content")));
                good.setDescription(request.getParameter("description"));
                good.setAmount(Integer.parseInt(request.getParameter("amount")));
                good.setDuration(Integer.parseInt(request.getParameter("duration")));
                good.setFid(Integer.parseInt(request.getParameter("fid")));
                good.setGname(request.getParameter("gname"));
                good.setGsales(0);
                good.setGprice(Double.parseDouble(request.getParameter("gprice")));
                good.setSprice(Double.parseDouble(request.getParameter("sprice")));
                good.setGtype(Integer.parseInt(request.getParameter("gtype")));
                //String imgStr = request.getParameter("gimg");
                respEntity = supplierService.addGood(request,good);

            }
            else{
                respEntity.setHead(RespHead.TOKEN_ERROR);
            }
        }
        else {
            respEntity.setHead(RespHead.TOKEN_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //下架商品
    //@RequestMapping("/delgood")
    //获取所有库存申请
    @RequestMapping("/getapply")
    public void ckt_getApply(@RequestBody Object obj, HttpServletResponse response)throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"fid")){
            int fid = jsonObject.getInt("fid");
            respEntity = supplierService.getApply(fid);
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //库存申请处理
    @RequestMapping("/setstatus")
    public void ckt_setStatus(@RequestBody Object obj,HttpServletResponse response) throws IOException{
        RespEntity respEntity = new RespEntity();
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(checkJson.isEffective(jsonObject,"preorder")){
            jsonObject = jsonObject.getJSONObject("preorder");
            PreOrder preOrder = (PreOrder) JSONObject.toBean(jsonObject,PreOrder.class);
            respEntity = supplierService.setStatus(preOrder);
        }
        else{
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();
    }
    //查看商品库存
    //修改商品库存
    //查看零售商库存
    //查看实际订单
    //发货
    //显示厂商商品

    //supplier 的bean
}
