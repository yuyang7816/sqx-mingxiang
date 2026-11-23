package com.sqx.modules.scNotice.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.scNotice.entity.ScNotice;
import com.sqx.modules.scNotice.service.ScNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "发布公告", tags = {"管理端-发布公告"})
@RequestMapping(value = "/admin/notice/")
public class AdminScNoticeController {
    @Autowired
    private ScNoticeService scNoticeService;


    @ApiOperation("新增公告")
    @PostMapping("insertNotice")
    public Result insertNotice(@RequestBody ScNotice scNotice) {
        return scNoticeService.insertNotice(scNotice);
    }

    @ApiOperation("获取公告列表")
    @GetMapping("getNoticeList")
    public Result getNoticeList(Integer page, Integer limit, String title, Integer status) {
        return Result.success().put("data", scNoticeService.getNoticeList(page, limit, title, status));
    }

    @ApiOperation("删除公告")
    @GetMapping("deleteNoticeById")
    public Result deleteNoticeById(Long noticeId) {
        return Result.success().put("data", scNoticeService.removeById(noticeId));
    }

    @ApiOperation("修改公告")
    @PostMapping("updateNotice")
    public Result updateNotice(@RequestBody ScNotice scNotice) {
        return Result.success().put("data", scNoticeService.updateById(scNotice));
    }

}
