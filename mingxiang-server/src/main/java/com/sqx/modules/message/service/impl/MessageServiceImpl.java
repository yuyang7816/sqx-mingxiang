package com.sqx.modules.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.chat.service.ChatConversationService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.shop.entity.ScShop;
import com.sqx.modules.shop.service.ScShopService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageInfoDao, MessageInfo> implements MessageService {

    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ScShopService shopService;
    @Autowired
    private ChatConversationService conversationService;
    @Autowired
    private ScTableDataService tableDataService;
    @Autowired
    private ScShopService scShopService;
    @Autowired
    private UserDao userDao;


    @Override
    public Result auditMessage(Long messageId, Integer status, String auditContent) {
        MessageInfo messageInfo = baseMapper.selectById(messageId);
        messageInfo.setStatus(status);
        UserEntity byId = userService.getById(messageInfo.getByUserId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (status == 2) {
            if (byId.getStatus().equals(1)) {
                byId.setStatus(2);
                userService.updateById(byId);
                MessageInfo messageInfos = new MessageInfo();
                messageInfos.setContent("当前账号存在违规行为，已被系统封禁，如有疑问，请联系客服！");
                messageInfos.setTitle("账号封禁通知");
                messageInfos.setState(String.valueOf(6));
                messageInfos.setUserName(byId.getUserName());
                messageInfos.setUserId(String.valueOf(byId.getUserId()));
                messageInfos.setCreateAt(sdf.format(new Date()));
                messageInfos.setIsSee("0");
                baseMapper.insert(messageInfos);
            }

        } else if (status == 3) {
            if ("4".equals(messageInfo.getState())) {
                ScTableData tableData = tableDataService.getById(messageInfo.getDataId());
                tableData.setStatus(4);
                tableData.setRefuseReason(auditContent);
                tableDataService.updateById(tableData);
            } else if ("5".equals(messageInfo.getState())) {
                ScShop scShop = shopService.getById(messageInfo.getShopId());
                scShop.setStatus(4);
                scShop.setBanRemark(auditContent);
                shopService.updateById(scShop);
            }
        }
        messageInfo.setAuditContent(auditContent);
        baseMapper.updateById(messageInfo);
        return Result.success();
    }

    @Override
    public int sendMessage(String content, String title, UserEntity userEntity) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(6));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageInfo.setIsSee("0");
        return saveBody(messageInfo);
    }

    @Override
    public HashMap<String, Integer> getMessageSign(Long userId) {
        HashMap<String, Integer> map = new HashMap<>();
        Integer messageCount = baseMapper.selectCount(new QueryWrapper<MessageInfo>().eq("user_id", userId).eq("is_see", 0));
        int chatCount = 0;
        List<Map<String, Object>> mapList = conversationService.selectChatConversationPage(null, null, userId, null, null).getRecords();
        for (Map<String, Object> sMap : mapList) {
            chatCount += Integer.parseInt(sMap.get("contentCount").toString());
        }
        map.put("messageCount", messageCount);
        map.put("chatCount", chatCount);
        return map;
    }

    @Override
    public MessageInfo getUserMessageInfoListLimit1(MessageInfo messageInfo) {

        return baseMapper.selectOne(new QueryWrapper<MessageInfo>().eq("user_id", messageInfo.getUserId()).orderByDesc("create_at").last("limit 1"));


    }


    @Override
    public IPage<MessageInfo> selectMessageList(Integer page, Integer limit, MessageInfo messageInfo) {
        Page<MessageInfo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        if (StringUtils.isNotBlank(messageInfo.getUserId())) {
            baseMapper.updateSendState(Long.valueOf(messageInfo.getUserId()));
        }
        IPage<MessageInfo> selectPage = baseMapper.selectPage(pages, new QueryWrapper<>(messageInfo).orderByDesc("create_at"));
        for (MessageInfo record : selectPage.getRecords()) {
            if (record.getUserId() != null) {
                UserEntity user = userService.getById(record.getUserId());
                if (user != null) {
                    record.setUserName(user.getUserName());
                }
            }
            if (record.getByUserId() != null) {
                UserEntity user = userService.getById(record.getByUserId());
                if (user != null) {
                    record.setByUserName(user.getUserName());
                }
            }
            if ("4".equals(record.getState())) {
                record.setTableData(tableDataService.getById(record.getDataId()));
            } else if ("5".equals(record.getState())) {
                record.setScShop(scShopService.getById(record.getShopId()));
            } else if (("8".equals(record.getState()))) {
                record.setUserEntity(userService.getById(record.getUserId()));
            }
        }


        return selectPage;
    }

    @Override
    public int saveBody(MessageInfo messageInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setStatus(0);
        messageInfo.setCreateAt(sdf.format(now));
        return messageInfoDao.insert(messageInfo);
    }

    @Override
    public int update(MessageInfo messageInfo) {
        return messageInfoDao.updateById(messageInfo);
    }

    @Override
    public int delete(Long id) {
        return messageInfoDao.deleteById(id);
    }

    @Override
    public MessageInfo selectMessageById(Long id) {
        return messageInfoDao.selectById(id);
    }

    @Override
    public int updateSendState(Long userId) {
        return messageInfoDao.updateSendState(userId);
    }

    @Override
    public Result find(String state, Page<MessageInfo> pageable) {
        return Result.success().put("data", messageInfoDao.find(pageable, state));
    }

    @Override
    public Result findType(Integer type, Page<MessageInfo> pageable) {
        return Result.success().put("data", messageInfoDao.findType(pageable, type));
    }

    @Override
    public Result findTypeByUserId(String type, String userId, Page<MessageInfo> pages) {
        return Result.success().put("data", messageInfoDao.findTypeByUserId(pages, type, userId));
    }

    @Override
    public Result auditCancellation(Long messageId, Integer status, String auditContent) {
        MessageInfo messageInfo = baseMapper.selectById(messageId);
        if (messageInfo == null) {
            return Result.error("消息不存在！");
        }
        UserEntity userEntity = userService.getById(messageInfo.getUserId());
        if (userEntity == null) {
            return Result.error("用户不存在，请检查！");
        }
        if ("8".equals(messageInfo.getState())) {
            if (StringUtils.isNotBlank(auditContent)) {
                messageInfo.setAuditContent(auditContent);
            }
            messageInfo.setStatus(status);
            if (status == 1) {
                userDao.deleteById(userEntity.getUserId());
            }
            baseMapper.updateById(messageInfo);
            return Result.success();
        } else {
            return Result.error("当前不是注销信息，不能操作，请检查！");
        }
    }

}
