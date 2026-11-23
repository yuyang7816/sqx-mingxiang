package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.service.CourseLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2025/9/10 20:27
 */
@RestController
@RequestMapping("/app/courseLabel")
@Api(tags = "app查看课程标签相关")
public class AppCourseLabelController {

    @Autowired
    private CourseLabelService courseLabelService;

    @GetMapping( "/selectByCourseLabelList")
    @ApiOperation("app查看所有的标签信息")
    public Result selectByCourseLabelList(@RequestParam(value = "pageNum") Integer pageNum,
                                          @RequestParam(value = "pageSize") Integer pageSize,
                                          @RequestParam(value = "labelName",required = false) String labelName) {
        return courseLabelService.selectByCourseLabelList(pageNum,pageSize,labelName);
    }
}
