package com.sqx.modules.configI18n.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.configI18n.entity.ConfigI18n;

import java.util.HashMap;
import java.util.List;

/**
 * 国际化配置表 服务类
 * @author wuChang
 * @since 2023-11-09
 */

public interface ConfigI18nService extends IService<ConfigI18n> {
    /**
     * 新增或修改国际化配置表
     */
    Result saveConfigI18n (ConfigI18n configI18n);

    /**
     * 获取改国际化配置表列表
     */
    IPage<ConfigI18n> getConfigI18nList(Integer page, Integer limit, ConfigI18n configI18n);

    List<HashMap<String,String>> getLanguageList(Integer showType);


    IPage<ConfigI18n> getConfigI18nGroupList(Integer page, Integer limit, ConfigI18n configI18n);

    void saveConfigI18nApi(String language, String toLanguage, Integer showType)  throws Exception;

    JSONObject getLanguageJsonList(String language);

    void insertAppConfigI18n(JSONObject jsonObject);
}
