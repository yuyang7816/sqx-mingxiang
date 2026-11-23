package com.sqx.modules.configI18n.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.configI18n.service.ConfigI18nService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 国际化配置表-用户端
 * @author wuChang
 * @since 2023-11-09
 */
@RestController
@Api(value = "国际化配置表-用户端", tags = {"国际化配置表-用户端"})
@RequestMapping("/app/configI18n/")
public class AppConfigI18nController {
    @Autowired
    private ConfigI18nService configI18nService;

    /**
     * 获取国际化语种类别
     */
    @ApiOperation("获取国际化语种类别")
    @GetMapping(value = "getLanguageList")
    public Result getLanguageList() {
        return Result.success().put("data", configI18nService.getLanguageList(null));
    }
    @ApiOperation("获取指定语种数据")
    @GetMapping(value = "getLanguageJsonList")
    public Result getLanguageJsonList(String language){
        return Result.success().put("data", configI18nService.getLanguageJsonList(language));
    }

}
