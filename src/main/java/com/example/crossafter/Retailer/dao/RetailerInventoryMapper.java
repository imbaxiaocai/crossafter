package com.example.crossafter.Retailer.dao;

import com.example.crossafter.Retailer.bean.RetailerInventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@Mapper
public interface RetailerInventoryMapper {
    int addInventory(RetailerInventory retailerInventory);
    List<RetailerInventory> getInventory(@Param("uid") int uid, @Param("now") String now);
    List<RetailerInventory> getInventoryByFid(@Param("uid")int uid,@Param("now")String now);
    List<RetailerInventory> getExpired(String now);
    int getAmountByPoid(int poid);
    int subInventory(RetailerInventory retailerInventory);
    int addToHis(RetailerInventory retailerInventory);
    int delExpiredInventory(RetailerInventory retailerInventory);
    RetailerInventory getInventoryByPoid(int poid);
}
