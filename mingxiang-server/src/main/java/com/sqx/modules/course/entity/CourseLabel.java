package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程标签表
 * @TableName course_label
 */
@Data
public class CourseLabel implements Serializable {
    /**
     * 
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String labelName;

    /**
     * 标签等级
     */
    @ApiModelProperty("标签等级")
    private Integer level;

    /**
     * 是否删除 0未删除  1已删除
     */
    @ApiModelProperty("是否删除 0未删除  1已删除")
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
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateBy;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}