package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.CourseLabel;
import com.sqx.modules.course.entity.vo.CourseLabelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Admin
* @description 针对表【course_label(课程标签表)】的数据库操作Mapper
* @createDate 2024-06-20 13:28:41
* @Entity generator.domain.CourseLabel
*/
@Mapper
public interface CourseLabelDao extends BaseMapper<CourseLabel> {

    CourseLabelVO selectByLabelId(@Param("id") Long id);

}




