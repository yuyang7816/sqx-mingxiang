package com.sqx.modules.member.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员特权-用户端
 * @author wuChang
 * @since 2023-12-21
 */
@RestController
@Api(value = "会员特权-用户端", tags = {"会员特权-用户端"})
@RequestMapping("/app/member/")
public class AppMemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 获取会员特权列表
     * @param page 第几页
     * @param limit 多少条
     * @param member
     * @return
     */
    @ApiOperation("获取会员特权列表")
    @GetMapping(value = "getMemberList")
    public Result getMemberList(Integer page, Integer limit, Member member) {
        return Result.success().put("data", memberService.getMemberList(page, limit, member));
    }

}
