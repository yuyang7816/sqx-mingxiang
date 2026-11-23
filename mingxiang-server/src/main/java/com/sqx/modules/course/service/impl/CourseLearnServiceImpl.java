package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.course.dao.ChapterInfoDao;
import com.sqx.modules.course.dao.CourseInfoDao;
import com.sqx.modules.course.dao.CourseLearnDao;
import com.sqx.modules.course.dao.CourseTypeDao;
import com.sqx.modules.course.entity.ChapterInfo;
import com.sqx.modules.course.entity.CourseLearn;
import com.sqx.modules.course.entity.CourseType;
import com.sqx.modules.course.entity.vo.ChapterInfoVO;
import com.sqx.modules.course.entity.vo.DateGroupVO;
import com.sqx.modules.course.service.CourseLearnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/21 17:20
 */
@Service
public class CourseLearnServiceImpl extends ServiceImpl<CourseLearnDao, CourseLearn> implements CourseLearnService {

    @Autowired
    private CourseLearnDao courseLearnDao;

    @Autowired
    private ChapterInfoDao chapterInfoDao;

    @Autowired
    private CourseInfoDao courseInfoDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseTypeDao courseTypeDao;

    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    @Override
    public Result selectMyPlayed(Integer pageNum, Integer pageSize, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
//        List<CourseType> typeList = new ArrayList<>();
        // 先查出自己所有学习的记录，没有则返回空分页结果
        List<CourseLearn> courseLearnList = courseLearnDao.selectList(new QueryWrapper<CourseLearn>()
                .eq("user_id", userId)
                .eq("delete_flag", 0)
                .isNotNull("course_id")
                .orderByDesc("create_time"));
        // 用冥想学习的课程来查出课程类型，然后用课程类型来进行分组，确保同一个分类在不同天分开显示
        PageHelper.startPage(pageNum, pageSize);
        List<CourseType> types = courseTypeDao.selectByTypeId(userId);
        // 如果是空则返回空分页结果
        if (courseLearnList == null) {
            return Result.success().put("data", new PageInfo<>());
        }
        // 如果是空则返回空分页结果
        if (types == null) {
            return Result.success().put("data", new PageInfo<>());
        }
        types.forEach(type -> {
            // 查出章节信息及课程总章节
            List<ChapterInfoVO> chapterInfoList = chapterInfoDao.selectByCourseId(type.getCourseId());
            type.setChapterNum(chapterInfoList.size() + "章");
        });
        // 遍历每个课程类型下的课程，用课程类型和日期来分组，根据时间倒序，然后返回分页结果
        List<DateGroupVO> groups = types.stream().collect(Collectors.groupingBy(CourseType::getMeditationTime))
                .entrySet().stream()
                .map(entry -> {
                    DateGroupVO dateGroup = new DateGroupVO();
                    dateGroup.setDate(entry.getKey());
                    dateGroup.setList(entry.getValue());
                    return dateGroup;
                })
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());
//        List<DateGroupVO> grouped = list.stream().collect(Collectors.groupingBy(CourseInfoVO::getCreateTime))
//                .entrySet().stream()
//                .map(entry -> {
//                    DateGroupVO dateGroup = new DateGroupVO();
//                    dateGroup.setDate(entry.getKey());
//                    dateGroup.setList(entry.getValue());
//                    return dateGroup;
//                })
//                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
//                .collect(Collectors.toList());
        // 返回分页结果
        PageInfo<CourseType> pageInfo = new PageInfo<>(types);
//        PageInfo<DateGroupVO> pageInfo = new PageInfo<>();
        // 设置分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", groups);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("total", pageInfo.getTotal());
        result.put("pages", (int) Math.ceil((double) pageInfo.getTotal() / pageSize));

