package com.sqx.modules.app.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.dao.UserCertificationDao;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.AppService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.utils.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/user")
@Api(value = "APP管理", tags = {"APP管理"})
public class AppController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserDao userDao;

    @Login
    @PostMapping("/updateSendMsg")
    @ApiOperation("修改是否接受消息推送")
    public Result updateSendMsg(@LoginUser UserEntity user, Integer isSendMsg) {
        user.setIsSendMsg(isSendMsg);
        userService.updateById(user);
        return Result.success();
    }


    @Login
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户端修改密码")
    public Result updatePwd(@LoginUser UserEntity user, String pwd, String oldPwd) {
        if (!user.getPassword().equals(new Sha256Hash(oldPwd, user.getSalt()).toHex())) {
            return Result.error("原始密码不正确！");
        }
        if (pwd.equals(oldPwd)) {
            return Result.error("新密码不能与旧密码相同！");
        }
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(new Sha256Hash(pwd, salt).toHex());
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @ApiOperation("用户端换绑手机号")
    @ResponseBody
    public Result updatePhone(@RequestAttribute("userId") Long userId, @RequestParam String phone, @RequestParam String msg) {
        return userService.updatePhone(phone, msg, userId);
    }

    @Login
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUser(@RequestBody UserEntity userEntity, @RequestAttribute("userId") Long userId) {
        userEntity.setUserId(userId);
        if (userEntity.getPhone() != null) {
            int count = userDao.selectCount(new QueryWrapper<UserEntity>().eq("phone", userEntity.getPhone())
                    .ne("user_id", userId));
            if (count > 0) {
                return Result.error("当前手机号已被其他人注册，请检查！");
            }
        }
        userService.updateById(userEntity);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserImageUrl", method = RequestMethod.POST)
    @ApiOperation("用户修改头像")
    @ResponseBody
    public Result updateUserImageUrl(@LoginUser UserEntity user, String avatar) {
        user.setAvatar(avatar);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @ApiOperation("用户修改昵称")
    @ResponseBody
    public Result updateUserName(@LoginUser UserEntity user, String userName) {
        user.setUserName(userName);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@LoginUser UserEntity user) {

        userService.updateById(user);
        return Result.success().put("data", user);
    }


    @RequestMapping(value = "/selectNewApp", method = RequestMethod.GET)
    @ApiOperation("升级检测")
    @ResponseBody
    public Result selectNewApp() {
        return Result.success().put("data", appService.selectNewApp());
    }

    @GetMapping(value = "/updateClientId")

    @ApiOperation("绑定ClientId")
    @ResponseBody
    @Login
    public Result updateClientId(String clientId, @RequestAttribute("userId") Long userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setClientid(clientId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @GetMapping("/selectUserByUserId")
    @ApiOperation("查看用户信息")
    @ResponseBody
    @Login
    public Result selectUserByUserId(@LoginUser UserEntity user, Long userId) {
        return userService.selectUserByUserId(userId, user.getUserId());
    }

    @GetMapping("/selectInfoByUserId")
    @ApiOperation("查看用户信息")
    @ResponseBody
    public Result selectInfoByUserId(Long userId) {
        UserEntity userEntity = userService.getById(userId);
        return Result.success().put("data", userEntity);
    }

    @GetMapping("/selectUserInfoByUserId")
    public Result selectUserInfoByUserId(Long userId) {
        UserEntity userEntity = userService.getById(userId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", userEntity.getUserId());
        hashMap.put("userName", userEntity.getUserName());
        hashMap.put("avatar", userEntity.getAvatar());
        return Result.success().put("data", hashMap);

    }

}
