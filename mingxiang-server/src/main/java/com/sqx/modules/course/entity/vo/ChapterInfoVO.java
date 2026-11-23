package com.sqx.modules.course.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 章节信息(ChapterInfo)实体类
 *
 * @author makejava
 * @since 2024-05-16 15:20:27
 */
@Data
public class ChapterInfoVO implements Serializable {
    private static final long serialVersionUID = -44745963348151976L;
//    /**
//     * id
//     */
//    private String id;

    /**
     * 章节id
     */
    private Long id;
    /**
     * 课程id
     */
    private String courseId;
    /**
     * 章节名称
     */
    private String chapterName;
    /**
     * 章节序号
     */
    private Integer chapterNum;
    /**
     * 章节链接
     */
    private String chapterUrl;
    /**
     * 章节时长
     */
    private String chapterDuration;

    /**
     * 是否收费
     */
    private Integer chapterCharge;
//    /**
//     * 章节金额
//     */
//    private BigDecimal chapterPrice;
////    /**
//     * 章节状态 0正常 1下架
//     */
//    private Integer chapterStatus;
//    /**
//     * 创建时间
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;
//    /**
//     * 创建人
//     */
//    private String createBy;
//    /**
//     * 修改时间
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime updateTime;
//    /**
//     * 修改人
//     */
//    private String updateBy;
//    /**
//     * 备注
//     */
//    private String remark;

}

