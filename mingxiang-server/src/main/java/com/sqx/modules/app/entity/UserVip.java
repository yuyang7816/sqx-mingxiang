package com.sqx.modules.app.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName user_vip
 */
@Data
public class UserVip implements Serializable {
    /**
     * 会员id
     */
    private Long vipId;

    /**
     * 会员类型0月1季2年
     */
    private Integer vipNameType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 购买时间
     */
    private String createTime;

    /**
     * 到期时间
     */
    private String endTime;

    /**
     * 1是会员2不是
     */
    private Integer isVip;

    private static final long serialVersionUID = 1L;
}