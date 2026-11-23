package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserCertification;

public interface UserCertificationService extends IService<UserCertification> {
    Result insert(UserCertification userCertification);


    IPage<UserCertification> queryCertification(Integer page, Integer limit, UserCertification userCertification);

    Result queryUserCertification(IPage iPage, String name,String phone);


    Result queryInsert(Long userId);

    Result auditorUserCertification(Integer status, Long id, String remark, Integer isSubmit);
}
