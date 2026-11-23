package com.sqx.modules.scTableData.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.vo.ScTableDataVo;

public interface ScTableDataService extends IService<ScTableData> {
    Result insertData(ScTableData scTableData);


    IPage<ScTableDataVo> getMyDataList(Long userId, Long classId, Integer status,Integer page, Integer limit);

    Result examine(Integer res, Long dataId, String refuseReason);




    Result recommend(Long dataId);

    Result deleteData(Long userId, Long dataId);

    Result getDataById(Long userId,Long dataId);

    Result submit(Long userId, Long dataId);

    Result refreshData(Long userId, Long dataId);

    Result cancelShow(Long userId, Long dataId);

    IPage<ScTableDataVo> getUserDataList(Integer page, Integer limit, Long userId);

    IPage<ScTableDataVo> getDataList(Integer page, Integer limit, ScTableDataVo scTableDataVo, Double lng, Double lat, Integer createTimeSort,Integer priceSort,Integer distanceSort);

    IPage<ScTableDataVo> adminGetDataList(Integer page, Integer limit, ScTableDataVo scTableDataVo, Integer screen);

    Result showAndDown(Long userId, Long dataId);

    Result banData(Long dataId, String refuseReason);
}
