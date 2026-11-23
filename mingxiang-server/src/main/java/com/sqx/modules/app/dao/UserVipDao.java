package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.app.entity.UserVip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Admin
* @description 针对表【user_vip】的数据库操作Mapper
* @createDate 2024-06-25 09:49:27
* @Entity generator.domain.UserVip
*/
@Mapper
public interface UserVipDao extends BaseMapper<UserVip> {

    UserVip selectByUserId(@Param("userId") Long userId);
}




