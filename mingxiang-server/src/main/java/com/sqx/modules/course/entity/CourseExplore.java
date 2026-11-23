package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 探索页相关
 * @TableName course_explore
 */
@TableName(value ="course_explore")
@Data
public class CourseExplore implements Serializable {
    /**
     * 探索页id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "探索页id")
    private Long id;

    /**
     * 探索页名称
     */
    @ApiModelProperty(value = "探索页名称")
    private String exploreName;

    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private Long parentId;

    /**
     * 启用：1是 2否
     */
    @ApiModelProperty(value = "启用：1是 2否")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 子集集合
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "子集集合")
    private List<CourseExplore> children;

    private static final long serialVersionUID = 1L;
}