package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程类型
 * @TableName course_type
 */
@Data
public class CourseType implements Serializable {
    /**
     * 
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 类型名称
     */
    @ApiModelProperty("类型名称")
    private String typeName;

    /**
     * 是否启用 0启用  1未启用
     */
    @ApiModelProperty("是否启用 0启用  1未启用")
    private Integer typeStatus;

    /**
     * 类型描述
     */
    @ApiModelProperty("类型描述")
    private String remark;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    private Long parentId;

    /**
     * 级别： 1 一级 2 二级 3 三级
     */
    private Integer level;

    /**
     * 是否删除 0未删除 1已删除
     */
    @ApiModelProperty("是否删除 0未删除 1已删除")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 封面图
     */
    @ApiModelProperty("封面图")
    private String coverImage;


    /**
     * 课程列表
     */
    @TableField(exist = false)
    private List<CourseInfo> courseInfoList;

    /**
     * 子类型
     */
    @TableField(exist = false)
    List<CourseType> children;

    /**
     * 章节数量
     */
    @TableField(exist = false)
    private String chapterNum;

    /**
     * 冥想学习时间
     */
    @TableField(exist = false)
    @ApiModelProperty("冥想学习时间")
    private String meditationTime;

    /**
     * 课程id
     */
    @TableField(exist = false)
    @ApiModelProperty("课程id")
    private Long courseId;

    private static final long serialVersionUID = 1L;
}