package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.ChapterInfo;
/**
 * 章节信息(ChapterInfo)表服务接口
 *
 * @author makejava
 * @since 2024-05-16 15:20:27
 */
public interface ChapterInfoService extends IService<ChapterInfo>{

    /**
     * 管理后台查询章节信息
     * @param pageNum
     * @param pageSize
     * @param chapterName
     * @param courseId
     * @return
     */
    Result selectChapterInfoList(Integer pageNum, Integer pageSize,String chapterName,Long courseId);

    /**
     * 管理后台添加章节信息
     * @param chapterInfo
     * @return
     */
    Result saveInfo(ChapterInfo chapterInfo);

    /**
     * 管理后台修改章节信息
     * @param chapterInfo
     * @return
     */
    Result edit(ChapterInfo chapterInfo);

    /**
     * 管理后台删除章节信息
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 管理后台查询章节信息详情
     * @param id
     * @return
     */
    Result selectDetail(Long id);

    /**
     * app用户查看章节信息详情
     * @param id
     * @return
     */
    Result selectDetailByApp(Long id,Long userId);
}
