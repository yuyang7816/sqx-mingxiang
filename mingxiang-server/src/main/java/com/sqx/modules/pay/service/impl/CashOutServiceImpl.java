package com.sqx.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.pay.dao.CashOutDao;
import com.sqx.modules.pay.entity.CashOut;
import com.sqx.modules.pay.service.CashOutService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;
import weixin.popular.support.TokenManager;

import javax.websocket.SendResult;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现申请记录
 */
@Service
public class CashOutServiceImpl extends ServiceImpl<CashOutDao, CashOut> implements CashOutService {

    /**
     * 提现申请记录
     */
    @Autowired
    private CashOutDao cashOutDao;
    /**
     * 通用配置
     */
    @Autowired
    private CommonInfoService commonInfoService;
    /**
     * app用户
     */
    @Autowired
    private UserService userService;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private InviteMoneyDao inviteMoneyDao;

    @Override
    public PageUtils selectCashOutList(Map<String, Object> params) {
        String zhifubaoName = (String) params.get("zhifubaoName");
        String zhifubao = (String) params.get("zhifubao");
        String userId = String.valueOf(params.get("userId"));
        String classify = String.valueOf(params.get("classify"));
        String state = String.valueOf(params.get("state"));
        IPage<CashOut> page = this.page(
                new Query<CashOut>().getPage(params),
                new QueryWrapper<CashOut>()
                        .eq(StringUtils.isNotBlank(zhifubaoName), "zhifubao_name", zhifubaoName)
                        .eq(StringUtils.isNotBlank(zhifubao), "zhifubao", zhifubao)
                        .eq(StringUtils.isNotBlank(userId) && !"null".equals(userId), "user_id", userId)
                        .eq(StringUtils.isNotBlank(classify) && !"null".equals(classify) && "1".equals(classify), "classify", classify)
                        .eq(StringUtils.isNotBlank(state) && !"null".equals(state), "state", state)
                        .and(StringUtils.isNotBlank(classify) && !"null".equals(classify) && "2".equals(classify), wrapper -> wrapper.eq("classify", 2).or().eq("classify", 3))
                        .orderByDesc("id")
        );
        return new PageUtils(page);
    }


    @Override
    public CashOut selectById(Long id) {
        return cashOutDao.selectById(id);
    }

    @Override
    public int saveBody(CashOut cashOut) {
        return cashOutDao.insert(cashOut);
    }


    @Override
    public int update(CashOut cashOut) {
        return cashOutDao.updateById(cashOut);
    }


    @Override
    public void cashOutSuccess(UserEntity userByWxId, String date, String money, String payWay, String url) {
        if (userByWxId != null) {
            InviteMoney inviteMoney = inviteMoneyDao.selectOne(new QueryWrapper<InviteMoney>()
                    .eq("user_id", userByWxId.getUserId()));
            if (inviteMoney != null) {
                if (inviteMoney.getCashOut() == null) {
                    inviteMoney.setCashOut(Double.parseDouble(money));
                }
                inviteMoney.setCashOut(inviteMoney.getCashOut() + Double.parseDouble(money));
                inviteMoneyDao.updateById(inviteMoney);
            }else {
                inviteMoney = new InviteMoney();
                inviteMoney.setUserId(userByWxId.getUserId());
                inviteMoney.setMoneySum(0.00);
                inviteMoney.setCashOut(Double.parseDouble(money));
                inviteMoney.setMoney(0.00);
                inviteMoneyDao.insert(inviteMoney);
            }
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setState(String.valueOf(6));
            messageInfo.setContent("您好，您的提现转账成功，请注意查收！提现金额【" + money + "元】！感谢您的使用！如有疑问请在公众号中发送您的问题联系客服");
            messageInfo.setTitle("提现成功通知");
            messageInfo.setUserName(userByWxId.getUserName());
            messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            messageInfo.setCreateAt(sdf.format(now));
            messageInfo.setIsSee("0");
            messageInfoDao.insert(messageInfo);
            if (userByWxId.getClientid() != null) {
                userService.pushToSingle("提现成功通知", "您好，您的提现转账成功，请注意查收！提现金额【" + money + "元】！感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", userByWxId.getClientid());
            }
            CommonInfo three = commonInfoService.findOne(39);
            String apkey = "";
            if (three != null) {
                apkey = three.getValue();
            }
            if (StringUtils.isNotBlank(userByWxId.getOpenId())) {
                LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
                data.put("first", new TemplateMessageItem("您好，您的提现转账成功，请注意查收", "#d71345"));
                data.put("keyword1", new TemplateMessageItem(money + " 元", "#d71345"));
                data.put("keyword2", new TemplateMessageItem(date, "#d71345"));
                data.put("remark", new TemplateMessageItem("感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", null));
                sendWxMessage(apkey, data, userByWxId.getOpenId(), url);
            }
        }

    }

