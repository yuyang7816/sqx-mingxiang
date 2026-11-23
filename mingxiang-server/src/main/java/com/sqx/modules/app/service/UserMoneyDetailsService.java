package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;

import java.math.BigDecimal;
import java.util.HashMap;

public interface UserMoneyDetailsService extends IService<UserMoneyDetails> {
    Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type);
    Double monthIncome(String date,Long userId);

    void setParentMoneyDetails(Long userId, Long parentId, BigDecimal money, Integer userLevel);

    BigDecimal getOneProfit(Long userId, Integer level);

    void insertLanguageDetails(UserMoneyDetails userMoneyDetails, String titleCode, String[] titleStr, String contentCode, String[] contentStr);

    IPage<HashMap<String, Object>> getLevelIncome(Long userId, Integer type, Integer page, Integer limit);
}
