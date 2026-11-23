package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.dto.CourseTypeDTO;
import com.sqx.modules.course.service.CourseTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/21 11:08
 */
@RestController
@RequestMapping("/courseType")
@Api(tags = "管理后台课程类型相关")
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService;

    @GetMapping("/selectCourseTypeList")
    @ApiOperation("管理后台查看课程类型列表")
    public Result selectCourseTypeList(@RequestParam(value = "pageNum") Integer pageNum,
                                       @RequestParam(value = "pageSize") Integer pageSize,
                                       @RequestParam(value = "typeName",required = false) String typeName,
                                       @RequestParam(value = "parentId",required = false) Long parentId) {
        return courseTypeService.selectCourseTypeList(pageNum,pageSize,typeName,parentId);
    }

    @PostMapping("/saveInfo")
    @ApiOperation("管理后台添加课程类型")
    public Result saveInfo(@RequestBody CourseTypeDTO courseTypeDTO) {
        if (courseTypeDTO.getParentId() == null || ObjectUtils.isEmpty(courseTypeDTO.getParentId())) {
            courseTypeDTO.setParentId(0L);
        }
        return courseTypeService.saveInfo(courseTypeDTO);
    }

    @PostMapping("/edit")
    @ApiOperation("管理后台编辑课程类型")
    public Result edit(@RequestBody CourseTypeDTO courseTypeDTO) {
        return courseTypeService.edit(courseTypeDTO);
    }

    @PostMapping("/delete")
    @ApiOperation("管理后台删除课程类型")
    public Result delete(@RequestBody CourseTypeDTO courseTypeDTO) {
        if (courseTypeDTO.getId() == null){
            return Result.error("请选择要删除的课程类型");
        }
        return courseTypeService.delete(courseTypeDTO);
    }

    @GetMapping("/selectDetail")
    @ApiOperation("管理后台查看课程类型详情")
    public Result selectDetail(@RequestParam(value = "id") Integer id) {
        return courseTypeService.selectDetail(id);
    }

}
