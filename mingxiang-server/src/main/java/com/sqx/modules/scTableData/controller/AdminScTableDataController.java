package com.sqx.modules.scTableData.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.scTableData.vo.ScTableDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "信息管理", tags = {"管理端-信息管理"})
@RequestMapping(value = "/admin/ScTableData/")
public class AdminScTableDataController {
    @Autowired
    private ScTableDataService scTableDataService;


    /**
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation("管理员获取信息列表")
    @GetMapping("getDataList")
    public Result getDataList(Integer page, Integer limit, ScTableDataVo scTableDataVo,Integer screen) {

        return Result.success().put("data", scTableDataService.adminGetDataList(page, limit, scTableDataVo,screen));
    }

    /**
     * @param res          1同意 2拒绝
     * @param dataId       信息id
     * @param refuseReason 拒绝理由
     * @return
     */
    @ApiOperation("管理审核信息")
    @GetMapping("examine")
    public Result examine(Integer res, Long dataId, String refuseReason) {
        return scTableDataService.examine(res, dataId, refuseReason);
    }

    @ApiOperation("推荐服务")
    @GetMapping("recommend")
    public Result recommend(Long dataId) {
        return scTableDataService.recommend(dataId);
    }


    @ApiOperation("删除服务")
    @GetMapping("deleteData")
    public Result deleteData(Long dataId) {
        return scTableDataService.removeById(dataId)?Result.success():Result.error();
    }

    @ApiOperation("用户新增|修改服务")
    @PostMapping("insertData")
    public Result insertData( @RequestBody ScTableData scTableData) {
        scTableData.setStatus(2);
        return scTableDataService.insertData(scTableData);
    }

    @ApiOperation("上下架")
    @PostMapping("showAndDown")
    public Result showAndDown(Long userId,Long dataId) {
        return scTableDataService.showAndDown(userId, dataId);
    }

    @ApiOperation("封禁或取消封禁服务")
    @PostMapping("banData")
    public Result banData(Long dataId,String refuseReason) {
        return scTableDataService.banData(dataId,refuseReason);
    }
}
