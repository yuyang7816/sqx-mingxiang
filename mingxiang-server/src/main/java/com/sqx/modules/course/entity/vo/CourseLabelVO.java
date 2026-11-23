package com.sqx.modules.course.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程标签表
 * @TableName course_label
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CourseLabelVO implements Serializable {

    /**
     *
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String labelName;

    /**
     * 是否删除 0未删除  1已删除
     */
    @ApiModelProperty("是否删除 0未删除  1已删除")
    private Integer deleteFlag;



    private static final long serialVersionUID = 1L;
}