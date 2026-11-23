package com.sqx.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.AliPayService;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.scTableClass.entity.ScTableClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 充值记录
 */
@Service
public class PayDetailsServiceImpl extends ServiceImpl<PayDetailsDao, PayDetails> implements PayDetailsService {

    /**
     * 充值记录
     */
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private WxService wxService;
    @Autowired
    private AliPayService aliPayService;

    @Override
    public PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state) {
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        if (state != null && state == -1) {
            state = null;
        }
        return new PageUtils(payDetailsDao.selectPayDetails(pages, startTime, endTime, userId, state));
    }

    @Override
    public Double selectSumPay(String createTime, String endTime, Long userId) {
        if (userId == null || userId == -1) {
            return payDetailsDao.selectSumPay(createTime, endTime, null);
        }
        return payDetailsDao.selectSumPay(createTime, endTime, userId);
    }



    @Override
    public Double selectSumMember(String time, Integer flag) {
        return payDetailsDao.selectSumMember(time, flag);
    }

    @Override
    public Double selectSumPayByState(String time, Integer flag, Integer state) {
        return payDetailsDao.selectSumPayByState(time, flag, state);
    }

    @Override
    public Double selectSumPayByClassify(String time, Integer flag, Integer classify, Integer payClassify) {
        return payDetailsDao.selectSumPayByClassify(time, flag, classify, payClassify);
    }

    @Override
    public Double instantselectSumPay(String date, Long userId) {
        return payDetailsDao.instantselectSumPay(date, userId);
    }

    @Override
    public HashMap<String, Object> payMessage(String date, Integer type) {

        HashMap<String, Object> hashMap = new HashMap<>();
        //总金额
        BigDecimal allMoney = payDetailsDao.payMessage(date, type, null);
        //app支付
        BigDecimal appMoney = payDetailsDao.payMessage(date, type, 1);
        //微信二维码支付
        BigDecimal wxRwmMoney = payDetailsDao.payMessage(date, type, 2);
        //微信小程序支付
        BigDecimal wxAppMoney = payDetailsDao.payMessage(date, type, 3);
        //支付宝app支付
        BigDecimal zfbMoney = payDetailsDao.payMessage(date, type, 4);
        //支付宝H5支付
        BigDecimal zfbH5Money = payDetailsDao.payMessage(date, type, 5);

        hashMap.put("allMoney", allMoney);
        hashMap.put("appMoney", appMoney);
        hashMap.put("wxRwmMoney", wxRwmMoney);
        hashMap.put("wxAppMoney", wxAppMoney);
        hashMap.put("zfbMoney", zfbMoney);
        hashMap.put("zfbH5Money", zfbH5Money);

        return hashMap;


    }

    @Override
    public IPage<PayDetails> getPayDetailsList(Integer page, Integer limit, PayDetails payDetails, String startTime, String endTime) {
        Page<ScTableClass> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return baseMapper.getPayDetailsList(pages, payDetails, startTime, endTime);
    }

    @Override
    public Result refundPayDetails(Long payId) {
        PayDetails payDetails = baseMapper.selectById(payId);
        if (payDetails.getClassify() == 1 || payDetails.getClassify() == 2 || payDetails.getClassify() == 3) {
            if (wxService.refund(payDetails.getOrderId(), BigDecimal.valueOf(payDetails.getMoney()))) {
                return Result.success("退款成功");
            } else {
                return Result.error("退款异常,请重试");
            }
        } else {
            if (aliPayService.aliRefund(payDetails)) {
                return Result.success("退款成功");
            } else {
                return Result.error("退款异常,请重试");
            }
        }
    }
}
