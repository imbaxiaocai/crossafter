package com.example.crossafter.Retailer.dao;

import com.example.crossafter.Retailer.bean.PreOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
@Mapper
public interface PreOrderMapper {
    //预订单 库存
    int addPreOrder(PreOrder preOrder);
}
