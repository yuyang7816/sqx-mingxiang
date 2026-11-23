package com.sqx.modules.message.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.message.entity.MessageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.HashMap;
import java.util.Map;

public interface MessageService extends IService<MessageInfo> {

    IPage<MessageInfo> selectMessageList(Integer page, Integer limit,MessageInfo messageInfo);

    int saveBody(MessageInfo messageInfo);

    int update(MessageInfo messageInfo);

    int delete(Long id);

    MessageInfo selectMessageById(Long id);

    int updateSendState(Long userId);

    Result auditMessage(Long messageId,Integer status,String auditContent);

    int sendMessage(String content, String title, UserEntity userEntity);

    HashMap<String, Integer> getMessageSign(Long userId);

    MessageInfo getUserMessageInfoListLimit1(MessageInfo messageInfo);

    Result find(String state, Page<MessageInfo> pageable);

    Result findType(Integer type,Page<MessageInfo> pageable);

    Result findTypeByUserId(String type, String userId, Page<MessageInfo> pages);

    Result auditCancellation(Long messageId, Integer status, String auditContent);
}
