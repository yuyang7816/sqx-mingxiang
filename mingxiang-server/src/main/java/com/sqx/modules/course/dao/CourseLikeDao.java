package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.CourseLike;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Admin
* @description 针对表【course_like(我的喜欢（课程）表)】的数据库操作Mapper
* @createDate 2024-06-24 11:29:22
* @Entity generator.domain.CourseLike
*/
@Mapper
public interface CourseLikeDao extends BaseMapper<CourseLike> {

    int deleteByCourseId(CourseLike courseLike);

}




