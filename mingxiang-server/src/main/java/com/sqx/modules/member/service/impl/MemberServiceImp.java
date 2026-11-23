package com.sqx.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.languageType.dao.LanguageTypeDao;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.member.dao.MemberDao;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员特权 服务实现类
 *
 * @author wuChang
 * @since 2023-12-21
 */
@Service
public class MemberServiceImp extends ServiceImpl<MemberDao, Member> implements MemberService {


    @Override
    public Result saveMember(Member member) {
        if (member.getMemberId() != null) {
            return Result.upStatus(baseMapper.updateByMemberId(member));
        } else {
            return Result.upStatus(baseMapper.insertByMember(member));
        }
    }

    @Override
    public IPage<Member> getMemberList(Integer page, Integer limit, Member member) {
        Page<Member> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1,Integer.MAX_VALUE);
        }
        return baseMapper.selectPageByLanguageType(pages, member);
    }

    @Override
    public Result deleteByMemberId(Long memberId) {
        Member member = baseMapper.selectByMemberId(memberId);
        if (member != null) {
            baseMapper.deleteByMemberId(memberId);
            return Result.success();
        }
        return Result.error("该会员特权不存在");
    }
}
