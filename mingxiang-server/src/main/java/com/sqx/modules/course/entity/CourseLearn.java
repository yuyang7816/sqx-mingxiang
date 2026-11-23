package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 冥想练习表
 * @TableName course_learn
 */
@Data
public class CourseLearn implements Serializable {
    /**
     * 
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 课程的章节id
     */
    @ApiModelProperty("章节id")
    private Long chapterId;

    /**
     * 冥想类型id
     */
    @ApiModelProperty("冥想类型id")
    private Long meditationId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 备注或反馈
     */
    @ApiModelProperty("备注或反馈")
    private String remark;

    /**
     * 练习状态：0未开始 1进行中 2已完成
     */
    @ApiModelProperty("练习状态：0未开始 1进行中 2已完成")
    private Integer completionStatus;

    /**
     * 练习日期
     */
    @ApiModelProperty("练习日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate learnDate;

    /**
     * 练习时间
     */
    @ApiModelProperty("练习时间")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime learnTime;

    /**
     * 今日时长
     */
    @ApiModelProperty("今日时长 睡眠（也是专注、小憩、呼吸开始选的时间）")
    private Integer durationToday;

    /**
     * 明日时长
     */
    @ApiModelProperty("明日时长  睡眠")
    private Integer durationTomorrow;

    /**
     * 冥想练习、专注、小憩、呼吸时长
     */
    @ApiModelProperty("冥想练习、专注、小憩、呼吸时长")
    private Integer durationTime;

    /**
     * 睡眠开始时间
     */
    @ApiModelProperty("睡眠开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime alarmStartTime;

    /**
     * 睡眠结束时间
     */
    @ApiModelProperty("睡眠结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime alarmEndTime;

    /**
     * 是否删除：0未删除 1已删除
     */
    @ApiModelProperty("是否删除：0未删除 1已删除")
    private Integer deleteFlag;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateBy;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 小小憩、专注、呼吸时间（结束的时间）
     */
    @ApiModelProperty("小憩、专注、呼吸时间（结束的时间）")
    private Integer writeTime;

    /**
     * 0冥想练习  1睡眠 2专注 3小憩 4正念
     */
    @ApiModelProperty("0冥想练习  1睡眠 2专注 3小憩 4正念")
    private Integer type;

    /**
     * 课程id
     */
    @ApiModelProperty("课程id")
    private Long courseId;

    /**
     * 0冥想练习 1睡眠 2专注 3小憩 4正念 类型统计后的秒数
     */
    @TableField(exist = false)
    private int typeCount;

    private static final long serialVersionUID = 1L;
}