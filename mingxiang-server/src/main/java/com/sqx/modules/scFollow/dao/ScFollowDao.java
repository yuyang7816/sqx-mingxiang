package com.sqx.modules.scFollow.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.scFollow.entity.ScFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScFollowDao extends BaseMapper<ScFollow> {


    Integer getMyDataFollowCount(@Param("userId") Long userId);


    IPage<UserEntity> getMyFollowList(@Param("pages") Page<UserEntity> pages, @Param("userId") Long userId);


    IPage<UserEntity> getMyFansList(Page<UserEntity> pages, Long userId);

    Integer getMyFansListCount(@Param("userId") Long userId);
}
