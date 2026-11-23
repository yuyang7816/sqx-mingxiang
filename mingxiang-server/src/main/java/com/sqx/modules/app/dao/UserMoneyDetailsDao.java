package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserMoneyDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMoneyDetailsDao extends BaseMapper<UserMoneyDetails> {
    Double monthIncome(@Param("date") String date, @Param("userId") Long userId);

    Double selectMyProfit(Long userId);

    BigDecimal getOneProfit(@Param("userId") Long userId, @Param("level") Integer level);

    IPage<HashMap<String, Object>> getProfitList(@Param("pages") Page<HashMap<String, Object>> pages,
                                                 @Param("userId") Long userId,
                                                 @Param("type") Integer type);

    HashMap<String, Object> getProfitList1(@Param("userId") Long userId,
                                          @Param("type") Integer type);

    IPage<HashMap<String, Object>> getLevelIncome(@Param("pages") Page<HashMap<String, Object>> pages,
                                                  @Param("userId") Long userId,
                                                  @Param("type") Integer type);
}
