package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.PayDetails;

import java.math.BigDecimal;

public interface AliPayService {
    /**
     *
     * @param userId
     * @param money 支付金额
     * @param classify 4支付宝APP支付 5支付宝H5支付
     * @param parentId 关联id
     * @return
     */
    Result payMoney(Long userId, BigDecimal money,Long parentId, Integer classify,Integer type);

    boolean aliRefund(PayDetails payDetails);
}
