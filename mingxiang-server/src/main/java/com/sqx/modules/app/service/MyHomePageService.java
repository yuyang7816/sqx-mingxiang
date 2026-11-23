package com.sqx.modules.app.service;

import com.sqx.common.utils.Result;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/25 11:07
 */
public interface MyHomePageService {

    /**
     * app用户查看今日我的数据
     * @param userId
     * @param currentDate
     * @return
     */
    Result selectMyTodayData(Long userId,String currentDate);

    /**
     * app用户查看全部我的数据
     * @param userId
     * @return
     */
    Result selectMyAllData(Long userId);

    /**
     * app用户全部分类的数据统计数冥想、睡眠、专注所有的累计次数、累计分钟、累计天数(查今日的话传startTime)
     * @param userId
     * @return
     */
    Result selectMyAllTypeData(Long userId,String currentDate,Integer type);

    /**
     * app用户查看个人中心：累计登录天数 冥想份数  我的喜欢
     * @param userId
     * @return
     */
    Result selectMyCenter(Long userId);

    Result selectAllTypeData(String startTime, Integer type);
}
