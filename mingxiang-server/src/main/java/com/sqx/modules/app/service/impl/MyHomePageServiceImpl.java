package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.MyHomePageService;
import com.sqx.modules.course.dao.CourseLearnDao;
import com.sqx.modules.course.dao.CourseLikeDao;
import com.sqx.modules.course.entity.CourseLearn;
import com.sqx.modules.course.entity.CourseLike;
import com.sqx.modules.course.entity.vo.CourseLearnCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/25 11:07
 */
@Service
public class MyHomePageServiceImpl implements MyHomePageService {

    @Autowired
    private CourseLearnDao courseLearnDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseLikeDao courseLikeDao;


    @Override
    public Result selectMyTodayData(Long userId, String currentDate) {
        UserEntity user = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        Map<String, Object> map = new HashMap<>();
        // 查找当月冥想累计天数
        CourseLearnCount meditationMonth = courseLearnDao.selectByMeditationMonth(userId);
        int meditationMonthCount;
        if (meditationMonth == null || meditationMonth.getMeditationMonthCount() == 0) {
            meditationMonthCount = 0;
        } else {
            meditationMonthCount = meditationMonth.getMeditationMonthCount();
        }
        // 如果结果为null或数值为空，则默认存入0
        map.put("meditationMonthCount", meditationMonthCount == 0 || ObjectUtils.isEmpty(meditationMonthCount) ? 0 : meditationMonthCount);
        // 查找当月睡眠累计天数
        CourseLearnCount sleepMonth = courseLearnDao.selectBySleepMonth(userId);
        int sleepMonthCount;
        if (sleepMonth == null || sleepMonth.getSleepMonthCount() == 0) {
            sleepMonthCount = 0;
        } else {
            sleepMonthCount = sleepMonth.getSleepMonthCount();
        }
        map.put("sleepMonthCount", sleepMonthCount == 0 || ObjectUtils.isEmpty(sleepMonthCount) ? 0 : sleepMonthCount);
        // 查找当月专注累计天数
        CourseLearnCount focusMonth = courseLearnDao.selectByFocusMonth(userId);
        int focusMonthCount;
        if (focusMonth == null || focusMonth.getFocusMonthCount() == 0) {
            focusMonthCount = 0;
        } else {
            focusMonthCount = focusMonth.getFocusMonthCount();
        }
        map.put("focusMonthCount", focusMonthCount == 0 || ObjectUtils.isEmpty(focusMonthCount) ? 0 : focusMonthCount);
        // 查出今日冥想总共的时长秒数，并将其转为分钟数
        CourseLearn courseLearn = courseLearnDao.selectByUserId(userId, currentDate);
        int minutes;
        if (courseLearn == null || courseLearn.getTypeCount() == 0) {
            minutes = 0;
        } else {
            int totalSeconds = courseLearn.getTypeCount();
            minutes = totalSeconds;
        }
        map.put("todayMeditation", minutes);
        // 查出今日睡眠总共的时长秒数，并将其转为分钟数
        CourseLearn courseSleep = courseLearnDao.selectSleepByUserId(userId, currentDate);
        int minutesSleep;
        if (courseSleep == null || courseSleep.getTypeCount() == 0) {
            minutesSleep = 0;
        } else {
            int totalSecondsSleep = courseSleep.getDurationToday();
            minutesSleep = totalSecondsSleep;
        }
        map.put("todaySleep", minutesSleep);
        // 查出今日专注总共的时长秒数，并将其转为分钟数
        CourseLearn courseFocus = courseLearnDao.selectFocusByUserId(userId, currentDate);
        int minutesFocus;
        if (courseFocus == null || courseFocus.getTypeCount() == 0) {
            minutesFocus = 0;
        } else {
            int totalSecondsFocus = courseFocus.getDurationToday();
            minutesFocus = totalSecondsFocus;
        }
        map.put("todayFocus", minutesFocus);
        return Result.success().put("data", map);
    }

