package com.sqx.modules.invite.controller;


import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "邀请收益", tags = {"邀请收益"})
@RequestMapping(value = "/admin/invite")
public class InviteController {

    @Autowired
    private UserService userService;
    @ApiOperation("用户推广信息")
    @GetMapping(value = "getExtensionData")
    public Result getExtensionData(Long userId) {
        return Result.success().put("data", userService.getExtensionData(userId));
    }

    @ApiOperation("获取推广用户列表")
    @GetMapping(value = "getExtensionList")
    public Result getExtensionList(Long userId,Integer type, Integer page, Integer limit) {
        return Result.success().put("data", userService.getExtensionList(userId,type, page, limit));
    }

    @ApiOperation("获取推广用户收益列表")
    @GetMapping(value = "getUserIncomeList")
    public Result getUserIncomeList(Long userId,Integer type, Integer page, Integer limit) {
        return Result.success().put("data", userService.getUserIncomeList(userId,type, page, limit));
    }

    @ApiOperation("获取邀请的用户收益列表")
    @GetMapping(value = "getProfitList")
    public Result getProfitList(Long userId,Integer type, Integer page, Integer limit) {
        return Result.success().put("data", userService.getProfitList(userId,type, page, limit));
    }


}