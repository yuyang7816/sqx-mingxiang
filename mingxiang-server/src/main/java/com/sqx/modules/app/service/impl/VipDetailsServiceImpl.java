package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.VipDetailsService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.invite.entity.Invite;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.AliPayService;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.scTableClass.entity.ScTableClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VipDetailsServiceImpl extends ServiceImpl<VipDetailsDao, VipDetails> implements VipDetailsService {
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private WxService wxService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private InviteMoneyDao inviteMoneyDao;
    @Autowired
    private InviteDao inviteDao;
    @Autowired
    private UserDao userDao;

    @Override
    public IPage<VipDetails> getVipDetailsList(Integer page, Integer limit, Integer isEnable) {
        Page<VipDetails> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1,Integer.MAX_VALUE);
        }
        QueryWrapper<VipDetails> wrapper = new QueryWrapper<>();
        if (isEnable != null) {
            wrapper.eq("is_enable", isEnable);
        }
        wrapper.orderByDesc("update_time");
        return vipDetailsDao.selectPage(pages, wrapper);

    }

    @Override
    public Result insetVipDetails(VipDetails vipDetails) {
        VipDetails name = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("vip_name", vipDetails.getVipName()));
        if (name != null) {
            return Result.error("会员分类名称不能重复!");
        }
        vipDetails.setUpdateTime(new Date());
        return Result.upStatus(vipDetailsDao.insert(vipDetails));

    }

    @Override
    public Result updateVipDetails(VipDetails vipDetails) {
        vipDetails.setUpdateTime(new Date());
        return Result.upStatus(vipDetailsDao.updateById(vipDetails));
    }

    @Override
    public Result deleteVipDetails(Long id) {
        return Result.upStatus(vipDetailsDao.deleteById(id));


    }

    @Override
    public Result userBuyVip(Long userId, Long id, Integer classify, Integer type) throws Exception {
        VipDetails vipDetails = vipDetailsDao.selectById(id);
        if (vipDetails == null) {
            return Result.error("当前会员类型不存在!");
        }
        if (classify == 0) {
            //余额
            return balancePay(userId, id);
        } else if (classify == 1 || classify == 2 || classify == 3) {
            //微信
            return wxService.payMoney(userId, vipDetails.getMoney(), vipDetails.getId(), classify, type);
        } else if (classify == 4 || classify == 5) {
            //支付宝支付
            return aliPayService.payMoney(userId, vipDetails.getMoney(), vipDetails.getId(), classify, type);
        } else {
            return Result.error("支付方式错误!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result balancePay(Long userId, Long id) {
        VipDetails vipDetails = vipDetailsDao.selectById(id);
        if (vipDetails == null) {
            return Result.error("当前会员类型不存在!");
        }
//        UserMoney wallet = userMoneyService.selectUserMoneyByUserId(userId);
        UserEntity userEntity = userService.queryByUserId(userId);
        if (userEntity.getBalance().compareTo(vipDetails.getMoney()) >= 0) {
//            userMoneyService.updateMoney(2, userId, vipDetails.getMoney().doubleValue());
            userEntity.setBalance(userEntity.getBalance().subtract(vipDetails.getMoney()));
            userDao.updateById(userEntity);
            buyVip(userId, vipDetails, 0);
            String generateOrderId = getGeneralOrder();
            PayDetails payDetails = new PayDetails();
            payDetails.setOrderId(generateOrderId);
            payDetails.setMoney(vipDetails.getMoney().doubleValue());
            payDetails.setUserId(userId);
            payDetails.setType(2);
            payDetails.setClassify(0);
            payDetails.setParentId(id);
            payDetails.setState(1);
            payDetails.setCreateTime(new SimpleDateFormat().format(new Date()));
            payDetails.setRemark("会员购买");
            payDetails.setPayTime(new SimpleDateFormat().format(new Date()));
            payDetailsService.save(payDetails);
            return Result.success("购买成功!");
        } else {
            return Result.error("你的余额不足,请充值后重试!");
        }
    }
    private String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

    /**
     * 微信 支付宝（购买会员、充值金额）回调
     *
     * @param outTradeNo 支付订单号
     */
    @Override
    public void callbackBusiness(String outTradeNo) {
        PayDetails payDetails = payDetailsService.getOne(new QueryWrapper<PayDetails>().eq("order_id", outTradeNo));
        VipDetails details = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("id", payDetails.getParentId()));
        if (payDetails.getState() == 1) {
            if (payDetails.getType() == 2) {
                buyVip(payDetails.getUserId(), details, payDetails.getClassify());
            } else {
                userMoneyService.updateMoney(1, payDetails.getUserId(), payDetails.getMoney());
                UserMoneyDetails moneyDetails = new UserMoneyDetails();
                moneyDetails.setTitle("余额充值");
                moneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                moneyDetails.setUserId(payDetails.getUserId());
                moneyDetails.setClassify(payDetails.getClassify());
                moneyDetails.setType(1);
                moneyDetails.setState(2);
                moneyDetails.setContent("余额充值:" + payDetails.getMoney() + "元");
                moneyDetails.setCreateTime(DateUtils.format(new Date()));
                userMoneyDetailsService.save(moneyDetails);
//                userMoneyDetailsService.insertLanguageDetails(moneyDetails, "10055",null, "10056",new String[]{String.valueOf(payDetails.getMoney())});
            }
        }

    }

    @Override
    public Result giveUserVip(Long userId, Integer num) {
        UserEntity userEntity = userService.getById(userId);
        if (userEntity.getVipEndTime() != null) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                userEntity.setVipEndTime(LocalDateTime.now().plusDays(num));
            } else {
                userEntity.setVipEndTime(userEntity.getVipEndTime().plusDays(num));
            }
        } else {
            userEntity.setVipEndTime(LocalDateTime.now().plusDays(num));
        }
        userEntity.setIsVip(1);
        userService.updateById(userEntity);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setUserId(userId.toString());
        messageInfo.setTitle("赠送会员");
        messageInfo.setContent("系统赠送您会员" + "共" + num + "天");
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Override
    public Result cancelUserVip(Long userId) {
        UserEntity userEntity = userService.getById(userId);
        userEntity.setIsVip(0);
        userEntity.setVipEndTime(LocalDateTime.now());
        return userService.updateById(userEntity) ? Result.success() : Result.error();
    }

    /**
     * @param userId   用户id
     * @param details  会员信息
     * @param classify 支付类型
     */
    private void buyVip(Long userId, VipDetails details, Integer classify) {
        UserEntity userEntity = userService.getById(userId);
        userEntity.setIsVip(1);
        if (userEntity.getVipEndTime() != null) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                userEntity.setVipEndTime(LocalDateTime.now().plusMonths(details.getVipMonthCount()));
            } else {
                userEntity.setVipEndTime(userEntity.getVipEndTime().plusMonths(details.getVipMonthCount()));
            }
        } else {
            userEntity.setVipEndTime(LocalDateTime.now().plusMonths(details.getVipMonthCount()));
        }
        userService.updateById(userEntity);
        UserMoneyDetails moneyDetails = new UserMoneyDetails();
        moneyDetails.setTitle("购买会员《" + details.getVipName() + "》");
        moneyDetails.setMoney(details.getMoney());
        moneyDetails.setUserId(userId);
        moneyDetails.setClassify(1);
        moneyDetails.setType(2);
        moneyDetails.setState(2);
        moneyDetails.setContent("购买会员《" + details.getVipName() + "》花费:" + details.getMoney() + "元");
        moneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetailsService.save(moneyDetails);
