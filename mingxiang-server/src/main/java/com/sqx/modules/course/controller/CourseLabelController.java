package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseLabel;
import com.sqx.modules.course.service.CourseLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/20 16:37
 */
@RestController
@RequestMapping("/courseLabel")
@Api(tags = "管理后台课程标签相关")
public class CourseLabelController {

    @Autowired
    private CourseLabelService courseLabelService;

    @GetMapping( "/selectByCourseLabelList")
    @ApiOperation("管理后台查看所有的标签信息")
    public Result selectByCourseLabelList(@RequestParam(value = "pageNum") Integer pageNum,
                                          @RequestParam(value = "pageSize") Integer pageSize,
                                          @RequestParam(value = "labelName",required = false) String labelName) {
        return courseLabelService.selectByCourseLabelList(pageNum,pageSize,labelName);
    }

    @PostMapping( "/saveInfo")
    @ApiOperation("管理后台添加标签信息")
    public Result saveInfo(@RequestBody CourseLabel courseLabel) {
        return courseLabelService.saveInfo(courseLabel);
    }

    @PostMapping("/edit")
    @ApiOperation("管理后台修改标签信息")
    public Result edit(@RequestBody CourseLabel courseLabel) {
        return courseLabelService.edit(courseLabel);
    }

    @PostMapping("/delete")
    @ApiOperation("管理后台删除标签信息")
    public Result delete(@RequestBody CourseLabel courseLabel) {
        return courseLabelService.delete(courseLabel.getId());
    }

    @GetMapping("/selectDetail")
    @ApiOperation("管理后台查询标签详情")
    public Result selectDetail(@RequestParam(value = "id") Long id) {
        return courseLabelService.selectDetail(id);
    }

}
