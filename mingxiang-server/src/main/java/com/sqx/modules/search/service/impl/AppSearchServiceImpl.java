package com.sqx.modules.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.search.dao.AppSearchDao;
import com.sqx.modules.search.entity.Search;
import com.sqx.modules.search.service.AppSearchService;
import com.sqx.modules.search.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@AllArgsConstructor
public class AppSearchServiceImpl extends ServiceImpl<AppSearchDao, Search> implements AppSearchService {
    private AppSearchDao appSearchDao;
    private SearchService searchService;

    /**
     * 经常搜索的名称
     *
     * @return
     */
    @Override
    public IPage<Search> selectUserSearchList(Long userId, Integer page, Integer limit, Integer type) {
        Page<Search> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return appSearchDao.selectPage(pages, new QueryWrapper<Search>().eq("user_id", userId).orderByDesc("update_time").eq("type", type));
    }

    /**
     * 删除用户的搜索记录
     *
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Result deleteAppSearch(Long userId, Integer type) {
        appSearchDao.deleteAppSearch(userId,type);
        return Result.success();
    }

    /**
     * 添加搜索记录
     * @param userId
     * @param searchName
     * @return
     */
    @Override
    public void insertSearch(Long userId,String searchName,Integer type){
        Search searchInfo = baseMapper.selectOne(new QueryWrapper<Search>().eq("user_id", userId).eq("search_name", searchName));
        if (searchInfo == null) {
            Search search = new Search();
            search.setUserId(userId);
            search.setSearchName(searchName);
            search.setType(type);
            search.setUpdateTime(new Date());
            searchService.save(search);
        } else {
            searchInfo.setUpdateTime(new Date());
            searchService.updateById(searchInfo);
        }
    }
}
