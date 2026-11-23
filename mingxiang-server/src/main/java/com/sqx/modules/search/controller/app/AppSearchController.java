package com.sqx.modules.search.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.App;
import com.sqx.modules.search.entity.Search;
import com.sqx.modules.search.service.AppSearchService;
import com.sqx.modules.search.service.SearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * 搜索记录
 *
 * @author liyuan
 * @since 2021-07-17
 */
@RestController
@RequestMapping("app/Search")
@AllArgsConstructor
@Slf4j
public class AppSearchController {

    private AppSearchService appSearchService;

    /**
     * 查询搜索记录
     */
    @RequestMapping(value = "/selectUserSearchList", method = RequestMethod.GET)
    public Result selectUserSearchList(Long userId, Integer page, Integer limit,Integer type) {
        return Result.success().put("data", appSearchService.selectUserSearchList(userId, page, limit,type));
    }

    /**
     * 删除用户的搜索记录
     */
    @Login
    @RequestMapping(value = "/deleteAppSearch", method = RequestMethod.GET)
    public Result deleteAppSearch(@RequestAttribute Long userId,Integer type) {
        return appSearchService.deleteAppSearch(userId,type);
    }
}
