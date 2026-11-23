package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.dao.CourseInfoDao;
import com.sqx.modules.course.entity.CourseInfo;
import com.sqx.modules.course.entity.dto.CourseInfoDTO;
import com.sqx.modules.course.service.CourseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 课程信息(CourseInfo)表控制层
 *
 * @author makejava
 * @since 2024-05-16 15:19:26
 */
@RestController
@RequestMapping("/courseInfo")
@Api(tags = "后台管理课程信息相关")
public class CourseInfoController {

    @Autowired
    private CourseInfoService courseInfoService;

    @GetMapping("/getCourseInfoAllList")
    @ApiOperation("管理后台查询所有的课程信息")
    public Result getCourseInfoAllList(@RequestParam(value = "pageNum") Integer pageNum,
                                       @RequestParam(value = "pageSize") Integer pageSize,
                                       @RequestParam(value = "courseName", required = false) @ApiParam("课程名称") String courseName,
                                       @RequestParam(required = false) @ApiParam("课程类型") String typeName,
                                       @RequestParam(required = false) @ApiParam("课程标题") String courseTitle,
                                       @RequestParam(required = false) @ApiParam("导师标题") String mentorTitle,
                                       @RequestParam(required = false) @ApiParam("导师名称") String mentor,
                                       @RequestParam(required = false) @ApiParam("课程状态：0正常 1下架") Integer courseStatus) {
        return courseInfoService.getCourseInfoAllList(pageNum, pageSize, courseName, typeName, courseTitle, mentorTitle, mentor, courseStatus);
    }

    @PostMapping("/saveInfo")
    @ApiOperation("管理后台添加课程信息")
    public Result saveInfo(@RequestBody CourseInfoDTO courseInfoDTO) {
        return courseInfoService.saveInfo(courseInfoDTO);
    }

    @PostMapping("/edit")
    @ApiOperation("管理后台修改课程信息")
    public Result edit(@RequestBody CourseInfoDTO courseInfoDTO) {
        return courseInfoService.edit(courseInfoDTO);
    }

    @PostMapping("/delete")
    @ApiOperation("管理后台删除课程信息")
    public Result delete(@RequestBody CourseInfoDTO courseInfoDTO) {
        return courseInfoService.delete(courseInfoDTO.getId());
    }

    @GetMapping("/selectDetail")
    @ApiOperation("管理后台查询课程详情")
    public Result selectDetail(@RequestParam(value = "id") Long id) {
        return courseInfoService.selectDetail(id);
    }
}

