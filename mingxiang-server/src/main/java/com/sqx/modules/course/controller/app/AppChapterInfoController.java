package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.service.ChapterInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/20 16:58
 */
@RestController
@RequestMapping("/app/chapterInfo")
@Api(tags = "app用户章节信息相关")
public class AppChapterInfoController {

    @Autowired
    private ChapterInfoService chapterInfoService;

    @Login
    @GetMapping("/selectDetailByApp")
    @ApiOperation("app用户查看章节信息详情")
    public Result selectDetailByApp(@RequestParam(required = false)@ApiParam("章节id") Long id,
                                    @RequestAttribute("userId") Long userId) {
        return chapterInfoService.selectDetailByApp(id, userId);
    }
}
