package com.sqx.modules.shop.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.shop.entity.ScShop;
import com.sqx.modules.shop.service.ScShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"管理端-店铺"})
@RestController
@RequestMapping("/admin/scShop/")
public class ScShopController {
    @Autowired
    private ScShopService scShopService;

    @ApiOperation(value = "获取店铺列表")
    @GetMapping(value = "getAdminShopList")
    public Result getAdminShopList(Integer page, Integer limit, String shopName, Integer status,String userName,String phone,String city) {
        return Result.success().put("data", scShopService.getAdminShopList(page, limit, shopName, status,userName,phone,city));
    }

    @ApiOperation(value = "审核店铺")
    @GetMapping(value = "examine")
    public Result examine(Long shopId, Integer res, String remarks) {
        return scShopService.examine(shopId, res, remarks);
    }

    @ApiOperation(value = "修改店铺")
    @PostMapping(value = "updateGoods")
    public Result updateGoods(Long userId, @RequestBody ScShop scShop) {
        return scShopService.updateGoods(userId, scShop);
    }





    @ApiOperation(value = "根据店铺id获取店铺信息")
    @GetMapping(value = "getShopInfoById")
    public Result getShopInfoById(Long shopId) {
        return Result.success().put("data", scShopService.getShopInfoById(shopId, null, null, null));
    }

    @ApiOperation(value = "封禁|取消店铺")
    @GetMapping(value = "banShopEnable")
    public Result banShopEnable(Long shopId, String banRemark) {
        return scShopService.banShopEnable(shopId, banRemark);
    }
    @ApiOperation("上下架")
    @PostMapping("showAndDown")
    public Result showAndDown(Long userId,Long dataId) {
        return scShopService.showAndDown(userId, dataId);
    }
    @ApiOperation(value = "删除店铺")
    @GetMapping(value = "deleteShop")
    public Result deleteShop(Long shopId) {
        return scShopService.removeById(shopId) ? Result.success() : Result.error();
    }
    @ApiOperation(value = "删除店铺")
    @GetMapping(value = "recommendShop")
    public Result recommendShop(Long shopId){
        return scShopService.recommendShop(shopId);
    }
}
