package com.sqx.modules.invite.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "邀请收益", tags = {"邀请收益"})
@RequestMapping(value = "/app/invite")
public class AppInviteController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private InviteMoneyDao inviteMoneyDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/selectInviteCount", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员数量")
    @ResponseBody
    public Result selectInviteCount(Integer state, Long userId) {
        return Result.success().put("data", inviteService.selectInviteCount(state, userId));
    }

    @Login
    @RequestMapping(value = "/selectInviteAndPoster", method = RequestMethod.GET)
    @ApiOperation("查看我的邀请码和海报二维码")
    @ResponseBody
    public Result selectInviteAndPoster(@RequestAttribute Long userId) {
        UserEntity userEntity = userService.queryByUserId(userId);
        CommonInfo one = commonInfoService.findOne(19);
        Map<String, Object> map = new HashMap<>();
        map.put("url", one.getValue());
        map.put("user", userEntity);
        return Result.success().put("data", map);
    }

    @Login
    @RequestMapping(value = "/selectInviteMoney", method = RequestMethod.GET)
    @ApiOperation("我的收益")
    @ResponseBody
    public Result selectInviteMoney(@RequestAttribute("userId") Long userId) {
        Double inviteMoney = inviteService.selectInviteSum(-1, userId);
        Integer inviteCount = inviteService.selectInviteCount(-1, userId);
        UserEntity userEntity = userService.queryByUserId(userId);
        // 已提现金额
        InviteMoney inviteMoneys = inviteMoneyDao.selectOne(new QueryWrapper<InviteMoney>()
                .eq("user_id", userId));
        Integer countChildren = userDao.selectCount(new QueryWrapper<UserEntity>().eq("inviter_code", userEntity.getInvitationCode()));
        Integer countChildrenChildren = userDao.selectCount(new QueryWrapper<UserEntity>().eq("inviter_inviter_code", userEntity.getInvitationCode()));
        BigDecimal oneMoney = userMoneyDetailsService.getOneProfit(userId, 1);
        BigDecimal twoMoney = userMoneyDetailsService.getOneProfit(userId, 2);
        Map<String, Object> result = new HashMap<>();
        result.put("inviteMoney", oneMoney.add(twoMoney));
        result.put("inviteCount", countChildren + countChildrenChildren);
        if (inviteMoneys != null) {
            BigDecimal cashOut = BigDecimal.valueOf(inviteMoneys.getCashOut());
            result.put("cashOut", cashOut);
        } else {
            result.put("cashOut", 0);
        }
        return Result.success().put("data", result);
    }

    @GetMapping("/mpCreateQr")
    @ApiOperation("小程序推广二维码")
    public void mpCreateQr(@RequestParam String invitationCode, HttpServletResponse response) {
        SenInfoCheckUtil.getPoster(invitationCode, response);
    }


    @Login
    @RequestMapping(value = "/selectInviteByUserIdLists", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员列表(查看所有邀请列表)")
    @ResponseBody
    public Result selectInviteByUserIdLists(int page, int limit, @RequestAttribute("userId") Long userId) {
        PageUtils pageUtils = inviteService.selectInviteUser(page, limit, userId, null);
        return Result.success().put("data", pageUtils);
    }

    @Login
    @ApiOperation("用户推广信息")
    @GetMapping(value = "getExtensionData")
    public Result getExtensionData(@RequestAttribute("userId") Long userId) {
        return Result.success().put("data", userService.getExtensionData(userId));
    }

    @Login
    @ApiOperation("获取推广用户列表")
    @GetMapping(value = "getExtensionList")
    public Result getExtensionList(@RequestAttribute("userId") Long userId, Integer type, Integer page, Integer limit) {
        return Result.success().put("data", userService.getExtensionList(userId, type, page, limit));
    }


    @Login
    @ApiOperation("获取邀请的用户收益列表")
    @GetMapping(value = "getProfitList")
    public Result getProfitList(@RequestAttribute("userId") Long userId, Integer type, Integer page, Integer limit) {
        return Result.success().put("data", userService.getProfitList(userId, type, page, limit));
    }

    @Login
    @ApiOperation("获取邀请的用户收益列表(有一、二级之分)")
    @GetMapping(value = "getLevelIncome")
    public Result getLevelIncome(@RequestAttribute("userId") Long userId, Integer type, Integer page, Integer limit) {
        return Result.success().put("data", userMoneyDetailsService.getLevelIncome(userId, type, page, limit));
    }

}