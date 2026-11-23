package com.sqx.modules.search.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.search.entity.Search;

/**
 * app搜索
 */
public interface AppSearchService {


    IPage<Search> selectUserSearchList(Long userId, Integer page, Integer limit, Integer type);

    Result deleteAppSearch(Long userId, Integer type);

    void insertSearch(Long userId,String searchName,Integer type);
}
