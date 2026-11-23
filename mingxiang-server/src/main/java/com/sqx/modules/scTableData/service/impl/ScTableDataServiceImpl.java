package com.sqx.modules.scTableData.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.scBrowse.entity.ScBrowse;
import com.sqx.modules.scBrowse.service.ScBrowseService;
import com.sqx.modules.scTableClass.entity.ScTableClass;
import com.sqx.modules.scTableClass.service.ScTableClassService;
import com.sqx.modules.scTableColumn.entity.ScTableColumn;
import com.sqx.modules.scTableColumn.service.ScTableColumnService;
import com.sqx.modules.scTableData.dao.ScTableDataDao;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.scTableData.vo.ScTableDataVo;
import com.sqx.modules.scTableData.vo.DataJsonVo;
import com.sqx.modules.utils.SenInfoCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class ScTableDataServiceImpl extends ServiceImpl<ScTableDataDao, ScTableData> implements ScTableDataService {
    @Autowired
    private ScTableDataDao scTableDataDao;
    @Autowired
    private ScTableClassService classService;
    @Autowired
    private ScTableColumnService columnService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private ScBrowseService scBrowseService;
    @Autowired
    private UserCertificationService certificationService;
    @Autowired
    private MessageService messageService;

    @Override
    public Result insertData(ScTableData scTableData) {
        UserEntity userEntity = userService.getById(scTableData.getUserId());
        UserCertification certification = certificationService.getOne(new QueryWrapper<UserCertification>().eq("user_id", scTableData.getUserId()));
        if (certification != null && certification.getStatus() != 1) {
            return Result.error("审核未通过");
        }
        switch (checkTableData(scTableData.getDataJson(), scTableData.getClassId())) {
            case 0:
                break;
            case 1:
                return Result.error("分类不存在");
            case 2:
                return Result.error("提交的字段有不是模板中包含的字段");
            case 3:
                return Result.error("提交的字段缺少模板中需要的字段");
            case 4:
                return Result.error("必填字段为空");
        }

        List<ScTableColumn> classList = columnService.list(new QueryWrapper<ScTableColumn>().eq("class_id", scTableData.getClassId()));
        List<DataJsonVo> dataJson = scTableData.getDataJson();
        for (DataJsonVo dataJsonVo : dataJson) {
            for (ScTableColumn tableColumn : classList) {
                if (dataJsonVo.getHtmlType() == 2 || dataJsonVo.getHtmlType() == 3 || dataJsonVo.getHtmlType() == 4 || dataJsonVo.getHtmlType() == 5) {
                    if (dataJsonVo.getColumnName().equals(tableColumn.getColumnName())) {
                        dataJsonVo.setOptionJson(tableColumn.getOptionJson());
                    }
                }
            }
        }
        scTableData.setDataJson(dataJson);
        QueryWrapper<ScTableData> wrapper = new QueryWrapper<ScTableData>().eq("user_id", scTableData.getUserId());
        //新增
        if (scTableData.getDataId() == null) {
            if (userEntity.getIsVip() != 1) {
                //系统允许最多发布数量
                Integer maxCount = Integer.valueOf(commonInfoService.findOne(308).getValue());
                Integer dataCunt = scTableDataDao.selectCount(wrapper);
                if (dataCunt >= maxCount) {
                    return Result.error("您提交或通过审核的信息已超过最大限制" + maxCount + "条,加入会员即可享受更多特权!");
                }
            }
            scTableData.setCreateTime(LocalDateTime.now());
            scTableData.setUpdateTime(LocalDateTime.now());
            scTableData.setIsRecommend(0);
            scTableData.setBrowseCount(0);
            scTableData.setUserId(scTableData.getUserId());
            return Result.upStatus(scTableDataDao.insert(scTableData));
            //修改
        } else {
            if (userEntity.getIsVip() != 1) {
                //修改时排除当前
                wrapper.ne("data_id", scTableData.getDataId());
                Integer maxCount = Integer.valueOf(commonInfoService.findOne(308).getValue());
                Integer dataCunt = scTableDataDao.selectCount(wrapper);
                if (dataCunt >= maxCount) {
                    return Result.error("您提交或通过审核的信息已超过最大限制" + maxCount + "条,加入会员即可享受更多特权!");
                }
            }
            ScTableData tableData = scTableDataDao.selectOne(new QueryWrapper<ScTableData>().eq("data_id", scTableData.getDataId()).eq("user_id", scTableData.getUserId()));
            if (tableData == null) {
                return Result.error("当前服务不存在!");
            }
            scTableData.setUpdateTime(LocalDateTime.now());
            return Result.upStatus(scTableDataDao.updateById(scTableData));
        }
    }

    @Override
    public Result submit(Long userId, Long dataId) {
        ScTableData scTableData = scTableDataDao.selectById(dataId);
        UserEntity userEntity = userService.getById(userId);
        if (userEntity.getIsVip() != 1) {
            QueryWrapper<ScTableData> wrapper = new QueryWrapper<ScTableData>().eq("user_id", userId).in("status", 1, 2);
            //系统允许最多发布数量
            Integer maxCount = Integer.valueOf(commonInfoService.findOne(308).getValue());
            Integer dataCunt = scTableDataDao.selectCount(wrapper);
            if (dataCunt >= maxCount) {
                return Result.error("您提交或通过审核的信息已超过最大限制" + maxCount + "条,加入会员即可享受更多特权!");
            }
        }
        scTableData.setStatus(1);
        return Result.upStatus(scTableDataDao.updateById(scTableData));
    }

    @Override
    public Result refreshData(Long userId, Long dataId) {
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        if (userEntity.getRefreshCount() > 0) {
            ScTableData scTableData = new ScTableData();
            scTableData.setDataId(dataId);
            scTableData.setUpdateTime(LocalDateTime.now());
            scTableDataDao.updateById(scTableData);
            userEntity.setRefreshCount(userEntity.getRefreshCount() - 1);
            userService.updateById(userEntity);
            return Result.success("刷新成功");
        } else {
            return Result.error("刷新失败,您今天无法再次刷新服务,开通VIP可享受发布无上限、增加刷新次数等特权!");
        }


    }

    @Override
    public Result cancelShow(Long userId, Long dataId) {
        ScTableData scTableData = scTableDataDao.selectOne(new QueryWrapper<ScTableData>().eq("data_id", dataId).eq("user_id", userId));
        scTableData.setStatus(0);
        return Result.upStatus(scTableDataDao.updateById(scTableData));

    }

    @Override
    public IPage<ScTableDataVo> getUserDataList(Integer page, Integer limit, Long userId) {
        Page<ScTableDataVo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return scTableDataDao.getUserDataList(pages, userId);


    }

    /**
     * 检测提交的参数是否符合模板字段。
     *
     * @return 0=正确 1=分类不存在 2提交的参数字段不是模板下定义的字段 3 提交的参数字段缺少模板定义的字段(不是必填的,可以为空,但必须传) 4模板必填字段为空
     */
    private Integer checkTableData(List<DataJsonVo> dataJson, Long classId) {
        //校验提交的分类是否存在
        ScTableClass tableClass = classService.getOne(new QueryWrapper<ScTableClass>().eq("class_id", classId));
        if (tableClass == null) {
            return 1;
        }
        List<ScTableColumn> tableColumnList = columnService.list(new QueryWrapper<ScTableColumn>().eq("class_id", classId));
        //校验提交的参数是否是分类下模板定义的字段。
        for (DataJsonVo dataJsonVo : dataJson) {
            List<ScTableColumn> columns = columnService.list(new QueryWrapper<ScTableColumn>().eq("column_name", dataJsonVo.getColumnName()).eq("class_id", classId));
            if (columns.size() <= 0) {
                log.error("提交的参数中字段" + dataJsonVo.getColumnName() + "不存在!");
                return 2;
            }
        }
        //校验提交的参数是否符合模板字段
        for (ScTableColumn tableColumn : tableColumnList) {
            if (!checkColumn(tableColumn.getColumnName(), dataJson)) {
                log.error("提交的参数中缺少字段" + tableColumn.getColumnName());
                return 3;
            }
        }
        //校验模板必填字段是否为空
        for (ScTableColumn tableColumn : tableColumnList) {
            for (DataJsonVo dataJsonVo : dataJson) {
                if (tableColumn.getColumnName().equals(dataJsonVo.getColumnName())) {
                    if (tableColumn.getIsRequired() == 1) {
                        if (StringUtils.isBlank(dataJsonVo.getColumnValue())) {
                            log.error("提交的参数中字段" + dataJsonVo.getColumnName() + "不能为空!");
                            return 4;
                        }
                    }
                }
            }
        }


        return 0;
    }

    /**
     * 校验提交的参数字段是否包含模板的字段。
     *
     * @param columnName 模板字段
     * @param dataJson   提交的字段
     * @return
     */
    private boolean checkColumn(String columnName, List<DataJsonVo> dataJson) {
        for (DataJsonVo dataJsonVo : dataJson) {
            if (columnName.equals(dataJsonVo.getColumnName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IPage<ScTableDataVo> getDataList(Integer page, Integer limit, ScTableDataVo scTableDataVo, Double lng, Double lat, Integer createTimeSort, Integer priceSort, Integer distanceSort) {
        Page<ScTableDataVo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return scTableDataDao.getDataList(pages, scTableDataVo, lng, lat, createTimeSort, priceSort, distanceSort);
    }

    @Override
    public IPage<ScTableDataVo> adminGetDataList(Integer page, Integer limit, ScTableDataVo scTableDataVo, Integer screen) {
        Page<ScTableDataVo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return scTableDataDao.adminGetDataList(pages, scTableDataVo, screen);
    }

    @Override
    public Result showAndDown(Long userId, Long dataId) {
        ScTableData scTableData = baseMapper.selectById(dataId);
        if (scTableData != null) {
            UserEntity userEntity = userService.getById(scTableData.getUserId());
            if (scTableData.getStatus() == 1) {
                return Result.error("当前服务未通过审核");
            }
            if (scTableData.getStatus() == 4) {
                return Result.error("当前服务已被管理员下架,请修改后重新提交审核");
            }
            if (userId != null && !userId.equals(scTableData.getUserId())) {
                return Result.error("你无权操作当前数据");
            }

            if (scTableData.getIsEnable() == 0) {
                if (userEntity.getIsVip() == 0) {
                    int maxCount = Integer.parseInt(commonInfoService.findOne(308).getValue());
                    int count = baseMapper.selectCount(new QueryWrapper<ScTableData>().eq("user_id", scTableData.getUserId()).eq("is_enable", 1));
                    if (count >= maxCount) {
                        return Result.error("您上架的服务已超过最大限制" + maxCount + "条,加入会员即可享受更多特权!");
                    }
                }
                scTableData.setIsEnable(1);
            } else {
                scTableData.setIsEnable(0);
            }
            return Result.upStatus(baseMapper.updateById(scTableData));
        } else {
            return Result.error("服务不存在");
        }
    }

    @Override
    public Result banData(Long dataId, String refuseReason) {
        ScTableData scTableData = baseMapper.selectById(dataId);
        if (scTableData != null) {
            if (scTableData.getStatus() == 2) {
                scTableData.setStatus(4);
                scTableData.setIsEnable(0);
            } else {
                scTableData.setStatus(2);
                scTableData.setIsEnable(1);
            }
            scTableData.setRefuseReason(refuseReason);
            return Result.upStatus(baseMapper.updateById(scTableData));
        } else {
            return Result.error("当前服务不存在,请刷新后重试");
        }
    }

    @Override
    public IPage<ScTableDataVo> getMyDataList(Long userId, Long classId, Integer status, Integer page, Integer limit) {
        Page<ScTableDataVo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return scTableDataDao.getMyDataList(pages, userId, classId, status);


    }

    @Override
    public Result examine(Integer res, Long dataId, String refuseReason) {
        ScTableData data = scTableDataDao.selectOne(new QueryWrapper<ScTableData>().eq("data_id", dataId));
        if (data == null) {
            return Result.error("当前信息不存在!");
        } else {
            if (res == 1) {
                data.setStatus(2);
            } else {
                data.setStatus(3);
                data.setRefuseReason(refuseReason);
            }
            UserEntity userEntity = userService.getById(data.getUserId());
            if (StringUtils.isNotBlank(userEntity.getOpenId())) {
                List<String> msgList = new ArrayList<>();
                msgList.add("服务发布");
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                msgList.add(fmt.format(data.getCreateTime()));
                if (res == 1) {
                    msgList.add("审核已通过");
                } else {
                    msgList.add("审核未通过");
                }
                String str = "";
                if (refuseReason.length() > 20) {
                    str = refuseReason.substring(0, 16) + "...";
                } else {
                    str = refuseReason;
                }
                CommonInfo one = commonInfoService.findOne(292);
                msgList.add(str);
                msgList.add(fmt.format(LocalDateTime.now()));
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
            }
            MessageInfo messageInfo = new MessageInfo();
            String content = "";
            if (res == 1) {
                content = "您的服务《" + data.getDataTitle() + "》审核已通过";
            } else {
                content = "您的服务《" + data.getDataTitle() + "》审核未通过,原因是:" + refuseReason;
            }
            messageInfo.setTitle("审核通知");
            messageInfo.setState(String.valueOf(6));
            messageInfo.setContent(content);
            messageInfo.setUserName(userEntity.getUserName());
            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
            messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);

            if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                userService.pushToSingle("审核通知", content, userEntity.getClientid());
            }

            return Result.upStatus(scTableDataDao.updateById(data));
        }
    }


    @Override
    public Result recommend(Long dataId) {
        ScTableData data = scTableDataDao.selectOne(new QueryWrapper<ScTableData>().eq("data_id", dataId));
        if (data == null) {
            return Result.error("当前服务不存在!");
        }
        if (data.getIsRecommend() == 0) {
            data.setIsRecommend(1);
            scTableDataDao.updateById(data);
            return Result.success("推荐成功!");
        } else {
            data.setIsRecommend(0);
            scTableDataDao.updateById(data);
            return Result.success("取消推荐成功!");
        }
    }

    @Override
    public Result deleteData(Long userId, Long dataId) {
        ScTableData tableData = scTableDataDao.selectOne(new QueryWrapper<ScTableData>().eq("user_id", userId).eq("data_id", dataId));
        if (tableData != null) {
            return Result.upStatus(scTableDataDao.deleteById(dataId));
        } else {
            return Result.error("当前服务不存在!");
        }
    }

    @Override
    public Result getDataById(Long userId, Long dataId) {
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        ScTableData tableData = scTableDataDao.selectOne(new QueryWrapper<ScTableData>().eq("data_id", dataId));
        if (tableData != null) {
            tableData.setBrowseCount(tableData.getBrowseCount() + 1);
            scTableDataDao.updateById(tableData);
            //历史浏览记录
            if (userEntity != null) {
                ScBrowse browse = scBrowseService.getOne(new QueryWrapper<ScBrowse>().eq("data_id", dataId).eq("user_id", userId));
                if (browse == null) {
                    ScBrowse scBrowse = new ScBrowse();
                    scBrowse.setDataId(dataId);
                    scBrowse.setUserId(userId);
                    scBrowse.setType(1);
                    scBrowse.setUpdateTime(new Date());
                    scBrowseService.save(scBrowse);
                } else {
                    browse.setUpdateTime(new Date());
                    scBrowseService.updateById(browse);
                }
            }
        }
        return Result.success().put("data", scTableDataDao.getDataById(userId, dataId));
    }


}
