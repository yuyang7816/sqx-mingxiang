package com.sqx.modules.scBrowse.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.scBrowse.entity.ScBrowse;
import com.sqx.modules.scBrowse.service.ScBrowseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "浏览记录", tags = {"浏览记录"})
@RequestMapping(value = "/app/scBrowse/")
public class ScBrowseController {
    @Autowired
    private ScBrowseService browseService;

    @Login
    @ApiOperation("我的浏览记录列表")
    @GetMapping(value = "getMyBrowse")
    public Result getMyBrowse(@RequestAttribute("userId") Long userId, Integer page, Integer limit,Integer type) {
        return Result.success().put("data", browseService.getMyBrowse(userId, page, limit,type));
    }

    @Login
    @ApiOperation("删除浏览记录")
    @GetMapping(value = "deleteBrowse")
    public Result deleteBrowse(@RequestAttribute("userId") Long userId, Long browseId) {
        browseService.remove(new QueryWrapper<ScBrowse>().eq("user_id",userId).eq("browse_id", browseId));
        return Result.success();
    }
}
