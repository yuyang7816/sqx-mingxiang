package com.sqx.modules.scTableColumn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.utils.typeHandler.ListStringTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(autoResultMap = true) //是否构建自定义typeHandler,否则List<Object>无法自动转为字符串
public class ScTableColumn implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long columnId;

    /** 分类id */
    @ApiModelProperty(name = "分类id")
    private Long classId;

    /** 列名称 */
    @ApiModelProperty(name = "列名称")
    private String columnName;

    /** 列描述 */
    @ApiModelProperty(name = "列描述")
    private String columnComment;

    /** 列长度 */
    @ApiModelProperty(name = "列长度")
    private Integer columnLength;

    /** 是否必填（1是） */
    @ApiModelProperty(name = "是否必填")
    private Integer isRequired;

    /** 显示类型（1文本框、2文本域、3下拉框、4复选框、5单选框、6日期控件） */
    @ApiModelProperty(name = "显示类型")
    private Integer htmlType;

    /** 选项值 */
    @ApiModelProperty(name = "选项值")
    @TableField(typeHandler = ListStringTypeHandler.class)
    private List<String> optionJson;

    /** 展示类型 1仅用户端展示 2仅管理端展示 3都展示 */
    @ApiModelProperty(name = "展示类型 1仅用户端展示 2仅管理端展示 3都展示")
    private Integer showType;

    /** 排序 */
    @ApiModelProperty(name = "排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "更新时间")
    private Date updateTime;
}
