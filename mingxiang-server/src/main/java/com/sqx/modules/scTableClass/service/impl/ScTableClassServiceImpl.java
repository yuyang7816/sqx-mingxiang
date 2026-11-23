package com.sqx.modules.scTableClass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.common.utils.ShiroUtils;
import com.sqx.modules.scTableClass.dao.ScTableClassDao;
import com.sqx.modules.scTableClass.entity.ScTableClass;
import com.sqx.modules.scTableClass.service.ScTableClassService;
import com.sqx.modules.scTableColumn.entity.ScTableColumn;
import com.sqx.modules.scTableColumn.service.ScTableColumnService;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ScTableClassServiceImpl extends ServiceImpl<ScTableClassDao, ScTableClass> implements ScTableClassService {
    @Autowired
    private ScTableClassDao scTableClassDao;
    @Autowired
    private ScTableColumnService columnService;
    @Autowired
    private ScTableDataService tableDataService;

    @Override
    public Result addClass(ScTableClass scTableClass) {
        SysUserEntity sysUser = (SysUserEntity) ShiroUtils.getSubject().getPrincipal();
        ScTableClass tableClass = scTableClassDao.selectOne(new QueryWrapper<ScTableClass>().eq("class_name", scTableClass.getClassName()));
        if (tableClass == null) {
            scTableClass.setCreateTime(new Date());
            scTableClass.setCreateUserId(sysUser.getUserId());
            return Result.upStatus(scTableClassDao.insert(scTableClass));
        } else {
            return Result.error("重复分类名称!");
        }

    }

    @Override
    public IPage<ScTableClass> getClassList(Integer page, Integer limit, Integer isEnable, String className) {
        Page<ScTableClass> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        QueryWrapper<ScTableClass> queryWrapper = new QueryWrapper<>();
        if (className != null) {
            queryWrapper.like("class_name", className);
        }
        if (isEnable != null) {
            queryWrapper.like("is_enable", isEnable);
        }
        return scTableClassDao.selectPage(pages, queryWrapper);
    }

    @Override
    public Result updateClass(ScTableClass scTableClass) {
        ScTableClass tableClass = scTableClassDao.selectOne(new QueryWrapper<ScTableClass>().eq("class_id", scTableClass.getClassId()));
        if (tableClass == null) {
            return Result.error("当前分类不存在!");
        } else {
            //判断修改提交的分类名是否重复
            ScTableClass aClass = scTableClassDao.selectOne(new QueryWrapper<ScTableClass>().eq("class_name", scTableClass.getClassName()).ne("class_id", scTableClass.getClassId()));
            if (aClass != null) {
                return Result.error("不能添加重复分类!");
            }
        }
        return Result.upStatus(scTableClassDao.updateById(scTableClass));
    }

    @Override
    public Result deleteClass(Long classId) {
        scTableClassDao.deleteById(classId);
        columnService.remove(new QueryWrapper<ScTableColumn>().eq("class_id", classId));
        tableDataService.remove(new QueryWrapper<ScTableData>().eq("class_id", classId));
        return Result.success();
    }

    @Override
    public HashMap<String, Integer> hasTableOrData(Long classId) {
        List<ScTableColumn> columnList = columnService.list(new QueryWrapper<ScTableColumn>().eq("class_id", classId));
        List<ScTableData> tableDataList = tableDataService.list(new QueryWrapper<ScTableData>().eq("class_id", classId));
        HashMap<String, Integer> map = new HashMap<>();
        map.put("columnCount", columnList.size());
        map.put("columnDataCount", tableDataList.size());
        return map;
    }
}
