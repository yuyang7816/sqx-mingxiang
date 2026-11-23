package com.sqx.modules.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.shop.entity.ScShop;

public interface ScShopService extends IService<ScShop> {
    Result insertShop(Long userId,ScShop scShop);

    /**
     *
     * @param page
     * @param limit
     * @param screen 排序方式 1总和排序 2最新发布 3附近店铺
     * @param shopName
     * @param city
     * @return
     */
    IPage<ScShop> getShopList(Integer page, Integer limit, Double lng, Double lat, Integer screen, String shopName, String city);

    IPage<ScShop> getAdminShopList(Integer page, Integer limit, String shopName, Integer status, String userName, String phone, String city);

    /**
     *
     * @param shopId
     * @param res  1同意  0拒绝
     * @param remarks 审核备注
     * @return
     */
    Result examine(Long shopId, Integer res,String remarks);

    Result updateGoods(Long userId, ScShop scShop);

    ScShop getShopInfoById(Long shopId, Double lng, Double lat, Long userId);

    IPage<ScShop> getMyShopList(Integer page, Integer limit, ScShop scShop);

    Result updateShopEnable(Long userId, Long shopId);

    Result banShopEnable(Long shopId, String banRemark);

    Result recommendShop(Long shopId);

    Result showAndDown(Long userId, Long dataId);
}
