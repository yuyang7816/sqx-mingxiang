package com.sqx.modules.invite.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.entity.Invite;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 邀请记录
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteDao, Invite> implements InviteService {


    @Autowired
    private InviteDao inviteDao;
    @Autowired
    private UserService userService;


    @Override
    public PageUtils selectInviteList(int page,int limit,Integer state,Long userId){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        if(state==null || state==-1){
            state=null;
        }
        return new PageUtils(inviteDao.selectInviteList(pages,state,userId));
    }


    @Override
    public PageUtils selectInviteUser(int page,int limit,Long userId,Integer state){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.selectInviteUser(pages,userId,state));
    }

    @Override
    public Integer selectInviteByUserIdCountNotTime(Long userId) {
        return inviteDao.selectInviteByUserIdCountNotTime(userId);
    }

    @Override
    public Integer selectInviteByUserIdCount(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdCount(userId,startTime,endTime);
    }

    @Override
    public Double selectInviteByUserIdSum(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdSum(userId,startTime,endTime);
    }

    @Override
    public Double sumInviteMoney(String time, Integer flag) {
        return inviteDao.sumInviteMoney(time,flag);
    }

    @Override
    public PageUtils inviteAnalysis(int page,int limit, String time, Integer flag) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.inviteAnalysis(pages,time,flag));
    }

    @Override
    public Integer selectInviteCount(Integer state,Long userId){
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteCount(state,userId);
    }

    @Override
    public Double selectInviteSum(Integer state, Long userId) {
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteSum(state,userId);
    }







}
