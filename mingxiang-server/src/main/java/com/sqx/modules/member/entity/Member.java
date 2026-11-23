package com.sqx.modules.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 会员特权
 *
 * @author wuChang
 * @since 2023-12-21
 */
@Data
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员特权id
     */
    @ApiModelProperty("会员特权id")
    @TableId(value = "member_id", type = IdType.AUTO)
    private Long memberId;

    /**
     * 特权图标
     */
    @ApiModelProperty("特权图标")
    private String memberImg;

    /**
     * 特权名称
     */
    @ApiModelProperty("特权名称")
    private String memberName;
   /**
     * 特权描述
     */
    @ApiModelProperty("特权描述")
    private String memberContent;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private String sort;

    /**
     * 语言类型
     */
    @ApiModelProperty("语言类型")
    private String languageType;

    /**
     * 语言名称
     */
    @ApiModelProperty("语言名称")
    private String languageName;

}
