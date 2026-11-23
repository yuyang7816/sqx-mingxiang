package com.sqx.modules.pay.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.PayDetails;

import java.util.HashMap;

public interface PayDetailsService extends IService<PayDetails> {

    PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state);

    Double selectSumPay(String createTime, String endTime, Long userId);

    Double selectSumMember(String time, Integer flag);

    Double selectSumPayByState(String time, Integer flag, Integer state);

    Double selectSumPayByClassify(String time, Integer flag, Integer classify,Integer payClassify);

    Double instantselectSumPay(String date, Long userId);

    HashMap<String,Object> payMessage(String date, Integer type);

    IPage<PayDetails> getPayDetailsList(Integer page, Integer limit, PayDetails payDetails, String startTime, String endTime);

    Result refundPayDetails(Long payId);
}
