package com.sqx.modules.scNotice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.common.utils.ShiroUtils;
import com.sqx.modules.scBrowse.entity.ScBrowse;
import com.sqx.modules.scNotice.dao.ScNoticeDao;
import com.sqx.modules.scNotice.entity.ScNotice;
import com.sqx.modules.scNotice.service.ScNoticeService;
import com.sqx.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScNoticeServiceImpl extends ServiceImpl<ScNoticeDao, ScNotice> implements ScNoticeService {
    @Autowired
    private ScNoticeDao scNoticeDao;

    @Override
    public Result insertNotice(ScNotice scNotice) {
        SysUserEntity userEntity = (SysUserEntity) ShiroUtils.getSubject().getPrincipal();
        scNotice.setCreateTime(new Date());
        scNotice.setStatus(0);
        scNotice.setUserId(userEntity.getUserId());
        return Result.upStatus(scNoticeDao.insert(scNotice));
    }

    @Override
    public IPage<ScNotice> getNoticeList(Integer page, Integer limit, String title,Integer status) {
        Page<ScNotice> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return scNoticeDao.getNoticeList(pages,title,status);



    }
}
