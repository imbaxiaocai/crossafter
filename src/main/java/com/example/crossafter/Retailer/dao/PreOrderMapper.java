package com.example.crossafter.Retailer.dao;

import com.example.crossafter.Retailer.bean.PreOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@Mapper
public interface PreOrderMapper {
    //预订单
    int addPreOrder(PreOrder preOrder);
    List<PreOrder> getApply(int fid);
    int setStatus(PreOrder preOrder);
    PreOrder getPreOrderByPoid(int poid);
    List<PreOrder> getPreOrderByRid(int rid);
}
