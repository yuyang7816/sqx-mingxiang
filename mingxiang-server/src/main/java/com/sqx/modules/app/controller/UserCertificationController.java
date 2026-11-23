package com.sqx.modules.app.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userCertification")
@Api(value = "实名认证-管理端", tags = {"实名认证-管理端"})
public class UserCertificationController {
    @Autowired
    private UserCertificationService userCertificationService;
    @Autowired
    private UserService userService;


    /**
     * 实名列表
     */
    @RequestMapping("/queryCertification")
    @ApiOperation("实名列表")
    public Result queryCertification(Integer page, Integer limit, UserCertification userCertification) {

        return Result.success().put("data", userCertificationService.queryCertification(page, limit, userCertification));
    }

    @PostMapping("/deleteCertification")
    @ApiOperation("删除实名认证信息")
    public Result deleteCertification(Long id){
        UserCertification userCertification = userCertificationService.getById(id);
        UserEntity userEntity = userService.getById(userCertification.getUserId());
        userCertificationService.removeById(id);
        userEntity.setUserType(0);
        userService.updateById(userEntity);
        return Result.success();
    }

    /**
     * 查询已经进行实名的用户
     */
    @RequestMapping("/queryUserCertification")
    @ApiOperation("查询已经进行实名的用户")
    public Result queryUserCertification(Long page, Long limit, String name,String phone) {
        if (page == null || limit == null) {
            return Result.error("分页条件为空");
        } else {
            IPage<UserCertification> iPage = new Page<>(page, limit);

            return userCertificationService.queryUserCertification(iPage,name,phone);
        }
    }

    /**
     * 审核实名认证
     */
    @RequestMapping("/auditorUserCertification")
    @ApiOperation("审核实名认证")
    public Result auditorUserCertification(Integer status, Long id, String remark,Integer isSubmit) {
        return userCertificationService.auditorUserCertification(status, id, remark,isSubmit);
    }

    @PostMapping("/updateUserCertification")
    @ApiOperation("修改实名认证信息")
    public Result updateUserCertification(@RequestBody UserCertification userCertification) {
        userCertificationService.updateById(userCertification);
        return Result.success();
    }

}
