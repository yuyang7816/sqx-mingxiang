package com.sqx.modules.course.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseInfo;
import com.sqx.modules.course.entity.dto.CourseInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 课程信息(CourseInfo)表服务接口
 *
 * @author makejava
 * @since 2024-05-16 15:19:26
 */
public interface CourseInfoService extends IService<CourseInfo>{

    /**
     * 管理后台查询所有的课程信息
     * @param pageNum
     * @param pageSize
     * @param courseName
     * @return
     */
    Result getCourseInfoAllList(Integer pageNum, Integer pageSize, String courseName,String typeName,String courseTitle,
                                String mentorTitle,String mentor,Integer courseStatus);

    /**
     * 管理后台添加课程信息
     * @param courseInfoDTO
     * @return
     */
    Result saveInfo(CourseInfoDTO courseInfoDTO);

    /**
     * 管理后台修改课程信息
     * @param courseInfoDTO
     * @return
     */
    Result edit(CourseInfoDTO courseInfoDTO);

    /**
     * 管理后台删除课程信息
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 管理后台查询课程详情
     * @param id
     * @return
     */
    Result selectDetail(Long id);

    /**
     * app用户查询所有类型下的课程信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result selectCourseInfoList(Integer pageNum, Integer pageSize);

    /**
     * app用户根据课程id查询课程信息详情
     * @param id
     * @return
     */
    Result selectCourseInfoByDetail(Long id,Long userId);

    /**
     * app用户查看导师信息
     * @param id
     * @param userId
     * @return
     */
    Result selectByMentor(Long id,Long userId);

    /**
     * app用户推荐相关课程
     * @param id
     * @return
     */
    Result selectRecommendCourseInfo(Long id);

    /**
     * app用户查询所有类型下的课程信息(首页)，通过typeId查下级
     * @param pageNum
     * @param pageSize
     * @param typeId
     * @return
     */
    Result selectCourseListByTypeId(Integer pageNum, Integer pageSize, String typeId);

    /**
     * app用户查询所有的课程信息
     * @param pageNum
     * @param pageSize
     * @param courseName
     * @return
     */
    Result selectCourseInfoByExplore(Integer pageNum, Integer pageSize, String courseName,Long exploreId,Long exploreParentId);
}
