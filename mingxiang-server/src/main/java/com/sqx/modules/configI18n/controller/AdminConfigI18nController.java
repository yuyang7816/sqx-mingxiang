package com.sqx.modules.configI18n.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.configI18n.entity.ConfigI18n;
import com.sqx.modules.configI18n.service.ConfigI18nService;
import com.sqx.modules.utils.DataSourceMessageSource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 国际化配置表-管理端
 * @author wuChang
 * @since 2023-11-09
 */
@RestController
@Api(value = "国际化配置表-管理端", tags = {"国际化配置表-管理端"})
@RequestMapping("/admin/configI18n/")
public class AdminConfigI18nController {
    @Autowired
    private ConfigI18nService configI18nService;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    /**
     * 新增或修改国际化配置表
     */
    @ApiOperation("新增或修改国际化配置表")
    @PostMapping(value = "saveConfigI18n")
    public Result saveConfigI18n(ConfigI18n configI18n) {
        return configI18nService.saveConfigI18n(configI18n);
    }

    /**
     * 获取国际化配置表列表
     */
    @ApiOperation("获取国际化配置表列表")
    @GetMapping(value = "getConfigI18nList")
    public Result getConfigI18nList(Integer page, Integer limit, ConfigI18n configI18n) {

        return Result.success().put("data", configI18nService.getConfigI18nList(page, limit, configI18n));
    }

    @ApiOperation("获取国际化配置表列表（分组）")
    @GetMapping(value = "getConfigI18nGroupList")
    public Result getConfigI18nGroupList(Integer page, Integer limit, ConfigI18n configI18n) {
        return Result.success().put("data", configI18nService.getConfigI18nGroupList(page, limit, configI18n));
    }

    /**
     * 获取语种类型
     */
    @ApiOperation("获取国际化语种")
    @GetMapping(value = "getLanguageList")
    public Result getLanguageList(Integer showType) {
        return Result.success().put("data", configI18nService.getLanguageList(showType));
    }

    /**
     * 刷新缓存
     * @return
     */
    @ApiOperation("刷新缓存")
    @PostMapping(value = "refreshI18nCache")
    public Result refreshI18nCache() {
        dataSourceMessageSource.reload();
        return Result.success();
    }

    /**
     * 获取已缓存数据
     * @return
     */
    @ApiOperation("获取已缓存数据")
    @GetMapping(value = "getI18nCacheCount")
    public Result getI18nCacheCount() {
        return Result.success().put("data", dataSourceMessageSource.getI18nCount());
    }

    @ApiOperation("调用一键翻译")
    @GetMapping(value = "saveConfigI18nApi")
    public Result saveConfigI18nApi(String language, String toLanguage, Integer showType) throws Exception {
        configI18nService.saveConfigI18nApi(language, toLanguage, showType);
        return Result.success();
    }

    @ApiOperation("删除配置")
    @GetMapping(value = "removeI18n")
    public Result removeI18n(Long id) {
        return configI18nService.removeById(id) ? Result.success() : Result.error();
    }
}