//        userMoneyDetailsService.save(moneyDetails);
        CommonInfo commonInfo = commonInfoService.findOne(303);
        if ("是".equals(commonInfo.getValue())) {
            UserEntity parentEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("invitation_code", userEntity.getInviterCode()));
            //一级分佣
            if (parentEntity != null) {
                if (parentEntity.getZhiRate().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal rate = details.getMoney().multiply(parentEntity.getZhiRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    parentEntity.setBalance(parentEntity.getBalance().add(rate));
                    userService.updateById(parentEntity);
//                    InviteMoney inviteMoney = inviteMoneyDao.selectOne(new QueryWrapper<InviteMoney>()
//                            .eq("user_id", parentEntity.getUserId()));
//                    Invite invite1 = inviteDao.selectOne(new QueryWrapper<Invite>()
//                            .eq("user_id", userId)
//                            .eq("invitee_user_id",parentEntity.getUserId()));
//                    if (invite1 != null) {
//                        invite1.setMoney(invite1.getMoney() + rate.doubleValue());
//                        inviteDao.updateById(invite1);
//                    }
//                    if (inviteMoney == null){
//                        inviteMoney = new InviteMoney();
//                        inviteMoney.setUserId(userId);
//                        inviteMoney.setMoneySum(rate.doubleValue());
//                        inviteMoney.setMoney(rate.doubleValue());
//                        inviteMoneyDao.insert(inviteMoney);
//                    }else {
//                        inviteMoney.setMoneySum(inviteMoney.getMoneySum() + rate.doubleValue());
//                        inviteMoney.setMoney(inviteMoney.getMoney() + rate.doubleValue());
//                        inviteMoneyDao.updateById(inviteMoney);
//                    }
                    userMoneyDetailsService.setParentMoneyDetails(userId, parentEntity.getUserId(), rate, 1);
                    UserEntity parentParentEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("invitation_code", parentEntity.getInviterCode()));
                    //二级分佣
                    if (parentParentEntity != null) {
                        if (parentParentEntity.getFeiRate().compareTo(BigDecimal.ZERO) > 0) {
                            rate = details.getMoney().multiply(parentParentEntity.getFeiRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            parentParentEntity.setBalance(parentParentEntity.getBalance().add(rate));
                            userService.updateById(parentParentEntity);
//                            InviteMoney inviteMoney1 = inviteMoneyDao.selectOne(new QueryWrapper<InviteMoney>()
//                                    .eq("user_id", parentParentEntity.getUserId()));
//                            Invite invite2 = inviteDao.selectOne(new QueryWrapper<Invite>()
//                                    .eq("user_id", userId)
//                                    .eq("invitee_user_id",parentParentEntity.getUserId()));
//                            if (invite2 != null) {
//                                invite2.setMoney(invite2.getMoney() + rate.doubleValue());
//                                inviteDao.updateById(invite2);
//                            }
//                            if (inviteMoney1 == null){
//                                inviteMoney1 = new InviteMoney();
//                                inviteMoney1.setUserId(userId);
//                                inviteMoney1.setMoneySum(rate.doubleValue());
//                                inviteMoney1.setMoney(rate.doubleValue());
//                                inviteMoneyDao.insert(inviteMoney1);
//                            }else {
//                                inviteMoney1.setMoneySum(inviteMoney1.getMoneySum() + rate.doubleValue());
//                                inviteMoney1.setMoney(inviteMoney1.getMoney() + rate.doubleValue());
//                                inviteMoneyDao.updateById(inviteMoney1);
//                            }
                            userMoneyDetailsService.setParentMoneyDetails(userId, parentParentEntity.getUserId(), rate, 2);
                        }
                    }
                }
            }
        }
    }
}
