package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程和标签关联表
 * @TableName course_and_label
 */
@Data
public class CourseAndLabel implements Serializable {
    /**
     * 
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 课程标签表id
     */
    @ApiModelProperty("课程标签表id")
    private Long labelId;

    /**
     * 课程id
     */
    @ApiModelProperty("课程id")
    private Long courseId;

    /**
     * 是否删除  0未删除  1已删除
     */
    @ApiModelProperty("是否删除  0未删除  1已删除")
    private Integer deleteFlag;

    private static final long serialVersionUID = 1L;
}