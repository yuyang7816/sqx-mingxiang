package com.sqx.modules.scFollow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.shop.entity.ScShop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liyuan
 * @description sc_follow
 * @date 2021-08-12
 */
@Data
@ApiModel("sc_follow")
public class ScFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("信息id")
    private Long followId;

    /**
     * id
     */
    @ApiModelProperty("信息id")
    private Long dataId;
    /**
     * 店铺id
     */
    @ApiModelProperty("店铺id")
    private Long shopId;
    /**
     * 1服务 2店铺
     */
    @ApiModelProperty("1服务 2店铺")
    private Long type;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    @TableField(exist = false)
    private ScTableData scTableData;
    @TableField(exist = false)
    private ScShop scShop;

    public ScFollow() {
    }
}
