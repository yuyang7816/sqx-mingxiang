package com.sqx.modules.effect.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.effect.entity.SoundEffects;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【sound_effects(音效表)】的数据库操作Mapper
* @createDate 2024-06-19 14:05:42
* @Entity generator.domain.SoundEffects
*/
@Mapper
public interface SoundEffectsDao extends BaseMapper<SoundEffects> {


    List<SoundEffects> getSoundEffectsByUserId(@Param("userId") Long userId);


    List<SoundEffects> getSoundEffectsByUserChoose(@Param("userId") Long userId);
}




