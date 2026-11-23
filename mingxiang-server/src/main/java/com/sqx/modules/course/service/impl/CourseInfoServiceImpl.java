package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.course.dao.*;
import com.sqx.modules.course.entity.*;
import com.sqx.modules.course.entity.dto.CourseInfoDTO;
import com.sqx.modules.course.entity.vo.ChapterInfoVO;
import com.sqx.modules.course.entity.vo.CourseInfoVO;
import com.sqx.modules.course.entity.vo.CourseLabelVO;
import com.sqx.modules.course.entity.vo.MentorVO;
import com.sqx.modules.course.service.CourseInfoService;
import com.sqx.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程信息(CourseInfo)表服务实现类
 *
 * @author makejava
 * @since 2024-05-16 15:19:26
 */
@Service
@Slf4j
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoDao, CourseInfo> implements CourseInfoService {
    @Autowired
    private CourseInfoDao courseInfoDao;

    @Autowired
    private CourseTypeDao courseTypeDao;

    @Autowired
    private ChapterInfoDao chapterInfoDao;

    @Autowired
    private CourseLabelDao courseLabelDao;

    @Autowired
    private CourseAndLabelDao courseAndLabelDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseLikeDao courseLikeDao;

    @Autowired
    private CourseLearnDao courseLearnDao;

    @Autowired
    private CourseExploreDao courseExploreDao;

    @Override
    public Result getCourseInfoAllList(Integer pageNum, Integer pageSize, String courseName,String typeName,String courseTitle,
                                       String mentorTitle,String mentor,Integer courseStatus) {
        PageHelper.startPage(pageNum, pageSize);
        List<CourseInfo> courseInfoList = courseInfoDao.selectLists(courseName, typeName, courseTitle, mentorTitle, mentor, courseStatus);
        return Result.success().put("data", new PageInfo<>(courseInfoList));
    }

    @Override
    public Result saveInfo(CourseInfoDTO courseInfoDTO) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(principal)) {
            // 检查名称是否重复
            int count = courseInfoDao.selectCount(new QueryWrapper<CourseInfo>().eq("course_name", courseInfoDTO.getCourseName())
                    .eq("delete_flag", 0));
            if (count > 0) {
                return Result.error("该课程名称已存在");
            }
            CourseInfo courseInfo = new CourseInfo();
            BeanUtils.copyProperties(courseInfoDTO, courseInfo);
            // 插入课程信息
            courseInfo.setCreateBy(principal.getUserId());
            courseInfo.setCreateTime(new Date());
            if (courseInfoDTO.getExploreId() != null) {
                CourseExplore courseExplore = courseExploreDao.selectById(courseInfo.getExploreId());
                if (courseExplore != null) {
                    courseInfo.setExploreId(courseExplore.getId());
                }
            }
            if (courseInfo.getExploreParentId() != null) {
                CourseExplore courseExplore1 = courseExploreDao.selectById(courseInfoDTO.getExploreParentId());
                if (courseExplore1 != null) {
                    courseInfo.setExploreParentId(courseExplore1.getId());
                }
            }
            int result = courseInfoDao.insert(courseInfo);
            if (result <= 0) {
                return Result.error("添加失败");
            }
            // 将拿到的标签存到课程与标签关联表
            List<CourseAndLabel> courseAndLabels = new ArrayList<>();
            if (courseInfo.getLabelIds() != null) {
                String[] labelIds = courseInfo.getLabelIds().split(",");
                for (String labelId : labelIds) {
                    CourseAndLabel courseAndLabel = new CourseAndLabel();
                    courseAndLabel.setCourseId(courseInfo.getId());
                    courseAndLabel.setLabelId(Long.valueOf(labelId));
                    courseAndLabels.add(courseAndLabel);
                }
            }
            if (!ObjectUtils.isEmpty(courseAndLabels)) {
                int resultCode = courseAndLabelDao.insertBatch(courseAndLabels);
                if (resultCode <= 0) {
                    return Result.error("添加失败");
                }
            }
            return Result.success("添加成功");
        }
        return Result.error("该用户不存在");
    }

    @Override
    public Result edit(CourseInfoDTO courseInfoDTO) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(principal)) {
            // 检查名称是否重复
            int count = courseInfoDao.selectCount(new QueryWrapper<CourseInfo>().eq("course_name", courseInfoDTO.getCourseName())
                    .eq("delete_flag", 0)
                    .ne("id", courseInfoDTO.getId()));
            if (count > 0) {
                return Result.error("该课程名称已存在");
            }
            CourseInfo courseInfo = new CourseInfo();
            BeanUtils.copyProperties(courseInfoDTO, courseInfo);
            // 查看课程信息是否存在
            CourseInfo courseInfo1 = courseInfoDao.selectById(courseInfo.getId());
            if (!ObjectUtils.isEmpty(courseInfo1)) {
                List<CourseAndLabel> courseAndLabels = new ArrayList<>();
                // 如果获得的标签id不为空，则删除该课程下的所有标签，再插入新的标签
                if (!ObjectUtils.isEmpty(courseInfo.getLabelIds())) {
                    courseAndLabels = courseAndLabelDao.selectList(new QueryWrapper<CourseAndLabel>().eq("course_id", courseInfo.getId()));
                    if (!ObjectUtils.isEmpty(courseAndLabels)) {
                        int result = courseAndLabelDao.delete(new QueryWrapper<CourseAndLabel>().eq("course_id", courseInfo.getId()));
                        if (result <= 0) {
                            return Result.error("修改失败");
                        }
                    }
                    if (courseInfo.getLabelIds() != null) {
                        String[] labelIds = courseInfo.getLabelIds().split(",");
                        for (String labelId : labelIds) {
                            CourseAndLabel courseAndLabel = new CourseAndLabel();
                            courseAndLabel.setCourseId(courseInfo.getId());
                            courseAndLabel.setLabelId(Long.valueOf(labelId));
                            courseAndLabels.add(courseAndLabel);
                        }
                    }
                    if (!ObjectUtils.isEmpty(courseAndLabels)) {
                        int resultCode = courseAndLabelDao.insertBatch(courseAndLabels);
                        if (resultCode <= 0) {
                            return Result.error("修改失败");
                        }
                    }
                }
                // 修改课程信息
                courseInfo.setUpdateBy(principal.getUserId());
                courseInfo.setUpdateTime(new Date());
                if (courseInfoDTO.getExploreId() != null) {
                    CourseExplore courseExplore = courseExploreDao.selectById(courseInfo.getExploreId());
                    if (courseExplore != null) {
                        courseInfo.setExploreId(courseExplore.getId());
                    }
                }
                if (courseInfo.getExploreParentId() != null) {
                    CourseExplore courseExplore1 = courseExploreDao.selectById(courseInfoDTO.getExploreParentId());
                    if (courseExplore1 != null) {
                        courseInfo.setExploreParentId(courseExplore1.getId());
                    }
                }
                int result = courseInfoDao.updateById(courseInfo);
                if (result <= 0) {
                    return Result.error("修改失败");
                }
                return Result.success("修改成功");
            } else {
                return Result.error("该课程不存在");
            }
        }
        return Result.error("该用户不存在");
    }

    @Override
    public Result delete(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在");
        }
        CourseInfo courseInfo = courseInfoDao.selectById(id);
        if (ObjectUtils.isEmpty(courseInfo)) {
            return Result.error("该课程不存在");
        }
        courseInfo.setDeleteFlag(1);
        int result = courseInfoDao.updateById(courseInfo);
        if (result <= 0) {
            return Result.error("删除失败");
        }
        List<CourseAndLabel> courseAndLabels = courseAndLabelDao.selectList(new QueryWrapper<CourseAndLabel>().eq("course_id", id));
        courseAndLabels.forEach(courseAndLabel -> {
            courseAndLabelDao.deleteById(courseAndLabel.getId());
        });
        return Result.success("删除成功");
    }

    @Override
    public Result selectDetail(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在");
        }
        CourseInfoVO courseInfo = courseInfoDao.selectByDetail(id);
        if (ObjectUtils.isEmpty(courseInfo)) {
            return Result.error("该课程不存在");
        }
        // 查出章节信息及章节并将课程信息添加到课程详情
        List<ChapterInfoVO> chapterInfoList = chapterInfoDao.selectByCourseId(id);
        courseInfo.setChapterInfoList(chapterInfoList);
        courseInfo.setCountChapter(chapterInfoList.size() + "章");
        // 查出课程的标签
        List<CourseAndLabel> courseLabelList = courseAndLabelDao.selectList(new QueryWrapper<CourseAndLabel>().eq("course_id", id));
        // 初始化一个列表来存储标签名称
        List<String> labelNames = new ArrayList<>();
        if (!ObjectUtils.isEmpty(courseLabelList)) {
            for (CourseAndLabel courseAndLabel : courseLabelList) {
                CourseLabelVO courseLabel = courseLabelDao.selectByLabelId(courseAndLabel.getLabelId());
                if (!ObjectUtils.isEmpty(courseLabel)) {
                    labelNames.add(courseLabel.getLabelName());
                }
            }
        }
        courseInfo.setLabelNames(labelNames);
        return Result.success().put("data", courseInfo);
    }

    @Override
    public Result selectCourseInfoList(Integer pageNum, Integer pageSize) {
//        UserEntity user = userDao.selectById(userId);
//        if (ObjectUtils.isEmpty(user)) {
//            return Result.error("该用户不存在");
//        }
        PageHelper.startPage(pageNum, pageSize);
        List<CourseType> courseTypeList = courseTypeDao.selectListByCourse();
        if (!ObjectUtils.isEmpty(courseTypeList)) {
            for (CourseType courseType : courseTypeList) {
//                List<CourseType> children = courseTypeDao.selectList(new QueryWrapper<CourseType>()
//                        .eq("parent_id", 0)
//                        .eq("delete_flag", 0));
//                if (!ObjectUtils.isEmpty(children)) {
//                    courseType.setChildren(children);
//                    for (CourseType courseType1 : children) {
                List<CourseInfo> courseInfoList = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                        .eq("type_id", courseType.getId()));
                if (!ObjectUtils.isEmpty(courseInfoList)) {
                    courseType.setCourseInfoList(courseInfoList);
                    for (CourseInfo courseInfo : courseInfoList) {
                        List<ChapterInfoVO> chapterInfoList = chapterInfoDao.selectByCourseId(courseInfo.getId());
                        courseType.setChapterNum(chapterInfoList.size() + "章");
                    }
//                        }
//                    }
                }
            }
        }
        return Result.success().put("data", new PageInfo<>(courseTypeList));
    }

    @Override
    public Result selectCourseInfoByDetail(Long id, Long userId) {
        CourseInfo courseInfoList = courseInfoDao.selectOne(new QueryWrapper<CourseInfo>()
                .eq("id", id)
                .eq("course_status", 0)
                .eq("delete_flag", 0));
        CourseLike courseLike;
        if (courseInfoList != null) {
            int count = courseLearnDao.selectCount(new QueryWrapper<CourseLearn>()
                    .eq("course_id", courseInfoList.getId())
                    .eq("type", 0)
                    .select("DISTINCT user_id"));
            courseInfoList.setCountPractice(count);
            courseLike = courseLikeDao.selectOne(new QueryWrapper<CourseLike>()
                    .eq("course_id", courseInfoList.getId())
                    .eq("user_id", userId));
            courseInfoList.setIsLike(ObjectUtils.isEmpty(courseLike) ? 0 : 1);
//            for (CourseInfo courseInfo : courseInfoList) {
            // 查出章节信息及章节并将课程信息添加到课程详情
            List<ChapterInfoVO> chapterInfoList = chapterInfoDao.selectByCourseId(courseInfoList.getId());
            courseInfoList.setChapterInfoList(chapterInfoList);
            courseInfoList.setCountChapter(chapterInfoList.size() + "章");
            // 查出课程的标签
            List<CourseAndLabel> courseLabelList = courseAndLabelDao.selectList(new QueryWrapper<CourseAndLabel>()
                    .eq("course_id", courseInfoList.getId()));
            // 初始化一个列表来存储标签名称
            Set<String> labelNames = new HashSet<>();
            if (!ObjectUtils.isEmpty(courseLabelList)) {
                // 提取所有的 labelId
                List<Long> labelIds = courseLabelList.stream()
                        .map(CourseAndLabel::getLabelId)
                        .collect(Collectors.toList());
                List<CourseLabel> labelVOList = courseLabelDao.selectBatchIds(labelIds);
                if (!ObjectUtils.isEmpty(labelVOList)){
                    labelNames = labelVOList.stream()
                            .map(CourseLabel::getLabelName)
                            .collect(Collectors.toSet());
                }
            }
            courseInfoList.setLabelNames(new ArrayList<>(labelNames));
        }
        return Result.success().put("data", courseInfoList);
    }

    @Override
    public Result selectByMentor(Long id, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        MentorVO courseInfo = courseInfoDao.selectByCourseId(id);
        if (ObjectUtils.isEmpty(courseInfo)) {
            return Result.error("该导师信息不存在");
        }
        return Result.success().put("data", courseInfo);
    }

    @Override
    public Result selectRecommendCourseInfo(Long id) {
//        CourseType courseType = courseTypeDao.selectById(id);
//        if (ObjectUtils.isEmpty(courseType)) {
//            return Result.success();
//        }
//        CourseType parents = courseTypeDao.selectOne(new QueryWrapper<CourseType>()
//                .eq("id", courseType.getParentId()));
//        if (ObjectUtils.isEmpty(parents)) {
//            return Result.success();
//        }
//        List<CourseType> courseTypeList = courseTypeDao.selectList(new QueryWrapper<CourseType>()
//                .ne("id", id));
        CourseInfo courseInfo = baseMapper.selectOne(new QueryWrapper<CourseInfo>()
                .eq("id", id)
                .eq("course_status", 0)
                .eq("delete_flag", 0));
        List<CourseInfo> courseInfoList = new ArrayList<>();
        if (courseInfo != null) {
            List<CourseInfo> courseInfos = baseMapper.selectList(new QueryWrapper<CourseInfo>()
                    .ne("id", id)
                    .eq("type_id", courseInfo.getTypeId())
                    .eq("course_status", 0)
                    .eq("delete_flag", 0));
            if (courseInfos != null) {
                for (CourseInfo courseInfo1 : courseInfos) {
                    // 查出课程的标签
                    List<CourseAndLabel> courseLabelList = courseAndLabelDao.selectList(new QueryWrapper<CourseAndLabel>()
                            .eq("course_id", courseInfo1.getId()));
                    if (!ObjectUtils.isEmpty(courseLabelList)) {
                        // 初始化一个列表来存储标签名称
                        List<String> labelNames = new ArrayList<>();
                        for (CourseAndLabel courseAndLabel : courseLabelList) {
                            CourseLabelVO courseLabel = courseLabelDao.selectByLabelId(courseAndLabel.getLabelId());
                            if (!ObjectUtils.isEmpty(courseLabel)) {
                                labelNames.add(courseLabel.getLabelName());
                            }
                        }
                        courseInfo.setLabelNames(labelNames);
                    }
                    courseInfoList.add(courseInfo1);
                }
            }
        }
        return Result.success().put("data", courseInfoList);
    }

    // 获取当前课程的所有标签id
