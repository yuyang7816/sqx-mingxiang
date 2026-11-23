package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.MeditationType;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Admin
* @description 针对表【meditation_type(冥想类型表)】的数据库操作Mapper
* @createDate 2024-06-21 15:54:55
* @Entity generator.domain.MeditationType
*/
@Mapper
public interface MeditationTypeDao extends BaseMapper<MeditationType> {

}




