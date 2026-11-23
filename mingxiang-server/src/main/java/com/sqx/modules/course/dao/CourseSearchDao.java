package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.CourseSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【course_search(搜索热门表)】的数据库操作Mapper
* @createDate 2024-06-21 15:00:28
* @Entity generator.domain.CourseSearch
*/
@Mapper
public interface CourseSearchDao extends BaseMapper<CourseSearch> {

    List<CourseSearch> selectByHOT();


    int insertBatch(@Param("courseSearchList")List<CourseSearch> courseSearchList);
}




