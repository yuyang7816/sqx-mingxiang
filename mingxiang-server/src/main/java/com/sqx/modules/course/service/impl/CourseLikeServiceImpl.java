package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.course.dao.ChapterInfoDao;
import com.sqx.modules.course.dao.CourseInfoDao;
import com.sqx.modules.course.dao.CourseLikeDao;
import com.sqx.modules.course.dao.CourseTypeDao;
import com.sqx.modules.course.entity.CourseInfo;
import com.sqx.modules.course.entity.CourseLike;
import com.sqx.modules.course.entity.CourseType;
import com.sqx.modules.course.entity.vo.ChapterInfoVO;
import com.sqx.modules.course.service.CourseLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/24 11:37
 */
@Service
public class CourseLikeServiceImpl extends ServiceImpl<CourseLikeDao, CourseLike> implements CourseLikeService {

    @Autowired
    private CourseLikeDao courseLikeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseTypeDao courseTypeDao;

    @Autowired
    private CourseInfoDao courseInfoDao;

    @Autowired
    private ChapterInfoDao chapterInfoDao;

    @Override
    public Result add(CourseLike courseLike) {
        UserEntity user = userDao.selectById(courseLike.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        int count = courseLikeDao.selectCount(new QueryWrapper<CourseLike>()
                .eq("user_id", courseLike.getUserId())
                .eq("course_id", courseLike.getCourseId())
                .eq("delete_flag", 0));
        if (count > 0) {
            return Result.error("该课程已添加到我的喜欢,请勿重复添加");
        }
        if (courseLikeDao.insert(courseLike) > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result delete(CourseLike courseLike) {
        UserEntity user = userDao.selectById(courseLike.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
//        courseLike.setDeleteFlag(1);
        if (courseLikeDao.deleteByCourseId(courseLike) > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result selectCourseLikeList(Integer pageNum, Integer pageSize, Long userId, Long exploreId, Long exploreParentId) {
        UserEntity user = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        List<CourseInfo> courseInfoList = new ArrayList<>();
        Set<Long> likedCourseIds = new HashSet<>(getLikedCourseIds(userId));
        Set<Long> allTypeIds = new HashSet<>();
        // 查询所有课程类型并构建树形结构
        PageHelper.startPage(pageNum, pageSize);
//        List<CourseInfo> courses;
        List<CourseType> children = new ArrayList<>();
        if (likedCourseIds.isEmpty()){
            return Result.success().put("data", new PageInfo<>(children));
        }
        if (exploreId != null) {
            courseInfoList = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                    .in("id", likedCourseIds)
                    .eq("explore_id", exploreId)
                    .eq("delete_flag", 0));
            if (!ObjectUtils.isEmpty(courseInfoList)) {
                for (CourseInfo courseInfo : courseInfoList) {
                    CourseType courseType = courseTypeDao.selectById(courseInfo.getTypeId());
                    courseInfo.setTypeName(courseType.getTypeName());
                    courseInfo.setTypeRemark(courseType.getRemark());
                    allTypeIds.add(courseInfo.getTypeId());
                }
                if (allTypeIds.isEmpty()) {
                    return Result.success().put("data", new PageInfo<>(children));
                }
                children = courseTypeDao.selectList(new QueryWrapper<CourseType>()
                        .in("id", allTypeIds)
                        .eq("delete_flag", 0));
                if (!ObjectUtils.isEmpty(children)) {
                    for (CourseType courseType1 : children) {
                        List<CourseInfo> courseInfoList1 = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                                .eq("type_id", courseType1.getId()));
                        if (!ObjectUtils.isEmpty(courseInfoList1)) {
                            for (CourseInfo courseInfo : courseInfoList1) {
                                List<ChapterInfoVO> chapterInfoList = chapterInfoDao.selectByCourseId(courseInfo.getId());
                                courseType1.setChapterNum(chapterInfoList.size() + "章");
                                courseType1.setCourseInfoList(courseInfoList1);
                            }
                        }
                    }
                }
                return Result.success().put("data", new PageInfo<>(courseInfoList));
            } else {
                return Result.success().put("data", new PageInfo<>(courseInfoList));
            }
        } else if (exploreParentId != null) {
            courseInfoList = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                    .in("id", likedCourseIds)
                    .eq("explore_parent_id", exploreParentId)
                    .eq("delete_flag", 0));
            if (!ObjectUtils.isEmpty(courseInfoList)) {
                for (CourseInfo courseInfo : courseInfoList) {
                    CourseType courseType = courseTypeDao.selectById(courseInfo.getTypeId());
                    courseInfo.setTypeName(courseType.getTypeName());
                    courseInfo.setTypeRemark(courseType.getRemark());
                    allTypeIds.add(courseInfo.getTypeId());
                }
                if (allTypeIds.isEmpty()) {
                    return Result.success().put("data", new PageInfo<>(children));
                }
                children = courseTypeDao.selectList(new QueryWrapper<CourseType>()
                        .in("id", allTypeIds)
                        .eq("delete_flag", 0));
                if (!ObjectUtils.isEmpty(children)) {
                    for (CourseType courseType1 : children) {
                        List<CourseInfo> courseInfoList1 = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                                .eq("type_id", courseType1.getId()));
                        if (!ObjectUtils.isEmpty(courseInfoList1)) {
                            for (CourseInfo courseInfo : courseInfoList1) {
                                List<ChapterInfoVO> chapterInfoList = chapterInfoDao.selectByCourseId(courseInfo.getId());
                                courseType1.setChapterNum(chapterInfoList.size() + "章");
                                courseType1.setCourseInfoList(courseInfoList1);
                            }
                        }
                    }
                }
                return Result.success().put("data", new PageInfo<>(courseInfoList));
            } else {
                return Result.success().put("data", new PageInfo<>(courseInfoList));
            }
        }
        return Result.success().put("data", new PageInfo<>(courseInfoList));
    }
////        if (level == 1) {
//            // 获取所有一级类型ID
//            List<CourseType> levelOneTypes = courseTypeDao.selectList(new QueryWrapper<CourseType>().eq("parent_id", 0L));
//            Set<Long> allRelevantTypeIds = new HashSet<>();
//            // 遍历一级类型，获取它们及其所有子类型的ID
//            for (CourseType levelOneType : levelOneTypes) {
//                List<Long> typeIdsForLevelAndBelow = getAllSubTypeIds(levelOneType.getId());
//                allRelevantTypeIds.addAll(typeIdsForLevelAndBelow);
//            }
//            if (allRelevantTypeIds.isEmpty()){
//                return Result.error("该类型下没有课程");
//            }
//            courses = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
//                    .in("type_id", allRelevantTypeIds)
//                    .in("id", likedCourseIds)
//                    .eq("delete_flag", 0)
//                    .orderByDesc("create_time"));
////        } else {
//            // 查询指定类型及其子类型下的所有课程ID
//            List<Long> relevantTypeIds = getAllSubTypeIds(id);
//            if (relevantTypeIds.isEmpty()){
//                return Result.error("该类型下没有课程");
//            }
//            // 包含当前类型ID
//            relevantTypeIds.add(id);
//            courses = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
//                    .in("type_id", relevantTypeIds)
//                    .in("id", likedCourseIds)
//                    .eq("delete_flag", 0)
//                    .orderByDesc("create_time"));
////        }
//        return Result.success().put("data", courses);
//    }

    private List<Long> getAllSubTypeIds(Long id) {
        List<Long> ids = new ArrayList<>();
        collectSubTypeIds(id, ids);
        return ids;
    }

    private void collectSubTypeIds(Long id, List<Long> ids) {
        List<CourseType> children = courseTypeDao.selectList(new QueryWrapper<CourseType>()
                .eq("parent_id", id));
        for (CourseType child : children) {
            ids.add(child.getId());
            collectSubTypeIds(child.getId(), ids);
        }
    }

    private List<Long> getLikedCourseIds(Long userId) {
        return courseLikeDao.selectList(new QueryWrapper<CourseLike>()
                        .eq("user_id", userId)
                        .eq("delete_flag", 0))
                .stream()
                .map(CourseLike::getCourseId)
                .collect(Collectors.toList());
    }


}
