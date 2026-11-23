package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 课程的章节信息表
 * @TableName chapter_info
 */
@Data
public class ChapterInfo implements Serializable {
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

    /**
     * 章节名称
     */
    @ApiModelProperty("章节名称")
    private String chapterName;

    /**
     * 章节序号
     */
    @ApiModelProperty("章节序号")
    private Integer chapterNum;

    /**
     * 章节链接
     */
    @ApiModelProperty("章节链接")
    private String chapterUrl;

    /**
     * 章节时长
     */
    @ApiModelProperty("章节时长")
    private String chapterDuration;

    /**
     * 章节金额
     */
    @ApiModelProperty("章节金额")
    private BigDecimal chapterPrice;

    /**
     * 是否收费：0免费 1收费
     */
    @ApiModelProperty("是否收费：0免费 1收费")
    private Integer chapterCharge;

    /**
     * 章节状态：0正常 1下架
     */
    @ApiModelProperty("章节状态：0正常 1下架")
    private Integer chapterStatus;

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
     * 是否删除：0未删除  1已删除
     */
    @ApiModelProperty("是否删除：0未删除  1已删除")
    private Integer deleteFlag;

    /**
     * 章节图片
     */
    @ApiModelProperty("章节图片")
    private String chapterImg;

    /**
     * 课程章节的章节(例如：第一章、第二章)
     */
    @TableField(exist = false)
    private String chapterSeveral;

    private static final long serialVersionUID = 1L;
}