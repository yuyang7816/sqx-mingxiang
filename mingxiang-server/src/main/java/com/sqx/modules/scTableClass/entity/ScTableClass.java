package com.sqx.modules.scTableClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分类(发布时的选项)对象 sc_table_class
 *
 * @author wuchang
 * @date 2022-08-10
 */
@Data
public class ScTableClass implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Long classId;

    /**
     * 分类名称
     */
    @ApiModelProperty(name = "分类名称")
    private String className;
 /**
     * 图标
     */
    @ApiModelProperty(name = "图标")
    private String classImg;

    /**
     * 是否启用 0不启用 1启用
     */
    @ApiModelProperty(name = "是否启用 0不启用 1启用")
    private Integer isEnable;

    /**
     * 备注
     */
    @ApiModelProperty(name = "备注")
    private String remarks;
    /**
     * 创建人Id
     */
    @ApiModelProperty(name = "创建人Id")
    private Long createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    private Date createTime;
}
