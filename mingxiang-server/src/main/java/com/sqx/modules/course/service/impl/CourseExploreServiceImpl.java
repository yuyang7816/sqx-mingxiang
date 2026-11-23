package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseExplore;
import com.sqx.modules.course.service.CourseExploreService;
import com.sqx.modules.course.dao.CourseExploreDao;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Admin
 * @description 针对表【course_explore(探索页相关)】的数据库操作Service实现
 * @createDate 2025-09-11 09:19:39
 */
@Service
public class CourseExploreServiceImpl extends ServiceImpl<CourseExploreDao, CourseExplore>
        implements CourseExploreService {

    @Override
    public Result addCourseExplore(CourseExplore courseExplore) {
        int count = baseMapper.selectCount(new QueryWrapper<CourseExplore>()
                .eq("explore_name", courseExplore.getExploreName()));
        if (count > 0) {
            return Result.error("该探索页名称已存在");
        }
        courseExplore.setCreateTime(new Date());
        if (baseMapper.insert(courseExplore) > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result editCourseExplore(CourseExplore courseExplore) {
        int count = baseMapper.selectCount(new QueryWrapper<CourseExplore>()
                .eq("explore_name", courseExplore.getExploreName())
                .ne("id", courseExplore.getId()));
        if (count > 0) {
            return Result.error("该探索页名称已存在");
        }
        if (baseMapper.updateById(courseExplore) > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    public Result deleteCourseExplore(CourseExplore courseExplore) {
        CourseExplore courseExplore1 = baseMapper.selectById(courseExplore.getId());
        if (courseExplore1 == null) {
            return Result.error("所选探索页不存在");
        }
        if (baseMapper.deleteById(courseExplore.getId()) > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result selectCourseExploreList(Integer pageNum, Integer pageSize, String exploreName, Long parentId) {
        PageHelper.startPage(pageNum, pageSize);
        if (parentId == null) {
            List<CourseExplore> courseExploreList = baseMapper.selectList(new QueryWrapper<CourseExplore>()
                    .eq("parent_id", 0)
                    .orderByDesc("create_time"));
            if (!courseExploreList.isEmpty()) {
                for (CourseExplore courseExplore : courseExploreList) {
                    courseExplore.setChildren(baseMapper.selectList(new QueryWrapper<CourseExplore>()
                            .eq("parent_id", courseExplore.getId())));
                }
            }
            return Result.success().put("data", new PageInfo<>(courseExploreList));
        } else {
            List<CourseExplore> courseExploreList = baseMapper.selectList(new QueryWrapper<CourseExplore>()
                    .eq("parent_id", parentId)
                    .orderByDesc("create_time"));
            if (!courseExploreList.isEmpty()) {
                for (CourseExplore courseExplore : courseExploreList) {
                    courseExplore.setChildren(baseMapper.selectList(new QueryWrapper<CourseExplore>()
                            .eq("parent_id", courseExplore.getId())));
                }
            }
            return Result.success().put("data", new PageInfo<>(courseExploreList));
        }
    }

    @Override
    public Result selectCourseExploreList1(String exploreName) {
        List<CourseExplore> courseExploreList = baseMapper.selectList(new QueryWrapper<CourseExplore>()
                .eq("parent_id", 0)
                .eq("status", 1));
        if (!courseExploreList.isEmpty()) {
            for (CourseExplore courseExplore : courseExploreList) {
                List<CourseExplore> children = baseMapper.selectList(new QueryWrapper<CourseExplore>()
                        .eq("parent_id", courseExplore.getId())
                        .eq("status", 1));
                if (!children.isEmpty()) {
                    courseExplore.setChildren(children);
                }
            }
        }
        return Result.success().put("data", courseExploreList);
    }

    @Override
    public Result selectCourseExploreById(Long id) {
        CourseExplore courseExplore = baseMapper.selectById(id);
        if (courseExplore == null) {
            return Result.error("所选探索页不存在");
        }
        return Result.success().put("data", courseExplore);
    }
}




