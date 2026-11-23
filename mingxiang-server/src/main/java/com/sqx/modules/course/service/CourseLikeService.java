package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseLike;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/24 11:38
 */
public interface CourseLikeService extends IService<CourseLike> {

    /**
     * app用户将课程添加点到我的喜欢列表
     * @param courseLike
     * @return
     */
    Result add(CourseLike courseLike);

    /**
     * app用户将课程从我的喜欢列表中取消
     * @param courseLike
     * @return
     */
    Result delete(CourseLike courseLike);

    /**
     * app用户查看我的喜欢列表
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    Result selectCourseLikeList(Integer pageNum, Integer pageSize, Long userId,Long exploreId,Long exploreParentId);
}
