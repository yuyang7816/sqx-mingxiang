package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "会员管理", tags = {"会员管理"})
@RequestMapping(value = "/admin/vipDetails/")
public class VipDetailsController {

    @Autowired
    private VipDetailsService vipDetailsService;

    @ApiOperation("获取会员分类列表")
    @GetMapping("getVipDetailsList")
    public Result getVipDetailsList(Integer page, Integer limit) {
        return Result.success().put("data", vipDetailsService.getVipDetailsList(page, limit, null));
    }

    @ApiOperation("新增会员分类")
    @PostMapping("insetVipDetails")
    public Result insetVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.insetVipDetails(vipDetails);
    }

    @ApiOperation("修改会员分类")
    @PostMapping("updateVipDetails")
    public Result updateVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.updateVipDetails(vipDetails);
    }

    @ApiOperation("删除会员分类")
    @GetMapping("deleteVipDetails")
    public Result deleteVipDetails(Long id) {
        return vipDetailsService.deleteVipDetails(id);
    }

    @ApiOperation("赠送用户会员")
    @PostMapping("giveUserVip")
    public Result giveUserVip(Long userId, Integer num) {
        return vipDetailsService.giveUserVip(userId, num);
    }

    @ApiOperation("取消用户会员")
    @PostMapping("cancelUserVip")
    public Result cancelUserVip(Long userId) {
        return vipDetailsService.cancelUserVip(userId);
    }
}
