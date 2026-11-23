package com.sqx.modules.scTableData.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sqx.modules.utils.typeHandler.DataJsonListTypeHandler;
import com.sqx.modules.utils.typeHandler.ListStringTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DataJsonVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 字段名
     */
    private String columnName;
    /**
     * 字段描述
     */
    private String columnComment;
    /**
     * 字段值
     */
    private String columnValue;
    /**
     * 类型
     */
    private Integer htmlType;

    /**
     * 是否必填
     */
    private Integer isRequired;
    @TableField(typeHandler = ListStringTypeHandler.class)
    private List<String> optionJson;

}
