package com.sqx.modules.scTableData.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.vo.ScTableDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScTableDataDao extends BaseMapper<ScTableData> {

    IPage<ScTableDataVo> getMyDataList(@Param("pages") Page<ScTableDataVo> pages, @Param("userId") Long userId, @Param("classId") Long classId, @Param("status") Integer status);

    IPage<ScTableDataVo> getDataList(Page<ScTableDataVo> pages, ScTableDataVo scTableDataVo, Double lng, Double lat, Integer createTimeSort,Integer priceSort,Integer distanceSort);


    ScTableDataVo getDataById(@Param("userId") Long userId, @Param("dataId") Long dataId);

    IPage<ScTableDataVo> adminGetDataList(@Param("pages") Page<ScTableDataVo> pages, @Param("scTableDataVo") ScTableDataVo scTableDataVo, @Param("screen") Integer screen);

    IPage<ScTableDataVo> getUserDataList(@Param("pages") Page<ScTableDataVo> pages, @Param("userId") Long userId);
}
