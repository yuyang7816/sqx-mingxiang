package com.sqx.modules.course.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程信息表之查询导师信息
 * @TableName course_info
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MentorVO implements Serializable {
    /**
     *
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 导师
     */
    @ApiModelProperty("导师")
    private String mentor;

    /**
     * 导师头像
     */
    @ApiModelProperty("导师头像")
    private String mentorPhoto;

    /**
     * 导师简介
     */
    @ApiModelProperty("导师简介")
    private String mentorIntroduction;


    private static final long serialVersionUID = 1L;
}