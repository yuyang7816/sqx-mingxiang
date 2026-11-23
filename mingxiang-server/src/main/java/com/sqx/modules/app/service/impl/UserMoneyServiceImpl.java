package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.*;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.*;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.pay.service.AliPayService;
import com.sqx.modules.pay.service.WxService;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private WxService wxService;
    @Autowired
    private AliPayService aliPayService;

    @Override
    public void updateMoney(int i, Long userId, double money) {
        baseMapper.updateMayMoney(i, userId, money);
    }

    @Override
    public UserMoney selectUserMoneyByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
    }

    @Override
    public Double selectMyProfit(Long userId) {

        return userMoneyDetailsDao.selectMyProfit(userId);
    }


    @Override
    public IPage<UserMoneyDetails> balanceDetailed( Long page, Long limit, UserMoneyDetails userMoneyDetails) {
        Page<UserMoneyDetails> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return userMoneyDetailsDao.selectPage(pages,new QueryWrapper<>(userMoneyDetails).orderByDesc("create_time"));
    }

    @Override
    public Result rechargeMoney(Long userId, BigDecimal money, Integer classify) throws Exception {
        if (classify == 1 || classify == 2 || classify == 3) {
            //微信
            return wxService.payMoney(userId, money, null, classify, 1);
        } else if (classify == 4 || classify == 5) {
            //支付宝支付
            return aliPayService.payMoney(userId, money, null, classify, 1);
        } else {
            return Result.error("支付方式错误!");
        }
    }


}
