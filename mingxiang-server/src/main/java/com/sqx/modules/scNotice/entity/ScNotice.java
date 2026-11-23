package com.sqx.modules.scNotice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("sc_notice")
public class ScNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    @TableId(type = IdType.AUTO)
    private Long noticeId;

    /**
     * 公告标题
     */
    @ApiModelProperty(name = "公告标题")
    private String title;

    /**
     * 公告内容
     */
    @ApiModelProperty(name = "公告内容")
    private String content;

    /**
     * 发布人id
     */
    @ApiModelProperty(name = "发布人id")
    private Long userId;
    /**
     * 发布人用户名
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 0未发布 1已发布
     */
    @ApiModelProperty(name = "0未发布 1已发布")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    private Date createTime;
    /**
     * 发布时间
     */
    @ApiModelProperty(name = "发布时间")
    private Date releaseTime;
}
