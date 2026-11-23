package com.sqx.modules.shop.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.vo.ScTableDataVo;
import lombok.Data;

@Data
public class UserEntityVo extends UserEntity {
    private IPage<ScTableDataVo> scTableDataVo;
}
