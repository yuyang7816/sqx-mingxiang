package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseType;
import com.sqx.modules.course.entity.dto.CourseTypeDTO;

/**
* @author Admin
* @description 针对表【course_type(课程类型)】的数据库操作Service
* @createDate 2024-06-19 19:34:26
*/
public interface CourseTypeService extends IService<CourseType> {

    /**
     * 管理后台查看课程类型列表
     * @param pageNum
     * @param pageSize
     * @param typeName
     * @param parentId
     * @return
     */
    Result selectCourseTypeList(Integer pageNum, Integer pageSize, String typeName,Long parentId);

    /**
     * 管理后台添加课程类型
     * @param courseTypeDTO
     * @return
     */
    Result saveInfo(CourseTypeDTO courseTypeDTO);

    /**
     * 管理后台编辑课程类型
     * @param courseTypeDTO
     * @return
     */
    Result edit(CourseTypeDTO courseTypeDTO);

    /**
     * 管理后台删除课程类型
     *  courseTypeDTO
     * @return
     */
    Result delete(CourseTypeDTO courseTypeDTO);

    /**
     * 管理后台查看课程类型详情
     * @param id
     * @return
     */
    Result selectDetail(Integer id);

//    /**
//     * app探索页的首页
//     * @param pageNum
//     * @param pageSize
//     * @param parentId
//     * @return
//     */
//    Result exploreHomePage(Integer pageNum,Integer pageSize,Long parentId);
    /**
     * app用户根据类型id查询课程信息
     * @param pageNum
     * @param pageSize
     * @param id 这里的id是课程类型id（typeId）
     * @param level
     * @return
     */
    Result exploreHomePageByType(Integer pageNum,Integer pageSize,Long id,Integer level);

    /**
     * app用户根据搜索条件查询课程信息
     * @param courseName
     * @return
     */
    Result selectBySearch(String courseName);

    /**
     * app用户搜索框外的热门课程列表
     * @return
     */
    Result selectByHOT();
}
