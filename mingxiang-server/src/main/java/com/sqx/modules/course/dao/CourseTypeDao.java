package com.sqx.modules.course.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.CourseType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【course_type(课程类型)】的数据库操作Mapper
* @createDate 2024-06-19 19:34:26
* @Entity generator.domain.CourseType
*/
@Mapper
public interface CourseTypeDao extends BaseMapper<CourseType> {

    CourseType selectByParentId(@Param("parentId") Long parentId);

    List<CourseType> selectListByCourse();

    List<CourseType> selectByUserId(Long userId);

    List<CourseType> selectByTypeId(Long userId);
}




