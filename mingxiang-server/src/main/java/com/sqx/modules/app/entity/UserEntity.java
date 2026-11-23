package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 用户
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 1男 2女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 微信小程序openId
     */
    private String openId;

    /**
     * 微信App  openId
     */
    private String wxOpenId;

    /**
     * 密码
     */
    private String password;
    /**
     * 加密盐
     */
    private String salt;

    /**
     * 苹果id
     */
    private String appleId;

    /**
     * 手机类型 1安卓 2ios
     */
    private Integer sysPhone;

    /**
     * 状态 1正常 2禁用
     */
    private Integer status;

    /**
     * 来源 APP 小程序  公众号
     */
    private String platform;

    /**
     * app推送id
     */
    private String clientid;

    /**
     * 支付宝名称
     */
    private String zhiFuBaoName;

    /**
     * 支付宝账号
     */
    private String zhiFuBao;

    /**
     * 是否推送消息
     */
    private Integer isSendMsg;

    /**
     * 微信提现图片
     */
    private String wxImg;

    /**
     * 最近一条信息内容
     */
    private String msg;

    /**
     * 用户类型 0未认证 1普通用户 2企业用户
     */
    private Integer userType;

    /**
     * 是否是会员(0不是 1是)
     */
    private Integer isVip;

    /**
     * vip到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime vipEndTime;

    /**
     * 剩余刷新次数
     */
    private Integer refreshCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 邀请码
     */
    private String invitationCode;

    /**
     * 邀请人的邀请码
     */
    private String inviterCode;
    /**
     * 邀请人的人上级
     */
    @TableField("inviter_inviter_code")
    private String inviterInviterCode;
    /**
     * 一级分成比例
     */
    private BigDecimal zhiRate;
    /**
     * 二级分成比例
     */
    private BigDecimal feiRate;
   /**
     * 收益
     */
    private BigDecimal balance;


    @TableField(exist = false)
    private String money;

    @TableField(exist = false)
    private Integer followCount;

    /**
     * 累计登录天数
     */
    @ApiModelProperty("累计登录天数")
    private Integer totalLoginDays;

    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastLoginTime;


}
