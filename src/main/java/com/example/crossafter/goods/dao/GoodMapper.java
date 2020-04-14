package com.example.crossafter.goods.dao;

import com.example.crossafter.goods.bean.Good;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@Mapper
public interface GoodMapper {
    List<Good> getAllGoods();
    Good getGoodById(int uid);
    double getSprice(int gid);
    int getDuration(int gid);
    int addGood(Good good);
    int getAmount(int gid);
    int setAmount(int gid);
}
