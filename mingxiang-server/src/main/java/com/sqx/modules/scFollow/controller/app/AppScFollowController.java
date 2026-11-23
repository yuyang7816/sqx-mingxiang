package com.sqx.modules.scFollow.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.service.ScFollowService;
import com.sqx.modules.scTableData.service.ScTableDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/scFollow/")
@Api(value = "收藏|关注-用户端", tags = {"收藏|关注-用户端"})
public class AppScFollowController {
    @Autowired
    private ScFollowService scFollowService;
    @Autowired
    private ScTableDataService scTableDataService;

    @Login
    @ApiOperation("收藏列表")
    @GetMapping("getMyFollowList")
    public Result getMyFollowList(@RequestAttribute("userId") Long userId,Integer page, Integer limit,ScFollow follow) {
        follow.setUserId(userId);
        return Result.success().put("data", scFollowService.getMyFollowList(page, limit, follow));
    }

    @Login
    @ApiOperation("收藏|取消收藏")
    @PostMapping("updateFollow")
    public Result updateFollow(@RequestAttribute("userId") Long userId, ScFollow follow) {
        follow.setUserId(userId);
        return scFollowService.updateFollow(follow);
    }
    @Login
    @ApiOperation("店铺关注列表")
    @GetMapping("getMyUserFollowList")
    public Result getMyUserFollowList(@RequestAttribute("userId") Long userId, Integer page, Integer limit) {
        return Result.success().put("data", scFollowService.getMyFollowList(userId, page, limit));
    }
    @Login
    @ApiOperation("店铺粉丝列表")
    @GetMapping("getMyFansList")
    public Result getMyFansList(@RequestAttribute("userId") Long userId, Integer page, Integer limit) {
        return Result.success().put("data", scFollowService.getMyFansList(userId, page, limit));
    }
}
