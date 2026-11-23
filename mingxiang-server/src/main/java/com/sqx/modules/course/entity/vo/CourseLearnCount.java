package com.sqx.modules.course.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 冥想练习表统计数据
 * @TableName course_learn
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseLearnCount {

    /**
     * 当月冥想统计
     */
    private Integer meditationMonthCount;

    /**
     * 当月睡眠统计
     */
    private Integer sleepMonthCount;

    /**
     * 当月专注统计
     */
    private Integer focusMonthCount;



    /**
     * 冥想次数统计
     */
    private Integer meditationCount;

    /**
     * 冥想分钟统计
     */
    private Integer meditationTime;

    /**
     * 冥想天数统计
     */
    private Integer meditationDay;

    /**
     * 睡眠次数统计
     */
    private Integer sleepCount;

    /**
     * 睡眠分钟统计
     */
    private Integer sleepTime;

    /**
     * 睡眠天数统计
     */
    private Integer sleepDay;

    /**
     * 专注次数统计
     */
    private Integer focusCount;

    /**
     * 专注分钟统计
     */
    private Integer focusTime;

    /**
     * 专注天数统计
     */
    private Integer focusDay;
}
