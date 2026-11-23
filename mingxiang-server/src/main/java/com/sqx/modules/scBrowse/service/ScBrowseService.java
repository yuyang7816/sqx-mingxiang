package com.sqx.modules.scBrowse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.scBrowse.entity.ScBrowse;

public interface ScBrowseService extends IService<ScBrowse> {

    IPage<ScBrowse> getMyBrowse(Long userId, Integer page, Integer limit, Integer type);


    Result selectAmount(Long userId);
}
