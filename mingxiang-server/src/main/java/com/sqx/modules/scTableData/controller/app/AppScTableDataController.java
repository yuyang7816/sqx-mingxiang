package com.sqx.modules.scTableData.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.scTableData.vo.ScTableDataVo;
import com.sqx.modules.search.entity.Search;
import com.sqx.modules.search.service.AppSearchService;
import com.sqx.modules.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@Api(value = "服务管理", tags = {"管理端-服务管理"})
@RequestMapping(value = "/app/ScTableData/")
public class AppScTableDataController {
    @Autowired
    private ScTableDataService scTableDataService;
    @Autowired
    private AppSearchService appSearchService;

    @Login
    @ApiOperation("用户新增|修改服务")
    @PostMapping("insertData")
    public Result insertData(@RequestAttribute("userId") Long userId, @RequestBody ScTableData scTableData) {
        scTableData.setStatus(1);
        scTableData.setUserId(userId);
        return scTableDataService.insertData( scTableData);
    }

    @Login
    @ApiOperation("提交审核")
    @PostMapping("submit")
    public Result submit(@RequestAttribute("userId") Long userId, Long dataId) {
        return scTableDataService.submit(userId, dataId);
    }

    @Login
    @ApiOperation("服务上下架")
    @PostMapping("showAndDown")
    public Result showAndDown(@RequestAttribute("userId")Long userId, Long dataId) {
        return scTableDataService.showAndDown(userId,dataId);
    }

    @Login
    @ApiOperation("用户删除服务")
    @PostMapping("deleteData")
    public Result deleteData(@RequestAttribute("userId") Long userId, Long dataId) {
        return scTableDataService.deleteData(userId, dataId);
    }

    @ApiOperation("用户获取服务列表")
    @GetMapping("getDataList")
    public Result getDataList(Long searchId, Long userId, Integer page, Integer limit, ScTableDataVo scTableDataVo,Double lng,Double lat,Integer createTimeSort,Integer priceSort,Integer distanceSort) {
        scTableDataVo.setUserId(userId);
        if (StringUtils.isNotBlank(scTableDataVo.getDataTitle()) && searchId != null) {
            appSearchService.insertSearch(searchId, scTableDataVo.getDataTitle(), 1);
        }
        return Result.success().put("data", scTableDataService.getDataList(page, limit, scTableDataVo,lng,lat,createTimeSort,priceSort,distanceSort));
    }

    @Login
    @ApiOperation("我的发布")
    @GetMapping("getMyDataList")
    public Result getMyDataList(@RequestAttribute("userId") Long userId, Long classId, Integer status, Integer page, Integer limit) {
        return Result.success().put("data", scTableDataService.getMyDataList(userId, classId, status, page, limit));
    }


    @ApiOperation("根据id查询服务详情")
    @GetMapping("getDataById")
    public Result getDataById(Long userId, Long dataId) {
        return scTableDataService.getDataById(userId, dataId);
    }

    @ApiOperation("商家刷新服务")
    @PostMapping("refreshData")
    public Result refreshData(@RequestAttribute("userId") Long userId, Long dataId) {
        return scTableDataService.refreshData(userId, dataId);
    }
}
