package com.sqx.modules.scNotice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.scNotice.entity.ScNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface ScNoticeDao extends BaseMapper<ScNotice> {

    IPage<ScNotice> getNoticeList(@Param("pages") Page<ScNotice> pages, @Param("title") String title, @Param("status") Integer status);
}
