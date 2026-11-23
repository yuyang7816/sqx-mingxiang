package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.service.UserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/app/userCertification")
@Api(value = "实名认证-用户端", tags = {"实名认证-用户端"})
public class AppUserCertificationController {

    private UserCertificationService userCertificationService;

    /**
     * 实名认证
     **/
    @RequestMapping("/insert")
    @Login
    @ApiOperation("实名认证")
    public Result insert(@RequestAttribute Long userId, UserCertification userCertification) {
        userCertification.setUserId(userId);
        return userCertificationService.insert(userCertification);

    }

    /**
     * 查询自己的实名信息
     */
    @RequestMapping("/queryInsert")
    @Login
    @ApiOperation("查询自己的实名信息")
    public Result queryInsert(@RequestAttribute Long userId) {

        return userCertificationService.queryInsert(userId);
    }
}
