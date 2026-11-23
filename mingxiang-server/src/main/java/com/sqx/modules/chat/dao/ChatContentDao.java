package com.sqx.modules.chat.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.chat.entity.ChatContent;
import com.sqx.modules.common.entity.CommonInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface ChatContentDao extends BaseMapper<ChatContent> {

    IPage<Map<String,Object>> selectChatContentPage(Page<Map<String,Object>> pages,@Param("chatConversationId") Long chatConversationId,@Param("content") String content);

    int updateChatContentStatusByUserIdAndChatId(@Param("userId") Long userId,@Param("chatConversationId") Long chatConversationId);

    int selectChatCount(@Param("userId") Long userId);

    List<Map<String,Object>> selectChatContentCountByWx(@Param("minute") Integer minute,@Param("counts") Integer counts);

    List<Map<String,Object>> selectChatContentCountByDx(@Param("minute") Integer minute,@Param("counts") Integer counts);

}