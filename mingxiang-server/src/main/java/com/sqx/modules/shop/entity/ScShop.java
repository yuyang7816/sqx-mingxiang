package com.sqx.modules.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Time;

@Data
public class ScShop implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 店铺id
     */
    @TableId(type = IdType.AUTO)
    private Long shopId;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "用户id")
    private Long userId;


    /**
     * 店铺名
     */
    @ApiModelProperty(name = "店铺名")
    private String shopName;
    /**
     * 状态 1待审核 2审核通过 3审核未通过 4已封禁
     */

    @ApiModelProperty(name = "状态")
    private Integer status;
     /**
     * 1正常 2vip过期超出次数的服务已下架
     */

    @ApiModelProperty(name = "1正常 2vip过期超出次数的服务已下架")
    private Integer state;
    /**
     * 店铺详细地址
     */
    @ApiModelProperty(name = "店铺详细地址")
    private String shopAddress;
    /**
     *是否是推荐 0否 1是
     */
    private Integer isRecommend;
    /**
     * 浏览次数
     */
    @ApiModelProperty(name = "浏览次数")
    private Integer browseCount;

    /**
     * 周一周二周三等
     */
    @ApiModelProperty(name = "周一周二周三等")
    private String businessData;

    /**
     * 营业开始时间
     */
    @JsonFormat(pattern = "HH-mm-ss")
    @ApiModelProperty(name = "营业开始时间")
    private Time statTime;

    /**
     * 营业结束时间
     */
    @JsonFormat(pattern = "HH-mm-ss")
    @ApiModelProperty(name = "营业结束时间")
    private Time entTime;

    /**
     * 简介
     */
    @ApiModelProperty(name = "简介")
    private String introduce;
    /**
     * 主图
     */
    @ApiModelProperty(name = "主图")
    private String shopTitleImg;
    /**
     * 主图
     */
    @ApiModelProperty(name = "轮播图")
    private String shopImg;
    /**
     * 店铺标签
     */
    @ApiModelProperty(name = "店铺标签")
    private String shopLabel;

    /**
     * 商铺经度
     */
    @ApiModelProperty(name = "商铺经度")
    private Double shopLng;

    /**
     * 商铺维度
     */
    @ApiModelProperty(name = "商铺维度")
    private Double shopLat;

    /**
     * 省
     */
    @ApiModelProperty(name = "省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(name = "市")
    private String city;

    /**
     * 区/县
     */
    @ApiModelProperty(name = "区/县")
    private String district;

    /**
     * 法人姓名
     */
    @ApiModelProperty(name = "法人姓名")
    private String realName;


    /**
     * 法人身份证
     */
    @ApiModelProperty(name = "法人身份证")
    private String identityCardNumber;
    /**
     * 营业执照
     */
    @ApiModelProperty(name = "营业执照")
    private String businessLicense;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /**
     * 审核备注
     */
    @ApiModelProperty(name = "审核备注")
    private String remarks;

    /**
     * 是否启用 0否 1是
     */
    @ApiModelProperty(name = "是否启用 0否 1是")
    private Integer isEnable;
    /**
     * 下架原因
     */
    @ApiModelProperty(name = "下架原因")
    private String banRemark;
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    @ApiModelProperty(name = "用户昵称")
    private String userName;
    /**
     * 距离
     */
    @TableField(exist = false)
    private String distance;
    /**
     * 手机号
     */
    @TableField(exist = false)
    private String phone;

    /**
     * 是否已关注 0否 1是
     */
    @TableField(exist = false)
    private Integer isFollow;
}
