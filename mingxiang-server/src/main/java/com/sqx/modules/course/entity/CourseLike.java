package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 我的喜欢（课程）表
 * @TableName course_like
 */
@Data
public class CourseLike implements Serializable {
    /**
     * 
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 课程id
     */
    @ApiModelProperty("课程id")
    private Long courseId;

    /**
     * 是否删除：0未删除  1已删除
     */
    @ApiModelProperty("是否删除：0未删除  1已删除")
    private Integer deleteFlag;

    private static final long serialVersionUID = 1L;
}