package com.sqx.modules.scTableData.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.scTableData.vo.DataJsonVo;
import com.sqx.modules.utils.typeHandler.DataJsonListTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@TableName(autoResultMap = true)
public class ScTableData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 数据id
     */
    @TableId(type = IdType.AUTO)
    private Long dataId;

    /**
     * 分类id
     */
    @ApiModelProperty(name = "分类id")
    private Long classId;

    /**
     * 创建人
     */
    @ApiModelProperty(name = "创建人")
    private Long userId;

    /**
     * 1待审核 2审核通过 3审核未通过 4已下架
     */
    @ApiModelProperty(name = "1待审核 2审核通过 3审核未通过 4已下架")
    private Integer status;
    /**
     * 1正常 2vip过期超出次数的服务已下架
     */
    @ApiModelProperty(name = "1正常 2vip过期超出次数的服务已下架")
    private Integer state;
    /**
         * 是否上架 0否 1是
     */
    @ApiModelProperty(name = "是否上架 0否 1是")
    private Integer isEnable;

    /**
     * 拒绝原因
     */
    @ApiModelProperty(name = "拒绝原因")
    private String refuseReason;
    /**
     * 首页图
     */
    @ApiModelProperty(name = "首页图")
    private String titleImg;

    /**
     * 标题
     */
    @ApiModelProperty(name = "标题")
    private String dataTitle;

    /**
     * 联系方式
     */
    @ApiModelProperty(name = "联系方式")
    private String phone;

    /**
     * 轮播图
     */
    @ApiModelProperty(name = "轮播图")
    private String rotationImg;

    /**
     * 标签
     */
    @ApiModelProperty(name = "标签")
    private String label;
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
     * 县/区
     */
    @ApiModelProperty(name = "县/区")
    private String county;

    /**
     * 浏览次数
     */
    @ApiModelProperty(name = "浏览次数")
    private Integer browseCount;


    @TableField(typeHandler = DataJsonListTypeHandler.class)
    @ApiModelProperty(name = "其他字段")
    private List<DataJsonVo> dataJson;

    /**
     * 详细信息
     */
    @ApiModelProperty(name = "详细信息")
    private String details;

    /**
     * 是否是推荐(1是 0不是)
     */
    @ApiModelProperty(name = "是否是推荐(1是 0不是)")
    private Integer isRecommend;
    /**
     * 价格信息
     * 这里不是填写货币,而是包括货币及单位
     */
    @ApiModelProperty(name = "价格信息")
    private String priceInfo;

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
     * 经度
     */
    @ApiModelProperty(name = "经度")
    private Double lng;
    /**
     * 纬度
     */
    @ApiModelProperty(name = "纬度")
    private Double lat;
}
