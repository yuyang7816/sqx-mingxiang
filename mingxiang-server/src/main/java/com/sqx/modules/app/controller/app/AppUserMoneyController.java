package com.sqx.modules.app.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/app/userMoney")
@Api(value = "用户钱包-管理端", tags = {"用户钱包-管理端"})
public class AppUserMoneyController {

    private UserMoneyService userMoneyService;

    @GetMapping("/selectMyMoney")
    @Login
    @ApiOperation("我的钱包余额")
    public Result selectMyMoney(@RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectUserMoneyByUserId(userId));
    }

    /**
     * 我的收益
     */
    @GetMapping("/selectMyProfit")
    @Login
    @ApiOperation("我的收益")
    public Result selectMyProfit(@RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectMyProfit(userId));
    }

    /**
     * 余额明细
     */
    @GetMapping("/balanceDetailed")
    @Login
    @ApiOperation("余额明细")
    public Result balanceDetailed(@RequestAttribute Long userId, Long page, Long limit, UserMoneyDetails userMoneyDetails) {
        userMoneyDetails.setUserId(userId);
        return Result.success().put("data", userMoneyService.balanceDetailed(page,limit, userMoneyDetails));
    }



    @Login
    @PostMapping("/rechargeMoney")
    @ApiOperation("充值余额")
    public Result RechargeMoney(@RequestAttribute Long userId, BigDecimal money, Integer classify) throws Exception {
        return userMoneyService.rechargeMoney(userId, money, classify);
    }


}
