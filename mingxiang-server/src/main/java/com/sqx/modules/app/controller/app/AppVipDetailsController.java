package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/vipDetails")
@Api("app 会员详情信息")
public class AppVipDetailsController {
    @Autowired
    private VipDetailsService vipDetailsService;

    @ApiOperation("获取会员分类列表")
    @GetMapping("getVipDetailsList")
    public Result getVipDetailsList(Integer page, Integer limit) {
        return Result.success().put("data", vipDetailsService.getVipDetailsList(page, limit, 1));
    }

    /**
     * @param id       关联id(如果是购买会员则是会员类型id)
     * @param classify 支付方式 0余额支付 1app  2 二维码支付  3小程序 公众号支付 4支付宝APP支付 5支付宝H5支付
     * @return
     * @throws Exception
     */
    @Login
    @ApiOperation("用户支付")
    @PostMapping("userBuyVip")
    public Result userBuyVip(@RequestAttribute("userId") Long userId, Long id, Integer classify) throws Exception {
        return vipDetailsService.userBuyVip(userId, id, classify, 2);
    }
}


