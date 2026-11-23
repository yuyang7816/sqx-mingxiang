package com.sqx.modules.course.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.course.entity.CourseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2025/9/4 16:54
 */
@Data
public class DateGroupVO implements Serializable {

    /**
     *  日期
     */
    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    /**
     * 课程信息列表
     */
    private List<CourseType> list;

}
