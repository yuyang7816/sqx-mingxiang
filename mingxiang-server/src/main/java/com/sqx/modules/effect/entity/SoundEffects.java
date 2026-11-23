package com.sqx.modules.effect.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 音效表
 * @TableName sound_effects
 */
@Data
public class SoundEffects implements Serializable {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 音效名称
     */
    @TableField(condition = SqlCondition.LIKE)
    @ApiModelProperty("音效名称")
    private String effectsName;

    /**
     * 封面
     */
    @ApiModelProperty("封面")
    private String cover;

    /**
     * 背景图片
     */
    @ApiModelProperty("背景图片")
    private String bgImg;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 音效路径
     */
    @ApiModelProperty("音效路径")
    private String filePath;

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
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 是否删除 0未删除 1已删除
     */
    @ApiModelProperty("是否删除 0未删除 1已删除")
    private Integer deleteFlag;

    /**
     * 是否选中：0未选  1选中
     */
    @TableField(exist = false)
    private Integer checked;

    /**
     * 混音场景id
     */
    @TableField(exist = false)
    private List<Long> effectsIds;

    private static final long serialVersionUID = 1L;
}