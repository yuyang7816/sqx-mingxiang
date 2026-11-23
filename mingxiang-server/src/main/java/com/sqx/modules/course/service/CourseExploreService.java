package com.sqx.modules.course.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseExplore;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Admin
* @description 针对表【course_explore(探索页相关)】的数据库操作Service
* @createDate 2025-09-11 09:19:39
*/
public interface CourseExploreService extends IService<CourseExplore> {

    Result addCourseExplore(CourseExplore courseExplore);

    Result editCourseExplore(CourseExplore courseExplore);

    Result deleteCourseExplore(CourseExplore courseExplore);

    Result selectCourseExploreList(Integer pageNum, Integer pageSize, String exploreName,Long parentId);

    Result selectCourseExploreList1(String exploreName);

    Result selectCourseExploreById(Long id);
}
