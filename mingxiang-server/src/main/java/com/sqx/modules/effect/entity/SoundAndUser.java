package com.sqx.modules.effect.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 音效和用户中间表
 * @TableName sound_and_user
 */
@Data
public class SoundAndUser implements Serializable {
    /**
     * 
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 音效id
     */
    private Long effectsId;

    /**
     * 是否删除： 0未删除 1已删除
     */
    private Integer deleteFlag;

    private static final long serialVersionUID = 1L;
}