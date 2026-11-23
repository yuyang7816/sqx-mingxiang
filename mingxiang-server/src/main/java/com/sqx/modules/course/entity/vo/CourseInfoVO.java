package com.sqx.modules.course.entity.vo;

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
 * 课程信息表
 * @TableName course_info
 */
@Data
public class CourseInfoVO implements Serializable {
//    /**
//     *
//     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程名称
     */
    @ApiModelProperty("课程名称")
    private String courseName;


    /**
     * 课程简介
     */
    @ApiModelProperty("课程简介")
    private String courseDesc;

    /**
     * 课程状态：0正常 1下架
     */
    @ApiModelProperty("课程状态：0正常 1下架")
    private Integer courseStatus;

    /**
     * 课程标题
     */
    @ApiModelProperty("课程标题")
    private String courseTitle;

    /**
     * 导师标题
     */
    @ApiModelProperty("导师标题")
    private String mentorTitle;

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

    /**
     * 封面
     */
    @ApiModelProperty("封面")
    private String cover;

    /**
     * 课程备注
     */
    @ApiModelProperty("课程备注")
    private String remark;

    /**
     * 背景图片
     */
    @ApiModelProperty("背景图片")
    private String backgroundImage;

    /**
     * 课程类型id
     */
    @ApiModelProperty("课程类型id")
    private Long typeId;

//    /**
//     * 创建人
//     */
//    @ApiModelProperty("创建人")
//    private Long createBy;
//
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
//
//    /**
//     * 修改人
//     */
//    @ApiModelProperty("修改人")
//    private Long updateBy;
//
//    /**
//     * 修改时间
//     */
//    @ApiModelProperty("修改时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date updateTime;

    /**
     * 类型名称
     */
    private String typeName;

//    /**
//     * 标签名称
//     */
//    private String labelName;

    /**
     * 章节数量
     */
    @TableField(exist = false)
    private String countChapter;

    /**
     * 章节信息
     */
    @TableField(exist = false)
    @ApiModelProperty("章节信息")
    private List<ChapterInfoVO> chapterInfoList;

    /**
     * 标签名称
     */
    @TableField(exist = false)
    @ApiModelProperty("标签名称")
    public List<String> labelNames;
    private Long userId;

    private static final long serialVersionUID = 1L;


}