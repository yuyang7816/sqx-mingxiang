package com.sqx.modules.scNotice.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.scNotice.service.ScNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "获取公告", tags = {"用户端-公告"})
@RequestMapping(value = "/app/notice/")
public class AppScNoticeController {
    @Autowired
    private ScNoticeService scNoticeService;

    @ApiOperation("获取公告列表")
    @GetMapping("getNoticeList")
    public Result getNoticeList(Integer page, Integer limit, String title) {
        return Result.success().put("data", scNoticeService.getNoticeList(page, limit, title, 1));
    }
}
