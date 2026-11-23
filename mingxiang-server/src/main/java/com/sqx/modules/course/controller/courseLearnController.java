package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.service.CourseLearnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/26 11:08
 */
@RestController
@RequestMapping("/admin/courseLearn")
@Api(tags = "管理端查看用户冥想练习、睡眠、专注、小憩、呼吸等功能相关接口")
public class courseLearnController {

    @Autowired
    private CourseLearnService courseLearnService;

    @GetMapping("/selectMyPlayed")
    @ApiOperation("app用户查看我的播放列表")
    public Result selectMyPlayed(@RequestParam(value = "pageNum") Integer pageNum,
                                 @RequestParam(value = "pageSize") Integer pageSize,
                                 @RequestParam(value = "userId") Long userId) {
        return courseLearnService.selectMyPlayed(pageNum, pageSize, userId);
    }



}
