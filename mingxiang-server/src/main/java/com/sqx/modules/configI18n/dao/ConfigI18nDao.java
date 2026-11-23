package com.sqx.modules.configI18n.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.configI18n.entity.ConfigI18n;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 国际化配置表 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-11-09
 */
@Mapper
public interface ConfigI18nDao extends BaseMapper<ConfigI18n> {

    List<HashMap<String,String>> getLanguageList(@Param("showType") Integer showType);

    IPage<ConfigI18n> getConfigI18nGroupList(@Param("pages") Page<ConfigI18n> pages, @Param("configI18n") ConfigI18n configI18n);

    List<ConfigI18n> needTranslateList(@Param("language") String language, @Param("toLanguage") String toLanguage, @Param("showType") Integer showType);
}
