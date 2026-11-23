package com.sqx.modules.effect.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.effect.entity.SoundAndUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【sound_and_user(音效和用户中间表)】的数据库操作Mapper
* @createDate 2024-06-19 15:43:59
* @Entity generator.domain.SoundAndUser
*/
@Mapper
public interface SoundAndUserDao extends BaseMapper<SoundAndUser> {

    int selectCountByUserId(@Param("id") Long id,
                            @Param("userId") Long userId);

    int insertBatch(@Param("list") List<SoundAndUser> toInsert);
}




