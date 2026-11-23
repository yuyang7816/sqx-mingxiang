package com.sqx.modules.languageType.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.languageType.dao.LanguageTypeDao;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 语种 服务实现类
 * @author wuChang
 * @since 2023-12-13
 */
@Service
public class LanguageTypeServiceImp extends ServiceImpl<LanguageTypeDao, LanguageType> implements LanguageTypeService {


    @Override
    public Result saveLanguageType(LanguageType languageType) {
        if (languageType.getLanguageId() != null) {
            return Result.upStatus(baseMapper.updateById(languageType));
        } else {
            languageType.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(languageType));
        }
    }

    @Override
    public IPage<LanguageType> getLanguageTypeList(Integer page, Integer limit, LanguageType languageType) {
        Page<LanguageType> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(languageType).orderByAsc("sort"));
    }
}
