package com.sqx.modules.scTableData.vo;

import com.sqx.modules.scTableData.entity.ScTableData;
import lombok.Data;

@Data
public class ScTableDataVo extends ScTableData {

    private String userName;

    private String className;

    private Integer isFollow;

    private Integer distance;
}
