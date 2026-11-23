package com.sqx.modules.scTableColumn.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class JsonRootBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long classId;
    private List<ColumnData> columnData;
    private Long columnId;
    private Date createTime;
    private Date updateTime;
}