        return Result.success().put("data", result);
    }

    @Override
    public Result startMeditation(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        ChapterInfo characterInfo = chapterInfoDao.selectById(courseLearn.getChapterId());
        if (characterInfo == null) {
            return Result.error("章节信息不存在");
        }
        courseLearn.setType(0);
        courseLearn.setLearnDate(LocalDate.now());
        courseLearn.setLearnTime(LocalTime.from(LocalDateTime.now()));
        courseLearn.setAlarmStartTime(LocalDateTime.now());
        courseLearn.setCreateTime(LocalDateTime.now());
        courseLearn.setCompletionStatus(1);
        courseLearn.setChapterId(characterInfo.getId());
        courseLearn.setCourseId(characterInfo.getCourseId());
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (courseLearnDao.insert(courseLearn) > 0) {
                return Result.success("冥想开始练习记录成功").put("data", courseLearn.getId());
            } else {
                return Result.error("冥想开始练习记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("冥想开始练习报错:" + e.getMessage(), e);
            return Result.error("冥想开始练习记录失败:" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }

    }

    @Override
    public Result endMeditation(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        CourseLearn courseLearn1 = courseLearnDao.selectById(courseLearn.getId());
        if (ObjectUtils.isEmpty(courseLearn1)) {
            return Result.error("冥想练习记录不存在");
        }
        courseLearn.setCompletionStatus(2);
//        // 获取当前日期
//        LocalDate currentDate = LocalDate.now();
//        // 构造完整的开始和结束日期时间对象
//        LocalDateTime startTime = LocalDateTime.of(currentDate, LocalTime.from(courseLearn1.getAlarmStartTime()));
//        LocalDateTime endTime = LocalDateTime.of(currentDate, LocalTime.from(courseLearn.getAlarmEndTime()));
        // 计算时长
//        Duration duration = Duration.between(startTime, endTime);
        // 设置学习时长，转换为秒
        LocalDateTime startTime = courseLearn1.getAlarmStartTime();
        int duration = (int) Duration.between(startTime, LocalDateTime.now()).toMinutes();
        if (duration <= 0) {
            courseLearn.setDurationTime(1);
        } else {
            courseLearn.setDurationTime(duration);
        }
        courseLearn.setUpdateTime(new Date());
        courseLearn.setAlarmEndTime(LocalDateTime.now());
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (courseLearnDao.updateById(courseLearn) > 0) {
                return Result.success("冥想结束练习记录成功");
            } else {
                return Result.error("冥想结束练习记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("冥想结束练习报错:" + e.getMessage(), e);
            return Result.error("冥想结束练习记录失败:" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }


    @Override
    public Result startAlarm(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        courseLearn.setType(1);
        courseLearn.setLearnDate(LocalDate.now());
        courseLearn.setLearnTime(LocalTime.from(LocalDateTime.now()));
        courseLearn.setAlarmStartTime(LocalDateTime.now());
        courseLearn.setCreateTime(LocalDateTime.now());
        courseLearn.setCompletionStatus(1);
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (courseLearnDao.insert(courseLearn) > 0) {
                return Result.success("夜间睡眠开始记录成功").put("data", courseLearn.getId());
            } else {
                return Result.error("夜间睡眠开始记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("夜间睡眠记录报错:" + e.getMessage(), e);
            return Result.error("夜间睡眠开始记录失败:" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Result endAlarm(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        CourseLearn courseLearn1 = courseLearnDao.selectById(courseLearn.getId());
        // 构建完整的开始时间和结束时间的对象
        LocalDateTime startTime = courseLearn1.getAlarmStartTime();
        LocalDateTime endTime = LocalDateTime.now();
        // 当前日期
        LocalDate currentDate = startTime.toLocalDate();
        int days = (int) ChronoUnit.DAYS.between(startTime.toLocalDate(), endTime.toLocalDate());
        LocalDate tomorrowDate = currentDate.plusDays(1);
        // 处理今天的睡眠时间
        CourseLearn todayRecord = new CourseLearn();
        todayRecord.setId(courseLearn.getId());
        todayRecord.setUserId(courseLearn.getUserId());
        todayRecord.setLearnDate(currentDate);
        todayRecord.setAlarmStartTime(startTime);
        todayRecord.setAlarmEndTime(endTime);
        todayRecord.setCompletionStatus(2);
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (processSleepDuration(courseLearn, todayRecord, startTime, endTime, currentDate, tomorrowDate)) {
                return Result.success("夜间睡眠结束记录成功");
            } else {
                return Result.error("夜间睡眠结束记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("夜间睡眠结束记录报错:" + e.getMessage(), e);
            return Result.error("夜间睡眠结束记录失败:" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }

    }

    private boolean processSleepDuration(CourseLearn originalCourseLearn, CourseLearn updatedCourseLearn, LocalDateTime startTime,
                                         LocalDateTime endTime, LocalDate currentDate, LocalDate tomorrowDate) {
        if (startTime.toLocalDate().isEqual(currentDate) && endTime.toLocalDate().isEqual(currentDate)) {
            // 完全在今天
            int duration = calculateDurationInDateRange(startTime, endTime, currentDate);
            updatedCourseLearn.setDurationToday(duration);
        } else if (startTime.toLocalDate().isEqual(currentDate) && endTime.toLocalDate().isEqual(tomorrowDate)) {
            // 跨越今天和明天
            int durationToday = calculateDurationInDateRange(startTime, LocalDateTime.of(currentDate.plusDays(1), LocalTime.MIN), currentDate);
            int durationTomorrow = calculateDurationInDateRange(LocalDateTime.of(tomorrowDate, LocalTime.MIN), endTime, tomorrowDate);
            updatedCourseLearn.setDurationToday(durationToday);
            updatedCourseLearn.setDurationTomorrow(durationTomorrow);
        } else if (startTime.toLocalDate().isEqual(tomorrowDate)) {
            // 完全在明天
            int duration = calculateDurationInDateRange(startTime, endTime, tomorrowDate);
            updatedCourseLearn.setDurationTomorrow(duration);
        } else {
            return false;
        }
        // 更新记录
        if (courseLearnDao.updateById(updatedCourseLearn) <= 0) {
            return false;
        }
        return true;
    }

    private int calculateDurationInDateRange(LocalDateTime startTime, LocalDateTime endTime, LocalDate date) {
        // 确保开始和结束时间都在指定的日期范围内
        LocalDateTime startDateInRange = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDateInRange = LocalDateTime.of(date, LocalTime.MAX);

        // 如果开始时间早于指定日期的开始，则调整为指定日期的开始
        if (startTime.isBefore(startDateInRange)) {
            startTime = startDateInRange;
        }
        // 如果结束时间晚于指定日期的结束，则调整为指定日期的结束
        if (endTime.isAfter(endDateInRange)) {
            endTime = endDateInRange;
        }
        // 确保开始时间不晚于结束时间，避免计算负值
        if (startTime.isAfter(endTime)) {
            return 0;
        }
        // 计算两个时间点之间的时间差，以秒为单位
        Duration duration = Duration.between(startTime, endTime);
        int durationInMinutes = (int) duration.toMinutes();
        if (durationInMinutes <= 0){
            durationInMinutes = 1;
        }
        return durationInMinutes;
    }

    @Override
    public Result startConcentrate(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        courseLearn.setType(2);
        courseLearn.setLearnDate(LocalDate.now());
        courseLearn.setLearnTime(LocalTime.from(LocalDateTime.now()));
        courseLearn.setCreateTime(LocalDateTime.now());
        courseLearn.setCompletionStatus(1);
        reentrantReadWriteLock.writeLock().lock();
        try {
            int result = courseLearnDao.insert(courseLearn);
            if (result > 0) {
                return Result.success("开始专注记录成功").put("data", courseLearn.getId());
            } else {
                return Result.error("开始专注记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("开始专注记录报错:" + e.getMessage(), e);
            return Result.error("开始专注记录报错：" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Result endConcentrate(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        CourseLearn courseLearn1 = courseLearnDao.selectById(courseLearn.getId());
        if (ObjectUtils.isEmpty(courseLearn1)) {
            return Result.error("专注记录不存在");
        }
        // 设置学习时长，转换为分钟
        courseLearn.setWriteTime((int) Duration.between(courseLearn1.getCreateTime(), LocalDateTime.now()).toMinutes());
        if (courseLearn.getWriteTime() <= 0) {
            courseLearn.setWriteTime(1);
            courseLearn.setDurationTime(1);
        } else {
            courseLearn.setDurationTime(courseLearn.getWriteTime());
        }
        courseLearn.setCompletionStatus(2);
        courseLearn.setUpdateTime(new Date());
        reentrantReadWriteLock.writeLock().lock();
        try {
            int result = courseLearnDao.updateById(courseLearn);
            if (result > 0) {
                return Result.success("结束专注记录成功");
            } else {
                return Result.error("结束专注记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("结束专注记录报错:" + e.getMessage(), e);
            return Result.error("结束专注记录报错：" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }

    }

    @Override
    public Result startNap(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        courseLearn.setType(3);
        courseLearn.setLearnDate(LocalDate.now());
        courseLearn.setLearnTime(LocalTime.from(LocalDateTime.now()));
        courseLearn.setCreateTime(LocalDateTime.now());
        courseLearn.setCompletionStatus(1);
        reentrantReadWriteLock.writeLock().lock();
        try {
            int result = courseLearnDao.insert(courseLearn);
            if (result > 0) {
                return Result.success("开始小憩成功").put("data", courseLearn.getId());
            } else {
                return Result.error("开始小憩失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("开始小憩报错:" + e.getMessage(), e);
            return Result.error("开始小憩报错：" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }

    }

    @Override
    public Result endNap(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        CourseLearn courseLearn1 = courseLearnDao.selectById(courseLearn.getId());
        if (ObjectUtils.isEmpty(courseLearn1)) {
            return Result.error("小憩记录不存在");
        }
        // 设置学习时长，转换为秒
        courseLearn.setWriteTime((int) Duration.between(courseLearn1.getCreateTime(), LocalDateTime.now()).toMinutes());
        if (courseLearn.getWriteTime() <= 0) {
            courseLearn.setWriteTime(1);
            courseLearn.setDurationTime(1);
        } else {
            courseLearn.setDurationTime(courseLearn.getWriteTime());
        }
        courseLearn.setCompletionStatus(2);
        courseLearn.setUpdateTime(new Date());
        reentrantReadWriteLock.writeLock().lock();
        try {
            int result = courseLearnDao.updateById(courseLearn);
            if (result > 0) {
                return Result.success("结束小憩成功");
            } else {
                return Result.error("结束小憩失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("结束小憩报错:" + e.getMessage(), e);
            return Result.error("结束小憩报错：" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }

    }

    @Override
    public Result startBreathe(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        courseLearn.setType(4);
        courseLearn.setLearnDate(LocalDate.now());
        courseLearn.setLearnTime(LocalTime.from(LocalDateTime.now()));
        courseLearn.setCreateTime(LocalDateTime.now());
        courseLearn.setCompletionStatus(1);
        reentrantReadWriteLock.writeLock().lock();
        try {
            int result = courseLearnDao.insert(courseLearn);
            if (result > 0) {
                return Result.success("添加呼吸练习成功").put("data", courseLearn.getId());
            } else {
                return Result.error("添加呼吸练习失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加呼吸练习报错:" + e.getMessage(), e);
            return Result.error("添加呼吸练习报错：" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Result endBreathe(CourseLearn courseLearn) {
        UserEntity user = userDao.selectById(courseLearn.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        CourseLearn courseLearn1 = courseLearnDao.selectById(courseLearn.getId());
        if (ObjectUtils.isEmpty(courseLearn1)) {
            return Result.error("呼吸记录不存在");
        }
        // 设置学习时长，转换为秒
        courseLearn.setWriteTime((int) Duration.between(courseLearn1.getCreateTime(), LocalDateTime.now()).toMinutes());
        if (courseLearn.getWriteTime() <= 0) {
            courseLearn.setWriteTime(1);
            courseLearn.setDurationTime(1);
        } else {
            courseLearn.setDurationTime(courseLearn.getWriteTime());
        }
        courseLearn.setCompletionStatus(2);
        courseLearn.setUpdateTime(new Date());
        reentrantReadWriteLock.writeLock().lock();
        try {
            int result = courseLearnDao.updateById(courseLearn);
            if (result > 0) {
                return Result.success("结束呼吸练习成功");
            } else {
                return Result.error("结束呼吸练习失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("结束呼吸练习报错:" + e.getMessage(), e);
            return Result.error("结束呼吸练习报错：" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

}
