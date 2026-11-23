package com.sqx.modules.app.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.pay.entity.PayDetails;

public interface VipDetailsService extends IService<VipDetails> {


    IPage<VipDetails> getVipDetailsList(Integer page, Integer limit,Integer isEnable);

    Result insetVipDetails(VipDetails vipDetails);

    Result updateVipDetails(VipDetails vipDetails);

    Result deleteVipDetails(Long id);

    Result userBuyVip(Long userId, Long id,Integer classify,Integer type) throws Exception;

    Result balancePay(Long userId, Long id);

    void callbackBusiness(String outTradeNo);

    Result giveUserVip(Long userId, Integer num);

    Result cancelUserVip(Long userId);
}
