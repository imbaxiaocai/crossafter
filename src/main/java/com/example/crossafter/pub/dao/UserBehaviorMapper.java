package com.example.crossafter.pub.dao;

import com.example.crossafter.pub.bean.UserBehavior;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
@Mapper
public interface UserBehaviorMapper {
    UserBehavior getByUidGid(UserBehavior userBehavior);
    int addBehavior(UserBehavior userBehavior);
    int updateBehavior(UserBehavior userBehavior);
    
    List<UserBehavior> getScoreByUid(@Param("uid") int uid);
    List<UserBehavior> getScoreByGid(@Param("gid") int gid);
}
