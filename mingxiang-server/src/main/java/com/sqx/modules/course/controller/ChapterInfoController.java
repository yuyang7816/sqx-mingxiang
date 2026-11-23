package com.sqx.modules.course.controller;

import com.sqx.modules.course.entity.ChapterInfo;
import com.sqx.modules.course.service.ChapterInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import com.sqx.common.utils.Result;
import com.alibaba.fastjson.JSONObject;
/**
 * 章节信息(ChapterInfo)表控制层
 *
 * @author makejava
 * @since 2024-05-16 15:20:27
 */
@RestController
@RequestMapping("/chapterInfo")
@Api(tags = "管理后台章节信息相关")
public class ChapterInfoController {
    /**
     * 服务对象
     */
    @Autowired
    private ChapterInfoService chapterInfoService;

    @GetMapping ( "/selectChapterInfoList")
    @ApiOperation("管理后台查询章节信息")
    public Result selectChapterInfoList(@RequestParam(value = "pageNum") Integer pageNum,
                                        @RequestParam(value = "pageSize") Integer pageSize,
                                        @RequestParam(value = "chapterName",required = false) String chapterName,
                                        @RequestParam(value = "courseId",required = false)Long courseId) {
       return chapterInfoService.selectChapterInfoList(pageNum,pageSize,chapterName,courseId);
    }

    @PostMapping( "/saveInfo")
    @ApiOperation("管理后台添加章节信息")
    public Result saveInfo(@RequestBody ChapterInfo chapterInfo) {
        return chapterInfoService.saveInfo(chapterInfo);
    }

    @PostMapping("/edit")
    @ApiOperation("管理后台修改章节信息")
    public Result edit(@RequestBody ChapterInfo chapterInfo) {
        return chapterInfoService.edit(chapterInfo);
    }

    @PostMapping("/delete")
    @ApiOperation("管理后台删除章节信息")
    public Result delete(@RequestParam(value = "id") Long id) {
        return chapterInfoService.delete(id);
    }

    @GetMapping("/selectDetail")
    @ApiOperation("管理后台查询章节信息详情")
    public Result selectDetail(@RequestParam(value = "id") Long id) {
        return chapterInfoService.selectDetail(id);
    }
}

