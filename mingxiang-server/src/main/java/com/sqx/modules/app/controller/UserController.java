package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.response.HomeMessageResponse;
import com.sqx.modules.app.response.UserMessageResponse;
import com.sqx.modules.app.service.MyHomePageService;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.course.dao.CourseLearnDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fang
 * @date 2020/7/30
 */
@RestController
@Api(value = "用户管理", tags = {"用户管理"})
@Slf4j
@RequestMapping(value = "/user")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private MyHomePageService myHomePageService;
    @Autowired
    private CourseLearnDao courseLearnDao;


    @RequestMapping(value = "/selectUserByInvitationCode", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserByInvitationCode(String invitationCode) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByInvitationCode(invitationCode);
        Long userId = userEntity.getUserId();
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Double money = 0.0;
        if (userMoney != null) {
            money = userMoney.getMoney().doubleValue();
        }
        //查询用户钱包
        Double m = money;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);
        map.put("userEntity", userEntity);
        map.put("money", userEntity.getBalance());
        map.put("consume", consume);
        map.put("income", income);
        return Result.success().put("data", map);
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@ApiParam("用户id") @PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByUserId(userId);
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Double money = 0.0;
        if (userMoney != null) {
            money = userMoney.getMoney().doubleValue();
        }
        //查询用户钱包
        Double m = money;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);

        map.put("userEntity", userEntity);
        map.put("money", userEntity.getBalance());
        map.put("consume", consume);
        map.put("income", income);
       /* map.put("takeOrdersCount", takeOrdersCount);
        map.put("myOrdersCount", myOrdersCount);*/
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/addCannotMoney/{userId}/{money}/{type}", method = RequestMethod.POST)
    @ApiOperation("修改金额")
    @ResponseBody
    public Result addCannotMoney(@PathVariable("userId") Long userId, @PathVariable("money") Double money, @PathVariable("type") Integer type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        UserEntity userEntity = userService.queryByUserId(userId);
        if (userEntity.getBalance() == null) {
            userEntity.setBalance(BigDecimal.ZERO);
        }
        BigDecimal balance = userEntity.getBalance();
        if (type == 1) {
//            userMoneyService.updateMoney(1, userId, money);
            userService.update(null, new LambdaUpdateWrapper<UserEntity>()
                    .eq(UserEntity::getUserId, userId)
                    .set(UserEntity::getBalance, balance.add(BigDecimal.valueOf(money))));
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统充值：" + money);
            userMoneyDetails.setTitle("系统充值金额");
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(0);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
//            userMoneyDetailsService.insertLanguageDetails(userMoneyDetails,"10063",null,"10062",new String[]{String.valueOf(money)});
            PayDetails payDetails = new PayDetails();
            payDetails.setState(1);
            payDetails.setCreateTime(format);
            payDetails.setOrderId("系统充值金额");
            payDetails.setUserId(userId);
            payDetails.setMoney(money);
            payDetails.setClassify(6);
            payDetailsService.save(payDetails);
        } else {
//            userMoneyService.updateMoney(2, userId, money);
            if (balance.compareTo(BigDecimal.valueOf(money)) <= 0) {
                balance = BigDecimal.ZERO;
                userService.update(null, new LambdaUpdateWrapper<UserEntity>()
                        .eq(UserEntity::getUserId, userId)
                        .set(UserEntity::getBalance, balance));
            } else {
                userService.update(null, new LambdaUpdateWrapper<UserEntity>()
                        .eq(UserEntity::getUserId, userId)
                        .set(UserEntity::getBalance, balance.subtract(BigDecimal.valueOf(money))));
            }
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setClassify(0);
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统减少：" + money);
            userMoneyDetails.setTitle("系统减少金额");
            userMoneyDetails.setType(2);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
//            userMoneyDetailsService.insertLanguageDetails(userMoneyDetails,"10065",null,"10064",new String[]{String.valueOf(money)});
            PayDetails payDetails = new PayDetails();
            payDetails.setState(1);
            payDetails.setCreateTime(format);
            payDetails.setOrderId("系统减少金额");
            payDetails.setUserId(userId);
            payDetails.setMoney(money);
            payDetails.setClassify(6);
            payDetailsService.save(payDetails);
        }
        return Result.success();
    }


    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserList(Integer page, Integer limit, String startTime, String endTime, UserEntity userEntity) {
        return Result.success().put("data", userService.selectUserPage(page, limit, startTime, endTime, userEntity));
    }


    @RequestMapping(value = "/deleteUserByUserId/{userId}", method = RequestMethod.POST)
    @ApiOperation("删除用户")
    @ResponseBody
    public Result deleteUserByUserId(@PathVariable("userId") Long userId) {
        userService.removeById(userId);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserByUserId", method = RequestMethod.POST)
    @ApiOperation("修改用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestBody UserEntity userEntity) {
        UserEntity userEntity1 = userService.selectUserById(userEntity.getUserId());
        if (StringUtils.isNotBlank(userEntity.getPhone())) {
            if (!userEntity1.getPhone().equals(userEntity.getPhone())) {
                int phone = userService.count(new QueryWrapper<UserEntity>().eq("phone", userEntity.getPhone()));
                if (phone > 0) {
                    return Result.error("当前手机号已被注册！");
                }
            }
        }
        userService.updateById(userEntity);
        return Result.success();
    }


    @RequestMapping(value = "/updateUserStatusByUserId", method = RequestMethod.GET)
    @ApiOperation("禁用或启用用户")
    @ResponseBody
    public Result updateUserByUserId(Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getStatus().equals(1)) {
            byId.setStatus(2);
        } else {
            byId.setStatus(1);
        }
        userService.updateById(byId);
        return Result.success();
    }


    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@PathVariable("code") String code, @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    /**
     * 信息分析
     *
     * @return
     */
    @GetMapping("/homeMessage")
    @ApiOperation("信息分析")
    public Result homeMessage() {
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null));

        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));

        return Result.success().put("data", homeMessageResponse);
    }


    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @ApiOperation("用户分析")
    public Result userMessage(String date, int type) {
        UserMessageResponse userMessageResponse = new UserMessageResponse();
        userMessageResponse.setTotalNumber(userService.queryUserCount(type, date));
        userMessageResponse.setVipUserNumber(userService.userMessage(date, type));
        userMessageResponse.setUserNumber(userService.queryUserCount(type, date) - userService.userMessage(date, type));
        return Result.success().put("data", userMessageResponse);
    }

    /**
     * 用户分析
     */
    @GetMapping("/payMessage")
    @ApiOperation("充值统计")
    public Result payMessage(String date, Integer type) {
        return Result.success().put("data", payDetailsService.payMessage(date, type));
    }

    @GetMapping("/selectMyAllTypeData")
    @ApiOperation("app用户全部分类的数据统计数冥想、睡眠、专注的累计次数、累计分钟、累计天数(查今日的话传startTime)")
    public Result selectMyAllTypeData(Long userId, String startTime, Integer type) {
        return myHomePageService.selectMyAllTypeData(userId, startTime, type);
    }

    @GetMapping("/selectMyCenter")
    @ApiOperation("app用户全部分类的数据统计数冥想、睡眠、专注的累计次数、累计分钟、累计天数")
    public Result selectMyCenter(Long userId) {
        return myHomePageService.selectMyCenter(userId);
    }

    @GetMapping("/selectAllTypeData")
    @ApiOperation("全部分类的数据统计数冥想、睡眠、专注的累计次数、累计分钟、累计天数(查今日的话传startTime)")
    public Result selectAllTypeData(String startTime, Integer type) {
        return myHomePageService.selectAllTypeData(startTime, type);
    }

    @GetMapping("/selectUserCountStatisticsByTime")
    @ApiOperation("新增的用户的统计折线图")
    public Result selectUserCountStatisticsByTime(@RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                                  @RequestParam(required = false) @ApiParam("结束时间") String endTime) {
        List<Integer> userCountList = new ArrayList<>();
        List<String> year = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
            calendar.setTime(parse);
        } catch (ParseException e) {
            log.error("时间转换错误");
        }
        while (true) {
            String dateTime = simpleDateFormat.format(calendar.getTime());
            int i = userService.queryUserCount(1, dateTime);
            userCountList.add(i);
            year.add(dateTime);
            if (dateTime.equals(endTime)) {
                break;
            }
            calendar.add(Calendar.DATE, 1);
        }
        Map<String, Object> result = new HashMap<>();
        // 折线图数据 新增的用户
        result.put("userCountList", userCountList);
        // 折线图数据 时间
        result.put("year", year);
        return Result.success().put("data", result);
    }


    @GetMapping("/selectMeditationStatisticsByTime")
    @ApiOperation("冥想、睡眠、专注的累计次数的每日统计的折线图")
    public Result selectMeditationStatisticsByTime(@RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                                   @RequestParam(required = false) @ApiParam("结束时间") String endTime) {
        // 冥想、睡眠、专注的累计次数
        List<Integer> meditationCountList = new ArrayList<>();
        List<Integer> sleepCountList = new ArrayList<>();
        List<Integer> focusCountList = new ArrayList<>();
        List<String> year = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
            calendar.setTime(parse);
        } catch (ParseException e) {
            log.error("时间转换错误");
        }
        while (true) {
            String dateTime = simpleDateFormat.format(calendar.getTime());
            int i = courseLearnDao.selectByMeditationCount2(dateTime, 1);
            int i1 = courseLearnDao.selectBySleepCount2(dateTime, 1);
            int i2 = courseLearnDao.selectByFocusCount2(dateTime, 1);
            meditationCountList.add(i);
            sleepCountList.add(i1);
            focusCountList.add(i2);
            year.add(dateTime);
            if (dateTime.equals(endTime)) {
                break;
            }
            calendar.add(Calendar.DATE, 1);
        }
        Map<String, Object> result = new HashMap<>();
        // 折线图数据 冥想统计
        result.put("meditationCountList", meditationCountList);
        // 折线图数据 睡眠统计
        result.put("sleepCountList", sleepCountList);
        // 折线图数据 专注统计
        result.put("focusCountList", focusCountList);
        // 折线图数据 时间
        result.put("year", year);
        return Result.success().put("data", result);
    }

}