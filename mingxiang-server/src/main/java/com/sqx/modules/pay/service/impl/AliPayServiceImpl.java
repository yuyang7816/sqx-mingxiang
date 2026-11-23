package com.sqx.modules.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.pay.config.AliPayConstants;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.AliPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sqx.modules.utils.wx.WxPayUtils.getGeneralOrder;

@Service
@Slf4j
public class AliPayServiceImpl implements AliPayService {
    @Autowired
    CommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Result payMoney(Long userId, BigDecimal money, Long parentId, Integer classify, Integer type) {
        CommonInfo one = commonInfoService.findOne(19);
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "余额充值" : one3.getValue();
        String generalOrder = getGeneralOrder();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String returnUrl = one.getValue() + "/pages/task/recharge";
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(type);
        payDetails.setParentId(parentId);
        payDetailsDao.insert(payDetails);
        if (classify == 4) {
            return payApp(generalOrder, name, money, url);
        }
        return payH5(generalOrder, name, money, url, returnUrl);
    }

    public Result payApp(String generalOrder, String name, BigDecimal money, String url) {
        try {
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody(name);
            model.setSubject(name);
            model.setOutTradeNo(generalOrder);
            model.setTimeoutExpress("30m");
            model.setTotalAmount(String.valueOf(money));
            model.setProductCode("QUICK_MSECURITY_PAY");
            request.setBizModel(model);
            request.setNotifyUrl(url);
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if (response.isSuccess()) {
                return Result.success().put("data", response.getBody());
            } else {
                return Result.error("获取订单失败！");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.error(-100, "获取订单失败！");
    }

    public Result payH5(String generalOrder, String name, BigDecimal money, String url, String returnUrl) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            JSONObject order = new JSONObject();
            order.put("out_trade_no", generalOrder); //订单号
            order.put("subject", name); //商品标题
            order.put("product_code", "QUICK_WAP_WAY");
            order.put("body", name);//商品名称
            order.put("total_amount", money); //金额
            alipayRequest.setBizContent(order.toString());
            //在公共参数中设置回跳和通知地址
            alipayRequest.setNotifyUrl(url);
            //通知页面地址
            alipayRequest.setReturnUrl(returnUrl);
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            return Result.success().put("data", form);
        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.error("获取订单信息错误！");
    }
    @Override
    public boolean aliRefund(PayDetails payDetails) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(payDetails.getTradeNo()); //订单号
            model.setRefundAmount(payDetails.getMoney() + ""); //退款金额
            model.setOutRequestNo(payDetails.getOrderId()); //退款单号
            request.setBizModel(model);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return true;
            } else {
                return false;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("支付宝退款失败！，订单号" + payDetails.getOrderId() + "，失败原因：" + e.getMessage(), e);
        }
        return false;
    }
}
