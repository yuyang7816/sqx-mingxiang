package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.MeditationType;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/21 16:11
 */
public interface MeditationTypeService extends IService<MeditationType> {

    /**
     * 管理后台查询冥想类型列表
     * @param pageNum
     * @param pageSize
     * @param meditationName
     * @return
     */
    Result selectMeditationTypeList(Integer pageNum, Integer pageSize, String meditationName);

    /**
     * 管理后台添加冥想类型
     * @param meditationType
     * @return
     */
    Result saveInfo(MeditationType meditationType);

    /**
     * 管理后台修改冥想类型
     * @param meditationType
     * @return
     */
    Result edit(MeditationType meditationType);

    /**
     * 管理后台删除冥想类型
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 管理后台查看冥想类型详情
     * @param id
     * @return
     */
    Result selectDetail(Long id);
}
