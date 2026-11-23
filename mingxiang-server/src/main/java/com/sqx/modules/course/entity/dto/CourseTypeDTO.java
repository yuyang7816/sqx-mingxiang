package com.sqx.modules.course.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/26 17:41
 */
@Data
public class CourseTypeDTO implements Serializable {

    /**
     *
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 类型名称
     */
    @ApiModelProperty("类型名称")
    private String typeName;

    /**
     * 类型描述
     */
    @ApiModelProperty("类型描述")
    private String remark;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    private Long parentId;

    /**
     * 级别： 1 一级 2 二级 3 三级
     */
    private Integer level;

//    /**
//     * 是否删除 0未删除 1已删除
//     */
//    @ApiModelProperty("是否删除 0未删除 1已删除")
//    private Integer deleteFlag;

//    /**
//     * 创建人
//     */
//    @ApiModelProperty("创建人")
//    private Long createBy;
//
//    /**
//     * 创建时间
//     */
//    @ApiModelProperty("创建时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date createTime;
//
//    /**
//     * 修改人
//     */
//    @ApiModelProperty("修改人")
//    private Long updateBy;
//
//    /**
//     * 修改时间
//     */
//    @ApiModelProperty("修改时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date updateTime;

//    /**
//     * 课程列表
//     */
//    @TableField(exist = false)
//    private List<CourseInfo> courseInfoList;
//
//    /**
//     * 子类型
//     */
//    @TableField(exist = false)
//    List<CourseType> children;

    private static final long serialVersionUID = 1L;
}
