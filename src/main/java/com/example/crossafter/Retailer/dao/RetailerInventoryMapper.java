package com.example.crossafter.Retailer.dao;

import com.example.crossafter.Retailer.bean.RetailerInventory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@Mapper
public interface RetailerInventoryMapper {
    int addInventory(RetailerInventory retailerInventory);
    List<RetailerInventory> getInventory(int uid,String now);
}
