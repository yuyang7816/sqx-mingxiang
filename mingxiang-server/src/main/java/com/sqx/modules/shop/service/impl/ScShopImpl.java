package com.sqx.modules.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.shop.dao.ScShopDao;
import com.sqx.modules.shop.entity.ScShop;
import com.sqx.modules.shop.service.ScShopService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ScShopImpl extends ServiceImpl<ScShopDao, ScShop> implements ScShopService {
    @Autowired
    private ScShopDao scShopDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCertificationService certificationService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MessageService messageService;
    @Override
    public Result insertShop(Long userId, ScShop scShop) {
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        if (userEntity != null && userEntity.getUserType() == 2) {
            UserCertification certification = certificationService.getOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
            if (certification == null) {
                return Result.error("实名信息不存在");
            }
            if (userEntity.getIsVip() != 1) {
                QueryWrapper<ScShop> wrapper = new QueryWrapper<ScShop>().eq("user_id", scShop.getUserId());
                //系统允许最多发布数量
                Integer maxCount = Integer.valueOf(commonInfoService.findOne(307).getValue());
                Integer dataCunt = baseMapper.selectCount(wrapper);
                if (dataCunt >= maxCount) {
                    return Result.error("您提交或通过审核的店铺已超过最大限制" + maxCount + "条,加入会员即可享受更多特权!");
                }
            }
            scShop.setCreateTime(LocalDateTime.now());
            scShop.setUpdateTime(LocalDateTime.now());
            scShop.setUserId(userId);
            scShop.setStatus(1);
            scShop.setBrowseCount(0);
            scShop.setRealName(certification.getRealName());
            scShop.setIdentityCardNumber(certification.getIdentityCardNumber());
            scShop.setBusinessLicense(certification.getBusinessLicense());
            return Result.upStatus(scShopDao.insert(scShop));
        } else {
            return Result.error("只有认证企业才能发布店铺");
        }

    }

    @Override
    public IPage<ScShop> getShopList(Integer page, Integer limit, Double lng, Double lat, Integer screen, String shopName, String city) {
        if (lng == null || lat == null || lng == 0 || lat == 0) {
            lng = 121.47;
            lat = 31.23;
        }
        Page<ScShop> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return scShopDao.getShopList(pages, lng, lat, screen, shopName, city);


    }

    @Override
    public IPage<ScShop> getAdminShopList(Integer page, Integer limit, String shopName, Integer status, String userName, String phone, String city) {
        Page<ScShop> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }

        return scShopDao.getAdminShopList(pages, shopName, status,userName,phone,city);
    }

    @Override
    public Result examine(Long shopId, Integer res, String remarks) {
        ScShop shop = scShopDao.selectOne(new QueryWrapper<ScShop>().eq("shop_id", shopId));
        shop.setStatus(res);
        shop.setRemarks(remarks);
        UserEntity userEntity = userService.getById(shop.getUserId());
        if (StringUtils.isNotBlank(userEntity.getOpenId())) {
            List<String> msgList = new ArrayList<>();
            msgList.add("店铺发布");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            msgList.add(fmt.format(shop.getCreateTime()));
            if (res == 1) {
                msgList.add("审核已通过");
            } else {
                msgList.add("审核未通过");
            }
            String str = "";
            if (remarks.length() > 20) {
                str = remarks.substring(0, 16) + "...";
            } else {
                str = remarks;
            }
            CommonInfo one = commonInfoService.findOne(292);
            msgList.add(str);
            msgList.add(fmt.format(LocalDateTime.now()));
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
        }
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTitle("审核通知");

        String content = "";
        if (res == 1) {
            content="您的店铺《" + shop.getShopName() + "》审核已通过";
        } else {
            content="您的店铺《" +shop.getShopName() + "》审核未通过,原因是:" + remarks;
        }
        messageInfo.setContent(content);
        messageInfo.setState(String.valueOf(6));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);


        if (StringUtils.isNotEmpty(userEntity.getClientid())) {
            userService.pushToSingle("审核通知",content,userEntity.getClientid());
        }
        return Result.upStatus(scShopDao.updateById(shop));

    }

    @Override
    public Result updateGoods(Long userId, ScShop scShop) {
        UserEntity userEntity = userService.getById(userId);
        ScShop shop = scShopDao.selectOne(new QueryWrapper<ScShop>().eq("shop_id", scShop.getShopId()));
        if (shop == null) {
            return Result.error("当前店铺不存在!");
        }
        if (userEntity.getIsVip() != 1) {
            QueryWrapper<ScShop> wrapper = new QueryWrapper<ScShop>().eq("user_id", scShop.getUserId()).ne("shop_id", scShop.getShopId());
            //系统允许最多发布数量
            Integer maxCount = Integer.valueOf(commonInfoService.findOne(307).getValue());
            Integer dataCunt = baseMapper.selectCount(wrapper);
            if (dataCunt >= maxCount) {
                return Result.error("您提交或通过审核的店铺已超过最大限制" + maxCount + "条,加入会员即可享受更多特权!");
            }
        }
        if (scShop.getIsEnable() == 1) {
            scShop.setStatus(2);
        }
        return Result.upStatus(scShopDao.updateById(scShop));

    }

    @Override
    public ScShop getShopInfoById(Long shopId, Double lng, Double lat, Long userId) {
        if (lng == null || lat == null || lng == 0 || lat == 0) {
            lng = 121.47;
            lat = 31.23;
        }
        ScShop scShop = scShopDao.getShopInfoById(shopId, lng, lat, userId);
        if (userId != null & !scShop.getUserId().equals(userId)) {
            scShop.setBrowseCount(scShop.getBrowseCount() + 1);
        }
        baseMapper.updateById(scShop);
        return scShop;

    }

    @Override
    public IPage<ScShop> getMyShopList(Integer page, Integer limit, ScShop scShop) {
        Page<ScShop> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(scShop).orderByDesc("create_time"));
    }

    @Override
    public Result updateShopEnable(Long userId, Long shopId) {
        ScShop scShop = baseMapper.selectOne(new QueryWrapper<ScShop>().eq("user_id", userId).eq("shop_id", shopId));
        if (scShop == null) {
            return Result.error("您无权修改当前店铺或店铺不存在");
        }
        if (scShop.getStatus() == 4) {
            return Result.error("您的店铺已被管理员下架");
        }
        if (scShop.getIsEnable() == 0) {
            scShop.setIsEnable(1);
        } else {
            scShop.setIsEnable(0);
        }
        return Result.upStatus(baseMapper.updateById(scShop));

    }

    @Override
    public Result banShopEnable(Long shopId, String banRemark) {
        ScShop scShop = baseMapper.selectById(shopId);
        scShop.setBanRemark(banRemark);
        if (scShop.getStatus()==4){
            scShop.setStatus(2);
            scShop.setIsEnable(1);
        }else {
            scShop.setStatus(4);
            scShop.setIsEnable(0);
        }
        scShop.setBanRemark(banRemark);
        return Result.upStatus(baseMapper.updateById(scShop));
    }

    @Override
    public Result recommendShop(Long shopId) {

        ScShop scShop = baseMapper.selectById(shopId);
        if (scShop != null) {
            if (scShop.getIsRecommend() == 0) {
                scShop.setIsRecommend(1);
                baseMapper.updateById(scShop);
                return Result.success("推荐成功");
            } else {
                scShop.setIsRecommend(0);
                baseMapper.updateById(scShop);
                return Result.success("取消推荐成功");
            }
        } else {
            return Result.error("店铺不存在");
        }
    }

    @Override
    public Result showAndDown(Long userId, Long dataId) {
        ScShop scShop = baseMapper.selectById(dataId);
        if (scShop!=null){
            UserEntity userEntity = userService.getById(scShop.getUserId());
            if (scShop.getStatus() == 1) {
                return Result.error("当前服务未通过审核");
            }
            if (scShop.getStatus() == 4) {
                return Result.error("当前店铺已被管理员下架,请修改后重新提交审核");
            }
            if (userId != null && !userId.equals(scShop.getUserId())) {
                return Result.error("你无权操作当前数据");
            }
            if (scShop.getIsEnable() == 0) {
                if (userEntity.getIsVip()==0){
                    int maxCount = Integer.parseInt(commonInfoService.findOne(307).getValue());
                    int count = baseMapper.selectCount(new QueryWrapper<ScShop>().eq("user_id", scShop.getUserId()).eq("is_enable", 1));
                    if (count >= maxCount) {
                        return Result.error("您上架的店铺已超过最大限制" + maxCount + "条,加入会员即可享受更多特权!");
                    }
                }
                scShop.setIsEnable(1);
            } else {
                scShop.setIsEnable(0);
            }
            return Result.upStatus(baseMapper.updateById(scShop));
        }else {
            return Result.error("当前店铺不存在");
        }

    }
}
