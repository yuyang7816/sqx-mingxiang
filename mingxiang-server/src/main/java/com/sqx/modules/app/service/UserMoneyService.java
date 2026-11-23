package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.math.BigDecimal;

public interface UserMoneyService extends IService<UserMoney> {

    UserMoney selectUserMoneyByUserId(Long userId);

    void updateMoney(int i, Long userId, double money);

    Double selectMyProfit(Long userId);

    IPage<UserMoneyDetails> balanceDetailed( Long page, Long limit, UserMoneyDetails userMoneyDetails);


    Result rechargeMoney(Long userId, BigDecimal money, Integer classify) throws Exception;
}
