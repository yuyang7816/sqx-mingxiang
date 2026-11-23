package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.service.CourseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/20 15:18
 */
@RestController
@RequestMapping("/app/courseInfo")
@Api(tags = "app端课程信息接口")
public class AppCourseInfoController {

    @Autowired
    private CourseInfoService courseInfoService;

    @GetMapping("/selectCourseInfoList")
    @ApiOperation("app用户查询所有类型下的课程信息(首页)")
    public Result selectCourseInfoList(@RequestParam(value = "pageNum") Integer pageNum,
                                       @RequestParam(value = "pageSize") Integer pageSize) {
        return courseInfoService.selectCourseInfoList(pageNum, pageSize);
    }

    @GetMapping("/selectCourseListByTypeId")
    @ApiOperation("app用户查询所有类型下的课程信息(首页)，通过typeId查下级")
    public Result selectCourseListByTypeId(@RequestParam(value = "pageNum") Integer pageNum,
                                           @RequestParam(value = "pageSize") Integer pageSize,
                                           @RequestParam(value = "typeId") String typeId) {
        return courseInfoService.selectCourseListByTypeId(pageNum, pageSize, typeId);
    }

    @Login
    @GetMapping("/selectCourseInfoByDetail")
    @ApiOperation("app用户根据课程id查询课程信息详情")
    public Result selectCourseInfoByDetail(@RequestParam(value = "id") Long id, @RequestAttribute("userId") Long userId) {
        return courseInfoService.selectCourseInfoByDetail(id, userId);
    }

    @Login
    @GetMapping("/selectByMentor")
    @ApiOperation("app用户查看导师信息")
    public Result selectByMentor(@RequestParam(value = "id") Long id,
                                 @RequestAttribute("userId") Long userId) {
        return courseInfoService.selectByMentor(id, userId);
    }

    @GetMapping("/selectRecommendCourseInfo")
    @ApiOperation("app推荐相关课程")
    public Result selectRecommendCourseInfo(@RequestParam(value = "id") Long id) {
        return courseInfoService.selectRecommendCourseInfo(id);
    }

    @GetMapping("/selectCourseInfoByExplore")
    @ApiOperation("探索页app用户查询所有课程信息根据探索页id或者探索页父id")
    public Result selectCourseInfoByExplore(@RequestParam(value = "pageNum") Integer pageNum,
                                            @RequestParam(value = "pageSize") Integer pageSize,
                                            @RequestParam(value = "courseName", required = false) String courseName,
                                            @RequestParam(value = "exploreId", required = false) Long exploreId,
                                            @RequestParam(value = "exploreParentId", required = false) Long exploreParentId) {
        return courseInfoService.selectCourseInfoByExplore(pageNum, pageSize, courseName, exploreId, exploreParentId);
    }
}
