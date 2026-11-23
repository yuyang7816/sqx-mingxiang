package com.sqx.modules.scTableColumn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.scTableClass.entity.ScTableClass;
import com.sqx.modules.scTableClass.service.ScTableClassService;
import com.sqx.modules.scTableColumn.dao.ScTableColumnDao;
import com.sqx.modules.scTableColumn.entity.ScTableColumn;
import com.sqx.modules.scTableColumn.service.ScTableColumnService;
import com.sqx.modules.scTableColumn.vo.ColumnData;
import com.sqx.modules.scTableColumn.vo.JsonRootBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScTableColumnServiceImpl extends ServiceImpl<ScTableColumnDao, ScTableColumn> implements ScTableColumnService {
    @Autowired
    private ScTableColumnDao scTableColumnDao;
    @Autowired
    ScTableClassService classService;

    @Override
    public Result insertScTable(JsonRootBean jsonRootBean) {
        ScTableClass tableClass = classService.getById(jsonRootBean.getClassId());
        if (tableClass == null) {
            return Result.error("当前分类不存在!");
        }
        List<ColumnData> columnData = jsonRootBean.getColumnData();
        //校验提交的字段是否重复,如果有则返回重复字段
        List<String> collect = columnData.stream().collect(Collectors.groupingBy(ColumnData::getColumnName, Collectors.counting()))
                .entrySet().stream().filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey).collect(Collectors.toList());
        if (collect.size() > 0) {
            StringBuilder str = new StringBuilder();
            str.append("同一分类下不能有重复提交的字段：");
            for (int i = 0; i < collect.size(); i++) {
                str.append(collect.get(i));
                if (i < collect.size() - 1) {
                    str.append(",");
                }
            }
            return Result.error(str.toString());
        }
        scTableColumnDao.delete(new QueryWrapper<ScTableColumn>().eq("class_id", tableClass.getClassId()));
        for (ColumnData columnDatum : columnData) {
            ScTableColumn tableColumn = new ScTableColumn();
            BeanUtils.copyProperties(columnDatum, tableColumn);
            tableColumn.setOptionJson(columnDatum.getOptionJson());
            tableColumn.setClassId(jsonRootBean.getClassId());
            tableColumn.setCreateTime(new Date());
            tableColumn.setUpdateTime(new Date());
            scTableColumnDao.insert(tableColumn);
        }
        return Result.success();
    }

    @Override
    public List<ScTableColumn> getTableByClassId(Long classId) {
        return scTableColumnDao.selectList(new QueryWrapper<ScTableColumn>().eq("class_id", classId).orderByAsc("sort"));

    }

    @Override
    public Result deleteTableByClassId(Long classId) {
        List<ScTableColumn> columnList = scTableColumnDao.selectList(new QueryWrapper<ScTableColumn>().eq("class_id", classId));
        if (columnList.size() > 0) {
            return Result.error("删除失败,改该模板下已有用户发布数据,无法删除!");
        }
        return Result.upStatus(scTableColumnDao.delete(new QueryWrapper<ScTableColumn>().eq("class_id", classId)));
    }
}
