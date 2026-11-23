package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.ChapterInfo;
import com.sqx.modules.course.entity.vo.ChapterInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【chapter_info(课程的章节信息表)】的数据库操作Mapper
* @createDate 2024-06-20 13:28:58
* @Entity generator.domain.ChapterInfo
*/
@Mapper
public interface ChapterInfoDao extends BaseMapper<ChapterInfo> {

    List<ChapterInfoVO> selectByCourseId(@Param("courseId") Long courseId);

}

