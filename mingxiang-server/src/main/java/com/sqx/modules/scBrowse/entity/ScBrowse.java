package com.sqx.modules.scBrowse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.shop.entity.ScShop;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sc_browse")
public class ScBrowse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 浏览记录id
     */
    @TableId(type = IdType.AUTO)
    private Long browseId;
    /**
     * 访问人id
     */
    private Long userId;
    /**
     * 信息id
     */
    private Long dataId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 浏览类型 1信息 2店铺
     */
    private Integer type;
   /**
     * 店铺id
     */
    private Long shopId;

    @TableField(exist = false)
    private ScTableData scTableData;
    @TableField(exist = false)
    private ScShop scShop;

}
