package com.sqx.modules.scFollow.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.service.ScFollowService;
import com.sqx.modules.scTableData.service.ScTableDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/scFollow")
@Api(value = "收藏|关注-管理端", tags = {"收藏|关注-管理端"})
public class ScFollowController {
    @Autowired
    private ScFollowService scFollowService;
    @Autowired
    private ScTableDataService scTableDataService;


    @ApiOperation("服务收藏列表")
    @GetMapping("getMyFollowList")
    public Result getMyFollowList(Long userId, Integer page, Integer limit, ScFollow follow) {
        follow.setUserId(userId);
        return Result.success().put("data", scFollowService.getMyFollowList(page, limit, follow));
    }

    @ApiOperation("店铺粉丝列表")
    @GetMapping("getMyFansList")
    public Result getMyFansList(Long userId, Integer page, Integer limit) {
        return Result.success().put("data", scFollowService.getMyFansList(userId, page, limit));
    }

    @ApiOperation("收藏|取消收藏")
    @PostMapping("updateFollow")
    public Result updateFollow(Long userId, ScFollow follow) {
        follow.setUserId(userId);
        return scFollowService.updateFollow(follow);
    }
}
