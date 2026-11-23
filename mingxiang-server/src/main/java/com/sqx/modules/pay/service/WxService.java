package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;

import java.math.BigDecimal;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {

    Result payOrder(Long id, Integer type) throws Exception;

    String payBack(String resXml,Integer type);

    Result payMoney(Long userId, BigDecimal money, Long parentId, Integer classify, Integer type) throws Exception;

    boolean refund(String ordersNo, BigDecimal refundMoney);

    //boolean refund(Orders orders);

}