package com.sqx.modules.dataCenter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.scBrowse.service.ScBrowseService;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.shop.entity.ScShop;
import com.sqx.modules.shop.service.ScShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/app/dataCenter/")
@Api("数据中心")
public class DataCenter {

    @Autowired
    private ScBrowseService browseService;
    @Autowired
    private ScShopService scShopService;
    @Autowired
    private ScTableDataService dataService;
    @Autowired
    private UserCertificationService certificationService;

    @Login
    @GetMapping("/selectAmount")
    @ApiOperation("用户服务收藏 店铺关注 店铺粉丝 历史浏览")
    public Result selectAmount(@RequestAttribute("userId") Long userId) {
        return browseService.selectAmount(userId);
    }

    @GetMapping("/getWaitExamine")
    @ApiOperation("获取待审核的数据")
    public Result getWaitExamine() {
        HashMap<String, Object> hashMap = new HashMap<>();
        //店铺待审核
        int shopCount = scShopService.count(new QueryWrapper<ScShop>().eq("status", 1));
        //服务待审核
        int dataCount = dataService.count(new QueryWrapper<ScTableData>().eq("status", 1));
        //个人认证待审核
        int userCount = certificationService.count(new QueryWrapper<UserCertification>().eq("user_type", 1).eq("status", 0));
        //企业认证待审核
        int enterpriseCount = certificationService.count(new QueryWrapper<UserCertification>().eq("user_type", 2).eq("status", 0));
        hashMap.put("shopCount",shopCount);
        hashMap.put("dataCount",dataCount);
        hashMap.put("userCount",userCount);
        hashMap.put("enterpriseCount",enterpriseCount);
        return Result.success().put("data", hashMap);
    }
}
