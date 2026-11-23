package com.sqx.modules.scFollow.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.scFollow.entity.ScFollow;

/**
 * @autorh liyuan
 * @description sc_follow
 * @date 2021-08-12
 */
public interface ScFollowService extends IService<ScFollow> {

    IPage<ScFollow> getMyFollowList(Integer page, Integer limit, ScFollow follow);

    Result updateFollow(ScFollow follow);

    IPage<UserEntity> getMyFollowList(Long userId, Integer page, Integer limit);

    IPage<UserEntity> getMyFansList(Long userId, Integer page, Integer limit);

    Integer getMyFansListCount(Long userId);
}
