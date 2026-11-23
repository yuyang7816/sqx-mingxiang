package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.configI18n.service.ConfigI18nService;
import com.sqx.modules.utils.DataSourceMessageSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
public class UserMoneyDetailsServiceImpl extends ServiceImpl<UserMoneyDetailsDao, UserMoneyDetails> implements UserMoneyDetailsService {

    @Autowired
    private ConfigI18nService configI18nService;

    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Override
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId, Integer classify, Integer type) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        if (classify != null) {
            queryWrapper.eq("classify", classify);
        }
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return Result.success().put("data", baseMapper.selectPage(page1, queryWrapper));
    }

    @Override
    public Double monthIncome(String date, Long userId) {
        return baseMapper.monthIncome(date, userId);
    }

    @Override
    public void setParentMoneyDetails(Long userId, Long parentId, BigDecimal money, Integer userLevel) {
        //添加用户邀请人钱包详情
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(parentId);
        userMoneyDetails.setType(1);
        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetails.setClassify(3);
        if (userLevel == 1) {
            userMoneyDetails.setTitle("一级用户开通会员分成");
            userMoneyDetails.setMoney(money);
            userMoneyDetails.setContent("一级用户开通会员分成金额:" + money);
            userMoneyDetails.setUserLevel(1);
            userMoneyDetails.setRelationId(userId);
//            userMoneyDetailsService.insertLanguageDetails(userMoneyDetails, "10069",null, "10070",new String[]{String.valueOf(money)});
            baseMapper.insert(userMoneyDetails);
        } else if (userLevel == 2) {
            userMoneyDetails.setTitle("二级用户开通会员分成");
            userMoneyDetails.setMoney(money);
            userMoneyDetails.setContent("二级用户开通会员分成金额:" + money);
            userMoneyDetails.setRelationId(userId);
            userMoneyDetails.setUserLevel(2);
            baseMapper.insert(userMoneyDetails);
//            userMoneyDetailsService.insertLanguageDetails(userMoneyDetails, "10072",null, "10071",new String[]{String.valueOf(money)});
        }

    }

    @Override
    public BigDecimal getOneProfit(Long userId, Integer level) {

        return baseMapper.getOneProfit(userId, level);

    }

    @Override
    public IPage<HashMap<String, Object>> getLevelIncome(Long userId, Integer type, Integer page, Integer limit) {
        Page<HashMap<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getLevelIncome(pages, userId, type);
    }


    @Override
    public void insertLanguageDetails(UserMoneyDetails userMoneyDetails, String titleCode, String[] titleStr, String contentCode, String[] contentStr) {
        List<HashMap<String, String>> languageList = configI18nService.getLanguageList(null);
        for (HashMap<String, String> hashMap : languageList) {
            String language = hashMap.get("language");
            String title = dataSourceMessageSource.getSourceFromCache(titleCode, titleStr, new Locale(language));
            userMoneyDetails.setTitle(title);
            if (StringUtils.isNotBlank(contentCode)) {
                String content = dataSourceMessageSource.getSourceFromCache(contentCode, contentStr, new Locale(language));
                userMoneyDetails.setContent(content);
            }
            userMoneyDetails.setLanguageType(language);
            baseMapper.insert(userMoneyDetails);
        }
    }
}
