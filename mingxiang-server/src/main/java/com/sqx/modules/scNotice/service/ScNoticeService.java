package com.sqx.modules.scNotice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.scNotice.entity.ScNotice;

public interface ScNoticeService  extends IService<ScNotice> {
    Result insertNotice(ScNotice scNotice);

    IPage<ScNotice> getNoticeList(Integer page, Integer limit, String title,Integer status);
}
