package com.sqx.modules.message.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.message.entity.ActivityMessageInfo;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.ActivityMessageService;
import com.sqx.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/ActivityMessage")
public class ActivityMessageController {

    @Autowired
    private ActivityMessageService activityMessageService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台公告详情")
    @ResponseBody
    public Result getMessage(@PathVariable Integer id) {
        return Result.success().put("data",messageService.selectMessageById(Long.valueOf(id)));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("管理平台和用户端通用接口添加公告")
    @ResponseBody
    public Result addMessage(@RequestBody MessageInfo messageInfo) {
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("管理平台修改公告接口")
    @ResponseBody
    public Result uUpdate(@RequestBody MessageInfo messageInfo) {
        messageService.update(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除公告接口")
    public Result deleteMessage(@PathVariable Long id) {
        messageService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation("管理平台获取全部公告接口")
    @ResponseBody
    public Result getMessageList() {
        List<MessageInfo> all = messageService.list();
        return Result.success().put("data",all);
    }

    @RequestMapping(value = "/page/{state}/{page}/{limit}", method = RequestMethod.GET)
    @ApiOperation("管理平台分页查询公告接口")
    @ResponseBody
    public Result getBodyPage(@PathVariable String state, @PathVariable Integer page, @PathVariable int limit) {
        Page<MessageInfo> pages;
        if (page != null && limit != 0){
            pages = new Page<>(page, limit);
        }else {
            pages = new Page<>(1,Integer.MAX_VALUE);
        }
        return messageService.find(state,pages);
    }

    @RequestMapping(value = "/type/{type}/{page}/{limit}", method = RequestMethod.GET)
    @ApiOperation("管理平台通过类型获取接口 type1为公告2位用户反馈 3为系统消息 4为订单信息 5为用户消息 6客服消息")
    @ResponseBody
    public Result findType(@PathVariable Integer type, @PathVariable Integer page, @PathVariable int limit) {
        Page<MessageInfo> pages;
        if (page != null && limit != 0){
            pages = new Page<>(page, limit);
        }else {
            pages = new Page<>(1,Integer.MAX_VALUE);
        }
        return messageService.findType(type,pages);
    }

    @RequestMapping(value = "/findType/{userId}/{type}/{page}/{limit}", method = RequestMethod.GET)
    @ApiOperation("用户端获取消息列表 type 4为订单信息 5为用户消息")
    @ResponseBody
    public Result findType(@PathVariable String userId, @PathVariable String type, @PathVariable Integer page, @PathVariable int limit) {
        Page<MessageInfo> pages;
        if (page != null && limit != 0){
            pages = new Page<>(page, limit);
        }else {
            pages = new Page<>(1,Integer.MAX_VALUE);
        }
        return Result.success().put("data",messageService.findTypeByUserId(type, userId,pages));
    }

}