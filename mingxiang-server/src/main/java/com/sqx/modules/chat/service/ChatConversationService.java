package com.sqx.modules.chat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.chat.entity.ChatContent;
import com.sqx.modules.chat.entity.ChatConversation;

import java.util.Map;


public interface ChatConversationService extends IService<ChatConversation> {

    IPage<Map<String,Object>> selectChatConversationPage(Integer page, Integer limit, Long userId, String nickName, String content);

    int insertChatConversation(ChatConversation chatConversation);

    ChatConversation selectChatConversation(Long userId,Long focusedUserId);

    Result insertChatConversations(ChatConversation chatConversation);

}