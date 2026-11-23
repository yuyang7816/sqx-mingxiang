package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.course.entity.vo.ChapterInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程信息表
 * @TableName course_info
 */
@Data
public class CourseInfo implements Serializable {
    /**
     *
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程名称
     */
    @ApiModelProperty("课程名称")
    private String courseName;

    /**
     * 课程类型id
     */
    @ApiModelProperty("课程类型id")
    private Long typeId;

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
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createBy;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
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
     * 是否删除  0未删除 1已删除
     */
    @ApiModelProperty("是否删除  0未删除 1已删除")
    private Integer deleteFlag;

    /**
     * 探索页二级id
     */
    @ApiModelProperty("探索页二级id")
    private Long exploreId;

    /**
     * 探索页二级名称
     */
    @ApiModelProperty("探索页二级名称")
    @TableField(exist = false)
    private String exploreName;

    /**
     * 探索页父id
     */
    @ApiModelProperty("探索页父id")
    private Long exploreParentId;

    /**
     * 探索页父名称
     */
    @ApiModelProperty("探索页父名称")
    @TableField(exist = false)
    private String exploreParentName;

    /**
     * 类型名称
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 类型副标题
     */
    @TableField(exist = false)
    private String typeRemark;

    /**
     * 标签名称
     */
    @TableField(exist = false)
    private String labelName;

    /**
     * 标签id集合
     */
    @TableField(exist = false)
    private String labelIds;
    /**
     * 标签名称的列表
     */
    @TableField(exist = false)
    private List<String> labelNames;

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
     * 是否喜欢 0未喜欢 1已喜欢
     */
    @TableField(exist = false)
    @ApiModelProperty("是否喜欢 0未喜欢 1已喜欢")
    private Integer isLike;

    /**
     * 多少人正在练习
     */
    @TableField(exist = false)
    @ApiModelProperty("多少人正在练习")
    private Integer countPractice;


    private static final long serialVersionUID = 1L;


}