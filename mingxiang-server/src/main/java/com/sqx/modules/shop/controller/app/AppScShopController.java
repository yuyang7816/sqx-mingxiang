package com.sqx.modules.shop.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.scBrowse.entity.ScBrowse;
import com.sqx.modules.scBrowse.service.ScBrowseService;
import com.sqx.modules.search.service.AppSearchService;
import com.sqx.modules.shop.entity.ScShop;
import com.sqx.modules.shop.service.ScShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Api(tags = {"用户端-店铺"})
@RestController
@RequestMapping("/app/scShop/")
public class AppScShopController {
    @Autowired
    private ScShopService scShopService;
    @Autowired
    private ScBrowseService scBrowseService;
    @Autowired
    private AppSearchService appSearchService;

    @Login
    @ApiOperation(value = "添加店铺")
    @PostMapping(value = "insertGoods")
    public Result insertShop(@RequestAttribute("userId") Long userId, @RequestBody ScShop scShop) {
        return scShopService.insertShop(userId, scShop);
    }

    @Login
    @ApiOperation(value = "修改店铺")
    @PostMapping(value = "updateGoods")
    public Result updateGoods(@RequestAttribute("userId") Long userId, @RequestBody ScShop scShop) {
        scShop.setStatus(1);
        scShop.setIsEnable(0);
        return scShopService.updateGoods(userId, scShop);
    }

    @ApiOperation(value = "获取店铺列表")
    @GetMapping(value = "getShopList")
    public Result getShopList(Integer page, Integer limit, Double lng, Double lat, Integer screen, Long userId, String shopName,String city) {
        if (StringUtils.isNotBlank(shopName) && userId != null) {
            appSearchService.insertSearch(userId, shopName, 2);
        }
        return Result.success().put("data", scShopService.getShopList(page, limit, lng, lat, screen, shopName,city));
    }

    @Login
    @ApiOperation(value = "获取我的店铺列表")
    @GetMapping(value = "getMyShopList")
    public Result getMyShopList(@RequestAttribute("userId") Long userId, Integer page, Integer limit, ScShop scShop) {
        scShop.setUserId(userId);
        return Result.success().put("data", scShopService.getMyShopList(page, limit, scShop));
    }

    @ApiOperation(value = "根据店铺id获取店铺信息")
    @GetMapping(value = "getShopInfoById")
    public Result getShopInfoById(Long userId, Long shopId, Double lng, Double lat) {
        ScBrowse browse = scBrowseService.getOne(new QueryWrapper<ScBrowse>().eq("shop_id", shopId).eq("user_id", userId));
        //历史记录
        if (browse == null) {
            ScBrowse scBrowse = new ScBrowse();
            scBrowse.setUserId(userId);
            scBrowse.setType(2);
            scBrowse.setShopId(shopId);
            scBrowse.setUpdateTime(new Date());
            scBrowseService.save(scBrowse);
        } else {
            browse.setUpdateTime(new Date());
            scBrowseService.updateById(browse);
        }
        return Result.success().put("data", scShopService.getShopInfoById(shopId, lng, lat, userId));
    }

    @Login
    @ApiOperation(value = "上下架店铺")
    @GetMapping(value = "updateShopEnable")
    public Result updateShopEnable(@RequestAttribute("userId") Long userId, Long shopId) {
        return scShopService.updateShopEnable(userId, shopId);
    }

    @Login
    @ApiOperation(value = "删除店铺")
    @GetMapping(value = "deleteShop")
    public Result deleteShop(@RequestAttribute("userId") Long userId, Long shopId) {
        ScShop scShop = scShopService.getById(shopId);
        if (!scShop.getUserId().equals(userId)) {
            return Result.error("非法操作");
        } else {
            return scShopService.removeById(shopId) ? Result.success() : Result.error();
        }
    }
}
