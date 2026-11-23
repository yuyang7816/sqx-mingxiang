package com.sqx.modules.pay.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "订单中心", tags = {"订单中心"})
@RequestMapping(value = "/admin/payDetails/")
public class PayDetailsController {
    @Autowired
    private PayDetailsService payDetailsService;

    @GetMapping("getPayDetailsList")
    @ApiOperation("获取第三方订单列表")
    public Result getPayDetailsList(Integer page, Integer limit, PayDetails payDetails,String startTime,String endTime){
        return Result.success().put("data", payDetailsService.getPayDetailsList(page,limit,payDetails,startTime,endTime));
    }
    @PostMapping("refundPayDetails")
    @ApiOperation("退款")
    public Result refundPayDetails(Long payId){
        return payDetailsService.refundPayDetails(payId);
    }
}
