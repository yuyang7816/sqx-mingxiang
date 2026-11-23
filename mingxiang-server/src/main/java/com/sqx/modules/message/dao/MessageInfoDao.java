package com.sqx.modules.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.message.entity.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author fang
 * @date 2020/7/9
 */
@Mapper
public interface MessageInfoDao extends BaseMapper<MessageInfo> {

    int updateSendState(@Param("userId") Long userId);

    Page<MessageInfo> find(@Param("pages")Page<MessageInfo> pages, @Param("state") String state);

    Page<MessageInfo> findType(@Param("pages")Page<MessageInfo> pages,@Param("type") Integer type);

    Page<MessageInfo> findTypeByUserId(@Param("pages")Page<MessageInfo> pages,@Param("type") String type, @Param("userId") String userId);
}
