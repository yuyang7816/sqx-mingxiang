package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.service.CourseTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/21 13:32
 */
@RestController
@RequestMapping("/app/explore")
@Api(tags = "app端探索页相关接口")
public class AppExploreController {

    @Autowired
    private CourseTypeService courseTypeService;

//    @GetMapping("/exploreHomePage")
//    @ApiOperation("app端首页探索页")
//    public Result exploreHomePage(@RequestParam(value = "pageNum") Integer pageNum,
//                                  @RequestParam(value = "pageSize") Integer pageSize,
//                                  @RequestParam(value = "parentId",required = false) Long parentId) {
//        return courseTypeService.exploreHomePage(pageNum, pageSize, parentId);
//    }

    @GetMapping("/exploreHomePageByType")
    @ApiOperation("app端首页探索页")
    public Result exploreHomePageByType(@RequestParam(value = "pageNum") Integer pageNum,
                                        @RequestParam(value = "pageSize") Integer pageSize,
                                        @RequestParam(value = "id",required = false) Long id,//这里的id是课程类型id（typeId）
                                        @RequestParam(value = "level",required = false)Integer level) {
        return courseTypeService.exploreHomePageByType(pageNum, pageSize, id,level);
    }

    @GetMapping("/selectBySearch")
    @ApiOperation("app用户根据搜索条件查询课程信息")
    public Result selectBySearch(@RequestParam(value = "courseName",required = false) String courseName) {
        return courseTypeService.selectBySearch(courseName);
    }

    @GetMapping("/selectByHOT")
    @ApiOperation("app用户搜索框外的热门课程列表")
    public Result selectByHOT() {
        return courseTypeService.selectByHOT();
    }


}
