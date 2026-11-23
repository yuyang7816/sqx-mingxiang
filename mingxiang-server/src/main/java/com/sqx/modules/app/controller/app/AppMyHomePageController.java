package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.service.MyHomePageService;
import com.sqx.modules.app.service.impl.MyHomePageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/25 15:47
 */
@RestController
@RequestMapping("/app/myHomePage")
@Api(tags = "app用户端我的主页相关接口(今日 全部类型的统计)")
public class AppMyHomePageController {

    @Autowired
    private MyHomePageService myHomePageService;


    @Login
    @GetMapping("/selectMyTodayData")
    @ApiOperation("app用户查看今日我的数据")
    public Result selectMyTodayData(@RequestAttribute("userId") Long userId,String startTime) {
        return myHomePageService.selectMyTodayData(userId,startTime);
    }

    @Login
    @GetMapping("/selectMyAllData")
    @ApiOperation("app用户查看全部我的数据")
    public Result selectMyAllData(@RequestAttribute("userId") Long userId) {
        return myHomePageService.selectMyAllData(userId);
    }

    @Login
    @GetMapping("/selectMyAllTypeData")
    @ApiOperation("app用户全部分类的数据统计数冥想、睡眠、专注的累计次数、累计分钟、累计天数(查今日的话传startTime)")
    public Result selectMyAllTypeData(@RequestAttribute("userId") Long userId,String startTime,Integer type) {
        return myHomePageService.selectMyAllTypeData(userId,startTime,type);
    }


    @GetMapping("/selectMyCenter")
    @ApiOperation("app用户全部分类的数据统计数冥想、睡眠、专注的累计次数、累计分钟、累计天数")
    public Result selectMyCenter(Long userId) {
        return myHomePageService.selectMyCenter(userId);
    }

}
