package com.sqx.modules.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.*;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.AliVerifyIdCardUtils;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.app.utils.UserConstantInterface;
import com.sqx.modules.app.utils.VerifyIdCardUtils;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.file.utils.Md5Utils;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.entity.Invite;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.scTableData.entity.ScTableData;
import com.sqx.modules.scTableData.service.ScTableDataService;
import com.sqx.modules.shop.vo.UserEntityVo;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.InvitationCodeUtil;
import com.sqx.modules.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;
import weixin.popular.support.TokenManager;
import weixin.popular.util.JsonUtil;

import javax.websocket.SendResult;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */

@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private JwtUtils jwtUtils;
    private int number = 1;
    @Autowired
    private UserMoneyDetailsService detailsService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ScTableDataService tableDataService;
    @Autowired
    private UserCertificationService certificationService;
    @Autowired
    private UserMoneyDetailsDao detailsDao;
    @Autowired
    private InviteDao inviteDao;

    @Override
    public UserEntity queryByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
    }

    @Override
    public UserEntity queryByOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id", openId));
    }

    @Override
    public UserEntity queryByWxOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_open_id", openId));
    }

    @Override
    public UserEntity queryByAppleId(String appleId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("apple_id", appleId));
    }

    @Override
    public UserEntity queryByUserId(Long userId) {

        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
    }

    @Override
    public UserEntity queryByInvitationCode(String invitationCode) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", invitationCode));
    }

    @Override
    public Result updatePhone(String phone, String msg, Long userId) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 != null) {
            UserEntity userInfo = queryByPhone(phone);
            if (userInfo != null) {
                return Result.error("手机号已经被其他账号绑定");
            } else {
                UserEntity one = baseMapper.selectById(userId);
                one.setPhone(phone);
                baseMapper.updateById(one);
                return Result.success();
            }
        }
        return Result.error("验证码不正确");
    }

    @Override
    public Result iosRegister(String appleId) {
        if (StringUtils.isEmpty(appleId)) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByAppleId(appleId);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            if (userInfo.getBalance() == null) {
                userInfo.setBalance(BigDecimal.ZERO);
            }
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(userInfo);
            //返回用户信息
            UserEntity user = queryByAppleId(appleId);
            return getResult(user);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }

    @Override
    public Result wxLogin(String code) {
        try {

            String appid = commonInfoService.findOne(45).getValue();
            String secret = commonInfoService.findOne(46).getValue();
            // 配置请求参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);
            param.put("secret", secret);
            param.put("js_code", code);
            param.put("grant_type", UserConstantInterface.WX_LOGIN_GRANT_TYPE);
            param.put("scope", "snsapi_userinfo");
            // 发送请求
            String wxResult = HttpClientUtil.doGet(UserConstantInterface.WX_LOGIN_URL, param);
            log.info(wxResult);
            JSONObject jsonObject = JSONObject.parseObject(wxResult);
            // 获取参数返回的
            String session_key = jsonObject.get("session_key").toString();
            //返回微信小程序openId
            String open_id = jsonObject.get("openid").toString();
            //判断是否注册过
            Map<String, String> map = new HashMap<>();
            // 封装返回小程序
            map.put("session_key", session_key);
            map.put("open_id", open_id);
            //    UserEntity userEntity = null;
            UserEntity userEntity;//存在手机号
            userEntity = queryByOpenId(open_id);
            if (userEntity != null && StringUtils.isNotEmpty(userEntity.getPhone())) {
                //存在手机号
                map.put("isPhone", "1");
            } else {
                //不存在手机号
                map.put("isPhone", "2");
            }
            if (jsonObject.get("unionid") != null) {
                String unionid = jsonObject.get("unionid").toString();
                map.put("unionid", unionid);
            } else {
                map.put("unionid", "-1");
            }
            return Result.success("登陆成功").put("data", map);
        } catch (Exception e) {
            System.err.println(e.toString());
            return Result.error("登录失败！");
        }
    }


    @Override
    public Result wxRegister(UserEntity userInfo1) {
        if (StringUtils.isEmpty(userInfo1.getOpenId())) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByOpenId(userInfo1.getOpenId());
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }

            if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                userInfo.setPhone(userInfo1.getPhone());
            }
            if (userInfo.getBalance() == null) {
                userInfo.setBalance(BigDecimal.ZERO);
            }
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(userInfo);
        } else {
            //判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = queryByPhone(userInfo1.getPhone());
            if (userByMobile != null) {
                //有账号则绑定账号
                userByMobile.setOpenId(userInfo1.getOpenId());
                baseMapper.updateById(userByMobile);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("小程序账号绑定成功！");
                messageInfo.setTitle("系统通知");
                messageInfo.setState(String.valueOf(6));
                messageInfo.setUserName(userByMobile.getUserName());
                messageInfo.setUserId(String.valueOf(userByMobile.getUserId()));
                messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("账号已被封禁，请联系客服处理！");
                }
            } else {
                //没有则生成新账号
                userInfo1.setCreateTime(LocalDateTime.now());
                userInfo1.setPlatform("小程序");
                userInfo1.setStatus(1);
                CommonInfo oneRate = commonInfoService.findOne(304);
                CommonInfo twoRate = commonInfoService.findOne(305);
                userInfo1.setZhiRate(new BigDecimal(oneRate.getValue()));
                userInfo1.setFeiRate(new BigDecimal(twoRate.getValue()));
                userInfo1.setInvitationCode(InvitationCodeUtil.toSerialCode());
                if (StringUtils.isNotBlank(userInfo1.getInviterCode())) {
                    UserEntity userEntity = queryByInvitationCode(userInfo1.getInviterCode());
                    if (userEntity != null) {
                        userInfo1.setInviterCode(userInfo1.getInviterCode());
                        //判断邀请员是否有上级
                        UserEntity parentParentUser = userDao.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", (userEntity.getInviterCode())));
                        if (parentParentUser != null) {
                            userInfo1.setInviterInviterCode(parentParentUser.getInvitationCode());
                        }
                    } else {
                        return Result.error("邀请码不存在");
                    }
                }
                //赠送vip
                int freeVip = Integer.parseInt(commonInfoService.findOne(306).getValue());
                if (freeVip > 0) {
                    userInfo1.setIsVip(1);
                    userInfo1.setVipEndTime(LocalDateTime.now().plusDays(freeVip));
                }
                String salt = RandomStringUtils.randomAlphanumeric(20);
                userInfo1.setSalt(salt);
                if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                    userInfo1.setPassword(new Sha256Hash(userInfo1.getPhone(), salt).toHex());

                } else {
                    userInfo1.setPassword(new Sha256Hash(userInfo1.getOpenId(), salt).toHex());
                }
                userInfo1.setBalance(BigDecimal.ZERO);
                baseMapper.insert(userInfo1);

                //给用户创建钱包
                UserMoney userMoney = new UserMoney();
                userMoney.setUserId(userInfo1.getUserId());
                userMoney.setMoney(BigDecimal.valueOf(0));
                userMoneyDao.insert(userMoney);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("恭喜您，账号注册成功！");
                messageInfo.setTitle("系统通知");
                messageInfo.setState(String.valueOf(6));
                messageInfo.setUserName(userInfo1.getUserName());
                messageInfo.setUserId(String.valueOf(userInfo1.getUserId()));
                messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
            }
        }
        //返回用户信息
        UserEntity user = queryByOpenId(userInfo1.getOpenId());
        return getResult(user);
    }


    @Override
    public Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getWxOpenId())) {
                return Result.error("当前手机号已经被其他微信绑定");
            }
            //小程序登陆过
            userInfo.setWxOpenId(wxOpenId);
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {

                userInfo.setSex(user.getSex());

            }
            if (userInfo.getBalance() == null) {
                userInfo.setBalance(BigDecimal.ZERO);
            }
            baseMapper.updateById(userInfo);
        } else {
            //小程序没有登陆过
            userInfo = new UserEntity();
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {

                userInfo.setSex(user.getSex());

            }
            String salt = RandomStringUtils.randomAlphanumeric(20);
            userInfo.setPassword(new Sha256Hash(phone, salt).toHex());
            userInfo.setSalt(salt);
            CommonInfo oneRate = commonInfoService.findOne(304);
            CommonInfo twoRate = commonInfoService.findOne(305);
            userInfo.setZhiRate(new BigDecimal(oneRate.getValue()));
            userInfo.setFeiRate(new BigDecimal(twoRate.getValue()));
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setWxOpenId(wxOpenId);
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(LocalDateTime.now());
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setBalance(BigDecimal.ZERO);
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByWxOpenId(userInfo.getWxOpenId());
        return getResult(userEntity);
    }

    @Override
    public Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getAppleId())) {
                return Result.error("当前手机号已经被其他苹果绑定");
            }
            userInfo.setAppleId(appleId);
            userInfo.setUpdateTime(LocalDateTime.now());
            if (userInfo.getBalance() == null) {
                userInfo.setBalance(BigDecimal.ZERO);
            }
            baseMapper.updateById(userInfo);
        } else {
            userInfo = new UserEntity();
            userInfo.setBalance(BigDecimal.ZERO);
            userInfo.setSex(0);
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            userInfo.setPhone(phone);

            String salt = RandomStringUtils.randomAlphanumeric(20);
            userInfo.setPassword(new Sha256Hash(phone, salt).toHex());
            userInfo.setSalt(salt);
            CommonInfo defaultRate = commonInfoService.findOne(303);
            userInfo.setZhiRate(new BigDecimal(defaultRate.getValue()));
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(LocalDateTime.now());
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByAppleId(userInfo.getAppleId());
        return getResult(userEntity);
    }


    @Override
    public Result wxAppLogin(String wxOpenId, String token) {
        UserEntity userEntity = queryByWxOpenId(wxOpenId);
        if (userEntity != null) {
            if (userEntity.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {

                userEntity.setSex(user.getSex());
            }
            userEntity.setUpdateTime(LocalDateTime.now());
            if (userEntity.getBalance() == null) {
                userEntity.setBalance(BigDecimal.ZERO);
            }
            baseMapper.updateById(userEntity);
            return getResult(userEntity);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }


    @Override
    public Result loginByOpenId(String openId) {
        UserEntity userEntity = queryByWxOpenId(openId);
        if (userEntity == null) {
            return Result.error(-200, "未注册！");
        }
        String token = jwtUtils.generateToken(userEntity.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", userEntity);
        return Result.success(map);
    }


    @Override
    public Result registerCode(String phone, String msg, String platform, Integer sysPhone, String openId, String inviterCode, String password, String userName) {
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(password)) {
                if (org.apache.commons.lang3.StringUtils.isEmpty(userInfo.getPassword())) {
                    return Result.error("当前账号未设置密码，请使用短信登录或忘记密码去重置密码！");
                }
                //密码登录  判断输入密码加密之后是否和数据库的加密密码相同
                String salt = userInfo.getSalt();
                String pwd = new Sha256Hash(password, salt).toHex();
                if (!userInfo.getPassword().equals(pwd)) {
                    return Result.error("账号或密码不正确！");
                }
            } else {
                //验证码的登录  判断输入的验证码 和发送验证码时存入数据库的验证码是否一致
                Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
                //校验短信验证码
                if (msg1 == null) {
                    return Result.error("验证码不正确");
                }
                msgDao.deleteById(msg1.getId());
            }
            userInfo.setUpdateTime(LocalDateTime.now());
            if (openId != null) {
                userInfo.setWxOpenId(openId);
            }
            if (userInfo.getBalance() == null) {
                userInfo.setBalance(BigDecimal.ZERO);
            }
            baseMapper.updateById(userInfo);
            return getResult(userInfo);
        } else {
            //没有注册过  则注册新的账号
            //注册账号需要先判断当前手机号是否是真实的手机号 则需要校验验证码
            Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
            //校验短信验证码
            if (msg1 == null) {
                return Result.error("验证码不正确");
            }
            msgDao.deleteById(msg1.getId());
            userInfo = new UserEntity();
            userInfo.setPhone(phone);
            if (StringUtils.isNotBlank(inviterCode)) {
                UserEntity userEntity = queryByInvitationCode(inviterCode);
                if (userEntity != null) {
                    userInfo.setInviterCode(inviterCode);
                    //判断邀请员是否有上级
                    UserEntity parentParentUser = userDao.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", (userEntity.getInviterCode())));
                    if (parentParentUser != null) {
                        userInfo.setInviterInviterCode(parentParentUser.getInvitationCode());
                    }
                } else {
                    return Result.error("邀请码不存在");
                }
            }
            //赠送vip
            int freeVip = Integer.parseInt(commonInfoService.findOne(306).getValue());
            if (freeVip > 0) {
                userInfo.setIsVip(1);
                userInfo.setVipEndTime(LocalDateTime.now().plusDays(freeVip));
            }
            CommonInfo oneRate = commonInfoService.findOne(304);
            CommonInfo twoRate = commonInfoService.findOne(305);
            userInfo.setZhiRate(new BigDecimal(oneRate.getValue()));
            userInfo.setFeiRate(new BigDecimal(twoRate.getValue()));
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setBalance(BigDecimal.ZERO);
            if (StringUtils.isBlank(inviterCode)) {
                userInfo.setInviterCode(commonInfoService.findOne(872).getValue());
            }
            if (StringUtils.isNotBlank(userName)) {
                userInfo.setUserName(userName);
            } else {
                userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            }
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(LocalDateTime.now());
            userInfo.setSysPhone(sysPhone);
            userInfo.setUserType(0);
            String salt = RandomStringUtils.randomAlphanumeric(20);
            userInfo.setSalt(salt);
            if (password != null) {
                userInfo.setPassword(new Sha256Hash(password, salt).toHex());
            } else {
                userInfo.setPassword(new Sha256Hash(userInfo.getPhone(), salt).toHex());
            }
            userInfo.setStatus(1);
            userInfo.setRefreshCount(1);
            if (StringUtils.isNotBlank(openId)) {
                userInfo.setWxOpenId(openId);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(userInfo);
            if (userInfo.getUserId() != null) {
                if (StringUtils.isNotBlank(inviterCode)) {
                    Invite invite = inviteDao.selectOne(new QueryWrapper<Invite>().eq("user_id", userInfo.getUserId()));
                    if (invite == null) {
                        invite = new Invite();
                        UserEntity userEntity = queryByInvitationCode(inviterCode);
                        invite.setUserId(userInfo.getUserId());
                        if (userEntity != null) {
                            invite.setInviteeUserId(userEntity.getUserId());
                            invite.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            invite.setMoney(0.00);
                            inviteDao.insert(invite);
                        }
                    }
                }
            }
            //给用户创建钱包
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(userInfo.getUserId());
            userMoney.setMoney(BigDecimal.valueOf(0));
            userMoneyDao.insert(userMoney);
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContent("恭喜您，账号注册成功！");
            messageInfo.setTitle("系统通知");
            messageInfo.setState(String.valueOf(6));
            messageInfo.setUserName(userInfo.getUserName());
            messageInfo.setUserId(String.valueOf(userInfo.getUserId()));
            messageInfo.setCreateAt(sdf.format(new Date()));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);
            return getResult(userInfo);
        }
    }


    @Override
    public Result login(String phone, String pwd) {
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(new Sha256Hash(pwd, userEntity.getSalt()).toHex())) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        userEntity.setUpdateTime(LocalDateTime.now());
        if (userEntity.getBalance() == null) {
            userEntity.setBalance(BigDecimal.ZERO);
        }
        baseMapper.updateById(userEntity);
        return getResult(userEntity);
    }


    @Override
    public Result getResult(UserEntity user) {
        //生成token
        String token = jwtUtils.generateToken(user.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", user);
        return Result.success(map);
    }


    public Result sendMsg(String phone, String state) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        System.out.println("sendMsg code is " + code);
        SmsSingleSenderResult result = null;
        if (phone != null) {
            CommonInfo three = commonInfoService.findOne(79);
            //默认使用腾讯云
            if (three == null || "1".equals(three.getValue())) {
                //腾讯云短信发送
                return sendMsgTencent(phone, state, code);
            } else if ("2".equals(three.getValue())) {
                //阿里云短信发送
                return sendMsgAlibaba(phone, state, code);
            } else {
                return sendMsgDXB(phone, state, code);
            }
        }
        return Result.error("验证码类型错误！");
    }


    @Override
    public Result sendMsg(String phone, String state, Integer code) {
        CommonInfo three = commonInfoService.findOne(79);
        //默认使用腾讯云
        if (three == null || "1".equals(three.getValue())) {
            //腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if ("2".equals(three.getValue())) {
            //阿里云短信发送
            return sendMsgAlibaba(phone, state, code);
        } else {
            return sendMsgDXB(phone, state, code);
        }
    }


    private Result sendMsgAlibaba(String phone, String state, int code) {
        //阿里云短信accessKeyId
        CommonInfo three = commonInfoService.findOne(83);
        String accessKeyId = three.getValue();
        //阿里云短信accessSecret
        CommonInfo four = commonInfoService.findOne(84);
        String accessSecret = four.getValue();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonInfo name = commonInfoService.findOne(6);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", name.getValue());
        String value = "";
        switch (state) {
            case "3":
                value = commonInfoService.findOne(80).getValue();
                break;
            case "forget":
                value = commonInfoService.findOne(81).getValue();
                break;
            case "bind":
                value = commonInfoService.findOne(82).getValue();
                break;
            case "dx":
                value = commonInfoService.findOne(85).getValue();
                break;
            case "1":
                value = commonInfoService.findOne(85).getValue();
                break;
            case "2":
                value = commonInfoService.findOne(86).getValue();
                break;
            case "4":
                value = commonInfoService.findOne(87).getValue();
                break;
            default:
                value = commonInfoService.findOne(80).getValue();
                break;
        }
        request.putQueryParameter("TemplateCode", value);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            String data = response.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            if ("OK".equals(jsonObject.get("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                return Result.success("login");
            } else {
                if (jsonObject.get("Message").toString().contains("分钟")) {
                    return Result.error("短信发送过于频繁，请一分钟后再试！");
                } else if (jsonObject.get("Message").toString().contains("小时")) {
                    return Result.error("短信发送过于频繁，请一小时后再试！");
                } else if (jsonObject.get("Message").toString().contains("天")) {
                    return Result.error("短信发送过于频繁，请明天再试！");
                }
                log.info(jsonObject.get("Message").toString());
                return Result.error("短信发送失败！");
            }
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgTencent(String phone, String state, int code) {
        SmsSingleSenderResult result = null;
        try {
            CommonInfo three = commonInfoService.findOne(31);
            String clientId = three.getValue();

            CommonInfo four = commonInfoService.findOne(32);
            String clientSecret = four.getValue();
            CommonInfo name = commonInfoService.findOne(6);
            /**
             * 发送短信验证码的状态、
             *
             * 在h5登录环境中 传的状态不是以下三种状态
             */
            SmsSingleSender ssender = new SmsSingleSender(Integer.valueOf(clientId), clientSecret);
            result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");

            switch (state) {
                case "register":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "forget":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "bind":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                default:
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
            }


            System.out.println(result);
            if (result.result == 0) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            } else {
                return Result.error(6, result.errMsg);
            }
        } catch (HTTPException | JSONException | IOException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgDXB(String phone, String state, int code) {
        CommonInfo three = commonInfoService.findOne(164);
        CommonInfo four = commonInfoService.findOne(165);
        CommonInfo name = commonInfoService.findOne(6);
        String testUsername = three.getValue(); //在短信宝注册的用户名
        String testPassword = four.getValue(); //在短信宝注册的密码
        String value;
        switch (state) {
            case "register":
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "forget":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "bind":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "dx":
                value = "【" + name.getValue() + "】您有" + code + "条未读消息，赶快上线查看吧！";
                break;
            default:
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
        }
        StringBuilder httpArg = new StringBuilder();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(Md5Utils.md5s(testPassword)).append("&");
        httpArg.append("m=").append(phone).append("&");
        httpArg.append("c=").append(Md5Utils.encodeUrlString(value, "UTF-8"));
        String result = Md5Utils.request("https://api.smsbao.com/sms", httpArg.toString());
        log.error("短信包返回值：" + result);
        if ("0".equals(result)) {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null) {
                return Result.success("login");
            } else {
                return Result.success("register");
            }
        } else {
//            return ResultUtil.error(6, result.errMsg);
            if ("30".equals(result)) {
                return Result.error("错误密码");
            } else if ("40".equals(result)) {
                return Result.error("账号不存在");
            } else if ("41".equals(result)) {
                return Result.error("余额不足");
            } else if ("43".equals(result)) {
                return Result.error("IP地址限制");
            } else if ("50".equals(result)) {
                return Result.error("内容含有敏感词");
            } else if ("51".equals(result)) {
                return Result.error("手机号码不正确");
            }
        }

        return Result.error("验证码发送失败");
    }


    @Override
    public Result getOpenId(String code, Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            String openid = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code).getOpenid();
            if (StringUtils.isNotEmpty(openid)) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(userId);
                userEntity.setOpenId(openid);
                if (userEntity.getBalance() == null) {
                    userEntity.setBalance(BigDecimal.ZERO);
                }
                baseMapper.updateById(userEntity);
                return Result.success().put("data", openid);
            }
            return Result.error("获取失败");
        } catch (Exception e) {
            log.error("GET_OPENID_FAIL");
            return Result.error("获取失败,出错了！");
        }
    }

    @Override
    public UserEntity selectUserById(Long userId) {
        return baseMapper.selectById(userId);
    }


    @Override
    public IPage<UserEntity> selectUserPage(Integer page, Integer limit, String startTime, String endTime, UserEntity userEntity) {
        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectUserPage(pages, startTime, endTime, userEntity);
    }

    @Override
    public int queryInviterCount(String inviterCode) {
        return baseMapper.queryInviterCount(inviterCode);
    }

    @Override
    public int queryUserCount(int type, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        if (date == null || date == "") {
            date = simpleDateFormat.format(new Date());
        }
        return baseMapper.queryUserCount(type, date);
    }

    @Override
    public Double queryPayMoney(int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String date = simpleDateFormat.format(new Date());
        return baseMapper.queryPayMoney(type, date);
    }


    @Override
    public int userMessage(String date, Integer type) {
        return baseMapper.userMessage(date, type);
    }


    @Override
    public void pushToSingle(String title, String content, String clientId) {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
        apiConfiguration.setAppId(commonInfoService.findOne(61).getValue());
        apiConfiguration.setAppKey(commonInfoService.findOne(60).getValue());
        apiConfiguration.setMasterSecret(commonInfoService.findOne(62).getValue());
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        // 实例化ApiHelper对象，用于创建接口对象
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
        PushApi pushApi = apiHelper.creatApi(PushApi.class);
        //根据cid进行单推
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        // 设置推送参数
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        pushDTO.setPushMessage(pushMessage);
        // 配置通知栏图标
        notification.setLogo("icon.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
        // 配置通知栏网络图标
        notification.setLogoUrl(commonInfoService.findOne(19).getValue() + "/logo.png");
        notification.setTitle(title);
        notification.setBody(content);
        notification.setClickType("url");
        notification.setUrl(commonInfoService.findOne(19).getValue());
        pushMessage.setNotification(notification);
        // 设置接收人信息
        Audience audience = new Audience();
        audience.addCid(clientId);
        pushDTO.setAudience(audience);
        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        if (apiResult.isSuccess()) {
            // success
            log.error("消息推送成功：" + apiResult.getData());
        } else {
            // failed
            log.error("消息推送成功失败：code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
    }

    @Override
    public Result loginApp(String phone, String password) {
        //md5加密
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserEntity userEntity = baseMapper.selectOne(queryWrapper);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(new Sha256Hash(password, userEntity.getSalt()).toHex())) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        // 登录成功后，处理累计登录天数
        handleTotalLoginDays(userEntity);
//        userEntity.setUpdateTime(LocalDateTime.now());
//        baseMapper.updateById(userEntity);
        return getResult(userEntity);

    }

    private void handleTotalLoginDays(UserEntity userEntity) {
        // 获取今天的日期
        LocalDate today = LocalDate.now();
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userEntity.getUserId());
        queryWrapper.eq("last_login_time", today);
        UserEntity user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            UserEntity record = new UserEntity();
            record.setUserId(userEntity.getUserId());
            record.setLastLoginTime(today);
            // totalLoginDays字段来记录累计登录天数
            if (userEntity.getTotalLoginDays() == null) {
                record.setTotalLoginDays(1);
            } else {
                record.setTotalLoginDays(userEntity.getTotalLoginDays() + 1);
            }
            baseMapper.updateById(record);
        }
    }

    @Override
    public Result registApp(String userName, String phone, String password, String msg, String platform, String inviterCode) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 == null) {
            return Result.error("验证码不正确");
        }
        msgDao.deleteById(msg1.getId());
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            return Result.error("手机号已经被注册！");
        } else {

            userInfo = new UserEntity();
            userInfo.setBalance(BigDecimal.ZERO);
            if (StringUtils.isNotBlank(inviterCode)) {
                UserEntity userEntity = queryByInvitationCode(inviterCode);
                if (userEntity != null) {
                    userInfo.setInviterCode(inviterCode);
                    //判断邀请员是否有上级
                    UserEntity parentParentUser = userDao.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", (userEntity.getInviterCode())));
                    if (parentParentUser != null) {
                        userInfo.setInviterInviterCode(parentParentUser.getInvitationCode());
                    }
                } else {
                    return Result.error("邀请码不存在");
                }
            }
            //赠送vip
            int freeVip = Integer.parseInt(commonInfoService.findOne(306).getValue());
            if (freeVip > 0) {
                userInfo.setIsVip(1);
                userInfo.setVipEndTime(LocalDateTime.now().plusDays(freeVip));
            }
            userInfo.setPhone(phone);
            userInfo.setUserName(userName);
            CommonInfo oneRate = commonInfoService.findOne(304);
            CommonInfo twoRate = commonInfoService.findOne(305);
            userInfo.setZhiRate(new BigDecimal(oneRate.getValue()));
            userInfo.setFeiRate(new BigDecimal(twoRate.getValue()));
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            //对密码进行MD5加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            userInfo.setPassword(new Sha256Hash(password, salt).toHex());
            userInfo.setSalt(salt);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(LocalDateTime.now());
            userInfo.setStatus(1);
            userInfo.setUserType(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setCreateTime(LocalDateTime.now());
            baseMapper.insert(userInfo);

            //给用户创建钱包
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(userInfo.getUserId());
            userMoney.setMoney(BigDecimal.valueOf(0));
            userMoneyDao.insert(userMoney);
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContent("恭喜您，账号注册成功！");
            messageInfo.setTitle("系统通知");
            messageInfo.setState(String.valueOf(6));
            messageInfo.setUserName(userInfo.getUserName());
            messageInfo.setUserId(String.valueOf(userInfo.getUserId()));
            messageInfo.setCreateAt(sdf.format(new Date()));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);
            return getResult(userInfo);
        }

    }


    @Override
    public Result forgetPwd(String pwd, String phone, String msg) {
        try {
            Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, msg);
            //校验短信验证码
            if (byPhoneAndCode == null) {
                return Result.error("验证码不正确");
            }
            UserEntity userByPhone = queryByPhone(phone);
            String salt = RandomStringUtils.randomAlphanumeric(20);
            userByPhone.setPassword(new Sha256Hash(pwd, salt).toHex());
            userByPhone.setSalt(salt);
            if (userByPhone.getBalance() == null) {
                userByPhone.setBalance(BigDecimal.ZERO);
            }
            msgDao.deleteById(byPhoneAndCode.getId());
            baseMapper.updateById(userByPhone);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("服务器内部错误");
        }
    }


    @Override
    public Result selectUserByUserId(Long userId, Long byUserId) {
        return Result.success().put("data", baseMapper.selectUserByUserId(userId, byUserId));
    }

    @Override
    public Result quickLogin(String phone, String verificationCode, String platform, Integer sysPhone, String openId, String inviterCode) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, verificationCode);
        //校验短信验证码
        if (msg1 == null) {
            return Result.error("验证码不正确");
        }
        UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
        if (userEntity == null) {
            return registerCode(phone, verificationCode, platform, sysPhone, openId, inviterCode, null, null);
        }
        if (userEntity.getStatus() != 1) {
            return Result.error("该账户已被禁用,请联系客服");
        }
        if (userEntity.getBalance() == null) {
            userEntity.setBalance(BigDecimal.ZERO);
        }
        userEntity.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(userEntity);
        return getResult(userEntity);
    }

    @Override
    public IPage<UserEntity> getShopList(Integer page, Integer limit, String userName) {

        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<UserEntity>().ne("user_type", 0).eq("status", 1);
        if (!StringUtils.isBlank(userName)) {
            wrapper.like("user_name", userName);
        }
        return userDao.selectPage(pages, wrapper);
    }


    @Override
    public UserEntityVo getShopDataList(Integer page, Integer limit, Long userId, Integer classId) {
        UserEntity userEntity = userDao.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        UserEntityVo userEntityVo = new UserEntityVo();
        BeanUtils.copyProperties(userEntity, userEntityVo);
        userEntityVo.setScTableDataVo(tableDataService.getUserDataList(page, limit, userId));
        return userEntityVo;

    }

    @Override
    public HashMap<String, Object> getExtensionData(Long userId) {

        UserEntity userEntity = baseMapper.selectById(userId);
        HashMap<String, Object> map = new HashMap<>();
        Integer countChildren = baseMapper.selectCount(new QueryWrapper<UserEntity>().eq("inviter_code", userEntity.getInvitationCode()));
        Integer countChildrenChildren = baseMapper.selectCount(new QueryWrapper<UserEntity>().eq("inviter_inviter_code", userEntity.getInvitationCode()));
        BigDecimal oneMoney = detailsService.getOneProfit(userId, 1);
        BigDecimal twoMoney = detailsService.getOneProfit(userId, 2);
        map.put("countChildren", countChildren);
        map.put("countChildrenChildren", countChildrenChildren);
        map.put("extensionProfit", oneMoney.add(twoMoney));
        map.put("oneMoney", oneMoney);
        map.put("twoMoney", twoMoney);
        return map;


    }

    @Override
    public IPage<UserEntity> getExtensionList(Long userId, Integer type, Integer page, Integer limit) {
        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity loginEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        if (loginEntity != null) {
            if (type == 1) {
                return baseMapper.selectPage(pages, new QueryWrapper<UserEntity>().eq("inviter_code", loginEntity.getInvitationCode()));
            } else if (type == 2) {
                return baseMapper.selectPage(pages, new QueryWrapper<UserEntity>().eq("inviter_inviter_code", loginEntity.getInvitationCode()));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public IPage<HashMap<String, Object>> getProfitList(Long userId, Integer type, Integer page, Integer limit) {

        Page<HashMap<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return detailsDao.getProfitList(pages, userId, type);


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
    public void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid, String url) {
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

    @Override
    public IPage<HashMap<String, Object>> getUserIncomeList(Long userId, Integer type, Integer page, Integer limit) {

        Page<HashMap<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity userEntity = baseMapper.selectById(userId);
        return baseMapper.getUserIncomeList(pages, type, userEntity.getInvitationCode(), userEntity.getUserId());
    }

    /**
     * 打开app时调用，目的是为了累计登录天数
     *
     * @return
     */
    @Override
    public Result launchApp(Long userId) {
        if (userId == null) {
            return Result.success();
        }
        UserEntity userEntity = baseMapper.selectById(userId);
        if (userEntity != null) {
            // 登录成功后，处理累计登录天数
            handleTotalLoginDays(userEntity);
            return Result.success().put("data", getResult(userEntity));
        }
        return Result.success();
    }


}
