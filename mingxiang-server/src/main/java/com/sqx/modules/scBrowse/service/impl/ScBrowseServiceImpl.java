package com.sqx.modules.scBrowse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.scBrowse.dao.ScBrowseDao;
import com.sqx.modules.scBrowse.entity.ScBrowse;
import com.sqx.modules.scBrowse.service.ScBrowseService;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.service.ScFollowService;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.shop.service.ScShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScBrowseServiceImpl extends ServiceImpl<ScBrowseDao, ScBrowse> implements ScBrowseService {
    @Autowired
    private ScBrowseDao browseDao;
    @Autowired
    private ScTableDataService tableDataService;
    @Autowired
    private ScShopService scShopService;
    @Autowired
    private ScFollowService scFollowService;

    @Autowired
    private ScBrowseService scBrowseService;
    @Override
    public IPage<ScBrowse> getMyBrowse(Long userId, Integer page, Integer limit, Integer type) {
        QueryWrapper<ScBrowse> wrapper = new QueryWrapper<ScBrowse>().eq("user_id", userId);
        Page<ScBrowse> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        if (type != null) {
            wrapper.eq("type", type);
        }
        wrapper.orderByDesc("update_time");
        IPage<ScBrowse> myBrowse = browseDao.selectPage(pages, wrapper);
        for (ScBrowse scBrowse : myBrowse.getRecords()) {
            if (scBrowse.getType() == 1) {
                scBrowse.setScTableData(tableDataService.getOne(new QueryWrapper<ScTableData>().eq("data_id", scBrowse.getDataId())));
            } else {
                scBrowse.setScShop(scShopService.getById(scBrowse.getShopId()));
            }
        }
        return myBrowse;
    }

    @Override
    public Result selectAmount(Long userId) {

        Map<String, Object> map = new HashMap<>();
        //服务收藏
        Integer myScFollowCount = scFollowService.count(new QueryWrapper<ScFollow>().eq("user_id", userId).eq("type", 1));
        //店铺关注
        Integer myUserFollowCount = scFollowService.count(new QueryWrapper<ScFollow>().eq("user_id", userId).eq("type", 2));
        //店铺粉丝
        Integer followCount = scFollowService.getMyFansListCount(userId);
        //历史浏览
        Integer browseCount = scBrowseService.getBaseMapper().selectCount(new QueryWrapper<ScBrowse>().eq("user_id", userId).eq("type", 1));

        map.put("browseCount", browseCount);
        map.put("followCount", followCount);
        map.put("myScFollowCount", myScFollowCount);
        map.put("myUserFollowCount", myUserFollowCount);
        return Result.success().put("data", map);


    }


}
