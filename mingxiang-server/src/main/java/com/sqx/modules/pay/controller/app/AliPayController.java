package com.sqx.modules.pay.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.VipDetailsService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.pay.config.AliPayConstants;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.AliPayService;
import com.sqx.modules.utils.AmountCalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.sqx.modules.utils.wx.WxPayUtils.getGeneralOrder;

/**
 * 支付宝支付处理--暂不做同步处理、回调方式使用异步
 */
@Slf4j
@RestController
@Api(value = "支付宝支付", tags = {"支付宝支付"})
@RequestMapping("/app/aliPay")
public class AliPayController {
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @RequestMapping(value = "/notifyApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonInfoService.findOne(64).getValue(), AliPayConstants.CHARSET, "RSA2");
            log.info(flag + "回调验证信息");
            if (flag) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    //支付宝返回的订单编号
                    String outTradeNo = params.get("out_trade_no");
                    log.error(outTradeNo);
                    //支付宝支付单号
                    String tradeNo = params.get("trade_no");
                    PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
                    if (payDetails!=null && payDetails.getState() == 0) {
                        payDetails.setState(1);
                        payDetails.setPayTime(DateUtils.format(new Date()));
                        payDetails.setTradeNo(tradeNo);
                        payDetailsDao.updateById(payDetails);
                        //处理业务
                        vipDetailsService.callbackBusiness(outTradeNo);
                    }

                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }
    @Login
    @ApiOperation("充值金额")
    @RequestMapping(value = "/payAppMoney", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payAppMoney(@RequestAttribute("userId") Long userId, BigDecimal money, Integer payType) {
        return aliPayService.payMoney(userId, money, null, payType, 1);
    }


    @Login
    @ApiOperation("支付宝h5充值金额")
    @RequestMapping(value = "/payH5Money", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payH5Money(@RequestAttribute("userId") Long userId, BigDecimal money) {
        //通知页面地址
        CommonInfo one = commonInfoService.findOne(19);
        String returnUrl = one.getValue() + "/pages/index/index";
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        log.info("回调地址:" + url);
        String generalOrder = getGeneralOrder();
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(4);
        payDetails.setType(1);
        payDetailsDao.insert(payDetails);
        return payH5(generalOrder, name, money, url, returnUrl);
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
   /* @Login
    @ApiOperation("支付宝支付订单")
    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payOrder(Long id) {
        //通知页面地址
        CommonInfo one = commonInfoService.findOne(19);
        String returnUrl = one.getValue() + "/#/pages/task/recharge";
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "陪玩接单" : one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        log.info("回调地址:" + url);
        PayOrder orders = ordersDao.selectById(id);
        try {
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            JSONObject order = new JSONObject();
            order.put("out_trade_no", orders.getOrdersNo()); //订单号
            order.put("subject", name); //商品标题
            order.put("product_code", "QUICK_WAP_WAY");
            order.put("body", name);//商品名称
            BigDecimal money = orders.getPayMoney();
            order.put("total_amount", money); //金额
            alipayRequest.setBizContent(order.toString());
            //在公共参数中设置回跳和通知地址
            alipayRequest.setNotifyUrl(url);
            //通知页面地址
            alipayRequest.setReturnUrl(returnUrl);
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            PayDetails payDetails = new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(orders.getOrdersNo());
            payDetails.setUserId(orders.getUserId());
            payDetails.setMoney(money.doubleValue());
            payDetails.setClassify(4);
            payDetailsDao.insert(payDetails);
            return Result.success().put("data", form);
        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.error("获取订单信息错误！");
    }


    @ApiOperation("充值")
    @RequestMapping(value = "/payApp",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payApp(Long id){
        CommonInfo one = commonInfoService.findOne(19);
        CommonInfo one3 = commonInfoService.findOne(12);
        String name=one3==null?"陪玩接单":one3.getValue();
        PayOrder orders = ordersDao.selectById(id);
        if (orders.getState().equals(1)) {
            return Result.error("订单已支付，请不要重复点击！");
        }
        String result="";
        try {
            String url=one.getValue()+"/sqx_fast/app/aliPay/notifyApp";
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody(name);
            model.setSubject(name);
            model.setOutTradeNo(orders.getOrdersNo());
            model.setTimeoutExpress("30m");
            model.setTotalAmount(orders.getPayMoney().toString());
            model.setProductCode("QUICK_MSECURITY_PAY");
            request.setBizModel(model);
            request.setNotifyUrl(url);
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if(response.isSuccess()){
                result=response.getBody();
            } else {
               return Result.error("获取订单失败！");
            }
            PayDetails payDetails = new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(orders.getOrdersNo());
            payDetails.setUserId(orders.getUserId());
            payDetails.setMoney(orders.getPayMoney().doubleValue());
            payDetails.setClassify(4);
            payDetailsDao.insert(payDetails);
            return Result.success().put("data",result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.error(-100,"获取订单失败！");
    }

    *//*public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }*//*


    *//**
     * 说明： 支付宝订单退款
     *
     * @return 公共返回参数 code,msg,   响应参数实例: https://docs.open.alipay.com/api_1/alipay.trade.refund
     *//*
     /**
     * 说明： 支付宝订单退款
     *
     * @return 公共返回参数 code,msg,   响应参数实例: https://docs.open.alipay.com/api_1/alipay.trade.refund
     */


}
