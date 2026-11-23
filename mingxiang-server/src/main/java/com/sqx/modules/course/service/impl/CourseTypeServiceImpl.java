package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.course.dao.CourseInfoDao;
import com.sqx.modules.course.dao.CourseSearchDao;
import com.sqx.modules.course.dao.CourseTypeDao;
import com.sqx.modules.course.entity.CourseInfo;
import com.sqx.modules.course.entity.CourseSearch;
import com.sqx.modules.course.entity.CourseType;
import com.sqx.modules.course.entity.dto.CourseTypeDTO;
import com.sqx.modules.course.service.CourseTypeService;
import com.sqx.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author Admin
 * @description 针对表【course_type(课程类型)】的数据库操作Service实现
 * @createDate 2024-06-19 19:34:26
 */
@Service
@Slf4j
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeDao, CourseType> implements CourseTypeService {

    @Autowired
    private CourseTypeDao courseTypeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseInfoDao courseInfoDao;

    @Autowired
    private CourseSearchDao courseSearchDao;

    @Override
    public Result selectCourseTypeList(Integer pageNum, Integer pageSize, String typeName, Long parentId) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<CourseType> queryWrapper = new QueryWrapper<CourseType>().like(!ObjectUtils.isEmpty(typeName), "type_name", "%" + typeName + "%")
                .eq("delete_flag", 0);
        if (parentId == null || parentId == 0L) {
            queryWrapper.ge("level", 1);
        } else {
            queryWrapper.eq("parent_id", parentId);
        }
        queryWrapper.orderByDesc("create_time");
        List<CourseType> flatList = courseTypeDao.selectList(queryWrapper);
        // 构建树形结构
        List<CourseType> treeList;
        if (Objects.isNull(parentId) || parentId == 0L) {
            // 构建一级菜单
            treeList = buildTree(flatList, 0L);
        } else {
            // 指定parentId时，直接返回指定父级下的子树结构
            // 返回的是该父级下的列表
            treeList = flatList;
        }
        return Result.success().put("data", new PageInfo<>(treeList));
    }

    private List<CourseType> buildTree(List<CourseType> allTypes, Long parentId) {
        List<CourseType> treeList = new ArrayList<>();
        for (CourseType type : allTypes) {
            if (Objects.equals(type.getParentId(), parentId)) {
                CourseType node = new CourseType();
                BeanUtils.copyProperties(type, node);
                node.setChildren(buildTree(allTypes, type.getId()));
                treeList.add(node);
            }
        }
        return treeList;
    }

    @Override
    public Result saveInfo(CourseTypeDTO courseTypeDTO) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(principal)) {
            CourseType courseType = new CourseType();
            BeanUtils.copyProperties(courseTypeDTO, courseType);
            int count = courseTypeDao.selectCount(new QueryWrapper<CourseType>().eq("type_name", courseType.getTypeName())
                    .eq("delete_flag", 0)
                    .ne(!ObjectUtils.isEmpty(courseType.getParentId()), "parent_id", courseType.getParentId()));
            if (count > 0) {
                return Result.error("该类型名称已存在");
            }
            int countResult = courseTypeDao.selectCount(new QueryWrapper<CourseType>().eq("type_name", courseType.getTypeName())
                    .eq("delete_flag", 0)
                    .eq("parent_id", courseType.getParentId()));
            if (countResult > 0) {
                return Result.error("该类型名称已存在");
            }
            if (courseType.getParentId() == 0) {
                courseType.setLevel(1);
            } else {
                CourseType parentCourseType = courseTypeDao.selectById(courseType.getParentId());
                if (ObjectUtils.isEmpty(parentCourseType)) {
                    return Result.error("该父类型不存在");
                }
                courseType.setLevel(parentCourseType.getLevel() + 1);
            }
            courseType.setCreateBy(principal.getUserId());
            courseType.setCreateTime(new Date());
            if (courseTypeDao.insert(courseType) > 0) {
                return Result.success("添加成功");
            }
            return Result.error("添加失败");
        }
        return Result.error("该用户不存在");
    }

    @Override
    public Result edit(CourseTypeDTO courseTypeDTO) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(principal)) {
            CourseType courseType = new CourseType();
            BeanUtils.copyProperties(courseTypeDTO, courseType);
            CourseType courseType1 = courseTypeDao.selectById(courseType.getId());
            if (ObjectUtils.isEmpty(courseType1)) {
                return Result.error("该类型不存在");
            }
            // 检查是否为末级分类
            List<CourseType> children = courseTypeDao.selectList(new QueryWrapper<CourseType>().eq("parent_id", courseType.getId())
                    .eq("delete_flag", 0));
            if (!ObjectUtils.isEmpty(children)) {
                return Result.error("该分类不是末级分类,请勿修改!");
            }
            // 如果父id修改了，则将原父id改回去，避免修改父id
            if (!Objects.equals(courseType1.getParentId(), courseType.getParentId()) && !isLeafType(courseType.getId())) {
                courseType.setParentId(courseType1.getParentId());
            }
            int count = courseTypeDao.selectCount(new QueryWrapper<CourseType>().eq("type_name", courseType.getTypeName())
                    .eq("delete_flag", 0)
                    .ne("id", courseType.getId()));
            if (count > 0) {
                return Result.error("该类型名称已存在");
            }
            courseType.setUpdateBy(principal.getUserId());
            courseType.setUpdateTime(new Date());
            if (courseTypeDao.updateById(courseType) > 0) {
                return Result.success("修改成功");
            }
            return Result.error("修改失败");
        }
        return Result.error("该用户不存在");
    }

    // 判断是否为末级分类
    private boolean isLeafType(Long id) {
        return courseTypeDao.selectCount(new QueryWrapper<CourseType>().eq("parent_id", id)) == 0;
    }

    @Override
    public Result delete(CourseTypeDTO courseTypeDTO) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在");
        }
        CourseType courseType = courseTypeDao.selectById(courseTypeDTO.getId());
        if (ObjectUtils.isEmpty(courseType)) {
            return Result.error("该类型不存在");
        }
        // 检查是否为末级分类
        List<CourseType> children = courseTypeDao.selectList(new QueryWrapper<CourseType>()
                .eq("parent_id", courseTypeDTO.getId()).eq("delete_flag", 0));
        if (!children.isEmpty()) {
            return Result.error("该分类包含子分类，不能直接删除");
        }
        courseType.setDeleteFlag(1);
        if (courseTypeDao.updateById(courseType) > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result selectDetail(Integer id) {
        CourseType courseType = courseTypeDao.selectById(id);
        if (ObjectUtils.isEmpty(courseType)) {
            return Result.error("该类型不存在");
        }
        return Result.success().put("data", courseType);
    }

/*
 以下是App端
 */


//    @Override
//    public Result exploreHomePage(Integer pageNum, Integer pageSize, Long parentId) {
////        UserEntity user = userDao.selectById(userId);
////        if (ObjectUtils.isEmpty(user)) {
////            return Result.error("该用户不存在");
////        }
//        // 查询所有课程类型并构建树形结构
//        PageHelper.startPage(pageNum, pageSize);
//        QueryWrapper<CourseType> queryWrapper = new QueryWrapper<CourseType>()
//                .eq("delete_flag", 0);
//        List<CourseType> flatList = courseTypeDao.selectList(queryWrapper);
//        List<CourseType> treeList = buildTree(flatList, 0L);
//        // 查询课程列表
//        if (!ObjectUtils.isEmpty(treeList) || parentId == null || parentId == 0L) {
//            // 如果只传了一级父id，遍历所有一级课程类型，查询其下所有课程
//            for (CourseType courseType : treeList) {
//                if (courseType.getChildren() != null && !courseType.getChildren().isEmpty()) {
//                    for (CourseType child : courseType.getChildren()) {
//                        child.setCourseInfoList(courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
//                                .eq("type_id", child.getTypeId())
//                                .eq("delete_flag", 0)));
//                    }
//                }
//            }
//        } else {
//            // 如果传了二级id，就查询二级课程类型下的课程
//            CourseType courseType = courseTypeDao.selectById(parentId);
//            if (!ObjectUtils.isEmpty(courseType)) {
//                courseType.setCourseInfoList(courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
//                        .eq("type_id", parentId)
//                        .eq("delete_flag", 0)));
//            }
//        }
//        return Result.success().put("data", treeList);
//    }

    @Override
    public Result exploreHomePageByType(Integer pageNum, Integer pageSize, Long id, Integer level) {
        // 查询所有课程类型并构建树形结构
        PageHelper.startPage(pageNum, pageSize);
        List<CourseInfo> courses = new ArrayList<>();
        if (level!= null &&  level == 1) {
            // 获取所有一级类型ID
            List<CourseType> levelOneTypes = courseTypeDao.selectList(new QueryWrapper<CourseType>().eq("parent_id", 0L));
            Set<Long> allRelevantTypeIds = new HashSet<>();
            // 遍历一级类型，获取它们及其所有子类型的ID
            for (CourseType levelOneType : levelOneTypes) {
                List<Long> typeIdsForLevelAndBelow = getAllSubTypeIds(levelOneType.getId());
                allRelevantTypeIds.addAll(typeIdsForLevelAndBelow);
            }
            if (!ObjectUtils.isEmpty(allRelevantTypeIds)) {
                courses = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                        .in("type_id", allRelevantTypeIds)
                        .eq("delete_flag", 0)
                        .orderByDesc("create_time"));
            }
        } else {
            if (id != null) {
                // 查询指定类型及其子类型下的所有课程ID
                List<Long> relevantTypeIds = getAllSubTypeIds(id);
                if (!ObjectUtils.isEmpty(relevantTypeIds)) {
                relevantTypeIds.add(id); // 包含当前类型ID
                    courses = courseInfoDao.selectList(new QueryWrapper<CourseInfo>()
                            .in("type_id", relevantTypeIds)
                            .eq("delete_flag", 0)
                            .orderByDesc("create_time"));
                }
            }
        }
        return Result.success().put("data", new PageInfo<>(courses));
    }

    private List<Long> getAllSubTypeIds(Long id) {
        List<Long> ids = new ArrayList<>();
        collectSubTypes(id, ids);
        return ids;
    }

    private void collectSubTypes(Long id, List<Long> ids) {
        List<CourseType> children = courseTypeDao.selectList(new QueryWrapper<CourseType>().eq("parent_id", id));
        if (!children.isEmpty()) {
            for (CourseType child : children) {
                ids.add(child.getId());
                // 递归调用自己以获取更深一层的子类型ID
                collectSubTypes(child.getId(), ids);
            }
        }
    }


    @Override
    public Result selectBySearch(String courseName) {
//        UserEntity user = userDao.selectById(userId);
//        if (ObjectUtils.isEmpty(user)) {
//            return Result.error("该用户不存在");
//        }
        List<CourseInfo> courseInfoList = courseInfoDao.selectCourseInfoList(courseName);
        if (ObjectUtils.isEmpty(courseInfoList)) {
            return Result.success().put("data", courseInfoList);
        }
        List<CourseSearch> courseSearchList = new ArrayList<>();
        for (CourseInfo courseInfo : courseInfoList) {
            CourseSearch courseSearch = new CourseSearch();
            courseSearch.setCourseId(courseInfo.getId());
            courseSearch.setCreateTime(new Date());
            courseSearch.setCourseName(courseInfo.getCourseName());
            courseSearch.setCourseWord(courseName);
            courseSearchList.add(courseSearch);
        }
        // 批量插入搜索记录
        if (!courseSearchList.isEmpty()) {
            int result = courseSearchDao.insertBatch(courseSearchList);
            if (result > 0) {
                log.info("搜索记录插入成功,插入了{}条", result);
            }
        } else {
            // 插入失败的逻辑处理
            log.error("批量插入搜索记录失败");
        }
        return Result.success().put("data", courseInfoList);
    }

    @Override
    public Result selectByHOT() {
        List<CourseSearch> countHOT = courseSearchDao.selectByHOT();
        if (ObjectUtils.isEmpty(countHOT)) {
            return Result.success().put("data", Collections.EMPTY_LIST);
        }
        return Result.success().put("data", countHOT);
    }

}




