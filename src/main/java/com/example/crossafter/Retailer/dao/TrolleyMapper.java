package com.example.crossafter.Retailer.dao;

import com.example.crossafter.Retailer.bean.Trolley;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@Mapper
public interface TrolleyMapper {
    int addTotrolley(Trolley trolley);
    int deleteFromTro(Trolley trolley);
    List<Trolley> getTroByUid(int uid);
    Trolley getTroByGid(Trolley trolley);
    int plus(Trolley trolley);
    int sub(Trolley trolley);
}
