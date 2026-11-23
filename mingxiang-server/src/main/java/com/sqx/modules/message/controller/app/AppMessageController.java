package com.sqx.modules.message.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.shop.service.ScShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @Login
    @GetMapping("/getUserMessageInfoList")
    @ApiOperation("消息列表")
    public Result getUserMessageInfoList(Integer page, Integer limit, @RequestAttribute("userId") Long userId, MessageInfo messageInfo) {
        messageInfo.setUserId(userId.toString());
        messageService.updateSendState(userId);
        return Result.success().put("data", messageService.selectMessageList(page, limit, messageInfo));
    }

    @Login
    @GetMapping("/getUserMessageInfoListLimit1")
    @ApiOperation("查看最新消息")
    public Result getUserMessageInfoListLimit1(@RequestAttribute("userId") Long userId, MessageInfo messageInfo) {
        messageInfo.setUserId(userId.toString());
        return Result.success().put("data", messageService.getUserMessageInfoListLimit1(messageInfo));
    }

    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("添加投诉")
    public Result insertMessage(@RequestAttribute("userId") Long userId, @RequestBody MessageInfo messageInfo) {
        if ("8".equals(messageInfo.getState())) {
            UserEntity userEntity = userService.getById(userId);
            messageInfo.setTitle(userEntity.getPhone());
            messageInfo.setContent("注销");
            if (userEntity.getUserName() != null) {
                messageInfo.setUserName(userEntity.getUserName());
            }
            int count = messageService.count(new QueryWrapper<MessageInfo>().eq("user_id", userId).eq("state", 8));
            if (count > 0) {
                return Result.error("注销申请已提交,请勿重复提交");
            }
        }
        messageInfo.setUserId(String.valueOf(userId));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/updateMessage")
    @ApiOperation("修改")
    public Result updateMessage(@RequestBody MessageInfo messageInfo) {
        messageService.update(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/deleteMessage")
    @ApiOperation("删除")
    public Result deleteMessage(Long id) {
        messageService.delete(id);
        return Result.success();
    }

    @Login
    @GetMapping("/getMessageSign")
    @ApiOperation("获取未读消息数量")
    public Result getMessageSign(@RequestAttribute("userId") Long userId) {
        return Result.success().put("data", messageService.getMessageSign(userId));
    }

}