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
import com.example.crossafter.pub.bean.User;
import com.example.crossafter.pub.dao.UserMapper;
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
    @Autowired
    private UserMapper userMapper;
    public RespEntity getInventoryByFid(int fid){
        RespEntity respEntity = new RespEntity();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String now = sdf.format(date);
            List<RetailerInventory> retailerInventories= retailerInventoryMapper.getInventoryByFid(fid,now);
            respEntity.setData(retailerInventories);
            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
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
            int status = preOrder.getStatus();
            preOrder = preOrderMapper.getPreOrderByPoid(preOrder.getPoid());
            preOrder.setStatus(status);
            //计算保证金
            double sum = 0;
            String rname = userMapper.getUnameById(preOrder.getRid());
            double rwallet = userMapper.getWallet(rname);
            String sname = userMapper.getUnameById(preOrder.getFid());
            double swallet = userMapper.getWallet(sname);
            sum = preOrder.getAmount()*goodMapper.getSprice(preOrder.getGid());
            //处理保证金和交易状态
            if (preOrder.getStatus()==1){
                int amount= goodMapper.getAmount(preOrder.getGid());
                if(amount>=preOrder.getAmount()) {
                    RetailerInventory retailerInventory = new RetailerInventory();
                    retailerInventory.setStatus(0);
                    retailerInventory.setDuration(preOrder.getDuration());
                    retailerInventory.setPoid(preOrder.getPoid());
                    retailerInventory.setGid(preOrder.getGid());
                    retailerInventory.setUid(preOrder.getRid());
                    retailerInventory.setFid(preOrder.getFid());
                    retailerInventory.setAmount(preOrder.getAmount());
                    retailerInventory.setUname(userMapper.getUnameById(preOrder.getRid()));
                    retailerInventory.setGname(goodMapper.getGname(preOrder.getGid()));
                    //计算日期偏移量
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    Date date = new Date();
                    String begin = sdf.format(date);
                    retailerInventory.setBegin_date(begin);
                    //偏移计算
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(begin));
                    cal.add(Calendar.DAY_OF_YEAR, preOrder.getDuration());
                    retailerInventory.setEnd_date(sdf.format(cal.getTime()));
                    goodMapper.setAmount(amount-preOrder.getAmount());
                    //厂商加钱
                    swallet = swallet + sum;
                    User supplier = new User();
                    supplier.setUname(sname);
                    supplier.setWallet(swallet);
                    userMapper.setWallet(supplier);
                    retailerInventoryMapper.addInventory(retailerInventory);
                }
                else {
                    respEntity.setHead(RespHead.REQ_ERROR);
                    return respEntity;
                }
            }
            else if(preOrder.getStatus()==2){
                //零售商加钱
                rwallet = rwallet + sum;
                User retailer = new User();
                retailer.setWallet(rwallet);
                retailer.setUname(rname);
                userMapper.setWallet(retailer);
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
            //添加商品
            goodMapper.addGood(good);
            //数据入库
            Evaluation evaluation = new Evaluation();
            //评价为空
            evaluation.setGid(good.getGid());
            evaluation.setWeight(-1);
            evaluationMapper.evalInit(evaluation);

            respEntity.setHead(RespHead.SUCCESS);
        }
        catch (Exception e){
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return  respEntity;
    }
    public RespEntity getGoodsByFid(int fid){
        RespEntity respEntity = new RespEntity();
        try{
            List<Good> goods = goodMapper.getGoodsByFid(fid);
            respEntity.setData(goods);
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