//        List<Long> labelIds = courseAndLabelDao.selectList(new QueryWrapper<CourseAndLabel>().eq("course_id", courseInfo.getId())
//                .eq("delete_flag", 0)).stream().map(CourseAndLabel::getLabelId).collect(Collectors.toList());
//        if (ObjectUtils.isEmpty(labelIds)) {
//            return Result.success().put("data", Collections.emptyList());
//        }
//        List<CourseInfo> recommendedCourses = new ArrayList<>();
//        // 先找出所有具有这些标签的课程ID（不包括当前课程）
//        List<Long> recommendedCourseIds = courseAndLabelDao.selectList(new QueryWrapper<CourseAndLabel>()
//                        .in("label_id", labelIds)
//                        .eq("delete_flag", 0)
//                        .ne("course_id", courseInfo.getId()))
//                .stream()
//                .map(CourseAndLabel::getCourseId)
//                .distinct()
//                .collect(Collectors.toList());

    // 用推荐课程的ID列表,查找出推荐课程
//        if(!recommendedCourseIds.isEmpty())
//
//    {
//        recommendedCourses = courseInfoDao.selectBatchIds(recommendedCourseIds);
//        if (!ObjectUtils.isEmpty(recommendedCourses)) {
//            recommendedCourses.forEach(courseInfo1 -> {
//                // 查出课程的标签
//                List<CourseAndLabel> courseLabelList = courseAndLabelDao.selectList(new QueryWrapper<CourseAndLabel>()
//                        .eq("course_id", courseInfo1.getId()));
//                if (!ObjectUtils.isEmpty(courseLabelList)) {
//                    // 初始化一个列表来存储标签名称
//                    List<String> labelNames = new ArrayList<>();
//                    for (CourseAndLabel courseAndLabel : courseLabelList) {
//                        CourseLabelVO courseLabel = courseLabelDao.selectByLabelId(courseAndLabel.getLabelId());
//                        if (!ObjectUtils.isEmpty(courseLabel)) {
//                            labelNames.add(courseLabel.getLabelName());
//                        }
//                    }
//                    courseInfo1.setLabelNames(labelNames);
//                }
//            });
//        }
//    }

    @Override
    public Result selectCourseListByTypeId(Integer pageNum, Integer pageSize, String typeId) {
        PageHelper.startPage(pageNum, pageSize);
        CourseType courseType = courseTypeDao.selectOne(new QueryWrapper<CourseType>()
                .eq("id", Long.valueOf(typeId)));
        if (!ObjectUtils.isEmpty(courseType)) {
            List<CourseInfo> courseInfoList = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                    .eq("type_id", courseType.getId()));
            if (!ObjectUtils.isEmpty(courseInfoList)) {
                for (CourseInfo courseInfo : courseInfoList) {
                    List<ChapterInfoVO> chapterInfoList = chapterInfoDao.selectByCourseId(courseInfo.getId());
                    courseType.setChapterNum(chapterInfoList.size() + "章");
                }
            }

            return Result.success().put("data", new PageInfo<>(courseInfoList));
        }

        return Result.success().put("data", new PageInfo<>());
    }

    @Override
    public Result selectCourseInfoByExplore(Integer pageNum, Integer pageSize, String courseName, Long exploreId, Long exploreParentId) {
        Set<Long> allTypeIds = new HashSet<>();
        PageHelper.startPage(pageNum, pageSize);
        List<CourseType> children = new ArrayList<>();
        List<CourseInfo> courseInfoList = new ArrayList<>();
        if (exploreId != null) {
            courseInfoList = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                    .like(!ObjectUtils.isEmpty(courseName), "course_name", courseName)
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
                    return Result.success().put("data", new PageInfo<>(courseInfoList));
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
            }
        } else if (exploreParentId != null) {
            courseInfoList = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                    .like(!ObjectUtils.isEmpty(courseName), "course_name", courseName)
                    .eq("explore_parent_id", exploreParentId)
                    .eq("delete_flag", 0));
            if (!ObjectUtils.isEmpty(courseInfoList)) {
                for (CourseInfo courseInfo : courseInfoList) {
                    CourseType courseType = courseTypeDao.selectById(courseInfo.getTypeId());
                    courseInfo.setTypeName(courseType.getTypeName());
                    courseInfo.setTypeRemark(courseType.getRemark());
                    allTypeIds.add(courseInfo.getTypeId());
                }
            }
            if (allTypeIds.isEmpty()) {
                return Result.success().put("data", new PageInfo<>(courseInfoList));
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
        }
        return Result.success().put("data", new PageInfo<>(courseInfoList));
    }


}
