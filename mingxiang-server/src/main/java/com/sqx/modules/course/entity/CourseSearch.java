package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 搜索热门表
 * @TableName course_search
 */
@Data
public class CourseSearch implements Serializable {
    /**
     * 
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 课程id
     */
    @ApiModelProperty("课程id")
    private Long courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    /**
     * 搜索关键词
     */
    @ApiModelProperty("搜索关键词")
    private String courseWord;

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
    private Date createTime;

    /**
     * 搜索统计
     */
    @TableField(exist = false)
    private Integer searchCount;

    private static final long serialVersionUID = 1L;
}