package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.CourseLearn;
import com.sqx.modules.course.service.CourseLearnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/25 11:16
 */
@RestController
@RequestMapping("/app/courseLearn")
@Api(tags = "app用户端冥想练习、睡眠、专注、小憩、呼吸等功能相关接口")
public class AppCourseLearnController {

    @Autowired
    private CourseLearnService courseLearnService;

    @Login
    @GetMapping("/selectMyPlayed")
    @ApiOperation("app用户查看我的播放列表")
    public Result selectMyPlayed(@RequestParam(value = "pageNum") Integer pageNum,
                                 @RequestParam(value = "pageSize") Integer pageSize,
                                 @RequestAttribute(value = "userId") Long userId) {
        return courseLearnService.selectMyPlayed(pageNum, pageSize, userId);
    }

    @Login
    @PostMapping("/startMeditation")
    @ApiOperation("app用户点冥想练习开始")
    public Result startMeditation(@RequestBody CourseLearn courseLearn,
                                  @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.startMeditation(courseLearn);
    }

    @Login
    @PostMapping("/endMeditation")
    @ApiOperation("app用户点冥想练习结束")
    public Result endMeditation(@RequestBody CourseLearn courseLearn,
                                @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.endMeditation(courseLearn);
    }

    @Login
    @PostMapping("/startAlarm")
    @ApiOperation("app用户点开始添加夜间睡眠闹钟")
    public Result startAlarm(@RequestBody CourseLearn courseLearn,
                             @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.startAlarm(courseLearn);
    }

    @Login
    @PostMapping("/endAlarm")
    @ApiOperation("app用户点结束夜间睡眠闹钟并计算出时长")
    public Result endAlarm(@RequestBody CourseLearn courseLearn,
                           @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.endAlarm(courseLearn);
    }

    @Login
    @PostMapping("/startConcentrate")
    @ApiOperation("app用户点开始添加专注")
    public Result startConcentrate(@RequestBody CourseLearn courseLearn,
                                   @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.startConcentrate(courseLearn);
    }

    @Login
    @PostMapping("/endConcentrate")
    @ApiOperation("app用户点结束专注")
    public Result endConcentrate(@RequestBody CourseLearn courseLearn,
                                 @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.endConcentrate(courseLearn);
    }

    @Login
    @PostMapping("/startNap")
    @ApiOperation("app用户点开始添加小憩")
    public Result startNap(@RequestBody CourseLearn courseLearn,
                           @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.startNap(courseLearn);
    }

    @Login
    @PostMapping("/endNap")
    @ApiOperation("app用户点结束小憩")
    public Result endNap(@RequestBody CourseLearn courseLearn,
                         @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.endNap(courseLearn);
    }

    @Login
    @PostMapping("/startBreathe")
    @ApiOperation("app用户点开始添加呼吸")
    public Result startBreathe(@RequestBody CourseLearn courseLearn,
                               @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.startBreathe(courseLearn);
    }

    @Login
    @PostMapping("/endBreathe")
    @ApiOperation("app用户点结束呼吸")
    public Result endBreathe(@RequestBody CourseLearn courseLearn,
                             @RequestAttribute(value = "userId") Long userId) {
        courseLearn.setUserId(userId);
        return courseLearnService.endBreathe(courseLearn);
    }
}
