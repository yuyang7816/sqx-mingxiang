package com.sqx.modules.languageType.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * 语种
 *
 * @author wuChang
 * @since 2023-12-13
 */
@Data
@TableName("language_type")
public class LanguageType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 语言id
     */
    @ApiModelProperty("语言id")
    @TableId(value = "language_id", type = IdType.AUTO)
    private Long languageId;

    /**
     * 语种
     */
    @ApiModelProperty("语种")
    private String language;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 是否开启 0否 1是
     */
    @ApiModelProperty("是否开启 0否 1是")
    private Integer isEnable;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
