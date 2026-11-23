package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.CourseAndLabel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Admin
* @description 针对表【course_and_label】的数据库操作Mapper
* @createDate 2024-06-20 13:28:47
* @Entity generator.domain.CourseAndLabel
*/
@Mapper
public interface CourseAndLabelDao extends BaseMapper<CourseAndLabel> {

    int insertBatch(List<CourseAndLabel> courseAndLabels);

}




