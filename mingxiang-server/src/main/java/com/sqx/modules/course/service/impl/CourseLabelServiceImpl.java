package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.course.dao.CourseLabelDao;
import com.sqx.modules.course.entity.CourseLabel;
import com.sqx.modules.course.service.CourseLabelService;
import com.sqx.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/20 16:17
 */
@Service
public class CourseLabelServiceImpl extends ServiceImpl<CourseLabelDao, CourseLabel> implements CourseLabelService {

    @Autowired
    private CourseLabelDao courseLabelDao;

    @Autowired
    private UserDao userDao;


    @Override
    public Result selectByCourseLabelList(Integer pageNum, Integer pageSize, String labelName) {
        PageHelper.startPage(pageNum, pageSize);
        List<CourseLabel> list = courseLabelDao.selectList(new QueryWrapper<CourseLabel>()
                .eq(!ObjectUtils.isEmpty(labelName),"label_name", "%" + labelName + "%")
                .eq("delete_flag", 0)
                .orderByDesc("create_time"));
        return Result.success().put("data",new PageInfo<>(list));
    }

    @Override
    public Result saveInfo(CourseLabel courseLabel) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)){
            return Result.error("该用户不存在");
        }
        int count = courseLabelDao.selectCount(new QueryWrapper<CourseLabel>().eq("label_name", courseLabel.getLabelName())
                .eq("delete_flag", 0));
        if (count > 0){
            return Result.error("该标签名称已存在");
        }
        courseLabel.setCreateBy(principal.getUserId());
        courseLabel.setCreateTime(new Date());
        if (courseLabelDao.insert(courseLabel) > 0){
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result edit(CourseLabel courseLabel) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)){
            return Result.error("该用户不存在");
        }
        int count = courseLabelDao.selectCount(new QueryWrapper<CourseLabel>().eq("label_name", courseLabel.getLabelName())
                .eq("delete_flag", 0)
                .ne("id",courseLabel.getId()));
        if (count > 0){
            return Result.error("该标签名称已存在");
        }
        CourseLabel courseLabel1 = courseLabelDao.selectById(courseLabel.getId());
        if (ObjectUtils.isEmpty(courseLabel1)){
            return Result.error("该标签不存在");
        }
        courseLabel.setUpdateBy(principal.getUserId());
        courseLabel.setUpdateTime(new Date());
        if (courseLabelDao.updateById(courseLabel) > 0){
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    public Result delete(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)){
            return Result.error("该用户不存在");
        }
        CourseLabel courseLabel = courseLabelDao.selectById(id);
        if (ObjectUtils.isEmpty(courseLabel)){
            return Result.error("该标签不存在");
        }
        courseLabel.setDeleteFlag(1);
        if (courseLabelDao.updateById(courseLabel) > 0){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result selectDetail(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)){
            return Result.error("该用户不存在");
        }
        CourseLabel courseLabel = courseLabelDao.selectById(id);
        if (ObjectUtils.isEmpty(courseLabel)){
            return Result.error("该标签不存在");
        }
        return Result.success().put("data",courseLabel);
    }


}
