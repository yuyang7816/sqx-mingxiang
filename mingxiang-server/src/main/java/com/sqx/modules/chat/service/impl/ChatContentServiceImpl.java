package com.sqx.modules.chat.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.chat.dao.ChatContentDao;
import com.sqx.modules.chat.entity.ChatContent;
import com.sqx.modules.chat.service.ChatContentService;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Service
public class ChatContentServiceImpl extends ServiceImpl<ChatContentDao, ChatContent> implements ChatContentService {


    @Override
    public IPage<Map<String,Object>> selectChatContentPage(Integer page, Integer limit, Long chatConversationId, String content) {
        Page<Map<String,Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }

        return baseMapper.selectChatContentPage(pages,chatConversationId,content);
    }

    @Override
    public int updateChatContentStatusByUserIdAndChatId(Long userId,Long chatConversationId){
        return baseMapper.updateChatContentStatusByUserIdAndChatId(userId, chatConversationId);
    }

    @Override
    public int selectChatCount(Long userId) {
        return baseMapper.selectChatCount(userId);
    }

}