    @Override
    public Result selectMyAllData(Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        Map<String, Object> map = new HashMap<>();
        // 累计登录天数
        map.put("totalLoginDays", user.getTotalLoginDays());
        // 累计冥想天数
        CourseLearnCount meditationCount = courseLearnDao.selectByAllMeditationMonth(userId);
        map.put("meditationCount", meditationCount == null || ObjectUtils.isEmpty(meditationCount) ? 0 : meditationCount);
        // 累计睡眠天数
        CourseLearnCount sleepCount = courseLearnDao.selectByAllSleepMonth(userId);
        map.put("sleepCount", sleepCount == null || ObjectUtils.isEmpty(sleepCount) ? 0 : sleepCount);
        // 累计专注天数
        CourseLearnCount focusCount = courseLearnDao.selectByAllFocusMonth(userId);
        map.put("focusCount", focusCount == null || ObjectUtils.isEmpty(focusCount) ? 0 : focusCount);
        return Result.success().put("data", map);
    }

    @Override
    public Result selectMyAllTypeData(Long userId, String currentDate, Integer type) {
        UserEntity user = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户不存在");
        }
        Map<String, Object> map = new HashMap<>();
        // 冥想所有的统计：次数统计(meditationCount) 冥想时长(分钟)统计(meditationTime) 冥想天数统计(meditationDay)
        CourseLearnCount meditationAllCount = courseLearnDao.selectByMeditationCount(userId, currentDate, type);
        map.put("meditationAllCount", meditationAllCount == null || ObjectUtils.isEmpty(meditationAllCount) ? 0 : meditationAllCount);
        // 睡眠所有的统计：次数统计(sleepCount) 睡眠时长(分钟)统计(sleepTime) 睡眠天数统计(sleepDay)
        CourseLearnCount sleepAllCount = courseLearnDao.selectBySleepCount(userId, currentDate, type);
        map.put("sleepAllCount", sleepAllCount == null || ObjectUtils.isEmpty(sleepAllCount) ? 0 : sleepAllCount);
        // 专注所有的统计：次数统计(focusCount) 专注时长(分钟)统计(focusTime) 专注天数统计(focusDay)
        CourseLearnCount focusAllCount = courseLearnDao.selectByFocusCount(userId, currentDate, type);
        map.put("focusAllCount", focusAllCount == null || ObjectUtils.isEmpty(focusAllCount) ? 0 : focusAllCount);
        return Result.success().put("data", map);
    }

    @Override
    public Result selectMyCenter(Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户不存在");
        }
        Map<String, Object> map = new HashMap<>();
        // 累计登录天数
        map.put("totalLoginDays", user.getTotalLoginDays());
        // 查出所有冥想总共的时长秒数，并将其转为分钟数
        CourseLearn courseLearn = courseLearnDao.selectByUserId(userId, null);
        int totalSeconds = courseLearn.getTypeCount();
        int minutes = totalSeconds;
        map.put("allMeditation", minutes);
        // 统计我的喜欢
        int courseLike = courseLikeDao.selectCount(new QueryWrapper<CourseLike>()
                .eq("user_id", userId)
                .eq("delete_flag", 0));
        map.put("courseLikeCount", courseLike);
        return Result.success().put("data", map);
    }

    @Override
    public Result selectAllTypeData(String startTime, Integer type) {
        Map<String, Object> map = new HashMap<>();
        // 冥想所有的统计：次数统计(meditationCount) 冥想时长(分钟)统计(meditationTime) 冥想天数统计(meditationDay)
        CourseLearnCount meditationAllCount = courseLearnDao.selectByMeditationCount1(startTime, type);
        map.put("meditationAllCount", meditationAllCount == null || ObjectUtils.isEmpty(meditationAllCount) ? 0 : meditationAllCount);
        // 睡眠所有的统计：次数统计(sleepCount) 睡眠时长(分钟)统计(sleepTime) 睡眠天数统计(sleepDay)
        CourseLearnCount sleepAllCount = courseLearnDao.selectBySleepCount1(startTime, type);
        map.put("sleepAllCount", sleepAllCount == null || ObjectUtils.isEmpty(sleepAllCount) ? 0 : sleepAllCount);
        // 专注所有的统计：次数统计(focusCount) 专注时长(分钟)统计(focusTime) 专注天数统计(focusDay)
        CourseLearnCount focusAllCount = courseLearnDao.selectByFocusCount1(startTime, type);
        map.put("focusAllCount", focusAllCount == null || ObjectUtils.isEmpty(focusAllCount) ? 0 : focusAllCount);
        return Result.success().put("data", map);
    }
}
