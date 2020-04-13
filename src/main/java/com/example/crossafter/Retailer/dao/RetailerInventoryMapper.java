package com.example.crossafter.Retailer.dao;

import com.example.crossafter.Retailer.bean.RetailerInventory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
@Mapper
public interface RetailerInventoryMapper {
    int addInventory(RetailerInventory retailerInventory);
}
