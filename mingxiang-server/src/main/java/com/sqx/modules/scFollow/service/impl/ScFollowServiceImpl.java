package com.sqx.modules.scFollow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.scFollow.dao.ScFollowDao;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.service.ScFollowService;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.shop.entity.ScShop;
import com.sqx.modules.shop.service.ScShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class ScFollowServiceImpl extends ServiceImpl<ScFollowDao, ScFollow> implements ScFollowService {
    @Autowired
    private ScFollowDao scFollowDao;
    @Autowired
    private ScTableDataService tableDataService;
    @Autowired
    private ScShopService scShopService;


    @Override
    public IPage<ScFollow> getMyFollowList(Integer page, Integer limit,ScFollow follow) {
        Page<ScFollow> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        IPage<ScFollow> followIPage = baseMapper.selectPage(pages, new QueryWrapper<>(follow).orderByDesc("create_time"));
        for (ScFollow record : followIPage.getRecords()) {
            if (record.getType()==1){
                record.setScTableData(tableDataService.getById(record.getDataId()));
            }else {
                record.setScShop(scShopService.getById(record.getShopId()));
            }

        }

        return followIPage;

    }

    @Override
    public Result updateFollow(ScFollow follow) {
        if (follow.getType() == 1) {
            ScTableData tableData = tableDataService.getOne(new QueryWrapper<ScTableData>().eq("data_id", follow.getDataId()));
            if (tableData == null) {
                return Result.error("当前服务不存在,请重试!");
            } else {
                ScFollow scFollow = scFollowDao.selectOne(new QueryWrapper<ScFollow>().eq("user_id", follow.getUserId()).eq("data_id", follow.getDataId()));
                if (scFollow != null) {
                    scFollowDao.deleteById(scFollow.getFollowId());
                    return Result.success("取消收藏成功");
                } else {
                    follow.setCreateTime(new Date());
                    scFollowDao.insert(follow);
                    return Result.success("收藏成功");
                }
            }
        } else {
            ScShop scShop = scShopService.getById(follow.getShopId());
            if (scShop == null) {
                return Result.error("当前店铺不存在,请重试!");
            } else {
                ScFollow scFollow = scFollowDao.selectOne(new QueryWrapper<ScFollow>().eq("user_id", follow.getUserId()).eq("shop_id", follow.getShopId()));
                if (scFollow != null) {
                    scFollowDao.deleteById(scFollow.getFollowId());
                    return Result.success("取消关注成功");
                } else {
                    follow.setCreateTime(new Date());
                    scFollowDao.insert(follow);
                    return Result.success("关注成功");
                }
            }
        }

    }

    @Override
    public IPage<UserEntity> getMyFollowList(Long userId, Integer page, Integer limit) {
        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return baseMapper.getMyFollowList(pages,userId);

    }

    @Override
    public IPage<UserEntity> getMyFansList(Long userId, Integer page, Integer limit) {
        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return baseMapper.getMyFansList(pages,userId);
    }

    @Override
    public Integer getMyFansListCount(Long userId) {
        return baseMapper.getMyFansListCount(userId);
    }
}
