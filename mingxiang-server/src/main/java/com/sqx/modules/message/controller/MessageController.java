package com.sqx.modules.message.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
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
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/auditMessage")
    @ApiOperation("审核投诉")
    public Result auditMessage(Long messageId,Integer status,String auditContent){
        return messageService.auditMessage(messageId,status,auditContent);
    }

    @PostMapping("/auditCancellation")
    @ApiOperation("审核注销申请")
    public Result auditCancellation(Long messageId,Integer status,String auditContent){
        return messageService.auditCancellation(messageId,status,auditContent);
    }


    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(Integer page, Integer limit, MessageInfo messageInfo){

        return Result.success().put("data",messageService.selectMessageList(page,limit,messageInfo));
    }

    @RequestMapping(value = "/selectMessageByType", method = RequestMethod.GET)
    @ApiOperation("获取消息 type1为公告2位用户反馈 3为投诉服务 4为投诉店铺 5为用户消息 6客服消息")
    @ResponseBody
    public Result selectMessageByType(Integer page, Integer limit,MessageInfo messageInfo){
        return Result.success().put("data",messageService.selectMessageList(page,limit,messageInfo));
    }

    @RequestMapping(value = "/selectMessageDetails", method = RequestMethod.GET)
    @ApiOperation("获取消息详细信息")
    @ResponseBody
    public Result selectMessageDetails(Long id){
        return Result.success().put("data",messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    @ApiOperation("修改消息")
    @ResponseBody
    public Result updateMessage(@RequestBody MessageInfo messageInfo){
        return Result.success().put("data",messageService.update(messageInfo));
    }


    @RequestMapping(value = "/deleteMessageById", method = RequestMethod.POST)
    @ApiOperation("删除消息")
    @ResponseBody
    public Result deleteMessageById(Long id){
        return Result.success().put("data",messageService.delete(id));
    }

    @RequestMapping(value = "/insertMessage", method = RequestMethod.POST)
    @ApiOperation("添加消息")
    @ResponseBody
    public Result insertMessage(MessageInfo messageInfo){
        return Result.success().put("data",messageService.saveBody(messageInfo));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台公告详情")
    @ResponseBody
    public Result getMessage(@PathVariable Long id) {
        return Result.success().put("data",messageService.selectMessageById(id));
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


}