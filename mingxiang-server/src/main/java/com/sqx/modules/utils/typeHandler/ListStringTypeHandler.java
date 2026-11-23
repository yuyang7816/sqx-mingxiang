package com.sqx.modules.utils.typeHandler;


import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * 自定义 Handler类
 * 将List<String>存入字段。
 */
public class ListStringTypeHandler extends ListTypeHandler<String> {

    @Override
    protected TypeReference<List<String>> specificType() {
        return new TypeReference<List<String>>() {
        };
    }
}

