package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserCertificationDao;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.VerifyIdCardUtils;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class UserCertificationImpl extends ServiceImpl<UserCertificationDao, UserCertification> implements UserCertificationService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Override
    public Result insert(UserCertification userCertifications) {
        //查询身份证是否被绑定
        UserCertification certification = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("identity_card_number", userCertifications.getIdentityCardNumber()).eq("status", 1));
        if (certification != null && !certification.getUserId().equals(userCertifications.getUserId())) {
            return Result.error("身份证号已经被绑定");
        } else {
            userCertifications.setStatus(0);
            if (userCertifications.getId() != null) {
                UserCertification userCertification = baseMapper.selectById(userCertifications.getId());
                if (userCertification.getIsSubmit()==1){
                    return Result.error("您的认证已拒绝,无法再次提交");
                }
                baseMapper.updateById(userCertifications);
            } else {
                userCertifications.setCreateTime(LocalDateTime.now());
                baseMapper.insert(userCertifications);
            }
        }
        return Result.success();
    }


//    @Override
//    public Result isInsert(Long userId) {
//        //UserCertification userCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId).eq("status", 1));
//        UserEntity userEntity = userDao.selectById(userId);
//        if (userEntity.getIsAuthentication() != null && userEntity.getIsAuthentication()==1) {
//            return Result.success();
//        } else {
//            return Result.error();
//        }
//    }

    public static Integer getAgeByCertId(String certId) {
        String birthday = "";
        if (certId.length() == 18) {
            birthday = certId.substring(6, 10) + "/"
                    + certId.substring(10, 12) + "/"
                    + certId.substring(12, 14);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        Date birth = new Date();
        try {
            birth = sdf.parse(birthday);
        } catch (ParseException e) {
        }
        long intervalMilli = now.getTime() - birth.getTime();
        int age = (int) (intervalMilli / (24 * 60 * 60 * 1000)) / 365;
        return age;
    }


    @Override
    public IPage<UserCertification> queryCertification(Integer page, Integer limit, UserCertification userCertification) {
        Page<UserCertification> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        IPage<UserCertification> selectPage = baseMapper.selectPage(pages, new QueryWrapper<>(userCertification).orderByDesc("create_time"));
        for (UserCertification record : selectPage.getRecords()) {
            record.setUserEntity(userDao.selectById(record.getUserId()));
        }
        return selectPage;
    }

    @Override
    public Result queryUserCertification(IPage iPage, String name, String phone) {
        String nname = null;
        if (name != null && !(name.equals(""))) {
            nname = "%" + name + "%";
        }
        String nphone = null;
        if (phone != null && !(phone.equals(""))) {
            nphone = "%" + phone + "%";
        }

        return Result.success().put("data", baseMapper.queryUserCertification(iPage, nname, nphone));
    }


    @Override
    public Result queryInsert(Long userId) {
        return Result.success().put("data", baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId)));
    }

    @Override
    public Result auditorUserCertification(Integer status, Long id, String remark, Integer isSubmit) {
        UserCertification userCertification = baseMapper.selectById(id);
        if (userCertification != null) {
            UserEntity userEntity = userDao.selectById(userCertification.getUserId());
            if (userEntity==null){
                return Result.error("用户不存在");
            }
            if (status == 1) {
                Integer age = getAgeByCertId(userCertification.getIdentityCardNumber());
                userEntity.setUserType(userCertification.getUserType());
                userEntity.setAge(age);
                userDao.updateById(userEntity);
            }
            userCertification.setIsSubmit(isSubmit);
            userCertification.setStatus(status);
            userCertification.setRemark(remark);
            if (StringUtils.isNotBlank(userEntity.getOpenId())){
                List<String> msgList = new ArrayList<>();
                msgList.add("实名认证");
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                msgList.add(fmt.format(userCertification.getCreateTime()));
                if (status == 1) {
                    msgList.add("审核已通过");
                } else {
                    msgList.add("审核未通过");
                }
                String str = "";
                if (remark.length() > 20) {
                    str = remark.substring(0, 16) + "...";
                } else {
                    str = remark;
                }
                CommonInfo one = commonInfoService.findOne(292);
                msgList.add(str);
                msgList.add(fmt.format(LocalDateTime.now()));
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
            }
            String content="";
            MessageInfo messageInfo = new MessageInfo();
            if (userCertification.getStatus()==1){
                content = "您的实名认证审核已通过";

            }else {
                content="您的实名认证审核未通过,请在实名认证页面查看原因";
            }
            messageInfo.setContent(content);
            messageInfo.setTitle("审核通知");
            messageInfo.setState(String.valueOf(6));
            messageInfo.setUserName(userEntity.getUserName());
            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
            messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);

            if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                userService.pushToSingle("审核通知", content,userEntity.getClientid());
            }
            return Result.upStatus(baseMapper.updateById(userCertification));
        } else {
            return Result.error("要审核的信息不存在！");
        }

    }


}
