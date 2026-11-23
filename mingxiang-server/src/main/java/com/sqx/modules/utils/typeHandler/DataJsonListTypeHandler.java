package com.sqx.modules.utils.typeHandler;

import com.alibaba.fastjson.TypeReference;
import com.sqx.modules.scTableData.vo.DataJsonVo;

import java.util.List;

/**
 *mysql存取时<List<DataJsonVo>自动转换。无需转义
 */
public class DataJsonListTypeHandler  extends ListTypeHandler<DataJsonVo> {

    @Override
    protected TypeReference<List<DataJsonVo>> specificType() {
        return new TypeReference<List<DataJsonVo>>() {
        };
    }
}
