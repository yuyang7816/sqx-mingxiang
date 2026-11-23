package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {



    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryUserCount(@Param("type") int type, @Param("date") String date);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date);


    int userMessage(String date, Integer type);



    UserEntity selectUserByUserId(@Param("userId") Long userId,@Param("byUserId") Long byUserId);

    IPage<UserEntity> selectUserPage(@Param("pages") Page<UserEntity> pages, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userEntity") UserEntity userEntity);

    IPage<HashMap<String, Object>> getUserIncomeList(@Param("pages") Page<HashMap<String, Object>> pages, @Param("type") Integer type, @Param("invitationCode") String invitationCode, @Param("userId") Long userId);
}