    /**
     * 退款成功通知
     *
     * @param
     * @param date
     * @param money
     * @param url
     */
    @Override
    public void refundSuccess(UserEntity userByWxId, String date, String money, String url, String content) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(6));
        messageInfo.setContent(content);
        messageInfo.setTitle("提现失败提醒");
        messageInfo.setUserName(userByWxId.getUserName());
        messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setCreateAt(sdf.format(now));
        messageInfo.setIsSee("0");
        messageInfoDao.insert(messageInfo);
        if (userByWxId.getClientid() != null) {
            userService.pushToSingle("提现失败提醒", content, userByWxId.getClientid());
        }
        CommonInfo three = commonInfoService.findOne(77);
        String apkey = "";
        if (three != null) {
            apkey = three.getValue();
        }
        if (StringUtils.isNotBlank(userByWxId.getOpenId())) {
            LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
            data.put("first", new TemplateMessageItem("您好，您发起的提现失败了", "#d71345"));
            data.put("keyword1", new TemplateMessageItem(money + " 元", "#d71345"));
            data.put("keyword2", new TemplateMessageItem(date, "#d71345"));
            data.put("keyword3", new TemplateMessageItem(content, "#d71345"));
            data.put("remark", new TemplateMessageItem("请您按照失败原因修改相关信息后，重新提现！", null));
            sendWxMessage(apkey, data, userByWxId.getOpenId(), url);
        }

    }

    @Override
    public Double selectCashOutSum(Long userId, Date startTime, Date endTime) {
        return cashOutDao.selectCashOutSum(userId, startTime, endTime);
    }

    @Override
    public Double sumMoney(String time, Integer flag) {
        return cashOutDao.sumMoney(time, flag);
    }

    @Override
    public Integer countMoney(String time, Integer flag) {
        return cashOutDao.countMoney(time, flag);
    }

    @Override
    public Integer stayMoney(String time, Integer flag) {
        return cashOutDao.stayMoney(time, flag);
    }

    @Override
    public void updateMayMoney(int i, Long userId, Double money) {
        cashOutDao.updateMayMoney(i, userId, money);
    }


    @Override
    public List<CashOut> selectCashOutLimit3() {
        return cashOutDao.selectCashOutLimit3();
    }

    private void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid, String url) {
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(openid);
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(data);
        templateMessage.setUrl(url);
        TemplateMessageResult templateMessageResult = MessageAPI.messageTemplateSend(getWxToken(), templateMessage);
        if (templateMessageResult.isSuccess()) {
            new SendResult();
        } else {
            new SendResult();
        }
    }

    private String getWxToken() {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            return TokenManager.getToken(one.getValue());
        } catch (Exception e) {
            throw new RuntimeException("GET_ACCESS_TOKEN_FAIL");
        }
    }


    @Override
    @Transactional
    public Result cashMoney(Long userId, BigDecimal money, Integer classify) {
        UserEntity userEntity = userService.selectUserById(userId);
        if (classify == null) {
            classify = 1;
        }
        CommonInfo one = commonInfoService.findOne(112);
        if (one != null && money.compareTo(new BigDecimal(one.getValue())) < 0) {
            return Result.error("输入金额不满足最低提现金额，请重新输入！");
        }

        //查询账户的余额
        if (userEntity.getBalance().compareTo(money) < 0) {
            return Result.error("金额不足，请输入正确的金额！");
        }

        //最高提现金额
        CommonInfo one2 = commonInfoService.findOne(153);
        if (one2 != null && money.compareTo(new BigDecimal(one2.getValue())) > 0) {
            return Result.error(-100, "输入金额过大，不能大于" + one2.getValue() + "，请重新输入！");
        }

        if (classify == 2 || classify == 3) {
            String value = commonInfoService.findOne(244).getValue();
            if ("2".equals(value)) {
                if (StringUtils.isEmpty(userEntity.getWxImg())) {
                    return Result.error("请绑定微信提现收款码！");
                }
            }
        }

        CommonInfo one3 = commonInfoService.findOne(152);
        //根据当前提现的金额获取手续费
        BigDecimal rate = money.multiply(new BigDecimal(one3.getValue()));
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        //扣除金额
        BigDecimal deductMoney;

        //用户余额减去提现的金额和手续费是否大于0
        if (userEntity.getBalance().subtract(money).subtract(rate).compareTo(BigDecimal.ZERO) >= 0) {
            deductMoney = rate.add(money);
        } else {
            //减去提现的费用
            money = money.subtract(rate);
            deductMoney = money.add(rate);
        }
        userEntity.setBalance(userEntity.getBalance().subtract(deductMoney));
        userService.updateById(userEntity);
        userMoneyDetails.setMoney(deductMoney);
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("提现：" + money);
        userMoneyDetails.setType(2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userMoneyDetails.setCreateTime(sdf.format(new Date()));

        CashOut cashOut = new CashOut();
        cashOut.setState(0);
        cashOut.setClassify(classify);
//        String content;
        if (classify == 2 || classify == 3) {
            cashOut.setWxImg(userEntity.getWxImg());
            userMoneyDetails.setContent("微信提现：" + money + "，扣除收益：" + deductMoney + "，手续费：" + rate);
//            content="20002";
        } else {
            userMoneyDetails.setContent("支付宝提现：" + money + "，扣除收益：" + deductMoney + "，手续费：" + rate);
//            content="20003";
        }
        cashOut.setZhifubao(userEntity.getZhiFuBao());
        cashOut.setZhifubaoName(userEntity.getZhiFuBaoName());
        cashOut.setMoney(money.toString());
        cashOut.setCreateAt(sdf.format(new Date()));
        cashOut.setUserId(userEntity.getUserId());
        cashOut.setRate(rate.doubleValue());
        cashOut.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        baseMapper.insert(cashOut);
        userMoneyDetailsService.save(userMoneyDetails);
//        userMoneyDetailsService.insertLanguageDetails(userMoneyDetails,"20001",new String[]{String.valueOf(money)},content,new String[]{String.valueOf(money), String.valueOf(deductMoney), String.valueOf(rate)});
        //扣除金额直接在数据库进行操作
        return Result.success("提现成功，将在三个工作日内到账，请耐心等待！");
    }

    @Override
    public IPage<CashOut> selectAdminHelpProfit(Integer page, Integer limit, String startTime, String endTime, CashOut cashOut) {
        Page<CashOut> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectAdminHelpProfit(pages, startTime, endTime, cashOut);
    }

}
