package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.MeditationType;
import com.sqx.modules.course.service.MeditationTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/21 16:38
 */
@RestController
@RequestMapping("/meditationType")
@Api(tags = "管理后台冥想类型相关")
public class MeditationTypeController {

    @Autowired
    private MeditationTypeService meditationTypeService;

    @GetMapping("/selectMeditationTypeList")
    @ApiOperation("管理后台查看冥想类型列表")
    public Result selectMeditationTypeList(@RequestParam(value = "pageNum") Integer pageNum,
                                           @RequestParam(value = "pageSize") Integer pageSize,
                                           @RequestParam(value = "meditationName",required = false) String meditationName){
        return meditationTypeService.selectMeditationTypeList(pageNum,pageSize,meditationName);
    }

    @PostMapping("/saveInfo")
    @ApiOperation("管理后台添加冥想类型")
    public Result saveInfo(@RequestBody MeditationType meditationType){
        return meditationTypeService.saveInfo(meditationType);
    }

    @PostMapping("/edit")
    @ApiOperation("管理后台修改冥想类型")
    public Result edit(@RequestBody MeditationType meditationType){
        return meditationTypeService.edit(meditationType);
    }

    @PostMapping("/delete")
    @ApiOperation("管理后台删除冥想类型")
    public Result delete(@RequestBody MeditationType meditationType){
        return meditationTypeService.delete(meditationType.getId());
    }

    @GetMapping("/selectDetail")
    @ApiOperation("管理后台查看冥想类型详情")
    public Result selectDetail(@RequestParam(value = "id") Long id){
        return meditationTypeService.selectDetail(id);
    }
}
