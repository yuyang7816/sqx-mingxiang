package com.sqx.modules.scTableClass.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.scTableClass.entity.ScTableClass;
import com.sqx.modules.scTableClass.service.ScTableClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wuchang
 * @date 2022/8/10
 */
@RestController
@Api(value = "发布分类", tags = {"管理端-发布分类"})
@RequestMapping(value = "/admin/scTableClass/")
public class AdminScTableClassController {
    @Autowired
    private ScTableClassService scTableClassService;

    @PostMapping(value = "addClass")
    @ApiOperation("新增分类")
    public Result addClass(@RequestBody  ScTableClass scTableClass) {
        return scTableClassService.addClass(scTableClass);
    }

    @PostMapping(value = "updateClass")
    @ApiOperation("修改分类")
    public Result updateClass(@RequestBody ScTableClass scTableClass) {
        return scTableClassService.updateClass(scTableClass);
    }

    @GetMapping(value = "getClassList")
    @ApiOperation("获取分类列表")
    public Result getClassList(Integer page, Integer limit,Integer isEnable, String className) {
        return Result.success().put("data", scTableClassService.getClassList(page, limit,isEnable, className));
    }

    @GetMapping(value = "deleteClass")
    @ApiOperation("删除分类")
    public Result deleteClass(Long classId){
        return scTableClassService.deleteClass(classId);
    }

    @ApiOperation("删除分类时判断当前分类下是否有模板和用户数据")
    @GetMapping("hasTableOrData")
    public Result hasTableOrData(Long classId){
        return Result.success().put("data", scTableClassService.hasTableOrData(classId));
    }
}
