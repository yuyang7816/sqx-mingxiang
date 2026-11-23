package com.sqx.modules.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.shop.entity.ScShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScShopDao extends BaseMapper<ScShop> {
    IPage<ScShop> getShopList(@Param("pages") Page<ScShop> pages, @Param("lng") Double lng, @Param("lat") Double lat, @Param("screen") Integer screen, @Param("shopName") String shopName, @Param("city") String city);

    IPage<ScShop> getAdminShopList(@Param("pages") Page<ScShop> pages, @Param("shopName") String shopName, @Param("status") Integer status, @Param("userName") String userName, @Param("phone") String phone, @Param("city") String city);

    ScShop getShopInfoById(@Param("shopId") Long shopId, @Param("lng") Double lng, @Param("lat") Double lat, @Param("userId") Long userId);
}
