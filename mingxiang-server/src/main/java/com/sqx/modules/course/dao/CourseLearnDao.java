package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.CourseInfo;
import com.sqx.modules.course.entity.CourseLearn;
import com.sqx.modules.course.entity.vo.CourseInfoVO;
import com.sqx.modules.course.entity.vo.CourseLearnCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【course_learn(冥想练习表)】的数据库操作Mapper
 * @createDate 2024-06-21 17:11:26
 * @Entity generator.domain.CourseLearn
 */
@Mapper
public interface CourseLearnDao extends BaseMapper<CourseLearn> {


    CourseLearnCount selectByMeditationMonth(@Param("userId") Long userId);

    CourseLearnCount selectBySleepMonth(@Param("userId") Long userId);

    CourseLearnCount selectByFocusMonth(@Param("userId") Long userId);

    CourseLearn selectByUserId(@Param("userId") Long userId,
                               @Param("currentDate") String currentDate);

    CourseLearn selectSleepByUserId(@Param("userId") Long userId,
                                    @Param("currentDate") String currentDate);

    CourseLearn selectFocusByUserId(@Param("userId") Long userId,
                                    @Param("currentDate") String currentDate);

    CourseLearnCount selectByAllMeditationMonth(@Param("userId") Long userId);

    CourseLearnCount selectByAllSleepMonth(@Param("userId") Long userId);

    CourseLearnCount selectByAllFocusMonth(@Param("userId") Long userId);

    CourseLearnCount selectByMeditationCount(@Param("userId") Long userId,
                                             @Param("currentDate") String currentDate,
                                             @Param("type") Integer type);

    CourseLearnCount selectBySleepCount(@Param("userId") Long userId,
                                        @Param("currentDate") String currentDate,
                                        @Param("type") Integer type);

    CourseLearnCount selectByFocusCount(@Param("userId") Long userId,
                                        @Param("currentDate") String currentDate,
                                        @Param("type") Integer type);

    CourseLearnCount selectByMeditationCount1(@Param("currentDate") String currentDate,
                                              @Param("type") Integer type);

    CourseLearnCount selectBySleepCount1(@Param("currentDate") String currentDate,
                                         @Param("type") Integer type);

    CourseLearnCount selectByFocusCount1(@Param("currentDate") String currentDate,
                                         @Param("type") Integer type);

    int selectByMeditationCount2(@Param("currentDate") String currentDate,
                                 @Param("type") Integer type);

    int selectBySleepCount2(@Param("currentDate") String currentDate,
                            @Param("type") Integer type);

    int selectByFocusCount2(@Param("currentDate") String currentDate,
                            @Param("type") Integer type);
}




