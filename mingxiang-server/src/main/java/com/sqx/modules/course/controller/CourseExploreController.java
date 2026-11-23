package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseExplore;
import com.sqx.modules.course.service.CourseExploreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2025/9/11 11:05
 */
@RestController
@RequestMapping("/courseExplore")
@Api(tags = "管理端探索页相关")
public class CourseExploreController {

    @Autowired
    private CourseExploreService courseExploreService;

    @PostMapping( "/addCourseExplore")
    @ApiOperation("添加探索页分类")
    public Result addCourseExplore(@RequestBody CourseExplore courseExplore){
        return courseExploreService.addCourseExplore(courseExplore);
    }

    @PostMapping( "/editCourseExplore")
    @ApiOperation("修改探索页分类")
    public Result editCourseExplore(@RequestBody CourseExplore courseExplore){
        return courseExploreService.editCourseExplore(courseExplore);
    }

    @PostMapping( "/deleteCourseExplore")
    @ApiOperation("删除探索页分类")
    public Result deleteCourseExplore(@RequestBody CourseExplore courseExplore){
        return courseExploreService.deleteCourseExplore(courseExplore);
    }

    @GetMapping( "/selectCourseExploreList")
    @ApiOperation("查询所有探索页分类")
    public Result selectCourseExploreList(@RequestParam(required = false) @ApiParam("页码") Integer pageNum,
                                          @RequestParam(required = false) @ApiParam("条数")Integer pageSize,
                                          @RequestParam(required = false) @ApiParam("父id")Long parentId,
                                          @RequestParam(required = false) @ApiParam("探索名称") String exploreName){
        return courseExploreService.selectCourseExploreList(pageNum, pageSize, exploreName,parentId);
    }

    @GetMapping( "/selectCourseExploreById")
    @ApiOperation("查询探索页分类")
    public Result selectCourseExploreById(@RequestParam(required = false) @ApiParam("探索id") Long id){
        return courseExploreService.selectCourseExploreById(id);
    }
}
