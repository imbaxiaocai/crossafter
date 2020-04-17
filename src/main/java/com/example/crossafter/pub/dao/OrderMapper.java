package com.example.crossafter.pub.dao;

import com.example.crossafter.pub.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@Mapper
public interface OrderMapper {
    int addOrder(Order order);
    List<Order> getUnshippedOrder(int fid);
    int shipOrder(Order order);
    int confirmOrder(int id);
    int evalOrder(int id);
    Order getOrderById(int id);
}
