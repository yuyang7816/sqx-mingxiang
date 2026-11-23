package com.sqx.modules.configI18n.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.configI18n.dao.ConfigI18nDao;
import com.sqx.modules.configI18n.entity.ConfigI18n;
import com.sqx.modules.configI18n.service.ConfigI18nService;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.utils.translate.TransApi;
import com.sqx.modules.utils.translate.TransResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 国际化配置表 服务实现类
 * @author wuChang
 * @since 2023-11-09
 */
@Service
@Slf4j
public class ConfigI18nServiceImp extends ServiceImpl<ConfigI18nDao, ConfigI18n> implements ConfigI18nService {
    @Autowired
    private LanguageTypeService languageTypeService;

    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    @Override
    public Result saveConfigI18n(ConfigI18n configI18n) {
        if (configI18n.getId() != null) {
            return Result.upStatus(baseMapper.updateById(configI18n));
        } else {
            int count = baseMapper.selectCount(new QueryWrapper<ConfigI18n>().eq("primary_key", configI18n.getPrimaryKey()).eq("language", configI18n.getLanguage()).eq("language_value", configI18n.getLanguageValue()));
            if (count > 0) {
                return Result.error("40076");
            }
            configI18n.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(configI18n));
        }
    }

    @Override
    public IPage<ConfigI18n> getConfigI18nList(Integer page, Integer limit, ConfigI18n configI18n) {
        Page<ConfigI18n> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(configI18n).orderByDesc("create_time"));
    }

    @Override
    public List<HashMap<String, String>> getLanguageList(Integer showType) {

        return baseMapper.getLanguageList(showType);
    }

    @Override
    public IPage<ConfigI18n> getConfigI18nGroupList(Integer page, Integer limit, ConfigI18n configI18n) {
        Page<ConfigI18n> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }

        return baseMapper.getConfigI18nGroupList(pages, configI18n);
    }


    @Override
    public void saveConfigI18nApi(String language, String toLanguage, Integer showType) throws Exception {
        reentrantReadWriteLock.writeLock().lock();
        try {
            List<ConfigI18n> selectedList = baseMapper.needTranslateList(language, toLanguage, showType);
            for (ConfigI18n configI18n : selectedList) {
                //判断是否已有当前翻译
                int count = baseMapper.selectCount(new QueryWrapper<ConfigI18n>().eq("language", toLanguage).eq("primary_key", configI18n.getPrimaryKey()).eq("show_type", showType));
                if (count == 0) {
                    if (configI18n.getLanguageValue().length() <= 2000) {
                        Thread.sleep(1000);
                        List<TransResult> transResult = TransApi.getTransResult(configI18n.getLanguageValue(), language, toLanguage);
                        for (TransResult result : transResult) {
                            //获取被翻译的语种参数
                                LanguageType languageType = languageTypeService.getOne(new QueryWrapper<LanguageType>().eq("language", toLanguage));
                                ConfigI18n i18n1 = new ConfigI18n();
                                i18n1.setCreateTime(LocalDateTime.now());
                                i18n1.setLanguage(toLanguage);
                                i18n1.setLanguageValue(result.getDst());
                                i18n1.setRemark(languageType.getRemark());
                                i18n1.setShowType(configI18n.getShowType());
                                i18n1.setPrimaryKey(configI18n.getPrimaryKey());
                                baseMapper.insert(i18n1);

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }

    }

    @Override
    public JSONObject getLanguageJsonList(String language) {

        List<ConfigI18n> selectedList = baseMapper.selectList(new QueryWrapper<ConfigI18n>().eq("language", language).eq("show_type", 2));
        JSONObject jsonObject = new JSONObject();
        for (ConfigI18n configI18n : selectedList) {
            jsonObject.put(configI18n.getPrimaryKey(), configI18n.getLanguageValue());
        }
        return jsonObject;

    }

    @Override
    public void insertAppConfigI18n(JSONObject jsonObject) {
        ConfigI18n configI18n = new ConfigI18n();
        for (Map.Entry<String, Object> stringObjectEntry : jsonObject.entrySet()) {
            String key = stringObjectEntry.getKey();
            String value = (String) stringObjectEntry.getValue();
            configI18n.setCreateTime(LocalDateTime.now());
            configI18n.setLanguage("zh");
            configI18n.setLanguageValue(value);
            configI18n.setRemark("简体中文");
            configI18n.setShowType(2);
            configI18n.setPrimaryKey(key);
            configI18n.setCreateTime(LocalDateTime.now());
            baseMapper.insert(configI18n);

        }


    }
}
