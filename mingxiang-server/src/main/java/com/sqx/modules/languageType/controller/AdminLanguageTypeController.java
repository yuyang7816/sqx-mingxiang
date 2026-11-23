package com.sqx.modules.languageType.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 语种-管理端
 * @author wuChang
 * @since 2023-12-13
 */
@RestController
@Api(value = "语种-管理端", tags = {"语种-管理端"})
@RequestMapping("/admin/languageType/")
public class AdminLanguageTypeController {
    @Autowired
    private LanguageTypeService languageTypeService;

    /**
     * 新增或修改语种
     * @param languageType
     * @return
     */
    @ApiOperation("新增或修改语种")
    @PostMapping(value = "saveLanguageType")
    public Result saveLanguageType(LanguageType languageType) {
        return languageTypeService.saveLanguageType(languageType);
    }

    /**
     * 获取语种列表
     * @param page 第几页
     * @param limit 多少条
     * @param languageType
     * @return
     */
    @ApiOperation("获取语种列表")
    @GetMapping(value = "getLanguageTypeList")
    public Result getLanguageTypeList(Integer page, Integer limit, LanguageType languageType) {

        return Result.success().put("data", languageTypeService.getLanguageTypeList(page, limit, languageType));
    }

    /**
     * 删除语种
     * @param languageId 语种id
     * @return
     */
    @ApiOperation("删除语种")
    @PostMapping(value = "deleteLanguageType")
    public Result deleteLanguageType(Long languageId) {
        return languageTypeService.removeById(languageId) ? Result.success() : Result.error();
    }

}
