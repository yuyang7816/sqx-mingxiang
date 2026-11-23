package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.service.CourseExploreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2025/9/11 11:04
 */
@RestController
@RequestMapping("/app/courseExplore")
@Api(tags = "app查看探索页相关")
public class AppCourseExploreController {

    @Autowired
    private CourseExploreService courseExploreService;

    @GetMapping( "/selectByCourseExploreList")
    @ApiOperation("app用户查看探索页")
    public Result selectByCourseExploreList(@RequestParam(value = "exploreName",required = false)@ApiParam("探索页名称") String exploreName) {
        return courseExploreService.selectCourseExploreList1(exploreName);
    }

    @GetMapping( "/selectCourseExploreById")
    @ApiOperation("app用户查看探索页详情")
    public Result selectCourseExploreById(@RequestParam(value = "id") @ApiParam("探索页id")Long id) {
        return courseExploreService.selectCourseExploreById(id);
    }
}
