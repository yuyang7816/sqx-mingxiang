package com.sqx.modules.scTableColumn.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ColumnData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 列名称
     */
    private String columnName;
    /**
     * 列描述
     */
    private String columnComment;
    /**
     *
     */
    private Integer columnType;
    /**
     * 列长度
     */
    private Integer columnLength;
    /**
     * 是否必填（0否 1是）
     */
    private Integer isRequired;
    /**
     * 显示类型（1文本框、2文本域、3下拉框、4复选框、5单选框、6日期控件、7数字类型）
     */
    private Integer htmlType;
    /**
     * 选项值
     */
    private List<String> optionJson;
    /**
     *
     */
    private Integer queryType;
    /**
     * 展示类型 1仅用户端展示 2仅管理端展示 3都展示
     */
    private Integer showType;
    /**
     * 排序
     */
    private Integer sort;
}
