package com.sqx.modules.pay.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 提现申请
 *
 * @author fang
 * @date 2020/7/8
 */
@Data
@TableName("cash_out")
public class CashOut implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 申请提现id
     */
    @Excel(name = "编号", orderNum = "1")
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 申请时间
     */
    @Excel(name = "申请时间", width = 35, orderNum = "10")
    private String createAt;

    /**
     * 转账时间
     */
    @Excel(name = "转账/拒绝时间", width = 35, orderNum = "11")
    private String outAt;

    /**
     * 提现金额
     */
    @Excel(name = "提现金额", orderNum = "7")
    private String money;

    /**
     * 是否转账
     */
    //表示不导出当前字段
    @ExcelIgnore
    @Excel(name = "是否转账")
    private Boolean isOut;

    /**
     * 会员编号
     */
    //表示不导出当前字段
    @ExcelIgnore
    @Excel(name = "会员编号")
    private String relationId;

    /**
     * 用户id
     */
    @ExcelIgnore
    @Excel(name = "用户id")
    private Long userId;

    /**
     * 支付宝账号
     */
    @Excel(name = "支付宝账号", width = 15, orderNum = "3")
    private String zhifubao;

    /**
     * 支付宝姓名
     */
    @Excel(name = "支付宝姓名", width = 15, orderNum = "4")
    private String zhifubaoName;

    /**
     * 订单编号
     */
    @Excel(name = "转账订单号", width = 30, orderNum = "12")
    private String orderNumber;

    /**
     * 状态 0待转账 1成功 -1退款
     */
    @Excel(name = "状态", replace = {"待转账_0", "已转账_1", "已拒绝_-1"}, orderNum = "8")
    private Integer state;

    /**
     * 原因
     */
    @Excel(name = "拒绝原因", orderNum = "9")
    private String refund;

    /**
     * 手续费
     */
    @ExcelIgnore
    @Excel(name = "手续费")
    private Double rate;

    /**
     * 提现方式 1支付宝 2微信小程序  3微信公众号
     */
    @Excel(name = "提现方式", replace = {"_null", "支付宝_1", "微信小程序_2", "微信公众号_3"}, orderNum = "6")
    private Integer classify;
    /**
     * 微信手动图片
     */
    @Excel(name = "收款二维码", orderNum = "5")
    private String wxImg;

    @TableField(exist = false)
    @Excel(name = "提现用户手机号", width = 30, orderNum = "2")
    private String phone;

}