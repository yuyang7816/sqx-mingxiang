package com.sqx.modules.scTableClass.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.scTableClass.entity.ScTableClass;

import java.util.HashMap;

public interface ScTableClassService extends IService<ScTableClass> {
    Result addClass(ScTableClass scTableClass);

    IPage<ScTableClass> getClassList(Integer page, Integer limit, Integer isEnable,String className);


    Result updateClass(ScTableClass scTableClass);

    Result deleteClass(Long classId);

    HashMap<String,Integer> hasTableOrData(Long classId);
}
