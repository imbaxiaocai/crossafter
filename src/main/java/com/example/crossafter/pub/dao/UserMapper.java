package com.example.crossafter.pub.dao;

import com.example.crossafter.pub.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
@Mapper
public interface UserMapper {
    int addUser(User user);
    User userLogin(User user);
}
