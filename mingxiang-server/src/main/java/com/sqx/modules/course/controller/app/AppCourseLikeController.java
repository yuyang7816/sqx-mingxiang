package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.CourseLike;
import com.sqx.modules.course.service.CourseLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/25 11:09
 */
@RestController
@RequestMapping("/app/CourseLike")
@Api("APP课程我的喜欢接口")
public class AppCourseLikeController {

    @Autowired
    private CourseLikeService courseLikeService;

    @Login
    @PostMapping("/add")
    @ApiOperation("app用户将课程添加点到我的喜欢列表")
    public Result add(@RequestBody CourseLike courseLike,@RequestAttribute("userId") Long userId) {
        courseLike.setUserId(userId);
        return courseLikeService.add(courseLike);
    }

    @Login
    @PostMapping("/delete")
    @ApiOperation("app用户将课程从我的喜欢列表中取消")
    public Result delete(@RequestBody CourseLike courseLike,@RequestAttribute("userId") Long userId) {
        courseLike.setUserId(userId);
        return courseLikeService.delete(courseLike);
    }

    @Login
    @GetMapping("/selectCourseLikeList")
    @ApiOperation("app用户查看我的喜欢列表")
    public Result selectCourseLikeList(@RequestParam(value = "pageNum") Integer pageNum,
                                       @RequestParam(value = "pageSize") Integer pageSize,
                                       @RequestAttribute(value = "userId") Long userId,
                                       @RequestParam(value = "exploreId", required = false) Long exploreId,
                                       @RequestParam(value = "exploreParentId", required = false) Long exploreParentId) {
        return courseLikeService.selectCourseLikeList(pageNum, pageSize, userId, exploreId, exploreParentId);
    }

}
