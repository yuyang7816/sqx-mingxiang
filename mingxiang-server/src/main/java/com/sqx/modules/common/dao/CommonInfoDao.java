package com.sqx.modules.common.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.common.entity.CommonInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/8
 */
@Mapper
public interface CommonInfoDao extends BaseMapper<CommonInfo> {

    List<CommonInfo> findByCondition(@Param("condition") String condition);

    CommonInfo findOne(@Param("type") int type);


}