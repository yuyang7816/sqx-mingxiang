package com.sqx.modules.scTableClass.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.scTableClass.service.ScTableClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "发布分类", tags = {"用户端-发布分类"})
@RequestMapping(value = "/app/scTableClass/")
public class ScTableClassController {
    @Autowired
    private ScTableClassService scTableClassService;

    /**
     * 获取分类列表
     * @param page 第几页
     * @param limit 第几条
     * @param className 分类名称
     * @return
     */
    @GetMapping(value = "getClassList")
    public Result getClassList(Integer page, Integer limit, String className) {
        return Result.success().put("data", scTableClassService.getClassList(page, limit,1, className));
    }
}
