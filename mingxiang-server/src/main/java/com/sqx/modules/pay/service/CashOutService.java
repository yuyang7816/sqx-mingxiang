package com.sqx.modules.pay.service;


import com.alipay.api.request.AlipayZdatafrontDatatransferedFileuploadRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.pay.entity.CashOut;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CashOutService {

    PageUtils selectCashOutList(Map<String, Object> params);

    int saveBody(CashOut cashOut);

    int update(CashOut cashOut);

    CashOut selectById(Long id);

    void cashOutSuccess(UserEntity userByWxId, String date, String money, String payWay, String url);

    List<CashOut> selectCashOutLimit3();

    void refundSuccess(UserEntity userByWxId, String date, String money, String url, String content);

    Double selectCashOutSum(Long userId, Date startTime, Date endTime);

    Double sumMoney(String time, Integer flag);

    Integer countMoney(String time, Integer flag);

    Integer stayMoney(String time, Integer flag);

    void updateMayMoney(int i, Long userId, Double money);

    Result cashMoney(Long userId, BigDecimal money, Integer classify);

    IPage<CashOut> selectAdminHelpProfit(Integer page, Integer limit, String startTime, String endTime, CashOut cashOut);
}
