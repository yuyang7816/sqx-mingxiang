package com.sqx.modules.member.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员特权-管理端
 * @author wuChang
 * @since 2023-12-21
 */
@RestController
@Api(value = "会员特权-管理端", tags = {"会员特权-管理端"})
@RequestMapping("/admin/member/")
public class AdminMemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 新增或修改会员特权
     * @param member
     * @return
     */
    @ApiOperation("新增或修改会员特权")
    @PostMapping(value = "saveMember")
    public Result saveMember(Member member) {
        return memberService.saveMember(member);
    }

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

    /**
     * 删除会员特权
     * @param memberId 特权id
     * @return
     */
    @ApiOperation("删除会员特权")
    @GetMapping(value = "deleteMember")
    public Result deleteMember(Long memberId) {
        return memberService.deleteByMemberId(memberId);
    }
}
