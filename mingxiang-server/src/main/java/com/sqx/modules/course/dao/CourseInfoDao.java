package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.CourseInfo;
import com.sqx.modules.course.entity.vo.CourseInfoVO;
import com.sqx.modules.course.entity.vo.MentorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程信息(CourseInfo)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-16 15:19:26
 */
@Mapper
public interface CourseInfoDao extends BaseMapper<CourseInfo> {

    CourseInfoVO selectByDetail(@Param("id") Long id);

    MentorVO selectByCourseId(@Param("id") Long id);

    List<CourseInfo> selectCourseInfoList(@Param("courseName") String courseName);

    List<CourseInfoVO> selectByUserId(@Param("userId") Long userId);

    List<CourseInfo> selectLists(@Param("courseName") String courseName,
                                 @Param("typeName") String typeName,
                                 @Param("courseTitle") String courseTitle,
                                 @Param("mentorTitle") String mentorTitle,
                                 @Param("mentor") String mentor,
                                 @Param("courseStatus") Integer courseStatus);
}

