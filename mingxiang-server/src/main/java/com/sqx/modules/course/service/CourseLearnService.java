package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseLearn;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/21 17:19
 */
public interface CourseLearnService extends IService<CourseLearn> {

    /**
     * app用户查询自己最近播放的课程
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    Result selectMyPlayed(Integer pageNum,Integer pageSize,Long userId);

    /**
     * app用户点冥想练习开始
     * @param courseLearn
     * @return
     */
    Result startMeditation(CourseLearn courseLearn);

    /**
     * app用户点冥想练习结束
     * @param courseLearn
     * @return
     */
    Result endMeditation(CourseLearn courseLearn);


    /**
     * app用户点开始添加夜间睡眠闹钟
     * @param courseLearn
     * @return
     */
    Result startAlarm(CourseLearn courseLearn);

    /**
     * app用户点结束夜间睡眠闹钟并计算出时长
     * @param courseLearn
     * @return
     */
    Result endAlarm(CourseLearn courseLearn);

    /**
     * app用户点开始添加专注
     * @param courseLearn
     * @return
     */
    Result startConcentrate(CourseLearn courseLearn);

    /**
     * app用户点结束专注
     * @param courseLearn
     * @return
     */
    Result endConcentrate(CourseLearn courseLearn);

    /**
     * app用户点开始添加小憩
     * @param courseLearn
     * @return
     */
    Result startNap(CourseLearn courseLearn);

    /**
     * app用户点结束小憩
     * @param courseLearn
     * @return
     */
    Result endNap(CourseLearn courseLearn);

    /**
     * app用户点开始添加呼吸练习
     * @return
     */
    Result startBreathe(CourseLearn courseLearn);

    /**
     * app用户点结束呼吸练习
     * @return
     */
    Result endBreathe(CourseLearn courseLearn);
}
