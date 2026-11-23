package com.sqx.modules.timedtask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.shop.entity.ScShop;
import com.sqx.modules.shop.service.ScShopService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class TimedTask {
    @Autowired
    private UserService userService;
    @Autowired
    private ScTableDataService tableDataService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private ScShopService scShopService;
    /**
     * 用户VIP是否到期
     */
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void checkUserTime() {
        List<UserEntity> list = userService.list(new QueryWrapper<UserEntity>().eq("is_vip", 1));
        for (UserEntity userEntity : list) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                //将用户改为非会员
                userEntity.setIsVip(0);
                userService.updateById(userEntity);
            }
        }
    }
    /**
     * 用户VIP是否到期(提前5天(advance天)提醒)
     */
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void advanceUserTime() {
        List<UserEntity> list = userService.list(new QueryWrapper<UserEntity>().eq("is_vip", 1));
        CommonInfo advance = commonInfoService.findOne(295);
        for (UserEntity userEntity : list) {
            Duration userHours = Duration.between(LocalDateTime.now(),userEntity.getVipEndTime());
           if (userHours.toDays()==Integer.parseInt(advance.getValue())){
               DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
               List<String> msgList = new ArrayList<>();
               msgList.add("VIP会员");
               msgList.add(fmt.format(userEntity.getVipEndTime()));
               msgList.add("为了您的服务数据不中断,请及时续费");
               CommonInfo commonInfo = commonInfoService.findOne(294);
                if (StringUtils.isNotBlank(userEntity.getOpenId())){
                    SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), commonInfo.getValue(), msgList, 3);
                }
               if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                   userService.pushToSingle("VIP即将到期提醒","为了您的服务数据不中断,请及时续费",userEntity.getClientid());
               }
           }
        }
    }
//    /**
//     * VIP到期后,将其他已上架信息下架,只保留免费条数的时间最近发布的数据。
//     */
//    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
//    public void downDataVipTime() {
//        List<UserEntity> userList = userService.list(new QueryWrapper<UserEntity>().eq("is_vip", 0));
//        for (UserEntity userEntity : userList) {
//            int count = tableDataService.count(new QueryWrapper<ScTableData>().eq("user_id", userEntity.getUserId()).in("status", 1,2));
//            int maxCount = Integer.parseInt(commonInfoService.findOne(308).getValue());
//            if (count > maxCount) {
//                List<ScTableData> dataList = tableDataService.list(new QueryWrapper<ScTableData>().in("status", 1, 2).eq("user_id", userEntity.getUserId()).orderByDesc("create_time").last("limit " + maxCount));
//                if (dataList.size() > 0) {
//                    ArrayList<Long> arrayList = new ArrayList<>();
//                    for (ScTableData tableData : dataList) {
//                        arrayList.add(tableData.getDataId());
//                    }
//                    ScTableData scTableData = new ScTableData();
//                    scTableData.setIsEnable(0);
//                    scTableData.setState(2);
//                    tableDataService.update(scTableData, new QueryWrapper<ScTableData>().in("status", 1,2).eq("user_id", userEntity.getUserId()).notIn("data_id", arrayList));
//                }
//            }
//
//            int shopCount = scShopService.count(new QueryWrapper<ScShop>().eq("user_id", userEntity.getUserId()).in("status", 1,2));
//            int maxShopCount = Integer.parseInt(commonInfoService.findOne(307).getValue());
//            if (shopCount > maxShopCount) {
//                List<ScShop> dataList = scShopService.list(new QueryWrapper<ScShop>().in("status", 1, 2).eq("user_id", userEntity.getUserId()).orderByDesc("create_time").last("limit " + maxCount));
//                if (dataList.size() > 0) {
//                    ArrayList<Long> arrayList = new ArrayList<>();
//                    for (ScShop scShop : dataList) {
//                        arrayList.add(scShop.getShopId());
//                    }
//                    ScShop scShop = new ScShop();
//                    scShop.setIsEnable(0);
//                    scShop.setState(2);
//                    scShopService.update(scShop, new QueryWrapper<ScShop>().in("status", 1,2).eq("user_id", userEntity.getUserId()).notIn("shop_id", arrayList));
//                }
//            }
//
//
//        }
//    }
//    /**
//     * VIP续费后,将系统下架的服务上架。
//     */
//    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
//    public void upDataVipTime() {
//        List<UserEntity> userList = userService.list(new QueryWrapper<UserEntity>().eq("is_vip",1));
//        for (UserEntity userEntity : userList) {
//            List<ScTableData> dataList = tableDataService.list(new QueryWrapper<ScTableData>().eq("user_id", userEntity.getUserId()).eq("state", 2));
//            for (ScTableData scTableData : dataList) {
//                scTableData.setState(1);
//                tableDataService.updateById(scTableData);
//            }
//
//        }
//    }
//    /**
//     * 每天给用户补充刷新次数
//     */
//    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Shanghai")
//    public void userRefreshCount() {
//        List<UserEntity> list = userService.list();
//        for (UserEntity userEntity : list) {
//            Integer freeRefreshCount = Integer.valueOf(commonInfoService.findOne(285).getValue());
//            Integer vipRefreshCount = Integer.valueOf(commonInfoService.findOne(286).getValue());
//            UserEntity entity = new UserEntity();
//            if (userEntity.getIsVip() == 1) {
//                entity.setRefreshCount(vipRefreshCount);
//            } else {
//                entity.setRefreshCount(freeRefreshCount);
//            }
//            userService.update(userEntity,new QueryWrapper<UserEntity>().eq("user_id", userEntity.getUserId()));
//        }
//    }
}
