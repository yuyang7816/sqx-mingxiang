package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseLabel;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/20 16:15
 */
public interface CourseLabelService extends IService<CourseLabel>{

    /**
     * 管理后台查看所以的标签信息
     * @param pageNum
     * @param pageSize
     * @param labelName
     * @return
     */
    Result selectByCourseLabelList(Integer pageNum, Integer pageSize,String labelName);

    /**
     * 管理后台添加标签信息
     * @param courseLabel
     * @return
     */
    Result saveInfo(CourseLabel courseLabel);

    /**
     * 管理后台修改标签信息
     * @param courseLabel
     * @return
     */
    Result edit(CourseLabel courseLabel);

    /**
     * 管理后台删除标签信息
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 管理后台查询标签详情
     * @param id
     * @return
     */
    Result selectDetail(Long id);

}
