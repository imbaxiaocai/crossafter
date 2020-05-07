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
    double getWallet(String uname);
    int setWallet(User user);
    String getUnameById(int uid);
    int getUidByUname(String uname);
    String getAvatarById(int uid);
    int setAvater(User user);
    User getUserInfo(int uid);
}
