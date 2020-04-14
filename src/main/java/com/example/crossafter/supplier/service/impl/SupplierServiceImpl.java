package com.example.crossafter.supplier.service.impl;

import com.example.crossafter.Retailer.bean.PreOrder;
import com.example.crossafter.Retailer.bean.RetailerInventory;
import com.example.crossafter.Retailer.dao.PreOrderMapper;
import com.example.crossafter.Retailer.dao.RetailerInventoryMapper;
import com.example.crossafter.goods.bean.Evaluation;
import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.goods.dao.EvaluationMapper;
import com.example.crossafter.goods.dao.GoodMapper;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class SupplierServiceImpl implements SupplierService{
    @Autowired
    private PreOrderMapper preOrderMapper;
    @Autowired
    private RetailerInventoryMapper retailerInventoryMapper;
    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private EvaluationMapper evaluationMapper;
    public RespEntity getApply(int fid){
        RespEntity respEntity = new RespEntity();
        try{
            List<PreOrder> preOrders = preOrderMapper.getApply(fid);
            respEntity.setData(preOrders);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity setStatus(PreOrder preOrder){
        RespEntity respEntity = new RespEntity();
        try{
            if (preOrder.getStatus()==1){
                RetailerInventory retailerInventory = new RetailerInventory();
                retailerInventory.setStatus(0);
                retailerInventory.setDuration(preOrder.getDuration());
                retailerInventory.setPoid(preOrder.getPoid());
                retailerInventory.setGid(preOrder.getGid());
                retailerInventory.setUid(preOrder.getRid());
                retailerInventory.setFid(preOrder.getFid());
                //计算日期偏移量
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
                String begin = sdf.format(date);
                retailerInventory.setBegin_date(begin);
                //偏移计算
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(begin));
                cal.add(Calendar.DAY_OF_YEAR,preOrder.getDuration());
                retailerInventory.setEnd_date(sdf.format(cal.getTime()));
                retailerInventoryMapper.addInventory(retailerInventory);
            }
            preOrderMapper.setStatus(preOrder);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    public RespEntity addGood(HttpServletRequest request, Good good){
        RespEntity respEntity = new RespEntity();
        try {
            MultipartHttpServletRequest req =(MultipartHttpServletRequest)request;
            MultipartFile multipartFile =  req.getFile("gimg");
            Random r = new Random();
            String t = "" + (r.nextInt(9000)+1000) + good.getGname();
            String filename = DigestUtils.md5DigestAsHex(t.getBytes());
            String realpath = "http://123.206.128.233:8080/waibaoimg/goods";
            good.setGimg(realpath+"/"+filename);
            String filepath = "/usr/www/waibaoimg/goods";
            File img = new File(filepath,filename);
            multipartFile.transferTo(img);
            //数据入库
            Evaluation evaluation = new Evaluation();
            //评价为空
            evaluation.setGid(good.getGid());
            evaluation.setWeight(-1);
            evaluationMapper.evalInit(evaluation);
            //添加商品
            goodMapper.addGood(good);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return  respEntity;
    }
}
