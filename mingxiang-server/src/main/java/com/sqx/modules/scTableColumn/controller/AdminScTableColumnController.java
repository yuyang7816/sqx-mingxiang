package com.sqx.modules.scTableColumn.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.scTableClass.service.ScTableClassService;
import com.sqx.modules.scTableColumn.entity.ScTableColumn;
import com.sqx.modules.scTableColumn.service.ScTableColumnService;
import com.sqx.modules.scTableColumn.vo.JsonRootBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@Api(value = "模板管理", tags = {"管理端-模板管理"})
@RequestMapping(value = "/admin/scTableColumn/")
public class AdminScTableColumnController {

    @Autowired
    private ScTableColumnService columnService;
    @ApiOperation("添加|修改模板")
    @PostMapping("insertScTable")
    public Result insertScTable(@RequestBody JsonRootBean jsonRootBean) {
        return columnService.insertScTable(jsonRootBean);
    }

    @ApiOperation("用分类查询模板")
    @GetMapping("getTableByClassId")
    public Result getTableByClassId(Long classId){
        return Result.success().put("data", columnService.getTableByClassId(classId));
    }
    @ApiOperation("删除模板")
    @GetMapping("deleteTableByClassId")
    public Result deleteTableByClassId(Long classId){
        return columnService.deleteTableByClassId(classId);
    }
}
