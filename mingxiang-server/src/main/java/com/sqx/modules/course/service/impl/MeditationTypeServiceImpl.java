package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.dao.MeditationTypeDao;
import com.sqx.modules.course.entity.MeditationType;
import com.sqx.modules.course.service.MeditationTypeService;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sun.org.apache.bcel.internal.generic.I2F;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/21 16:11
 */
@Service
public class MeditationTypeServiceImpl extends ServiceImpl<MeditationTypeDao, MeditationType> implements MeditationTypeService {

    @Autowired
    private MeditationTypeDao meditationTypeDao;

    @Override
    public Result selectMeditationTypeList(Integer pageNum, Integer pageSize, String meditationName) {
        PageHelper.startPage(pageNum,pageSize);
        List<MeditationType> meditationTypeList = meditationTypeDao.selectList(new QueryWrapper<MeditationType>()
                .like(!ObjectUtils.isEmpty(meditationName),"meditation_name","%"+meditationName+"%")
                .eq("delete_flag",0));
        return Result.success().put("data",new PageInfo<>(meditationTypeList));
    }

    @Override
    public Result saveInfo(MeditationType meditationType) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)){
            return Result.error("该用户不存在");
        }
        int count = meditationTypeDao.selectCount(new QueryWrapper<MeditationType>()
                .eq("meditation_name", meditationType.getMeditationName())
                .eq("delete_flag",0));
        if (count>0){
            return Result.error("该冥想类型名称已存在");
        }
        meditationType.setCreateBy(principal.getUserId());
        meditationType.setCreateTime(new Date());
        if (meditationTypeDao.insert(meditationType)>0){
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result edit(MeditationType meditationType) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)){
            return Result.error("该用户不存在");
        }
        int count = meditationTypeDao.selectCount(new QueryWrapper<MeditationType>()
                .eq("meditation_name", meditationType.getMeditationName())
                .eq("delete_flag",0)
                .ne("id",meditationType.getId()));
        if (count>0){
            return Result.error("该冥想类型名称已存在");
        }
        MeditationType meditationType1 = meditationTypeDao.selectById(meditationType.getId());
        if (ObjectUtils.isEmpty(meditationType1)){
            return Result.error("该冥想类型不存在");
        }
        meditationType.setUpdateBy(principal.getUserId());
        meditationType.setUpdateTime(new Date());
        if (meditationTypeDao.updateById(meditationType)>0){
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
        MeditationType meditationType = meditationTypeDao.selectById(id);
        if (ObjectUtils.isEmpty(meditationType)){
            return Result.error("该冥想类型不存在");
        }
        meditationType.setDeleteFlag(1);
        if (meditationTypeDao.updateById(meditationType)>0){
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
        MeditationType meditationType = meditationTypeDao.selectById(id);
        if (ObjectUtils.isEmpty(meditationType)){
            return Result.error("该冥想类型不存在");
        }
        return Result.success().put("data",meditationType);
    }


}
