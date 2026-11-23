package com.sqx.modules.configI18n.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * 国际化配置表
 *
 * @author wuChang
 * @since 2023-11-09
 */
@Data
@TableName("config_i18n")
public class ConfigI18n implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 语言关键词
     */
    @ApiModelProperty("语言关键词")
    private String primaryKey;

    /**
     * 语言
     */
    @TableField(condition = SqlCondition.LIKE)
    @ApiModelProperty("语言")
    private String language;

    /**
     * 语言值
     */
    @TableField(condition = SqlCondition.LIKE)
    @ApiModelProperty("语言值")
    private String languageValue;
    /**
     * 备注
     */
    @TableField(condition = SqlCondition.LIKE)
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 使用类型 1服务端 2用户端
     */
    @ApiModelProperty("使用类型 1服务端 2用户端")
    private Integer showType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
