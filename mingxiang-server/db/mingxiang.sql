/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : mingxiang

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 06/11/2026 17:04:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `address_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `user_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户电话',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `address_detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户详细地址（用户手动输入的地址）',
  `lng` double(20, 10) NULL DEFAULT NULL COMMENT '经度',
  `lat` double(20, 10) NULL DEFAULT NULL COMMENT '维度',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：未删除；1：删除）',
  `address_default` int(11) NULL DEFAULT NULL COMMENT '默认地址 0：非默认   1：默认',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`address_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 293 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `android_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `create_at` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `des` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `ios_version` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `ios_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `method` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `version` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app
-- ----------------------------

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'bannerid',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `state` int(2) NULL DEFAULT NULL COMMENT '状态1正常2隐藏',
  `classify` int(2) NULL DEFAULT NULL COMMENT '分类1banner图2金刚区分类',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址 ',
  `sort` int(4) NULL DEFAULT NULL COMMENT '顺序',
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `type` int(1) NULL DEFAULT NULL COMMENT '分类 1首页 2好店',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, '2024-04-25 16:50:25', '夜间睡眠', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/04/25/63993b49a3de2467de62f2514b885df8.png', 1, 3, NULL, NULL, '夜间睡眠', NULL);
INSERT INTO `banner` VALUES (2, '2024-04-25 16:50:25', '专注时钟', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/04/25/63993b49a3de2467de62f2514b885df8.png', 1, 3, NULL, NULL, '专注时钟', NULL);
INSERT INTO `banner` VALUES (3, '2024-04-25 16:50:25', '日间小憩', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/04/25/63993b49a3de2467de62f2514b885df8.png', 1, 3, NULL, NULL, '日间小憩', NULL);
INSERT INTO `banner` VALUES (4, '2024-04-25 16:50:25', '正念呼吸', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/04/25/63993b49a3de2467de62f2514b885df8.png', 1, 3, NULL, NULL, '正念呼吸', NULL);

-- ----------------------------
-- Table structure for cash_out
-- ----------------------------
DROP TABLE IF EXISTS `cash_out`;
CREATE TABLE `cash_out`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请提现id',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请时间',
  `is_out` int(2) NULL DEFAULT NULL COMMENT '是否转账',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现金额',
  `out_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转账时间',
  `relation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `zhifubao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `zhifubao_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝姓名',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `state` int(11) NULL DEFAULT NULL COMMENT '状态 0待转账 1成功 -1退款',
  `refund` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款原因',
  `classify` int(11) NULL DEFAULT NULL COMMENT '提现方式 1支付宝 2微信小程序  3微信公众号',
  `wx_img` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信图片',
  `rate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '利息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cash_out
-- ----------------------------

-- ----------------------------
-- Table structure for chapter_info
-- ----------------------------
DROP TABLE IF EXISTS `chapter_info`;
CREATE TABLE `chapter_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `chapter_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '章节名称',
  `chapter_num` int(11) NULL DEFAULT NULL COMMENT '章节序号',
  `chapter_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '章节链接',
  `chapter_duration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '章节时长',
  `chapter_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '章节金额',
  `chapter_charge` int(11) NULL DEFAULT NULL COMMENT '是否收费：0免费 1收费',
  `chapter_status` int(11) NULL DEFAULT NULL COMMENT '章节状态：0正常 1下架',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除：0未删除  1已删除',
  `chapter_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '章节图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课程的章节信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chapter_info
-- ----------------------------

-- ----------------------------
-- Table structure for chat_content
-- ----------------------------
DROP TABLE IF EXISTS `chat_content`;
CREATE TABLE `chat_content`  (
  `chat_content_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天内容id',
  `chat_conversation_id` int(11) NULL DEFAULT NULL COMMENT '聊天会话id',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `message_type` int(11) NULL DEFAULT NULL COMMENT '类型 1文字 2图片 3语音',
  `width` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片宽度',
  `height` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片高度',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '发送用户id',
  `status` int(11) NULL DEFAULT NULL COMMENT '0未读 1已读',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送时间',
  `voice_length` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 825 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_content
-- ----------------------------

-- ----------------------------
-- Table structure for chat_conversation
-- ----------------------------
DROP TABLE IF EXISTS `chat_conversation`;
CREATE TABLE `chat_conversation`  (
  `chat_conversation_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天会话id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '发送用户id',
  `focused_user_id` int(11) NULL DEFAULT NULL COMMENT '接受用户id',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_send_msg` int(11) NULL DEFAULT NULL,
  `is_wx_msg` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`chat_conversation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 255 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_conversation
-- ----------------------------

-- ----------------------------
-- Table structure for chats
-- ----------------------------
DROP TABLE IF EXISTS `chats`;
CREATE TABLE `chats`  (
  `chat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user_count` int(20) NULL DEFAULT 0 COMMENT '用户未读条数',
  `user_count2` int(20) NULL DEFAULT 0 COMMENT '好友未读条数',
  `user_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_head2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好友头像',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(20) NULL DEFAULT NULL COMMENT '好友id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_name2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好友昵称',
  `store_count` int(11) NULL DEFAULT 0 COMMENT '后台未读条数',
  `store_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后台头像',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '总后台id(总后台传0',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户昵称',
  PRIMARY KEY (`chat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天会话' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chats
-- ----------------------------

-- ----------------------------
-- Table structure for chats_content
-- ----------------------------
DROP TABLE IF EXISTS `chats_content`;
CREATE TABLE `chats_content`  (
  `chat_content_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话内容id',
  `chat_id` bigint(20) NULL DEFAULT NULL COMMENT '会话Id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '聊天内容',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `send_type` bigint(20) NULL DEFAULT NULL COMMENT '消息来源（1用户消息 2好友消息）',
  `status` int(11) NULL DEFAULT 1 COMMENT '是否已读(1未读 2已读)',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型（1文字 2图片 3链接）',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(20) NULL DEFAULT NULL COMMENT '好友id',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '后台id（总后台传0）',
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1249 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天会话内容' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chats_content
-- ----------------------------

-- ----------------------------
-- Table structure for common_info
-- ----------------------------
DROP TABLE IF EXISTS `common_info`;
CREATE TABLE `common_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置文件id',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `max` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `min` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置文件名称',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '值',
  `condition_from` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 877 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of common_info
-- ----------------------------
INSERT INTO `common_info` VALUES (1, '2023-07-18 09:40:54', NULL, '客服二维码', 1, NULL, 'xitong');
INSERT INTO `common_info` VALUES (2, '2020-02-21 21:17:17', NULL, '公众号二维码', 2, NULL, 'xitong');
INSERT INTO `common_info` VALUES (6, '2020-07-09 17:59:45', NULL, '微信公众号APPID	', 5, NULL, 'weixin');
INSERT INTO `common_info` VALUES (7, '2021-03-15 21:16:30', NULL, '短信签名', 6, '省钱兄', 'duanxin');
INSERT INTO `common_info` VALUES (16, '2020-02-25 20:43:38', NULL, '微信公众号秘钥	', 21, NULL, 'weixin');
INSERT INTO `common_info` VALUES (17, '2020-02-25 20:43:59', NULL, '公众号Token', 16, NULL, 'weixin');
INSERT INTO `common_info` VALUES (18, '2020-02-25 20:44:15', NULL, '公众号EncodingAESKey	', 17, NULL, 'weixin');
INSERT INTO `common_info` VALUES (20, '2023-07-17 15:26:13', NULL, '后台管理平台域名配置	', 20, NULL, 'xitong');
INSERT INTO `common_info` VALUES (21, '2023-07-07 13:32:18', NULL, 'h5服务域名配置	', 19, NULL, 'xitong');
INSERT INTO `common_info` VALUES (22, '2023-11-08 19:29:20', NULL, '短信服务商（1 腾讯云  2阿里云  3短信宝）', 79, '3', 'duanxin');
INSERT INTO `common_info` VALUES (23, '2022-03-20 15:08:22', NULL, '后台服务名称	', 12, '省钱兄', 'xitong');
INSERT INTO `common_info` VALUES (27, '2023-07-18 11:04:22', NULL, '通用APP下载地址	', 25, NULL, 'xitong');
INSERT INTO `common_info` VALUES (33, '2023-11-08 19:29:38', NULL, '腾讯云短信clientId	', 31, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (34, '2023-11-08 19:29:43', NULL, '腾讯云短信clientSecret	', 32, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (44, '2022-03-10 20:38:21', NULL, '客服微信号', 44, NULL, 'xitong');
INSERT INTO `common_info` VALUES (47, '2022-01-22 20:52:42', NULL, '微信小程序APPID', 45, NULL, 'weixin');
INSERT INTO `common_info` VALUES (48, '2022-01-22 20:52:48', NULL, '微信小程序秘钥', 46, NULL, 'weixin');
INSERT INTO `common_info` VALUES (51, '2020-03-29 00:21', NULL, '分享安卓下载地址', 49, NULL, 'xitong');
INSERT INTO `common_info` VALUES (52, '2020-03-29 00:21', NULL, '分享苹果下载地址', 50, NULL, 'xitong');
INSERT INTO `common_info` VALUES (58, '2020-03-29 00:21', NULL, '开启微信登录', 53, '是', 'kaiguans');
INSERT INTO `common_info` VALUES (69, '2020-06-04 16:34', NULL, 'APP消息推送PushAppKey', 60, NULL, 'push');
INSERT INTO `common_info` VALUES (70, '2020-06-04 16:34', NULL, 'APP消息推送PushAppId', 61, NULL, 'push');
INSERT INTO `common_info` VALUES (71, '2020-06-04 16:34', NULL, 'APP消息推送PushMasterSecret', 62, NULL, 'push');
INSERT INTO `common_info` VALUES (72, '2020-06-04 16:34', NULL, '企业支付宝APPID', 63, NULL, 'zhifubao');
INSERT INTO `common_info` VALUES (73, '2020-06-04 16:34', NULL, '企业支付宝公钥', 64, NULL, 'zhifubaos');
INSERT INTO `common_info` VALUES (74, '2020-06-04 16:34', NULL, '企业支付宝商户秘钥', 65, NULL, 'zhifubao');
INSERT INTO `common_info` VALUES (77, '2021-03-15 21:22:30', NULL, '文件上传阿里云Endpoint', 68, NULL, 'oss');
INSERT INTO `common_info` VALUES (78, '2021-03-15 21:22:38', NULL, '文件上传阿里云账号accessKeyId', 69, NULL, 'oss');
INSERT INTO `common_info` VALUES (79, '2021-03-15 21:22:46', NULL, '文件上传阿里云账号accessKeySecret', 70, NULL, 'oss');
INSERT INTO `common_info` VALUES (80, '2021-03-15 21:22:53', NULL, '文件上传阿里云Bucket名称', 71, NULL, 'oss');
INSERT INTO `common_info` VALUES (81, '2021-03-15 21:23:00', NULL, '文件上传阿里云Bucket域名', 72, NULL, 'oss');
INSERT INTO `common_info` VALUES (83, '2020-07-27 15:17', NULL, '微信APPappId', 74, NULL, 'weixin');
INSERT INTO `common_info` VALUES (84, '2020-07-27 15:17', NULL, '微信商户key', 75, NULL, 'weixins');
INSERT INTO `common_info` VALUES (85, '2020-07-27 15:17', NULL, '微信商户号mchId', 76, NULL, 'weixin');
INSERT INTO `common_info` VALUES (96, '2020-07-27 15:17', NULL, '阿里云登陆或注册模板code（开启阿里云短信必须配置）', 80, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (97, '2020-07-27 15:17', NULL, '阿里云找回密码模板code（开启阿里云短信必须配置）', 81, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (98, '2020-07-27 15:17', NULL, '阿里云绑定手机号模板code（开启阿里云短信必须配置）', 82, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (99, '2020-07-27 15:17', NULL, '阿里云短信accessKeyId', 83, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (100, '2020-07-27 15:17', NULL, '阿里云短信accessSecret', 84, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (102, '2022-04-21 19:40:07', NULL, '转账方式 1支付宝证书  2支付宝秘钥 3手动', 98, '3', 'zhifubao');
INSERT INTO `common_info` VALUES (108, '2023-07-14 16:40:21', NULL, '公众号是否自动登录', 108, '是', 'kaiguans');
INSERT INTO `common_info` VALUES (118, '2020-07-27 15:17', NULL, '邀请推广内容', 101, '我正在省钱兄电竞报名参与游戏竞技比赛，比赛胜利可以获得赏金奖励哦。报名参与和平精英比赛，游戏中击杀一人就能赚到对应奖励哦。快来和我一起参赛吧！\n?链接进入下载省钱兄电竞APP！填写邀请码：', 'xitongs');
INSERT INTO `common_info` VALUES (120, '2023-07-18 11:14:42', NULL, '邀请微信分享内容', 103, '邀请好友', 'weixin');
INSERT INTO `common_info` VALUES (130, '2020-11-04 10:31:40', NULL, '提现最低额度', 112, '10', 'fuwufei');
INSERT INTO `common_info` VALUES (134, '2023-07-18 11:04:46', NULL, '海报邀请语', 116, '呼朋唤友来注册', 'xitong');
INSERT INTO `common_info` VALUES (154, '2023-07-18 11:15:11', NULL, '是否开启APP微信分享', 136, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (159, '2023-07-18 11:15:15', NULL, 'H5推广是否分享APP下载页面', 141, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (170, '2022-03-17 18:03:01', NULL, '提现手续费（用户提现时候的手续费，可以作为平台服务费使用，设置0.3就是百分之30，举例提现100服务费30，到账70）', 152, '0.1', 'fuwufei');
INSERT INTO `common_info` VALUES (171, '2021-09-06 16:19:51', NULL, '最高提现金额', 153, '1000', 'fuwufei');
INSERT INTO `common_info` VALUES (182, '2022-03-20 15:06:28', NULL, '短信宝用户名', 164, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (183, '2022-03-20 15:06:40', NULL, '短信宝密码', 165, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (200, '2021-07-19 11:32:12', NULL, '帮助中心', 175, '<p>本应用尊重并保护所有使用服务用户的个人隐私权。为了给您提供更准确、更有个性化的服务，本应用会按照本隐私权政策的规定使用和披露您的个人信息。但本应用将以高度的勤勉、审慎义务对待这些信息。除本隐私权政策另有规定外，在未征得您事先许可的情况下，本应用不会将这些信息对外披露或向第三方提供。本应用会不时更新本隐私权政策。 您在同意本应用服务使用协议之时，即视为您已经同意本隐私权政策全部内容。本隐私权政策属于本应用服务使用协议不可分割的一部分。</p><p>1. 适用范围</p><p>(a) 在您注册本应用帐号时，您根据本应用要求提供的个人注册信息；</p><p>(b) 在您使用本应用网络服务，或访问本应用平台网页时，本应用自动接收并记录的您的浏览器和计算机上的信息，包括但不限于您的IP地址、浏览器的类型、使用的语言、访问日期和时间、软硬件特征信息及您需求的网页记录等数据；</p><p>(c) 本应用通过合法途径从商业伙伴处取得的用户个人数据。</p><p>您了解并同意，以下信息不适用本隐私权政策：</p><p>(a) 您在使用本应用平台提供的搜索服务时输入的关键字信息；</p><p>(b) 本应用收集到的您在本应用发布的有关信息数据，包括但不限于参与活动、成交信息及评价详情；</p><p>(c) 违反法律规定或违反本应用规则行为及本应用已对您采取的措施。</p><p>2. 信息使用</p><p>(a)本应用不会向任何无关第三方提供、出售、出租、分享或交易您的个人信息，除非事先得到您的许可，或该第三方和本应用（含本应用关联公司）单独或共同为您提供服务，且在该服务结束后，其将被禁止访问包括其以前能够访问的所有这些资料。</p><p>(b) 本应用亦不允许任何第三方以任何手段收集、编辑、出售或者无偿传播您的个人信息。任何本应用平台用户如从事上述活动，一经发现，本应用有权立即终止与该用户的服务协议。</p><p>(c) 为服务用户的目的，本应用可能通过使用您的个人信息，向您提供您感兴趣的信息，包括但不限于向您发出产品和服务信息，或者与本应用合作伙伴共享信息以便他们向您发送有关其产品和服务的信息（后者需要您的事先同意）。</p><p>3. 信息披露</p><p>在如下情况下，本应用将依据您的个人意愿或法律的规定全部或部分的披露您的个人信息：</p><p>(a) 经您事先同意，向第三方披露；</p><p>(b)为提供您所要求的产品和服务，而必须和第三方分享您的个人信息；</p><p>(c) 根据法律的有关规定，或者行政或司法机构的要求，向第三方或者行政、司法机构披露；</p><p>(d) 如您出现违反中国有关法律、法规或者本应用服务协议或相关规则的情况，需要向第三方披露；</p><p>(e) 如您是适格的知识产权投诉人并已提起投诉，应被投诉人要求，向被投诉人披露，以便双方处理可能的权利纠纷；</p><p>(f) 在本应用平台上创建的某一交易中，如交易任何一方履行或部分履行了交易义务并提出信息披露请求的，本应用有权决定向该用户提供其交易对方的联络方式等必要信息，以促成交易的完成或纠纷的解决。</p><p>(g) 其它本应用根据法律、法规或者网站政策认为合适的披露。</p><p>4. 信息存储和交换</p><p>本应用收集的有关您的信息和资料将保存在本应用及（或）其关联公司的服务器上，这些信息和资料可能传送至您所在国家、地区或本应用收集信息和资料所在地的境外并在境外被访问、存储和展示。</p><p>5. Cookie的使用</p><p>(a) 在您未拒绝接受cookies的情况下，本应用会在您的计算机上设定或取用cookies ，以便您能登录或使用依赖于cookies的本应用平台服务或功能。本应用使用cookies可为您提供更加周到的个性化服务，包括推广服务。</p><p>(b) 您有权选择接受或拒绝接受cookies。您可以通过修改浏览器设置的方式拒绝接受cookies。但如果您选择拒绝接受cookies，则您可能无法登录或使用依赖于cookies的本应用网络服务或功能。</p><p>(c) 通过本应用所设cookies所取得的有关信息，将适用本政策。</p><p>6. 信息安全</p><p>(a) 本应用帐号均有安全保护功能，请妥善保管您的用户名及密码信息。本应用将通过对用户密码进行加密等安全措施确保您的信息不丢失，不被滥用和变造。尽管有前述安全措施，但同时也请您注意在信息网络上不存在“完善的安全措施”。</p><p>(b) 在使用本应用网络服务进行网上交易时，您不可避免的要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或者邮政地址。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。如您发现自己的个人信息泄密，尤其是本应用用户名及密码发生泄露，请您立即联络本应用客服，以便本应用采取相应措施。</p><p>7.本隐私政策的更改</p><p>(a)如果决定更改隐私政策，我们会在本政策中、本公司网站中以及我们认为适当的位置发布这些更改，以便您了解我们如何收集、使用您的个人信息，哪些人可以访问这些信息，以及在什么情况下我们会透露这些信息。</p><p>(b)本公司保留随时修改本政策的权利，因此请经常查看。如对本政策作出重大更改，本公司会通过网站通知的形式告知。</p>', 'xieyi');
INSERT INTO `common_info` VALUES (201, '2022-04-18 14:05:03', NULL, '隐私政策', 176, '<p>个人信息保护政策</p><p>最近更新发布日期：2022年03月03日</p><p>版本生效日期：2022年03月03日</p><p>&nbsp;</p><p>温馨提示</p><p>省钱兄深知个人信息对您的重要性，并会尽全力保护您的个人信息安全可靠。我们致力于维持您对我们的信任，恪守以下原则，保护您的个人信息：权责一致原则、目的明确原则、选择同意原则、最小必要原则、确保安全原则、主体参与原则、公开透明原则等。同时，省钱兄承诺，我们将按业界成熟的安全标准，采取相应的安全保护措施来保护您的个人信息。</p><p>本政策由西安省钱兄网络科技有限公司及其关联公司提供，仅适用于我方平台提供的所有产品和服务（以下简称“我们的产品/或服务”）。西安省钱兄网络科技有限公司，是一家提供购物返利、促销信息、活动导购、广告等技术运营服务支持的互联网公司。您访问我方平台，使用我方平台提供的服务，及未设独立隐私权政策的我方平台网站、服务器、及/或登陆相关客户端，均适用本个人信息保护政策。如我们及关联公司（范围详见定义部分）的产品或服务中使用了省钱兄提供的产品或服务但未设个人信息保护政策的，则本政策同样适用于该部分产品或服务。我们及关联公司就其向您提供的产品或服务单独设立有个人信息保护政策的，则相应产品或服务适用相应个人信息保护政策。</p><p>在使用省钱兄各项产品或服务前，请您务必仔细阅读并透彻理解本政策，特别是以粗体/粗体下划线标识的条款，您应重点阅读，一旦您使用或继续使用我们的产品/服务，即意味着您同意本政策（含更新版本）内容，并且同意我们按照本政策收集、使用、保存和共享您的相关信息。</p><p>我们会遵循隐私政策收集、使用您的信息，但不会仅因您同意本隐私政策而采用强制捆绑的方式一揽子收集个人信息。当您使用或开启相关功能或使用服务时，为实现功能、服务所必需，我们会收集、使用相关信息。除非是为实现基本业务功能或根据法律法规要求所必需的必要信息，您均可以拒绝提供且不影响其他功能或服务。我们将在隐私政策中逐项说明哪些是必要信息。精确地理位置、摄像头、麦克风、相册权限，均不会默认开启，只有经过您的明示授权才会在为实现特定功能或服务时使用，您也可以撤回授权。特别需要指出的是，即使经过您的授权，我们获得了这些敏感权限，也不会在相关功能或服务不需要时而收集您的信息。</p><p>如果您未登录帐号，我们会通过设备对应的标识符信息来保障信息推送的基本功能。如果您登录了帐号，我们会根据帐号信息实现信息推送。</p><p>如对本政策内容有任何疑问、意见或建议，您可随时通过省钱兄APP/官网的在线客服系统与我们联系。</p><p>&nbsp;</p><p>第一部分&nbsp;定义</p><p>除您与省钱兄另有约定外，本协议及其补充协议和隐私政策当中的下列名词均采用如下解释：</p><p><strong>（1）本平台：</strong>指省钱兄享有全部著作权的、提供给您及其他用户登录和使用的省钱兄网站及APP客户端。</p><p><strong>（2）平台运营方：</strong>指省钱兄平台的网络及技术服务提供者。关于本政策相关事宜，您可以通过本协议第九条提供的方式与我们联系。<strong>省钱兄运营方可能根据平台业务调整而发生增加、变更，该等增加、变更不会影响您于本协议项下的权益。</strong></p><p><strong>（3）个人信息：</strong>指以电子或者其他方式记录的能够单独或者与其他信息结合识别特定自然人身份或者反映特定自然人活动情况的各种信息。</p><p><strong>（4）个人敏感信息：</strong>指包括身份证件号码、个人生物识别信息、银行账号、财产信息、行踪轨迹、交易信息、14岁以下（含）儿童信息这些个人信息（我们将在本隐私权政策中对具体个人敏感信息以粗体进行显著标识）。</p><p><strong>（5）个人信息删除：</strong>指在实现日常业务功能所涉及的系统中去除个人信息的行为，使其保持不可被检索、访问的状态。</p><p><strong>（6）儿童：</strong>指不满十四周岁的未成年人。</p><p>&nbsp;</p><p>&nbsp;</p><p>第二部分&nbsp;个人信息保护政策</p><p>本政策将帮助您了解以下内容：</p><p>1、我们如何收集和使用您的个人信息</p><p>2、我们如何使用 Cookie 和同类技术</p><p>3、我们如何共享、转让、公开披露您的个人信息</p><p>4、我们如何保护和保存您的个人信息</p><p>5、您的权利</p><p>6、未成年人信息的保护</p><p>7、您的个人信息如何在全球范围转移</p><p>8、关于本政策的更新</p><p>9、如何联系我们</p><p>附录：第三方合作SDK服务列表及其隐私政策链接明细表</p><p><strong>&nbsp;</strong></p><p>一、我们如何收集和使用您的个人信息</p><p><u>个人信息</u>是指以电子或者其他方式记录的能够单独或者与其他信息结合识别特定自然人身份或者反映特定自然人活动情况的各种信息。本个人信息保护政策中涉及的<u>个人信息</u>包括：<strong>基本信息（包括个人姓名、生日、性别、住址、个人电话号码、电子邮箱）；网络身份标识信息（包括系统账号、IP地址、邮箱地址及与前述有关的密码、口令、口令保护答案）；个人财产信息（交易和消费记录以及余额、支付宝账户信息）；个人上网记录（包括网站浏览记录、软件使用记录、点击记录）；个人常用设备信息（包括硬件型号、设备MAC地址、操作系统类型）、软件列表唯一设备识别码（不限于MEID/IMEI/android ID/IDFA/GUID、UUID、OAID、SIM卡IMSI信息在内的描述个人常用设备基本情况的信息；个人位置信息（包括行程信息、精准定位信息、经纬度）；</strong></p><p>个人敏感信息是指一旦泄露、非法提供或滥用可能危害人身和财产安全，极易导致个人名誉、身心健康受到损害或歧视性待遇等的个人信息，本个人信息保护政策中涉及的个人敏感信息包括：<strong>您的财产信息（包括交易记录及账户余额、支付宝账户信息）；网络身份识别信息（包括账户名、账户昵称、邮箱地址及与前述有关的密码和密码保护问题和答案）；其他信息（包括个人电话号码、手机号码、网页浏览记录、精准定位信息）。</strong></p><p>以上个人信息和个人敏感信息包含的内容出自于GB/T35273《个人信息安全规范》，分别与附录A附录A（资料性附录）-个人信息示例-表A.1 个人信息举例中的内容和附录B（资料性附录）-个人敏感信息判定-表B.1 个人敏感信息举例一致。</p><p>在您使用省钱兄产品或服务时，省钱兄可能自动接收并记录您的网站或应用程序上的服务器数值，包括但不限于省钱兄产品或服务的操作状态、网络环境、异常日志和其他一些明确且客观反映在省钱兄产品或服务服务器端的基本记录信息。您理解并同意，前述基本信息以及其他无法通过其单独或者与其他信息结合识别您的个人身份或活动情况的信息不属于您的个人信息。</p><p>我们仅会出于本政策所述的以下目的，收集和使用您的个人信息：</p><p><strong>（一）您须授权我们收集和使用您个人信息的情形</strong></p><p>我们的产品与/或服务包括一些核心功能，这些功能包含了实现网上购物所必须的功能、改进我们的产品与/或服务所必须的功能及保障交易安全所必须的功能<strong>。我们可能会收集、保存和使用下列与您有关的信息才能实现上述这些功能。如果您不提供相关信息，您将无法享受我们提供的产品与/或服务。这些功能包括：</strong></p><p>1、&nbsp;<strong>实现网上购物所必须的功能</strong></p><p><strong>（1）业务功能一：用户注册</strong></p><p>为完成创建账号，您需提供以下信息：您的<strong>姓名、手机号码、创建的用户名和密码</strong>。当您注册时向我们提供注册所用的<strong>手机号码</strong>（用于接收验证码），我们将通过发送短信验证码来验证您的身份是否有效。您的手机号码尾号四位为您的默认昵称，您可以修改补充您的昵称以及绑定您的<strong>支付宝账号/银行账户</strong>（用于账户提现）相关信息，这些信息均属于您的“账户信息”。您可以修改补充您的<strong>昵称、性别、生日、头像</strong>，这些信息均属于您的“账户信息”。当您使用第三方账号（微信账号、Apple账号）登录时，如果您决定绑定该第三方平台的账号，使用第三方账户登录，您将授权省钱兄读取您在第三方帐号注册的公开信息，包括<strong>用户名、手机号、头像、昵称、地区、性别</strong>（具体以第三方登录服务提供的信息为准）以及<strong>身份验证信息</strong>。绑定第三方帐号可以帮助您在遇到账号丢失、忘记密码问题时更便捷的找回账号和密码。我们收集上述信息是用于为您提供账号登录服务以及保障您的账户安全，防范安全风险。如果您拒绝授权上述信息，您将无法使用第三方平台的账号登录省钱兄平台，但不影响我们为您提供的其他产品和正常使用其他服务。</p><p>为了更好地向您提供导购服务以及个性化需求的信息展示，我们可在采取一定技术处理手段后对您包括但不限于第三方账号中的昵称、头像、优惠权益、粉丝昵称和数量信息（以下称“该等信息”）进行使用，并可向您本人或其他用户或好友展示该等信息。我们可能会视省钱兄产品具体情况为您提供相关权限，以便您可以对是否展示前述相关信息进行相应设置。</p><p>您补充的账户信息将有助于我们为您提供个性化的商品推荐和更优的购物体验，但如果您不提供这些补充信息，不会影响您网上购物的基本功能。另外我们会根据您的会员账户使用情况及平台设置的会员等级计算规则确定您当前的会员等级，并为您提供相应会员等级所对应的基本权益。</p><p>在注册过程中，如果您提供以下额外信息，将有助于我们给您提供更好的服务和体验：<strong>性别、出生日期、微信号、微信二维码、头像</strong>。但如果您不提供这些信息，将不会影响本服务的基本功能。</p><p><strong>（2）业务功能二：商品/服务信息展示和搜索</strong></p><p>为了让您快速地找到您所需要的商品或服务，我们可能会收集您使用我们的产品与/或服务的设备信息（包括<strong>设备名称、设备型号、设备识别码（android_id、oaid、imei、mac地址；其中imei、mac地址仅用于安全及风控管理）、操作系统和应用程序版本、语言设置、分辨率、服务提供商网络ID（PLMN））、浏览器类型</strong>来为您提供商品优惠券信息展示的最优方式。</p><p>您也可以通过搜索（包括但不限于关键词、标题及口令）来精准地找到您所需要的商品或服务。我们会保留您的搜索内容以方便您重复输入或为您展示与您搜索内容相关联的商品，搜索的内容将会存储在我们的服务器中，因为存储是实现服务所必需的，在读取分享口令时您的剪切板功能将会被调用。<strong>请您注意，您的搜索关键词信息无法单独识别您的身份，不属于您的个人信息，我们有权以任何的目的对其进行使用；只有当您的搜索关键词信息与您的其他信息相互结合使用并可以识别您的身份时，则在结合使用期间，我们会将您的搜索关键词信息作为您的个人信息，与您的搜索历史记录一同按照本个人信息保护政策对其进行处理与保护。</strong></p><p><strong>为向您提供更便捷、更符合您个性化需求的信息展示、搜索及推送服务，让您有更好的用户体验，我们会根据您的设备信息（设备标识符（IMEI、IDFA、Android ID、IMSI、MAC、OAID的相关信息）、应用信息（应用崩溃信息、通知开关状态、应用软件列表相关信息）、设备参数及系统信息（设备类型、设备型号、操作系统及硬件相关信息）、网络信息：IP地址，WiFi信息，基站信息相关信息）</strong>和服务日志信息，提取您的偏好特征，并基于特征标签产出间接人群画像，用于展示、推送信息和可能的商业广告。例如在省钱兄APP客户端可能设有专门的模块“猜你喜欢”“搜索发现”等功能。我们努力保障您的浏览体验。如果您不想接受我们给您发送的商业广告，您可通过短信提示回复退订或我们提供的其他方式进行<strong>退订或关闭（我的—设置—个性化商品推荐）</strong>；在您使用我们提供的站内搜索服务时，我们也同时提供了不针对您个人特征的通用排序选项，若您想关闭该定制化服务，您可以通过人工客服的方式与我们联系。</p><p>&nbsp;此外，我们也会为了不断改进和优化上述的功能来使用您的上述信息。</p><p><strong>（3）业务功能三：为您提供收藏、关注与分享功能</strong></p><p>&nbsp;&nbsp;&nbsp;&nbsp;在您浏览我们网站或客户端的过程中，您可以选择对感兴趣的商品及/或服务进行收藏、通过我们提供的功能组件向其他第三方分享信息。在您使用上述功能的过程中，我们会收集包括您的收藏及获取优惠券信息的记录、分享历史在内的服务日志信息用于实现上述功能及其他我们明确告知的目的。</p><p><strong>（4）业务功能四：客服与售后功能</strong></p><p>当您与我们联系或提出售中售后、争议纠纷处理申请时，为了保障您的账户及系统安全，<strong>我们需要您提供必要的个人信息以核验您的会员身份。此时</strong>我们的电话客服和售后功能可能会使用您的账号信息和订单信息。</p><p><strong>为便于与您联系、尽快帮助您解决问题或记录相关问题的处理方案及结果，我们可能会保存您与我们的通信/通话记录及相关内容（包括账号信息、订单信息、您为了证明相关事实提供的其他信息，或您留下的联系方式信息），如果您针对具体订单进行咨询、投诉或提供建议的，我们会使用您的账号信息和订单信息。</strong></p><p>（5）<strong>业务功能五：交易订单及服务</strong></p><p><strong>为了便于您了解查询部分订单信息并对部分订单信息进行管理，我们会收集您在使用我们服务过程中产品的部分订单信息（包括：订单来源（平台）、订单创建时间、订单金额、所购买的商品及/或服务信息、具体订单号、返利情况、订单状态）用于您对订单进行查询、共享、向您提供客服与售后服务及其他我们明确告知的目的。</strong></p><p>2、&nbsp;<strong>开启内部审计、数据分析和研究</strong></p><p><strong>我们可能会收集您的订单信息、浏览信息、您的兴趣爱好进行数据分析，用来将您感兴趣的商品或服务信息展示给您；为您提供个性化服务或内容；或在您搜索时向您展示您可能希望找到的商品。例如在省钱兄APP客户端可能设有专门的模块“猜你喜欢”等功能。如果您不希望接受个性化内容的影响，您可以在商品展示页选择按照分类进行浏览和查找商品服务；我们还可能会在app中向您提供了不针对个人喜好、个性化的商品和服务。</strong></p><p>3、&nbsp;<strong>安全管理</strong></p><p>为保障您使用我们的产品与/或服务时系统的安全性，更准确地预防钓鱼网站欺诈和保护账户安全，我们可能会通过了解<strong>您的浏览信息、订单信息、您常用的软件信息、设备信息（基站定位、sim卡、GPS地址、传感器、WiFi、蓝牙信息、设备硬件序列号）、您常用的软件信息、设备MAC地址、操作系统类型、软件列表唯一设备识别码（包括MEID/IMEI/android ID/IDFA/GUID、SIM卡IMSI信息）</strong>手段来判断您的账号风险，并可能会记录一些我们认为有风险的链接（“URL”））；我们也会收集您的设备信息（包括设备型号、操作系统版本、设备设置、MAC地址及IMEI、IDFA、OAID等设备标识符、设备环境、移动应用列表软硬件特征信息）对于省钱兄使用问题进行分析、统计流量并排查可能存在的风险、在您选择向我们发送异常信息时予以排查。如您不同意我们记录前述信息，可能无法完成风控验证。</p><p>4、&nbsp;<strong>日志信息</strong></p><p><strong>当您使用省钱兄的搜索和信息展示服务时，我们的服务器会自动记录您的日志信息，包括您输入的搜索关键词信息和点击链接以及您的IP地址、设备信息（IMEI/MAC/Android ID/IDFA/OpenUDID/GUID/IP地址/SIM 卡 IMSI 信息）、浏览器的类型和使用的语言、操作系统版本、网络运营商的信息、访问服务时间和收藏记录。</strong></p><p><strong>当您使用的客户端软件频繁向我们进行报错或显示错误日志信息时，为了检查软件错误，给您提供更好的服务，我们会在您的设备上记录、收集您的配置信息、异常数据等用于诊断并完善产品及服务的文件。</strong></p><p><strong>&nbsp;</strong></p><p><strong>（二）您可选择是否授权我们收集和使用您的个人信息的情形</strong></p><p>1、为使您购物更便捷或更有乐趣，从而提升您在省钱兄的购物体验，我们的以下附加功能中可能会收集和使用您的个人信息。<strong>如果您不提供这些个人信息，您依然可以进行网上购物，但您可能无法使用这些可以为您所带来购物乐趣的附加功能或在购买某些商品时需要重复填写一些信息。</strong>这些附加功能包括：</p><p><strong>（1）关于位置信息：</strong>经您授权，我们会收集您的<strong>位置信息</strong>（我们仅收集您当时所处的地理位置，但不会将您各时段的位置信息进行结合以判断您的行踪轨迹，也不会保存您的轨迹）来判断您所处的地点。地理位置信息属于敏感信息，拒绝提供该信息会影响您在第三方平台享受的部分服务（首页-热门-省钱外卖、首页-生活-携程/飞猪、首页-生活-加油优惠需要您提供具体位置信息来进行定位、导航服务），但不影响您正常使用省钱兄产品的其他功能，您也可以随时取消您的地理位置信息授权。</p><p><strong>（2）基于相机/摄像头的附加功能：</strong>您可以在开启相机/摄像头权限后使用该功能进行拍摄照片或视频用于商品的照片或视频素材下载、分享或设置账户头像及图搜功能。您还可以通过上传带有您账户信息的照片来实现客服对您的身份核验（我的-联系我们-专属客服）。</p><p><strong>（3）基于客服功能的附加功能：</strong>在您使用客服功能时，我们可能还会需要收集您的如下个人敏感信息：<strong>联系方式（您与我们联系时使用的电话号码/电子邮箱或您向我们主动提供的其他联系方式）、您与我们的沟通信息（包括文字/图片/音视频）、与您需求相关联的其他必要信息</strong>。在此过程中您需开启麦克风权限<strong>（仅限IOS申请权限）</strong>。我们收集这些信息是为了调查事实与帮助您解决问题，如您拒绝提供可能导致您无法使用我们的客服用户响应机制。</p><p>当您与我们的人工客服互动时，如使用到上传视频功能则需要您开启麦克风权限（我的-联系我们-专属客服-人工客服）。请您知晓，<strong>即使您已同意开启麦克风权限，我们也仅会在您主动点击客户端内麦克风图标时通过麦克风获取语音信息。</strong></p><p><strong>（4）基于日历的附加服务：</strong>在您开启我们可读取/写入您日历的权限后，我们将收集您的日历信息用于向您提供爆品推荐（首页-爆品推荐）、外卖点餐（首页-省钱外卖icon）这些服务的提醒。请您注意，我们仅会在以上情况下读取您购物相关的日历信息。</p><p><strong>（5）基于本地存储的附加功能：</strong>为了提供更好的服务，我们在系统运行时需要记录相关信息到您手机的外部存储器中，需要您开启可读取/写入您存储的权限。您也可以在系统设置中关闭此权限。</p><p><strong>（6）基于电话状态的附加服务</strong>：为保障您的账号与使用安全，您需要授权我们读取本机识别码。</p><p><strong>（7）基于快速识别商品搜索的附加功能（仅限安卓申请权限）</strong>。在您开启悬浮窗权限后，您可通过首页搜索栏处的相机图标拍摄商品照片在本APP内进行对应/相似商品的优惠信息搜索，或跳转第三方平台进行商品搜索。</p><p>2、上述附加功能可能需要您在您的设备中向我们开启您的<strong>地理位置（位置信息）、相册（图片库）、相机/摄像头、麦克风、日历、存储、悬浮窗的访问权限，</strong>以实现这些功能所涉及的信息的收集和使用。并可以决定将这些权限随时的开启或关闭。</p><p>3、系统权限的调用</p><p>省钱兄平台在提供服务过程中，<strong><u>会调用您的一些设备权限，您可以在您的设备设置或我们客户端“我-设置-隐私—系统权限”中逐项选择关闭部分或全部权限，从而拒绝省钱兄收集与使用相应的个人信息</u></strong>。在不同设备中，权限显示方式及关闭方式有所不同，请您参照设备或系统开发系统说明与指引。<strong><u>您的设备系统可能会在必要信息授权的同时开放其他权限，省钱兄自身无法通过手动或自动设置关闭授权。以下是省钱兄调用权限对应的业务功能、调用权限的目的以及调用权限前向您询问的情况：</u></strong></p><p>设备权限</p><p>业务功能</p><p>调用目的</p><p>可否手动关闭</p><p>定位</p><p>外卖、第三方平台携程、飞猪、淘油宝、团油</p><p>定基于（精准）位置信息的个性化推荐功能</p><p>可以</p><p>存储</p><p>数据存储</p><p>读取、修改或删除SD 卡的内容</p><p>可以</p><p>相机/摄像头</p><p>头像</p><p>实现拍照上传、反馈情况功能</p><p>可以</p><p>麦克风（仅IOS）</p><p>在线客服</p><p>使用客服功能的视频功能，进行音频录制、视频拍摄</p><p>可以</p><p>电话状态</p><p>问题诊断</p><p>获取电话、设备信息</p><p>可以</p><p>相册（图片库）</p><p>在线客服</p><p>上传/下载保存头像、照片、视频</p><p>可以</p><p>通知</p><p>全部功能</p><p>接收平台推送内容</p><p>可以</p><p>日历</p><p>外卖</p><p>读写日历事件，实现日历功能，实现预告提醒</p><p>可以</p><p>悬浮窗</p><p>搜索框图搜页面</p><p>通过照片进行商品识别，已实现加速购物搜索</p><p>可以</p><p>&nbsp;</p><p><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请您注意：您开启这些权限即代表您授权我们可以收集和使用这些个人信息来实现上述的功能，您关闭权限即代表您取消了这些授权，则我们将不再继续收集和使用您的这些个人信息，也无法为您提供上述与这些权限所对应的功能。您关闭权限的决定不会影响此前基于您的授权所进行的个人信息的处理。</strong></p><p><strong>（三）您充分知晓，以下情形中，我们收集、使用个人信息无需征得您的授权同意：</strong></p><p><strong>1、与国家安全、国防安全有关的；</strong></p><p><strong>2、与公共安全、公共卫生、重大公共利益有关的；</strong></p><p><strong>3、与犯罪侦查、起诉、审判和判决执行等有关的；</strong></p><p><strong>4、出于维护个人信息主体或其他个人的生命、财产等重大合法权益但又很难得到本人同意的；</strong></p><p><strong>5、所收集的个人信息是个人信息主体自行向社会公众公开的；</strong></p><p><strong>6、从合法公开披露的信息中收集的您的个人信息的，如合法的新闻报道、政府信息公开等渠道；</strong></p><p><strong>7、根据您的要求签订合同所必需的；</strong></p><p><strong>8、用于维护所提供的产品与/或服务的安全稳定运行所必需的，例如发现、处置产品与/或服务的故障；</strong></p><p><strong>9、为合法的新闻报道所必需的；</strong></p><p><strong>10、学术研究机构基于公共利益开展统计或学术研究所必要，且对外提供学术研究或描述的结果时，对结果中所包含的个人信息进行去标识化处理的；</strong></p><p><strong>11、法律法规规定的其他情形。</strong></p><p><strong>（四）我们从第三方获得您个人信息的情形</strong></p><p><strong>我们可能从第三方获取您授权共享的账户信息（头像、昵称），并在您同意本个人信息保护政策后将您的第三方账户与您的省钱兄账户绑定，使您可以通过第三方账户直接登录并使用我们的产品与/或服务。我们会将依据与第三方的约定、对个人信息来源的合法性进行确认后，在符合相关法律和法规规定的前提下，使用您的这些个人信息。</strong></p><p><strong>（五）您个人信息使用的规则</strong></p><p>1、我们会根据本个人信息保护政策的约定并为实现我们的产品与/或服务功能对所收集的个人信息进行使用。</p><p>2、在收集您的个人信息后，我们将通过技术手段对数据进行去标识化处理，去标识化处理的信息将无法识别主体。</p><p><strong>3、请您注意，您在使用我们的产品与/或服务时所提供的所有个人信息，除非您删除或通过系统设置拒绝我们收集，否则将在您使用我们的产品与/或服务期间持续授权我们使用。在您注销账号时，我们将停止使用并删除您的个人信息。</strong></p><p>4、我们会对我们的产品与/或服务使用情况进行统计，并可能会与公众或第三方共享这些统计信息，以展示我们的产品与/或服务的整体使用趋势。但这些统计信息不包含您的任何身份识别信息。</p><p>5、当我们展示您的个人信息时，我们会采用包括内容替换、匿名处理方式对您的信息进行脱敏，以保护您的信息安全。</p><p>6、当我们要将您的个人信息用于本政策未载明的其它用途时，或基于特定目的收集而来的信息用于其他目的时，会通过您主动做出勾选的形式事先征求您的同意。</p><p>7、服务交互&nbsp;&nbsp;&nbsp;&nbsp;</p><p>省钱兄APP中含有多种多样的小程序/H5服务，您使用小程序或H5时，省钱兄需要收集的您现该功能所必要的日志信息，用以为您提供小程序或/H5相关的技术支持。</p><p>为保障我们的服务的稳定运行和功能服务，我们的应用中会嵌入授权合作伙伴的软件工具包或其他类似的应用程序。您可以查看附录：第三方合作SDK服务列表及其隐私政策链接明细表我们接入的软件工具包采集信息的详情。我们会对相关程序/H5接口和软件工具包进行严格的安全检测，并与授权合作伙伴约定严格的数据保护措施，令其按照本政策以及其他任何相关的保密和安全措施来处理个人信息。</p><p><strong>当省钱兄与第三方应用交互为您提供服务时，省钱兄可能会根据服务场景的必需和系统权限的设定向您的设备系统或者第三方提供您的位置信息、手机号码。</strong>如您拒绝此类信息收集使用，需要在设备系统中进行设置或关闭相关程序/H5服务。</p><p>二、我们如何使用&nbsp;Cookie 和同类技术</p><p>1、为实现您联机体验的个性化需求，使您获得更轻松的访问体验。我们可能会在您的移动设备上发送一个或多个名为Cookie的小数据文件，指定给您的Cookies是唯一的，它只能被将Cookie发布给您的域中的Web服务器读取。我们向您发送Cookie是为了简化您重复登录的步骤、存储您的购物偏好或您购物车中的商品等数据进而为您提供购物的偏好设置、帮助您优化对广告的选择与互动、帮助判断您的登录状态以及账户或数据安全。</p><p>2、我们不会将 Cookie用于本个人信息保护政策所述目的之外的任何用途。您可根据自己的偏好管理或删除 Cookie。您可以清除软件内保存的所有 Cookie，大部分网络浏览器会自动接受Cookie，但您通常可根据自己的需要来修改浏览器的设置以拒绝 Cookie；另外，您也可以清除软件内保存的所有Cookie。但如果您这么做，您可能需要在每一次访问省钱兄时亲自更改用户设置，而且您之前所记录的相应信息也均会被删除。</p><p>3、SDK的使用。<strong>为了使您能够接收信息推送、在第三方平台分享信息、播放视频必要的功能用途，省钱兄部分产品中可能涉及第三方提供的自动化工具（包括但不限于代码、接口、开发工具包（SDK））嵌入或接入。</strong>如基于终端厂商的要求嵌入通知类SDK，基于统计分析的需求嵌入数据处理服务商的SDK,第三方所调用的权限类型可能包括获取<strong>您的实时位置信息、读/写您的外部存储卡、读取您的手机状态和身份，查看WLAN连接、检索正在运行的应用/蓝牙（详见《第三方合作SDK服务列表及隐私政策链接明细表》）</strong>。我们会对合作伙伴获取有关信息的应用程序接口（API）、软件工具开发包（SDK）进行严格的安全监测。<strong>请您在同意开通相应权限前认真阅读对应第三方服务商的隐私政策。如您不同意该第三方收集相关个人信息，可能无法获得相应服务，但不影响您正常使用省钱兄App的其他功能或服务。</strong></p><p>三、我们如何共享、转让、公开披露您的个人信息</p><p>（一）共享</p><p>&nbsp;&nbsp;1、我们不会与省钱兄以外的任何公司、组织和个人共享您的个人信息，但以下情况除外：</p><p>（1）事先获得您明确的同意或授权；</p><p>（2）根据适用的法律法规、法律程序的要求、强制性的行政或司法要求所必须的情况下进行提供；</p><p>（3）在法律法规允许的范围内，为维护省钱兄、省钱兄的关联方或合作伙伴、您或其他省钱兄用户或社会公众利益、财产或安全免遭损害而有必要提供；</p><p>（4）只有共享您的信息，才能实现我们的产品与/或服务的核心功能或提供您需要的服务；</p><p>（5）应您需求为您处理您与他人的纠纷或争议；</p><p>（6）符合与您签署的相关协议（包括在线签署的电子协议以及相应的平台规则）或其他的法律文件约定所提供；</p><p>（7）基于学术研究而使用；</p><p>（8）基于符合法律法规的社会公共利益而使用。</p><p><strong>2、我们可能会将您的个人信息与我们的关联方共享，但我们只会共享必要的个人信息，且受本个人信息保护政策中所声明目的的约束。我们的关联方如要改变个人信息的处理目的，将再次征求您的授权同意。</strong></p><p><strong>3、我们可能会向合作伙伴等第三方共享您的订单信息、账户信息以及位置信息，以保障为您提供的服务顺利完成。但我们仅会出于合法、正当、必要、特定、明确的目的共享您的个人信息，并且只会共享提供服务所必要的个人信息。我们的合作伙伴无权将共享的个人信息用于任何其他用途。</strong></p><p>我们的合作伙伴可能包括以下类型：</p><p><strong>（1）软件服务提供商、智能设备提供商和系统服务提供商。</strong>省钱兄与第三方软件、设备系统结合为您提供基于位置的服务时，可能会基于您对<strong>系统定位的授权及设定，收取您的位置信息及设备信息并提供给系统</strong>，特殊情景下会包含您另行填写的个人信息。如果您拒绝此类信息收集及使用，您需要在设备系统中进行设置或关闭服务的软件，省钱兄无法通过自动或手动设置关闭相关服务。</p><p><strong>（2）商品或技术服务的供应商。我们可能会将您的个人信息共享给支持我们功能的第三方。</strong>这些支持包括为我们的供货或提供基础设施技术服务、物流配送服务、支付服务、数据处理、安全认证。我们共享这些信息的目的是可以实现我们产品与/或服务的核心购物功能，比如当您通过省钱兄去第三方平台购买商品或服务时，我们需将您的<strong>订单信息</strong>与交易有关的必要信息与第三方商家共享来实现您向其具体平台购买商品或服务的需求，并促使其可以完成后续的售后服务。我们使用神策来分析用户日活、统计数据。</p><p><strong>（3）委托我们进行推广的合作伙伴。</strong>有时我们会代表其他企业向使用我们产品与/或服务的用户群提供促销推广的服务。<strong>我们可能会使用您的个人信息以及您的非个人信息集合形成的间接用户画像与委托我们进行推广的合作伙伴（“委托方”）共享，但我们仅会向这些委托方提供推广的覆盖面和有效性的信息，而不会提供您的个人身份信息（手机号码），或者我们将这些信息进行汇总，以便它不会识别您个人。</strong>比如我们可以告知该委托方有多少人看了他们的推广信息或在看到这些信息后购买了委托方的商品，或者向他们提供不能识别个人身份的统计信息，帮助他们了解其受众或顾客。</p><p><strong>（4）打车、加油服务合作伙伴。</strong>当您选择使用打车或加油服务时，为了您能够顺利用车/加油服务并为您提供更安全、便利的服务，服务提供方将获得<strong>您的手机号信息、叫车时的时间/地点信息、目的地信息、紧急联系人的信息、加油时的地点信息</strong>。</p><p><strong>（5）基于合理商业习惯而共享。</strong>如我们与第三方共享联合营销时，您作为中奖/获胜者的，我们可能会向第三方共享您的个人信息，以便您能够顺利领取您的奖励。</p><p><strong>（6）我们可能会将所收集到的个人信息再进行匿名化处理后用于大数据分析，如形成不包括任何个人信息的热力图、洞察报告等。我们可能对外公开并与我们的合作伙伴分享经统计加工后不含身份识别内容的信息，用于了解用户如何使用我们服务或让公众了解我们服务的总体使用趋势。</strong></p><p><strong>（二）转让</strong></p><p><strong>我们不会将您的个人信息转让给任何公司、组织和个人，但以下情况除外：</strong></p><p>1、事先获得您明确的同意或授权；</p><p>2、根据适用的法律法规、法律程序的要求、强制性的行政或司法要求所必须的情况进行提供；</p><p>3、符合与您签署的相关协议（包括在线签署的电子协议以及相应的平台规则）或其他的法律文件约定所提供；</p><p>4、在涉及合并、收购、资产转让或类似的交易时，如涉及到个人信息转让，我们会要求新的持有您个人信息的公司、组织继续受本个人信息保护政策的约束，否则,我们将要求该公司、组织重新向您征求授权同意。</p><p><strong>（三）公开披露</strong></p><p>我们仅会在以下情况下，公开披露您的个人信息：</p><p>1、获得您明确同意后；</p><p>2、基于法律的披露：在法律、法律程序、诉讼或政府主管部门强制性要求的情况下，我们可能会公开披露您的个人信息。</p><p>四、我们如何保护和保存您的个人信息</p><p>（一）我们保护您个人信息的技术与措施</p><p><strong>我们非常重视个人信息安全，并采取一切合理可行的措施，保护您的个人信息：</strong></p><p><strong>1、数据安全技术措施</strong></p><p>我们会采用符合业界标准的安全防护措施，包括建立合理的制度规范、安全技术来防止您的个人信息遭到未经授权的访问使用、修改，避免数据的损坏或丢失。</p><p>我们的网络服务采取了传输层安全协议等加密技术，确保用户数据在传输过程中的安全。</p><p>我们采取加密技术对用户个人信息进行加密保存，并通过隔离技术进行隔离。</p><p>我们采用严格的数据访问权限控制和多重身份认证技术保护个人信息，避免数据被违规使用。</p><p>我们采用代码安全自动检查、数据访问日志分析技术进行个人信息安全审计。</p><p><strong>2、省钱兄为保护个人信息采取的其他安全措施</strong></p><p>²&nbsp;我们通过建立数据分类分级制度、数据安全管理规范、数据安全开发规范来管理规范个人信息的存储和使用。</p><p>²&nbsp;我们通过信息接触者保密协议、监控和审计机制来对数据进行全面安全控制。</p><p>²&nbsp;我们建立信息保护专职部门、数据安全应急响应组织来推进和保障个人信息安全。</p><p><strong>3、我们仅允许有必要知晓这些信息的省钱兄及省钱兄关联方的员工、合作伙伴访问个人信息，并为此设置了严格的访问权限控制和监控机制。我们同时要求可能接触到您个人信息的所有人员履行相应的保密义务。如果未能履行这些义务，可能会被追究法律责任或被中止与省钱兄的合作关系。</strong>我们同时要求可能接触到您个人信息的所有人员履行相应的保密义务。如果未能履行这些义务，可能会被追究法律责任或被终止与省钱兄的合作关系。</p><p><strong>4、我们会采取一切合理可行的措施，确保未收集无关的个人信息。</strong>我们只会在达成本政策所述目的所需的期限内保留您的个人信息，除非需要延长保留期或受到法律的允许。</p><p>5、互联网并非绝对安全的环境，而且电子邮件、即时通讯、社交软件等与其他用户的交流方式无法确定是否完全加密，我们建议您使用此类工具时请使用复杂密码，并注意保护您的个人信息安全。</p><p>6、互联网环境并非百分之百安全，我们将尽力确保或担保您发送给我们的任何信息的安全性。如果我们的物理、技术、或管理防护设施遭到破坏，导致信息被非授权访问、公开披露、篡改、或毁坏，导致您的合法权益受损，我们将承担相应的法律责任。</p><p><strong>7、安全事件处置</strong></p><p>在通过省钱兄与第三方进行网上商品或服务的交易时，您不可避免地要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或者邮政地址等。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。</p><p>为应对个人信息泄露、损毁和丢失等可能出现的风险，我们制定了多项制度，明确安全事件、安全漏洞的分类分级标准及相应的处理流程。我们也为安全事件建立了专门的应急响应团队，按照安全事件处置规范要求，针对不同安全事件启动安全预案，进行止损、分析、定位、制定补救措施、联合相关部门进行溯源和打击。在不幸发生个人信息安全事件后，我们将按照法律法规的要求，及时向您告知：安全事件的基本情况和可能的影响、我们已采取或将要采取的处置措施、您可自主防范和降低风险的建议、对您的补救措施等。我们同时将及时将事件相关情况可能以信函、电话、推送通知等方式告知您，难以逐一告知个人信息主体时，我们会采取合理、有效的方式发布公告。同时，我们还将按照监管部门要求，主动上报个人信息安全事件的处置情况。</p><p>8、如果您对我们的个人信息保护有任何疑问，可通过本政策最下方约定的联系方式联系我们。如您发现自己的个人信息泄密，尤其是您的账户及密码发生泄露，请您立即通过本政策中约定的联系方式联络我们，以便我们采取相应措施。</p><p><strong>（二）您个人信息的保存</strong></p><p>1、您的个人信息将全被存储于中华人民共和国境内。<strong>如您使用跨境交易服务，且需要向境外传输您的个人信息完成交易的，我们会单独征得您的授权同意并要求接收方按照我们的说明、本个人信息保护政策以及其他任何相关的保密和安全措施来处理这些个人信息。</strong></p><p>2、除非法律法规另有规定，省钱兄将按如下期间保存您的信息：</p><p>（1）您在使用省钱兄产品或服务期间，我们将持续为您保存，除非您自主删除这些信息或注销账户；</p><p>（2）当您删除个人信息或注销账户后，我们将及时采取措施将您的个人信息从业务功能系统删除，使其保持不可被检索、访问的状态。</p><p>（3）为了维护您和省钱兄的合法权益，我们有权在您删除或注销账户之日起36个月内继续在单独的系统中保存您的信息用于证据保存。证据保存期限到期后，我们将及时对该等保存的个人信息进行匿名化处理。如您在注销帐号或删除信息时，愿意放弃对省钱兄与删除信息相关的全部法律救济权利省钱兄将依据网络安全法和其他法律法规规定在您删除信息后6个月将相关数据删除。</p><p>（4）如果我们终止服务或运营，我们会至少提前十五个工作日向您通知，并在终止服务或运营后对您的个人信息进行删除或匿名化处理。</p><p>五、您的权利</p><p>省钱兄非常重视您对个人信息的关注，并尽全力保护您对于您个人信息访问、更正、删除以及撤回同意的权利，以使您拥有充分的能力保障您的隐私和安全。您的权利包括：</p><p>（一）查询访问和更正您的个人信息</p><p>除法律法规规定外，您有权随时访问和更正您的个人信息。如果您想行使数据访问权，可以通过以下方式自行访问：</p><p>账户信息——如果您希望访问或编辑您的账户中的个人资料信息和支付信息、更改您的密码、添加安全信息或关闭您的账户，您可以通过访问“省钱兄APP——我的——设置”执行此类操作。</p><p>搜索信息——您可以在省钱兄APP 首页界面的搜索栏中访问或清除您的搜索历史记录。</p><p>对于您在使用我们产品或服务过程中产生的其他个人信息，只要我们不需要过多投入，我们会向您提供。如果您想行使数据访问权，请联系客服详细说明您的需求。</p><p>另外，如您发现我们处理的关于您的个人信息有错误时，您有权要求我们作出更正。您可以通过“（一）查询访问和更正您的个人信息”中罗列的方式提出更正申请。</p><p>如果您无法通过上述途径访问或更正这些个人信息，您可以随时通过省钱兄APP的客服联系我们。我们将在15个工作日内回复您的请求。</p><p>（二）删除您的个人信息</p><p>您在我们的产品与/或服务页面访问“省钱兄APP—我的—设置”或“省钱兄APP 首页界面的搜索栏”中可以直接清除或删除的信息，包括搜索历史信息、浏览信息；当您删除个人信息或注销账户删除信息后，我们将及时采取措施将您个人信息从业务功能系统删除，使其保持不可被检索、访问的状态。在以下情形中，您可以向我们提出删除个人信息的请求：</p><p>（1）如果我们处理个人信息的行为违反法律法规；</p><p>（2）如果我们收集、使用您的个人信息，却未征得您的同意；</p><p>（3）如果我们处理个人信息的行为违反了与您的约定；</p><p>（4）如果您注销了省钱兄账户；</p><p>（5）如果我们终止服务及运营。</p><p>若我们决定响应您的删除请求，我们还将同时通知从我们获得您的个人信息的实体，要求其及时删除，除非法律法规另有规定，或这些实体获得您的独立授权。当您从我们的服务中删除信息后，我们可能不会立即备份系统中删除相应的信息，但会在备份更新时删除这些信息。</p><p>（三）改变您授权同意的范围或撤回您的授权</p><p>您可以通过删除信息、关闭设备功能、在省钱兄软件中进行隐私设置这些方式改变您授权我们继续收集个人信息的范围或撤回您的授权。如通过“我的—设置—系统权限”进行操作；您也可以通过注销账户的方式，撤回我们继续收集您个人信息的全部授权。</p><p><strong>请您理解，每个业务功能需要一些基本的个人信息才能得以完成，当您撤回同意或授权后，我们无法继续为您提供撤回同意或授权所对应的服务，也不再处理您相应的个人信息。但您撤回同意或授权的决定，不会影响此前基于您的授权而开展的个人信息处理。</strong></p><p>（四）注销账户</p><p>您可以在我们的产品中通过“我的—设置—关于—帐号注销”的访问路径直接申请注销账户。<strong>关于您注销账户的方式，请详见《省钱兄账户注销规则》。我们将在收到您注销请求并验证您的身份后的7个工作日内答复。</strong>您注销账户后，我们将停止为您提供产品与/或服务，并依据您的要求，除法律法规另有规定外，我们将删除您的个人信息。</p><p><strong>（五）商品推荐、发送促销营销信息</strong></p><p>如果您不想接受我们给您发送的促销信息，您随时可通过以下方式取消：</p><p>（1）您可以通过移动端APP设置是否接受我们推送给您的商品和促销信息。</p><p>（2）我们会与第三方的平台或媒体（“平台”）合作基于您的个人信息向您推荐个性化的商品。这些个人信息包括诸如在该网站的购物情况、访问该网站关联公司网站的情况及您在您的账户设置中填写的兴趣爱好。平台仅向我们提供了展示商品的窗口，窗口内容的链接是省钱兄站内的个性化商品展示信息，由省钱兄进行管理，因此我们不会向广告商提供您的任何的个人信息。而且我们在推荐窗内设置了屏蔽功能，您可选择屏蔽您不喜欢的广告。如您不愿意接受省钱兄在单个平台上的推荐服务，请联系平台进行关闭。</p><p>（六）响应您的请求</p><p>如果您无法通过上述方式访问、更正或删除您的个人信息，或您需要访问、更正或删除您在使用我们产品与/或服务时所产生的其他个人信息，或您认为省钱兄存在任何违反法律法规或与您关于个人信息的收集或使用的约定，或您需要注销账户时，您均可以通过本协议指定的方式与我们联系。<strong>为了保障安全，我们可能需要您提供书面请求，或以其他方式证明您的身份，我们将在收到您反馈并验证您的身份后的7个工作日内答复您的请求。</strong>对于那些无端重复、需要过多技术手段（例如，需要开发新系统或从根本上改变现行惯例）、给他人合法权益带来风险或者非常不切实际（例如，涉及备份磁带上存放的信息）的请求，我们可能会予以拒绝。</p><p><strong>在以下情形中，按照法律法规要求，我们将无法响应您的请求：</strong></p><p><strong>（1）与国家安全、国防安全有关的；</strong></p><p><strong>（2）与公共安全、公共卫生、重大公共利益有关的；</strong></p><p><strong>（3）与犯罪侦查、起诉和审判等有关的；</strong></p><p><strong>（4）有充分证据表明您存在主观恶意或滥用权利的；</strong></p><p><strong>（5）响应您的请求将导致您或其他个人、组织的合法权益受到严重损害的。</strong></p><p>（七）<strong>获取个人信息副本</strong></p><p><strong>如您想获取我们所收集的关于您的个人基本资料订单信息的副本，您可以通过客服渠道与我们取得联系，我们将在15个工作日内回复您的获取请求。</strong></p><p>（八）<strong>约束信息系统自动决策</strong></p><p><strong>在某些业务功能中，我们可能仅依据信息系统、算法等在内的非人工自动决策机制做出决定。如果这些决定显著影响您的合法权益，您有权要求我们作出解释，我们也将提供适当的救济方式。</strong></p><p>六、未成年人的个人信息保护</p><p>我们的产品和服务主要面向成人。<strong>若您是18周岁以下的未成年人，在使用我们的产品与/或服务前，应事先取得您家长或法定监护人的书面同意。</strong></p><p>1、我们非常重视对未成年人个人信息的保护。省钱兄会根据国家相关法律法规的规定保护未成年人的个人信息。</p><p>2、对于经父母或法定监护人同意而收集未成年人个人信息的情况，我们只会在受到法律允许、父母或监护人明确同意或者保护未成年人所必要的情况下使用或公开披露此信息。</p><p>3、尽管当地法律和习俗对儿童的定义不同，但<strong>我们将不满14周岁的任何人均视为儿童</strong>。</p><p>4、如果我们发现自己在未事先获得可证实的父母或法定监护人同意的情况下收集了未成年人的个人信息，则会设法尽快删除相关数据。</p><p>七、您的个人信息如何在全球范围转移</p><p>原则上，我们在中华人民共和国境内收集和产生的个人信息，将存储在中华人民共和国境内。如未来因业务发展需要，我们通过遍布全球的资源和服务器提供产品或服务，在获得您的授权同意后，您的个人信息可能会被转移到您使用产品或服务所在国家/地区的境外管辖区，或者受到来自这些管辖区的访问。此类管辖区可能设有不同的数据保护法，甚至未设立相关法律。</p><p>在此类情况下，我们会确保您的个人信息得到在中华人民共和国境内足够同等的保护。例如，我们会请求您对跨境转移个人信息的同意，或者在跨境数据转移之前实施数据去标识化等安全举措。</p><p>八、关于本政策的更新</p><p>1、为给您提供更好的服务以及随着我们业务的发展，本个人信息保护政策也会随之更新。但未经您明确同意，我们不会削减您依据本个人信息保护政策所应享有的权利。我们会通过省钱兄移动端上发出更新版本并通过公告或以其他适当方式提醒您相关内容的更新，也请您访问省钱兄移动端以便及时了解最新的个人信息保护政策。</p><p>2、对于重大变更，我们还会提供更为显著的通知（我们会通过包括但不限于电话、短信或在浏览页面做特别提示等方式，说明个人信息保护政策的具体变更内容）。</p><p><strong>本政策所指的重大变更包括但不限于：</strong></p><p><strong>（1）我们的服务模式发生重大变化。如处理个人信息的目的、处理的个人信息类型、个人信息的使用方式等；</strong></p><p><strong>（2）我们在所有权结构、组织架构等方面发生重大变化。如业务调整、破产并购等引起的所有者变更等；</strong></p><p><strong>（3）个人信息共享、转让或公开披露的主要对象发生变化；</strong></p><p><strong>（4）您参与个人信息处理方面的权利及其行使方式发生重大变化；</strong></p><p><strong>（5）我们负责处理个人信息安全的责任部门、联络方式及投诉渠道发生变化时；</strong></p><p><strong>（6）个人信息安全影响评估报告表明存在高风险时。</strong></p><p>九、如何联系我们</p><p>1、如您对本个人信息保护政策或您在使用省钱兄产品或服务时，就您的个人信息的收集、使用、共享、查询、删除、更正等相关事宜有任何问题、意见或建议，或您在使用产品或服务时遇到哪些问题，您都可以通过访问省钱兄APP在线客服系统与我们联系。</p><p>2、我们还设立了个人信息保护专职部门，您可以通过在线客服的方式与我们联系。</p><p>3、特别提醒您，如您反馈的问题涉及您的个人信息，尤其是您的个人敏感信息，请您不要直接在电话或邮件中进行透露，我们将会在确保您个人信息安全的情况下与您进一步沟通。</p><p>4、我们将在收到您的反馈并验证您身份后15个工作日内答复您的相关请求。</p><p><strong>如果您对我们的服务回复不满意，特别是您认为我们的个人信息处理行为损害了您的合法权益，您还可以通过向被告住所地有管辖权的法院提起诉讼来寻求解决方案。</strong></p><p><strong>&nbsp;</strong></p><p><strong>附录：第三方合作SDK服务列表及其隐私政策链接明细表</strong></p><p>为保障省钱兄App 的稳定运行或实现相关功能，我们可能会接入由第三方提供的软件开发包（SDK）实现前述目的。我们会进到审慎义务，对合作方获取信息的软件工具开发包（SDK）进行严格的安全检测，以保护数据安全。</p><p>我们接入的第三方SDK主要服务于您以及其他用户的需求，因此在满足新的服务需求及业务功能变更时我们可能会调整我们接入的第三方SDK。我们会及时在本说明中向您公开说明接入第三方SDK 的最新情况。目前省钱兄App接入的第三方SDK列明如下：</p><p>SDK名称</p><p>场景描述</p><p>个人信息类型</p><p>第三方机构名称</p><p>支付宝SDK</p><p>使用支付宝账号授权在省钱兄APP进行提现；第三方账户在APP登录</p><p>网络身份标识信息（支付宝ID）</p><p>支付宝（中国）网络技术有限公司</p><p><a href=\"https://render.alipay.com/p/c/k2cx0tg8\" rel=\"noopener noreferrer\" target=\"_blank\">https://render.alipay.com/p/c/k2cx0tg8</a></p><p>微信</p><p>使用微信授权登录省钱兄APP、分享</p><p>网络身份标识信息（微信ID、头像、昵称）</p><p>深圳市腾讯计算机系统有限公司</p><p><a href=\"https://weixin.qq.com/cgi-bin/readtemplate?lang=zh_CN&amp;t=weixin_agreement&amp;s=privacy\" rel=\"noopener noreferrer\" target=\"_blank\">https://weixin.qq.com/cgi-bin/readtemplate?lang=zh_CN&amp;t=weixin_agreement&amp;s=privacy</a></p><p>苹果SDK</p><p>使用苹果账户授权登录</p><p>网络身份标识信息（苹果账号）</p><p>苹果公司</p><p>https://www.apple.com.cn/legal/privacy/</p><p>极光推送</p><p>用于实现消息推送（或其他推送）功能</p><p>个人常用设备信息、个人位置信息、网络身份标识信息</p><p>深圳市和讯华谷信息技术有限公司</p><p>https://www.jiguang.cn/license/privacy</p><p>bugly</p><p>查找对应崩溃信息</p><p>个人基本资料、系统日志信息</p><p>深圳市腾讯计算机系统有限公司</p><p>https://bugly.qq.com/v2/contract</p><p>MobTech ShareSDK</p><p>应用于APP微信登录，分享、QQ分享、微博分享</p><p>系统运行信息、网络状态信息、匿名设备标识符(OAID)、应用列表信息、基站信息、地理位置</p><p>上海游昆信息技术有限公司http://www.mob.com/about/policy</p><p>SensorsAnalytics SDK</p><p>用户浏览页面和点击行为采集</p><p>个人常用设备信息、个人位置信息、</p><p>神策网络科技 (北京) 有限公司</p><p>https://www.sensorsdata.cn/terms/privacy.html</p><p>阿里百川</p><p>淘宝授权，直播业务</p><p>个人常用设备信息、个人位置信息、网络身份标识信息、个人上网记录、麦克风权限</p><p>北京阿里巴巴云计算技术有限公司</p><p><a href=\"https://terms.alicdn.com/legal-agreement/terms/suit_bu1_b2b/suit_bu1_b2b201703271337_94551.html?spm=a260k.dacugeneral.home2019footer.31.6633436cKdjxBP\" rel=\"noopener noreferrer\" target=\"_blank\">https://terms.alicdn.com/legal-agreement/terms/suit_bu1_b2b/suit_bu1_b2b201703271337_94551.html?spm=a260k.dacugeneral.home2019footer.31.6633436cKdjxBP</a></p><p>定位功能</p><p>设备功能，具体定位信息</p><p>使用加油优惠、飞猪、口碑、美团业务，</p><p>车主邦（北京）科技有限公司：https://v3hydev.czb365.com/userprotocol</p><p>浙江飞猪网络技术有限公司：https://terms.alicdn.com/legal-agreement/terms/Flyingpig/Flyingpig201802061043_85261.html?spm=181.11358650.a1z6c.52.6263223eQ6iN2h&amp;ttid=sem.000001061</p><p>上海拉扎斯信息科技有限公司：https://rule.koubei.com/#/rule?type=suggest&amp;no=ysbh0302</p><p>京东sdk</p><p>监测app账号跳转京东app异常登录</p><p>个人常用设备信息</p><p>北京京东叁佰陆拾度电子商务有限公司</p><p>https://about.jd.com/privacy/</p><p>拼多多</p><p>跳转拼多多app</p><p>个人基本资料</p><p>上海寻梦信息技术有限公司https://mobile.yangkeduo.com/private_policy.html</p><p>友盟移动统计SDK</p><p>对用户浏览页面和点击行为进行采集统计分析服务</p><p>个人常用设备信息、个人位置信息</p><p>友盟同欣（北京）科技有限公司</p><p><u>https://www.umeng</u>.com/page/policy</p><p>flutter sdk</p><p>APP 内功能栏目界面设计构建</p><p>个人上网记录</p><p>Google公司</p><p>https://docs.github.com/cn/github/site-policy/github-privacy-statement</p><p>团油</p><p>加油服务</p><p>个人基本资料、个人位置信息</p><p>车主邦（北京）科技有限公司</p><p>https://v3hydev.czb365.com/userprotocol</p><p>实名认证SDK</p><p>实名校验</p><p>个人常用设备信息、个人基本资料</p><p>中国移动通信集团有限公司</p><p>http://dev.10086.cn/docInside?contentId=10000009833270</p><p>本机号码校验</p><p>使用移动手机号码授权登录</p><p>个人常用设备信息、个人基本资料</p><p>中国移动通信集团有限公司</p><p><a href=\"http://dev.10086.cn/docInside?contentId=10000009826661\" rel=\"noopener noreferrer\" target=\"_blank\">http://dev.10086.cn/docInside?contentId=10000009826661</a></p><p>天翼账号 SDK</p><p>使用电信手机号码授权登录</p><p>个人常用设备信息、个人基本资料</p><p>中国电信集团有限公司</p><p>https://e.189.cn/sdk/agreement/content.do?type=main&amp;appKey=&amp;hidetop=true&amp;returnUrl=</p><p>中国联通</p><p>使用联通手机号码授权登录</p><p>个人常用设备信息、个人基本资料</p><p>中国联合网络通信集团有限公司</p><p><a href=\"https://saas.wostore.cn/solution.html?param=1&amp;num=3\" rel=\"noopener noreferrer\" target=\"_blank\">https://saas.wostore.cn/solution.html?param=1&amp;num=3</a></p><p>魅族集成推送SDK</p><p>提供魅族设备消息推送服务</p><p>个人常用设备信息、个人位置信息、个人上网记录</p><p>珠海市魅族通讯设备有限公司</p><p>http://open-wiki.flyme.cn/doc-wiki/index#id?10</p><p>Zxing 二维码扫描</p><p>二维码扫描</p><p>个人基本资料、个人财产信息、个人常用设备信息、个人上网记录</p><p>Google公司</p><p>https://docs.github.com/cn/github/site-policy/github-privacy-statement</p><p>百川云旺·即时通讯</p><p>淘宝直播</p><p>个人常用设备信息</p><p>北京阿里巴巴云计算技术有限公司</p><p>https://baichuan.taobao.com/portal/newDocIndex.htm?spm</p><p>极光认证</p><p>一键登录号码认证</p><p>个人常用设备信息、个人基本资料、网络信息</p><p>设备信息：设备标识符（IMEI、IDFA、Android ID、MAC、OAID、MEID等相关信息）、应用信息（应用崩溃信息、通知开关状态、软件列表等相关信息）、设备参数及系统信息（设备类型、设备型号、操作系统及硬件相关信息）</p><p>深圳市和讯华谷信息技术有限公司</p><p>https://www.jiguang.cn/identify</p><p>阿里云 OSS 对象存储 SDK</p><p>修改头像，意见反馈等图片上传</p><p>个人常用设备信息</p><p>北京阿里巴巴云计算技术有限公司</p><p>https://help.aliyun.com/document_detail/32043.html?spm=&nbsp;</p><p>a2c4g.11186623.6.1345.254d37bboim1mo</p><p>华为云云视频点播 SDK</p><p>淘宝直播</p><p>个人常用设备信息、个人基本资料、网络信息、网络身份标识信息、个人上网记录</p><p>华为软件技术有限公司</p><p>https://developer.huaweicloud.com/sdk?all#ANDROID</p><p>网易七鱼云客服SDK</p><p>提供在线人工客服系统、智能机器人客服系统</p><p>设备MAC地址、设备信息、设备登录的用户昵称、手机号</p><p>杭州网易质云科技有限公司</p><p>https://reg.163.com/agreement_mobile_ysbh_wap.shtml?v=20171127</p><p><strong>&nbsp;</strong></p><p>个人信息字段定义以及穷举</p><p>设备MAC地址：设备的mac地址，确认移动设备位置的地址</p><p>唯一设备识别码：iOS设备为IDFA、UUID，Android设备为IMEI、oaid、AndroidId、UUID（Android以上识别码可能会收集多个）</p><p>设备IP地址：移动互联网链接协议，确认网络连接的运营商服务</p><p>设备信息：设备品牌、设备型号、操作系统、操作系统版本</p><p>网络信息：当前网络环境（如WIFI、5G、4G、3G、2G）</p><p>运营商信息：移动、联通、电信</p><p>应用列表：判断是否安装了合作的第三方APP（如微信、QQ、新浪微博、淘宝、支付宝、天猫、美团、饿了么）</p><p>&nbsp;</p><p>附件:《省钱兄账号注销规则》</p><p>您在申请注销省钱兄账号前，应当认真阅读《省钱兄账号注销规则》（以下称“《注销规则》”）。</p><p>请您务必审慎阅读、充分理解协议中相关条款内容，其中包括：</p><p>1、与您约定免除或限制责任的条款；</p><p>2、其他以粗体下划线标识的重要条款。</p><p>如您对本《注销规则》有任何疑问，可通过省钱兄APP联系专属客服，您也可通过省钱兄APP的“意见反馈”提出申请需求。</p><p>【特别提示】当您按照注销页面提示阅读并同意本《注销规则》及相关条款与条件且完成全部注销程序后，即表示您已充分阅读、理解并接受本《注销规则》的全部内容。阅读本《注销规则》的过程中，如果您不同意相关任何条款和条件约定，请您立即停止账号注销程序。</p><p>&nbsp;</p><p>亲爱的省钱兄个人消费者用户：</p><p>我们在此善意地提醒您，您注销账号的行为会给您的售后维权带来诸多不便，且注销省钱兄账号后，您的个人信息我们只会在省钱兄的前台系统中去除，使其保持不可被检索、访问的状态，或对其进行匿名化处理。您知晓并理解，根据相关法律规定相关交易记录须在省钱兄后台保存5年甚至更长的时间。</p><p>1.&nbsp;如果您仍执意注销账号，您的账号需同时满足以下条件：</p><p>（1）在最近一个月内，账号没有进行更改密码、更改绑定信息等敏感操作，账户没有被盗、被封等风险；</p><p>（2）账户在省钱兄系统中无资产和虚拟权益（包括但不限于账户收益、用户权益、优惠券等）；</p><p>（3）账号无任何纠纷，包括投诉举报或被投诉举报；</p><p>（4）账号为正常使用中的账号且无任何账户被限制的记录；</p><p>1.&nbsp;省钱兄账号一旦被注销将不可恢复，请您在操作之前自行备份省钱兄账号相关的所有信息和数据。请您保存好订单商品和服务的交易凭证、票据等资料，否则您有可能须支付额外的账户和订单查询费用，或无法享受售后服务。</p><p>2.&nbsp;在省钱兄账户注销期间，如果您的省钱兄账户涉及争议纠纷，包括但不限于投诉、举报、诉讼、仲裁、国家有权机关调查等，省钱兄有权自行终止本省钱兄账号的注销而无需另行得到您的同意。</p><p>3.&nbsp;注销省钱兄账号，您将无法再使用本省钱兄账号，也将无法找回您省钱兄账号中及与账户相关的任何内容或信息，包括但不限于：</p><p>（1）您将无法登录、使用本省钱兄账户；</p><p>（2）本省钱兄账号的个人资料和历史信息（包括但不限于用户名、头像、购物记录、粉丝数量和昵称等）都将无法找回；</p><p>（3）您通过省钱兄账号使用、授权登录或绑定省钱兄账号后使用的省钱兄相关或第三方的其他服务的所有记录将无法找回。您将无法再登录、使用前述服务，您曾获得或即将获得的返利、推广奖励、优惠券、订单、用户权益、及其他卡券、未完成订单权益等视为您自行放弃，将无法继续使用。您理解并同意，省钱兄无法协助您重新恢复前述服务。</p><p>5.如您确认放弃账号中所有已享有或即将享有的任何权益，并要求省钱兄为您强制注销的，以上所有权益将视为您主动放弃并不再享有，同时可能引起的任何法律责任，与省钱兄无关。</p><p>1.&nbsp;注销本省钱兄账号并不代表本省钱兄账户注销前的账号行为和相关责任得到豁免或减轻。</p><p>&nbsp;</p>', 'xieyi');
INSERT INTO `common_info` VALUES (202, '2021-08-14 19:09:54', NULL, '注册协议', 177, '<p>尊敬的用户您好：在您使用本服务之前，请您认真阅读本用户协议，更好的了解我们所提供的服务以及您享有的权利义务。您开始使用时，即表示您已经了解并确认接受了本文件中的全部条款，包括我们对本服务条款随时做的任何修改。</p><p>一、协议的效力</p><p>本协议内容包括协议正文及所有已经发布或将来可能发布的各类规则。所有规则为本协议不可分割的组成部分，与协议正文具有同等法律效力。您承诺接受并遵守本协议的约定。如果您不同意本协议的约定，您应立即停止使用本平台服务。</p><p>二、用户行为规范</p><p>用户同意将不会利用本服务进行任何违法或不正当的活动，包括但不限于下列行为∶</p><p>发布或以其它方式传送含有下列内容之一的信息：</p><p>反对宪法所确定的基本原则的；</p><p>危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；</p><p>损害国家荣誉和利益的；</p><p>煽动民族仇恨、民族歧视、破坏民族团结的；</p><p>破坏国家宗教政策，宣扬邪教和封建迷信的；</p><p>散布谣言，扰乱社会秩序，破坏社会稳定的；</p><p>散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；</p><p>侮辱或者诽谤他人，侵害他人合法权利的；</p><p>含有虚假、诈骗、有害、胁迫、侵害他人隐私、骚扰、侵害、中伤、粗俗、猥亵、或其它道德上令人反感的内容；</p><p>含有当地法律、法规、规章、条例以及任何具有法律效力之规范所限制或禁止的其它内容的；</p><p>含有不适合在本平台展示的内容；</p><p>以任何方式危害他人的合法权益；</p><p>冒充其他任何人或机构，或以虚伪不实的方式陈述或谎称与任何人或机构有关；</p><p>将依据任何法律或合约或法定关系（例如由于雇佣关系和依据保密合约所得知或揭露之内部资料、专属及机密资料）知悉但无权传送之任何内容加以发布、发送电子邮件或以其它方式传送；</p><p>将侵害他人著作权、专利权、商标权、商业秘密、或其它专属权利（以下简称“专属权利”）之内容加以发布或以其它方式传送；</p><p>将任何广告信函、促销资料、“垃圾邮件”、““滥发信件”、“连锁信件”、“直销”或其它任何形式的劝诱资料加以发布、发送或以其它方式传送；</p><p>将设计目的在于干扰、破坏或限制任何计算机软件、硬件或通讯设备功能之计算机病毒（包括但不限于木马程序（trojan horses）、蠕虫（worms）、定时炸弹、删除蝇（cancelbots）（以下简称“病毒”）或其它计算机代码、档案和程序之任何资料，加以发布、发送或以其它方式传送；</p><p>干扰或破坏本服务或与本服务相连线之服务器和网络，或违反任何关于本服务连线网络之规定、程序、政策或规范；</p><p>跟踪、人肉搜索或以其它方式骚扰他人；</p><p>故意或非故意地违反任何适用的当地、国家法律，以及任何具有法律效力的规则；</p><p>未经合法授权而截获、篡改、收集、储存或删除他人个人信息、站内邮件或其它数据资料，或将获知的此类资料用于任何非法或不正当目的。</p><p>三、知识产权</p><p>本平台所有设计图样以及其他图样、产品及服务名称。任何人不得使用、复制或用作其他用途。未经我们许可，任何单位和个人不得私自复制、传播、展示、镜像、上载、下载、使用，或者从事任何其他侵犯我们知识产权的行为。否则，我们将追究相关法律责任。</p><p>我们鼓励用户充分利用平台自由地张贴和共享自己的信息，但这些内容必须位于公共领域内，或者用户拥有这些内容的使用权。同时，用户对于其创作并在本平台上发布的合法内容依法享有著作权及其相关权利。</p><p>四、免责声明</p><p>互联网是一个开放平台，用户将照片等个人资料上传到互联网上，有可能会被其他组织或个人复制、转载、擅改或做其它非法用途，用户必须充分意识此类风险的存在。用户明确同意其使用本服务所存在的风险将完全由其自己承担；因其使用本服务而产生的一切后果也由其自己承担，我们对用户不承担任何责任。</p><p>对于用户上传的照片、资料、证件等，已采用相关措施并已尽合理努力进行审核，但不保证其内容的正确性、合法性或可靠性，相关责任由上传上述内容的会员负责。</p><p>尽管已采取相应的技术保障措施 ，但用户仍有可能收到各类的广告信或其他不以招聘/应聘为目的邮件或其它方式传送的任何内容，本平台不承担责任。</p><p>对于各种广告信息、链接、资讯等，不保证其内容的正确性、合法性或可靠性，相关责任由广告商承担；用户通过本服务与广告商进行任何形式的通讯或商业往来，或参与促销活动，包含相关商品或服务之付款及交付，以及达成的其它任何相关条款、条件、保证或声明，完全为用户与广告商之间之行为，与本平台无关。用户因前述任何交易或前述广告商而遭受的任何性质的损失或损害，本平台不承担任何责任。</p><p>本平台不保证其提供的服务一定能满足用户的要求和期望，也不保证服务不会中断，对服务的及时性、安全性、准确性也都不作保证。对于因不可抗力或无法控制的原因造成的网络服务中断或其他缺陷，不承担任何责任。我们不对用户所发布信息的删除或储存失败承担责任。我们有权判断用户的行为是否符合本网站使用协议条款之规定，如果我们认为用户违背了协议条款的规定，我们有终止向其提供服务的权利。</p><p>本平台保留变更、中断或终止部分网络服务的权利。保留根据实际情况随时调整平台提供的服务种类、形式的权利。本平台不承担因业务调整给用户造成的损失。本平台仅提供相关服务，除此之外与本服务有关的设备（如电脑、调制解调器及其他与接入互联网有关的装置）及所需的费用（如为接入互联网而支付的电话费及上网费）均应由用户自行负担。</p>', 'xieyi');
INSERT INTO `common_info` VALUES (203, '2021-08-14 19:09:54', NULL, '关于我们', 187, '关于我们，有问题可以在线联系客服哦。', 'xieyi');
INSERT INTO `common_info` VALUES (204, '2026-05-27 17:44:26', NULL, '登录是否获取手机号', 188, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (217, '2020-07-27 15:17', NULL, '腾讯地图key', 217, NULL, 'xitong');
INSERT INTO `common_info` VALUES (234, '2020-11-04 10:31:40', NULL, '上传方式 1阿里云oss  2本地', 234, '2', 'oss');
INSERT INTO `common_info` VALUES (236, '2020-11-04 10:31:40', NULL, '退款微信通知', 236, NULL, 'weixin');
INSERT INTO `common_info` VALUES (240, '2020-11-04 10:31:40', NULL, '微信消息通知模版', 240, NULL, 'weixin');
INSERT INTO `common_info` VALUES (241, '2020-11-04 10:31:40', NULL, '超时几分钟短信通知', 241, '5', 'xitong');
INSERT INTO `common_info` VALUES (242, '2020-11-04 10:31:40', NULL, '几条未读短信通知', 242, '1', 'xitong');
INSERT INTO `common_info` VALUES (243, '2020-11-04 10:31:40', NULL, '是否开启微信提现', 243, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (244, '2021-12-22 10:11:36', NULL, '微信提现方式 1自动 2手动', 244, '2', 'xitong');
INSERT INTO `common_info` VALUES (245, '2020-11-04 10:31:40', NULL, '是否开启支付宝提现', 245, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (246, '2020-10-28 16:57:07', NULL, '微信证书地址', 201, NULL, 'weixin');
INSERT INTO `common_info` VALUES (257, '2020-11-04 10:31:40', NULL, '身份验证SecretID', 257, NULL, 'xitong');
INSERT INTO `common_info` VALUES (258, '2020-11-04 10:31:40', NULL, '身份验证SecretKey', 258, NULL, 'xitongs');
INSERT INTO `common_info` VALUES (259, '2020-11-04 10:31:40', NULL, '身份验证实例ID', 259, NULL, 'xitong');
INSERT INTO `common_info` VALUES (262, '2022-05-26 21:31:52', NULL, '小程序是否上架', 262, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (276, '2023-10-20 16:46:12', NULL, '推广文案', 276, '邀请好友注册', 'xitong');
INSERT INTO `common_info` VALUES (285, '2022-04-29 19:19:05', NULL, '免费用户每天最多刷新几次', 285, '1', 'xitongs');
INSERT INTO `common_info` VALUES (286, '2023-06-21 11:35:07', NULL, 'VIP用户每天最多刷新几次', 286, '3', 'xitongs');
INSERT INTO `common_info` VALUES (289, '2023-07-13 16:29:40', NULL, '推广图片', 289, NULL, 'image');
INSERT INTO `common_info` VALUES (290, '2023-07-10 10:32:30', NULL, '关键词过滤', 290, '滚 111', 'xitong');
INSERT INTO `common_info` VALUES (291, '2023-07-10 10:36:08', NULL, '聊天警告文字提醒', 291, '聊天过程中，请不要透露自己的个人信息，包括并不仅限于：QQ号、微信号、支付宝、密码、银行卡等，请文明交流。', 'xitong');
INSERT INTO `common_info` VALUES (292, '2023-07-10 17:31:30', NULL, '审核状态通知', 292, NULL, 'weixin');
INSERT INTO `common_info` VALUES (293, '2023-07-10 17:31:30', NULL, '用户注销协议', 293, '1、除非您已充分阅读、完全理解并接受本协议所有条款，否则，**请您不要发起账号注销流程、操作。**您单击“同意”或“下一步”，或您开始进行任何账号注销的流程、操作，或者以其他任何明示或者默示方式表示接受本协议的，均视为您已阅读并同意签署本协议。本协议产生法律效力，成为对双方均具有约束力的法律文件。 请您务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款、对您权利进行限制的条款、约定争议解决方式和司法管辖的条款（如第四条相关约定）等。限制、免责条款或者其他涉及您重大权益的条款可能以加粗、加下划线等形式提示您重点注意。', 'xieyi');
INSERT INTO `common_info` VALUES (294, '2023-07-11 13:32:12', NULL, 'vip到期提醒', 294, NULL, 'weixin');
INSERT INTO `common_info` VALUES (295, '2023-07-11 13:32:12', NULL, 'vip到期提前几天提醒', 295, '5', 'weixin');
INSERT INTO `common_info` VALUES (296, '2023-07-18 10:09:35', NULL, '客服类型(1:电话，2:企业微信，3:客服二维码)', 296, '1', 'xitong');
INSERT INTO `common_info` VALUES (297, '2023-07-11 16:07:32', NULL, '企业微信客服APPID\r\n', 297, NULL, 'xitong');
INSERT INTO `common_info` VALUES (298, '2023-07-11 16:04:28', NULL, '客服电话', 298, NULL, 'xitong');
INSERT INTO `common_info` VALUES (300, '2023-07-11 16:07:27', NULL, '企业客户链接', 300, NULL, 'xitong');
INSERT INTO `common_info` VALUES (301, '2023-10-20 17:31:46', NULL, '小程序上线开关1', 301, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (302, '2023-10-20 16:10:15', NULL, '小程序上线开关2', 302, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (303, '2023-07-12 09:42:00', NULL, '是否开启分佣提成', 303, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (304, '2023-07-12 09:42:00', NULL, '一级佣金', 304, '0.3', 'fuwufei');
INSERT INTO `common_info` VALUES (305, '2023-07-14 15:09:03', NULL, '二级佣金', 305, '0.1', 'fuwufei');
INSERT INTO `common_info` VALUES (306, '2023-07-13 17:50:00', NULL, '新人赠送vip天数(0表示不赠送)', 306, '3', 'xitong');
INSERT INTO `common_info` VALUES (307, '2023-07-14 17:27:22', NULL, '免费用户最多发布多少店铺', 307, '1', 'xitong');
INSERT INTO `common_info` VALUES (308, '2023-07-12 13:47:09', NULL, '免费用户最多发布多少信息', 308, '2', 'xitong');
INSERT INTO `common_info` VALUES (309, '2023-07-17 15:23:54', NULL, '用户小程序名称配置', 309, '省钱兄同城服务', 'xitong');
INSERT INTO `common_info` VALUES (414, '2023-07-26 16:27:15', NULL, '地图方式 1腾讯 2天地图', 414, '2', 'xitong');
INSERT INTO `common_info` VALUES (415, '2023-07-26 16:27:15', NULL, '天地图key', 415, NULL, 'xitong');
INSERT INTO `common_info` VALUES (422, '2024-06-18 14:27:31', NULL, '百度翻译app_id', 422, NULL, 'xitong');
INSERT INTO `common_info` VALUES (423, '2024-06-18 14:27:31', NULL, '百度翻译app_secret', 423, NULL, 'xitong');
INSERT INTO `common_info` VALUES (424, '2024-06-20 09:45:10', NULL, '夜间睡眠', 424, '夜间睡眠', 'xitong');
INSERT INTO `common_info` VALUES (425, '2024-06-20 09:45:10', NULL, '专注时钟', 425, '专注时钟', 'xitong');
INSERT INTO `common_info` VALUES (426, '2024-06-20 09:45:10', NULL, '日间小憩', 426, '日间小憩', 'xitong');
INSERT INTO `common_info` VALUES (427, '2024-06-20 09:45:10', NULL, '正念呼吸', 427, '正念呼吸', 'xitong');
INSERT INTO `common_info` VALUES (871, '2024-06-18 14:27:31', NULL, '默认语种', 871, 'en', 'xitong');
INSERT INTO `common_info` VALUES (872, '2024-06-18 14:27:31', NULL, '官方邀请码', 872, '666666', 'xitong');
INSERT INTO `common_info` VALUES (873, '2024-06-18 14:27:31', NULL, 'VIP权益条款', 873, '这里是会员的权益条款', 'xitong');
INSERT INTO `common_info` VALUES (874, '2024-06-18 14:27:31', NULL, '邀请规则', 874, '<p>这里是邀请规则</p>', 'xieyi');
INSERT INTO `common_info` VALUES (875, '2024-06-18 14:27:31', NULL, 'VIP权益条款', 875, '<p>VIP权益条款</p>', 'xieyi');
INSERT INTO `common_info` VALUES (876, '2024-06-18 14:27:31', NULL, '用户服务', 876, '<p>这里是用户服务</p>', 'xieyi');

-- ----------------------------
-- Table structure for config_i18n
-- ----------------------------
DROP TABLE IF EXISTS `config_i18n`;
CREATE TABLE `config_i18n`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `primary_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语言关键词',
  `language` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语言',
  `language_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '语言值',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `show_type` int(11) NULL DEFAULT NULL COMMENT '使用类型 1服务端 2用户端',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '国际化配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_i18n
-- ----------------------------

-- ----------------------------
-- Table structure for course_and_label
-- ----------------------------
DROP TABLE IF EXISTS `course_and_label`;
CREATE TABLE `course_and_label`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label_id` int(11) NULL DEFAULT NULL COMMENT '课程标签表id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除  0未删除  1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 639 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课程和标签中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_and_label
-- ----------------------------

-- ----------------------------
-- Table structure for course_explore
-- ----------------------------
DROP TABLE IF EXISTS `course_explore`;
CREATE TABLE `course_explore`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '探索页id',
  `explore_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '探索页名称',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父id',
  `status` int(1) NULL DEFAULT NULL COMMENT '启用：1是 2否',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '探索页相关' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_explore
-- ----------------------------

-- ----------------------------
-- Table structure for course_info
-- ----------------------------
DROP TABLE IF EXISTS `course_info`;
CREATE TABLE `course_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '课程类型id',
  `course_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '课程简介',
  `course_status` int(11) NULL DEFAULT NULL COMMENT '课程状态：0正常 1下架',
  `course_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程标题',
  `mentor_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导师标题',
  `mentor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导师',
  `mentor_photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导师头像',
  `mentor_introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '导师简介',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程备注',
  `background_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '背景图片',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除  0未删除 1已删除',
  `explore_id` int(11) NULL DEFAULT NULL COMMENT '探索页二级id',
  `explore_parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '探索页父id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_info
-- ----------------------------

-- ----------------------------
-- Table structure for course_label
-- ----------------------------
DROP TABLE IF EXISTS `course_label`;
CREATE TABLE `course_label`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `level` int(11) NULL DEFAULT NULL COMMENT '标签等级',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除 0未删除  1已删除',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课程标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_label
-- ----------------------------

-- ----------------------------
-- Table structure for course_learn
-- ----------------------------
DROP TABLE IF EXISTS `course_learn`;
CREATE TABLE `course_learn`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chapter_id` int(11) NULL DEFAULT NULL COMMENT '课程的章节id',
  `meditation_id` int(11) NULL DEFAULT NULL COMMENT '冥想类型id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注或反馈',
  `completion_status` int(11) NULL DEFAULT NULL COMMENT '练习状态：0未开始 1进行中 2已完成',
  `learn_date` date NULL DEFAULT NULL COMMENT '练习日期',
  `learn_time` time(0) NULL DEFAULT NULL COMMENT '练习时间',
  `duration_today` int(11) NULL DEFAULT 0 COMMENT '今日时长 睡眠（也是专注、小憩、呼吸开始选的时间）',
  `duration_tomorrow` int(11) NULL DEFAULT 0 COMMENT '明日时长  睡眠',
  `duration_time` int(11) NULL DEFAULT 0 COMMENT '冥想练习、专注、小憩、呼吸时长',
  `alarm_start_time` datetime(0) NULL DEFAULT NULL COMMENT '睡眠开始时间',
  `alarm_end_time` datetime(0) NULL DEFAULT NULL COMMENT '睡眠结束时间',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `write_time` int(11) NULL DEFAULT NULL COMMENT '小憩、专注、呼吸时间（结束的时间）',
  `type` int(11) NULL DEFAULT NULL COMMENT '0冥想练习  1睡眠 2专注 3小憩 4正念',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程信息id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 301 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '冥想练习表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_learn
-- ----------------------------

-- ----------------------------
-- Table structure for course_like
-- ----------------------------
DROP TABLE IF EXISTS `course_like`;
CREATE TABLE `course_like`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除：0未删除  1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '我的喜欢（课程）表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_like
-- ----------------------------

-- ----------------------------
-- Table structure for course_search
-- ----------------------------
DROP TABLE IF EXISTS `course_search`;
CREATE TABLE `course_search`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `course_word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索关键词',
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '搜索热门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_search
-- ----------------------------

-- ----------------------------
-- Table structure for course_type
-- ----------------------------
DROP TABLE IF EXISTS `course_type`;
CREATE TABLE `course_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `type_status` int(11) NULL DEFAULT 0 COMMENT '是否启用 0启用  1未启用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型描述',
  `level` int(11) NULL DEFAULT NULL COMMENT '级别：1 一级 2二级  3三级',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父id',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `cover_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '封面图',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课程类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_type
-- ----------------------------

-- ----------------------------
-- Table structure for course_user
-- ----------------------------
DROP TABLE IF EXISTS `course_user`;
CREATE TABLE `course_user`  (
  `id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `chapter_id` int(11) NULL DEFAULT NULL COMMENT '章节id',
  `completed` int(11) NULL DEFAULT NULL COMMENT '是否完成 0完成 1未完成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和课程、章节的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_user
-- ----------------------------

-- ----------------------------
-- Table structure for help_classify
-- ----------------------------
DROP TABLE IF EXISTS `help_classify`;
CREATE TABLE `help_classify`  (
  `help_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助中心分类',
  `help_classify_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级id',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_classify
-- ----------------------------
INSERT INTO `help_classify` VALUES (3, '活动报名', 2, 0, '2020-12-12 12:12:12');

-- ----------------------------
-- Table structure for help_word
-- ----------------------------
DROP TABLE IF EXISTS `help_word`;
CREATE TABLE `help_word`  (
  `help_word_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助文档id',
  `help_word_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帮助标题',
  `help_word_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '帮助文档内容',
  `help_classify_id` int(11) NULL DEFAULT NULL COMMENT '帮助分类id',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_word_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_word
-- ----------------------------
INSERT INTO `help_word` VALUES (3, '已报名的活动可以取消吗', '可以，需要扣除百分之20', 3, 1, '2020-12-12 12:12:12');

-- ----------------------------
-- Table structure for invite
-- ----------------------------
DROP TABLE IF EXISTS `invite`;
CREATE TABLE `invite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '邀请者id',
  `invitee_user_id` int(11) NULL DEFAULT NULL COMMENT '被邀请者id',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '收益',
  `create_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `money_type` int(1) NULL DEFAULT NULL COMMENT '1会员2陪玩',
  `state` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 968 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邀请信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite
-- ----------------------------

-- ----------------------------
-- Table structure for invite_money
-- ----------------------------
DROP TABLE IF EXISTS `invite_money`;
CREATE TABLE `invite_money`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收益钱包id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `money_sum` decimal(10, 2) NULL DEFAULT NULL COMMENT '总获取收益',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '当前金额',
  `cash_out` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计提现',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite_money
-- ----------------------------

-- ----------------------------
-- Table structure for language_type
-- ----------------------------
DROP TABLE IF EXISTS `language_type`;
CREATE TABLE `language_type`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '语言id',
  `language` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语种',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_enable` int(1) NULL DEFAULT 1 COMMENT '是否开启 0否 1是',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '语种' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of language_type
-- ----------------------------

-- ----------------------------
-- Table structure for meditation_type
-- ----------------------------
DROP TABLE IF EXISTS `meditation_type`;
CREATE TABLE `meditation_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `meditation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冥想类型名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `delete_flag` int(1) NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '冥想类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meditation_type
-- ----------------------------

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `member_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员特权id',
  `member_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特权图标',
  `member_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特权名称',
  `sort` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`member_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (3, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/2ef451b3e0f7f98464b38430c13051b0.png', '所有课程', '1');
INSERT INTO `member` VALUES (4, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/37c225b26b3f011319de1cb4f1c18baf.png', '海量音乐', '2');
INSERT INTO `member` VALUES (6, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/d03a8a0f55240c438209777b896ebd94.png', '亲友体验卡', '3');
INSERT INTO `member` VALUES (9, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/071892b060308ae7f0c7275d26d92f80.png', '自由练习', '5');
INSERT INTO `member` VALUES (10, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/b9e72742a7bba9a20cf2d13906af441c.png', '专属客服', '4');
INSERT INTO `member` VALUES (11, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/423a302253532b6a993788efab1ce8f7.png', 'VIP标识', '6');
INSERT INTO `member` VALUES (12, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/dfb149846d4cc92a9f00d261d4c3cca1.png', '专属奖励', '7');

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `create_at` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `image` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '图片',
  `is_see` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `send_state` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `send_time` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `state` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '1为公告2位用户反馈 3为投诉服务 4为投诉店铺 5为用户消息 6客服消息',
  `title` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `url` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '地址',
  `type` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `platform` int(11) NULL DEFAULT NULL COMMENT '来源',
  `user_id` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `audit_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `by_user_id` int(11) NULL DEFAULT NULL,
  `platform_id` int(11) NULL DEFAULT NULL,
  `shop_id` int(11) NULL DEFAULT NULL COMMENT '店铺id',
  `data_id` int(11) NULL DEFAULT NULL COMMENT '服务id',
  `phone` varchar(25) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '联系方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 411 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_info
-- ----------------------------

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '短信验证码',
  `phone` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`code`, `phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of msg
-- ----------------------------

-- ----------------------------
-- Table structure for pay_classify
-- ----------------------------
DROP TABLE IF EXISTS `pay_classify`;
CREATE TABLE `pay_classify`  (
  `pay_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '充值分类id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售价',
  `give_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '赠送数量',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '水晶数量',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`pay_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_classify
-- ----------------------------

-- ----------------------------
-- Table structure for pay_details
-- ----------------------------
DROP TABLE IF EXISTS `pay_details`;
CREATE TABLE `pay_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值id',
  `classify` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 1app  2微信二维码支付  3微信小程序 公众号支付 4支付宝APP支付 5支付宝H5支付',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付单号',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `state` int(4) NULL DEFAULT NULL COMMENT '0待支付 1支付成功 2失败',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `pay_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付时间',
  `type` int(4) NULL DEFAULT NULL COMMENT '购买类型 1充值余额 2购买会员',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '关联id(如果是购买会员则是会员类型id)',
  `stripe_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'stripeId',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 690 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_details
-- ----------------------------

-- ----------------------------
-- Table structure for sc_browse
-- ----------------------------
DROP TABLE IF EXISTS `sc_browse`;
CREATE TABLE `sc_browse`  (
  `browse_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '浏览记录id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `data_id` int(11) NULL DEFAULT NULL COMMENT '信息id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '1服务 2店铺',
  `shop_id` int(11) NULL DEFAULT NULL COMMENT '店铺id',
  PRIMARY KEY (`browse_id`) USING BTREE,
  INDEX `data_browse`(`data_id`) USING BTREE,
  INDEX `shop_borwse`(`shop_id`) USING BTREE,
  CONSTRAINT `sc_browse_ibfk_1` FOREIGN KEY (`data_id`) REFERENCES `sc_table_data` (`data_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sc_browse_ibfk_2` FOREIGN KEY (`shop_id`) REFERENCES `sc_shop` (`shop_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 637 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_browse
-- ----------------------------

-- ----------------------------
-- Table structure for sc_follow
-- ----------------------------
DROP TABLE IF EXISTS `sc_follow`;
CREATE TABLE `sc_follow`  (
  `follow_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关注id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `data_id` int(11) NULL DEFAULT NULL COMMENT '被关注服务id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '1服务 2店铺',
  `shop_id` int(11) NULL DEFAULT NULL COMMENT '店铺id',
  PRIMARY KEY (`follow_id`) USING BTREE,
  INDEX `delete_shop`(`shop_id`) USING BTREE,
  INDEX `delete_data`(`data_id`) USING BTREE,
  CONSTRAINT `sc_follow_ibfk_1` FOREIGN KEY (`data_id`) REFERENCES `sc_table_data` (`data_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sc_follow_ibfk_2` FOREIGN KEY (`shop_id`) REFERENCES `sc_shop` (`shop_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_follow
-- ----------------------------

-- ----------------------------
-- Table structure for sc_notice
-- ----------------------------
DROP TABLE IF EXISTS `sc_notice`;
CREATE TABLE `sc_notice`  (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公告内容',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '发布人id',
  `status` int(1) NULL DEFAULT NULL COMMENT '0未发布 1已发布',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `release_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sc_shop
-- ----------------------------
DROP TABLE IF EXISTS `sc_shop`;
CREATE TABLE `sc_shop`  (
  `shop_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '店铺id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名',
  `shop_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺详细地址',
  `business_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '周一周二周三等',
  `stat_time` time(0) NULL DEFAULT NULL COMMENT '营业开始时间',
  `ent_time` time(0) NULL DEFAULT NULL COMMENT '营业结束时间',
  `introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '简介',
  `shop_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺标签',
  `shop_lng` double(20, 6) NULL DEFAULT NULL COMMENT '商铺经度',
  `shop_lat` double(20, 6) NULL DEFAULT NULL COMMENT '商铺维度',
  `province` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区/县',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `shop_title_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '主图',
  `shop_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '轮播图',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人姓名',
  `identity_card_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人身份证',
  `business_license` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照',
  `browse_count` int(11) NULL DEFAULT NULL COMMENT '浏览次数',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态  1待审核 2审核通过 3审核未通过 4已封禁',
  `ban_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '下架原因',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `is_enable` int(1) NULL DEFAULT 0 COMMENT '是否上架 0否 1是',
  `is_recommend` int(1) NULL DEFAULT 0 COMMENT '是否是推荐 0否 1是',
  `state` int(1) NULL DEFAULT 1,
  PRIMARY KEY (`shop_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_shop
-- ----------------------------

-- ----------------------------
-- Table structure for sc_table_class
-- ----------------------------
DROP TABLE IF EXISTS `sc_table_class`;
CREATE TABLE `sc_table_class`  (
  `class_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `class_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图标',
  `is_enable` int(1) NULL DEFAULT NULL COMMENT '是否启用 0不启用 1启用',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_table_class
-- ----------------------------

-- ----------------------------
-- Table structure for sc_table_column
-- ----------------------------
DROP TABLE IF EXISTS `sc_table_column`;
CREATE TABLE `sc_table_column`  (
  `column_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `class_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类id',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '列描述',
  `column_length` int(10) NULL DEFAULT NULL COMMENT '列长度',
  `is_required` int(1) NOT NULL COMMENT '是否必填（1是）',
  `html_type` int(1) NULL DEFAULT NULL COMMENT '显示类型（1文本框、2文本域、3下拉框、4复选框、5单选框、6日期控件、7数字类型）',
  `option_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项值',
  `show_type` int(1) NULL DEFAULT NULL COMMENT '展示类型 1仅用户端展示 2仅管理端展示 3都展示',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 350 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sc_table_data
-- ----------------------------
DROP TABLE IF EXISTS `sc_table_data`;
CREATE TABLE `sc_table_data`  (
  `data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `class_id` int(11) NULL DEFAULT NULL COMMENT '分类id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `status` int(1) NULL DEFAULT NULL COMMENT '1待审核 2审核通过 3审核未通过 4已封禁',
  `refuse_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拒绝原因',
  `title_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '首页图',
  `data_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `rotation_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '轮播图',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `browse_count` int(10) NULL DEFAULT NULL COMMENT '浏览次数',
  `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'json数据',
  `is_recommend` int(1) NULL DEFAULT NULL COMMENT '是否是推荐(1是 0不是)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `county` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '县/区',
  `price_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格信息,这里不是仅填写货币,而是包括货币及单位',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详细信息',
  `lng` double(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `lat` double(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  `state` int(1) NULL DEFAULT 1 COMMENT '1正常 2vip过期超出次数的服务已下架',
  `is_enable` int(1) NULL DEFAULT 1 COMMENT '是否上架 0否 1是',
  PRIMARY KEY (`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发布服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_table_data
-- ----------------------------

-- ----------------------------
-- Table structure for search
-- ----------------------------
DROP TABLE IF EXISTS `search`;
CREATE TABLE `search`  (
  `search_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '搜索id',
  `search_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '1服务 2店铺',
  PRIMARY KEY (`search_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of search
-- ----------------------------

-- ----------------------------
-- Table structure for sound_and_user
-- ----------------------------
DROP TABLE IF EXISTS `sound_and_user`;
CREATE TABLE `sound_and_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `effects_id` int(11) NULL DEFAULT NULL COMMENT '音效id',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除  0未删除  1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 170 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '音效和用户中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sound_and_user
-- ----------------------------

-- ----------------------------
-- Table structure for sound_effects
-- ----------------------------
DROP TABLE IF EXISTS `sound_effects`;
CREATE TABLE `sound_effects`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `effects_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '音效名称',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `bg_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '背景图片',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `file_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '音效路径',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_flag` int(1) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '音效表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sound_effects
-- ----------------------------

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典码',
  `value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `type`(`type`, `code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 489 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 190 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (2, 126, '管理员列表', 'user', NULL, 1, 'UserFilled', 10);
INSERT INTO `sys_menu` VALUES (3, 126, '角色管理', 'role', NULL, 1, 'Avatar', 11);
INSERT INTO `sys_menu` VALUES (4, 126, '菜单管理', 'menu', NULL, 1, 'menu', 12);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, 0, '用户中心', 'userList', '', 1, 'Avatar', 2);
INSERT INTO `sys_menu` VALUES (33, 0, '数据中心', 'home', '', 1, 'Grid', 1);
INSERT INTO `sys_menu` VALUES (34, 0, '财务中心', 'financeList', '', 1, 'Money', 3);
INSERT INTO `sys_menu` VALUES (35, 34, '查看', '', 'financeList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (36, 34, '转账', '', 'financeList:transfer', 2, '', 0);
INSERT INTO `sys_menu` VALUES (37, 34, '退款', '', 'financeList:refund', 2, '', 0);
INSERT INTO `sys_menu` VALUES (38, 34, '退款', '', 'financeList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (39, 34, '修改', '', 'financeList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (40, 34, '删除', '', 'financeList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (41, 0, '消息中心', 'message', '', 1, 'ChatDotSquare', 4);
INSERT INTO `sys_menu` VALUES (42, 41, '查看', '', 'message:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (43, 41, '消息推送', '', 'message:push', 2, '', 0);
INSERT INTO `sys_menu` VALUES (50, 0, '首页装修', 'bannerList', '', 1, 'Memo', 6);
INSERT INTO `sys_menu` VALUES (51, 50, '查看', '', 'bannerList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (52, 50, '添加', '', 'bannerList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (53, 50, '修改', '', 'bannerList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (54, 50, '删除', '', 'bannerList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (57, 0, '系统配置', 'allocationList', '', 1, 'menu', 19);
INSERT INTO `sys_menu` VALUES (58, 57, '查看', '', 'allocationList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (59, 57, '修改', '', 'allocationList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (60, 32, '查看', '', 'userList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (61, 32, '删除', '', 'userList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (69, 41, '添加', '', 'message:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (70, 41, '修改', '', 'message:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (71, 41, '删除', '', 'message:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (72, 0, '类型配置', 'courseType', '', 1, 'DataAnalysis', 9);
INSERT INTO `sys_menu` VALUES (73, 72, '添加', '', 'courseType:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (74, 72, '查看', '', 'courseType:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (75, 72, '修改', '', 'courseType:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (76, 72, '删除', '', 'courseType:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (77, 0, '服务管理', 'autonym', '', 1, 'renwu', 17);
INSERT INTO `sys_menu` VALUES (78, 77, '列表', '', 'autonym:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (90, 32, '修改积分', '', 'userList:updatejf', 2, '', 0);
INSERT INTO `sys_menu` VALUES (91, 32, '修改用户状态', '', 'userList:updateStatus', 2, '', 0);
INSERT INTO `sys_menu` VALUES (92, 77, '通过', '', 'autonym:tongguo', 2, '', 0);
INSERT INTO `sys_menu` VALUES (93, 77, '拒绝', '', 'autonym:jujue', 2, '', 0);
INSERT INTO `sys_menu` VALUES (105, 32, '修改积分', '', 'userList:updatebl', 2, '', 0);
INSERT INTO `sys_menu` VALUES (111, 0, '公告管理', 'dynamic', '', 1, 'leibie', 17);
INSERT INTO `sys_menu` VALUES (112, 111, '查看', '', 'dynamic:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (113, 111, '添加', '', 'dynamic:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (114, 111, '修改', '', 'dynamic:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (115, 111, '删除', '', 'dynamic:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (116, 0, '音乐场景配置', 'coupon', '', 1, 'DocumentAdd', 14);
INSERT INTO `sys_menu` VALUES (117, 116, '查看', '', 'coupon:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (118, 116, '添加', '', 'coupon:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (119, 116, '修改', '', 'coupon:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (120, 116, '删除', '', 'coupon:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (126, 0, '系统管理', '', '', 0, 'Setting', 20);
INSERT INTO `sys_menu` VALUES (127, 111, '审核', '', 'dynamic:shenhe', 2, '', 0);
INSERT INTO `sys_menu` VALUES (144, 0, '帮助中心', 'materialsList', '', 1, 'Connection', 17);
INSERT INTO `sys_menu` VALUES (145, 144, '查看', '', 'materialsList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (146, 144, '添加', '', 'materialsList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (147, 144, '修改', '', 'materialsList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (148, 144, '删除', '', 'materialsList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (149, 77, '删除', '', 'autonym:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (150, 0, '认证审核', 'authentication', '', 1, 'Collection', 10);
INSERT INTO `sys_menu` VALUES (151, 150, '通过', '', 'authentication:pass', 2, '', 0);
INSERT INTO `sys_menu` VALUES (152, 150, '拒绝', '', 'authentication:refuse', 2, '', 0);
INSERT INTO `sys_menu` VALUES (154, 0, '课程管理', 'courseManagement', '', 1, 'Calendar', 7);
INSERT INTO `sys_menu` VALUES (155, 154, '修改', '', 'courseManagement:updata', 2, '', 0);
INSERT INTO `sys_menu` VALUES (157, 154, '删除', '', 'courseManagement:delete', 2, 'shuju', 3);
INSERT INTO `sys_menu` VALUES (158, 154, '查看', '', 'courseManagement:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (159, 150, '修改', '', 'authentication:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (160, 150, '删除', '', 'authentication:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (161, 32, '设置会员', '', 'userList:updateVip', 2, '', 0);
INSERT INTO `sys_menu` VALUES (162, 0, '注销信息', 'messageZx', NULL, 1, 'ChatLineRound', 5);
INSERT INTO `sys_menu` VALUES (163, 162, '查看', NULL, 'messageZx:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (164, 162, '注销', NULL, 'messageZx:shenhe', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (165, 77, '添加', NULL, 'autonym:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (166, 77, '修改', NULL, 'autonym:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (167, 77, '封禁', NULL, 'autonym:fengjin', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (168, 0, '订单中心', 'orderCenter', NULL, 1, 'Tickets', 8);
INSERT INTO `sys_menu` VALUES (169, 168, '查看', NULL, 'orderCenter:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (170, 168, '退款', NULL, 'orderCenter:tuikuan', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (171, 0, '聊天记录', 'chatRecord', NULL, 1, 'pinglun', 17);
INSERT INTO `sys_menu` VALUES (172, 171, '查看', NULL, 'chatRecord:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (173, 34, '导出', NULL, 'financeList:daochu', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (174, 0, '会员特权', 'vipPrivilege', NULL, 1, 'ElementPlus', 15);
INSERT INTO `sys_menu` VALUES (175, 174, '查看', NULL, 'vipPrivilege:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (176, 174, '添加', NULL, 'vipPrivilege:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (177, 174, '修改', NULL, 'vipPrivilege:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (178, 174, '删除', NULL, 'vipPrivilege:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (179, 126, '升级配置', 'app', NULL, 1, 'shezhi', 0);
INSERT INTO `sys_menu` VALUES (180, 179, '查看', NULL, 'app:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (181, 179, '添加', NULL, 'app:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (182, 179, '修改', NULL, 'app:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (183, 179, '删除', NULL, 'app:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (184, 33, '查看', NULL, 'home:list', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (185, 0, '会员配置', 'vipConfi', NULL, 1, 'Shop', 14);
INSERT INTO `sys_menu` VALUES (186, 185, '查看', NULL, 'vipConfi:list', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (187, 185, '添加', NULL, 'vipConfi:add', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (188, 185, '修改', NULL, 'vipConfi:update', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (189, 185, '删除', NULL, 'vipConfi:delete', 2, NULL, 1);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'super', 2, '2021-06-04 17:38:28');
INSERT INTO `sys_role` VALUES (4, '普通用户', '普通用户', 2, '2021-09-10 15:33:47');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5898 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (5295, 4, 33);
INSERT INTO `sys_role_menu` VALUES (5296, 4, 60);
INSERT INTO `sys_role_menu` VALUES (5297, 4, 90);
INSERT INTO `sys_role_menu` VALUES (5298, 4, 91);
INSERT INTO `sys_role_menu` VALUES (5299, 4, 105);
INSERT INTO `sys_role_menu` VALUES (5300, 4, 161);
INSERT INTO `sys_role_menu` VALUES (5301, 4, 35);
INSERT INTO `sys_role_menu` VALUES (5302, 4, 36);
INSERT INTO `sys_role_menu` VALUES (5303, 4, 37);
INSERT INTO `sys_role_menu` VALUES (5304, 4, 38);
INSERT INTO `sys_role_menu` VALUES (5305, 4, 39);
INSERT INTO `sys_role_menu` VALUES (5306, 4, 173);
INSERT INTO `sys_role_menu` VALUES (5307, 4, 42);
INSERT INTO `sys_role_menu` VALUES (5308, 4, 162);
INSERT INTO `sys_role_menu` VALUES (5309, 4, 163);
INSERT INTO `sys_role_menu` VALUES (5310, 4, 164);
INSERT INTO `sys_role_menu` VALUES (5311, 4, 51);
INSERT INTO `sys_role_menu` VALUES (5312, 4, 154);
INSERT INTO `sys_role_menu` VALUES (5313, 4, 155);
INSERT INTO `sys_role_menu` VALUES (5314, 4, 158);
INSERT INTO `sys_role_menu` VALUES (5315, 4, 157);
INSERT INTO `sys_role_menu` VALUES (5316, 4, 168);
INSERT INTO `sys_role_menu` VALUES (5317, 4, 169);
INSERT INTO `sys_role_menu` VALUES (5318, 4, 170);
INSERT INTO `sys_role_menu` VALUES (5319, 4, 73);
INSERT INTO `sys_role_menu` VALUES (5320, 4, 74);
INSERT INTO `sys_role_menu` VALUES (5321, 4, 75);
INSERT INTO `sys_role_menu` VALUES (5322, 4, 77);
INSERT INTO `sys_role_menu` VALUES (5323, 4, 78);
INSERT INTO `sys_role_menu` VALUES (5324, 4, 92);
INSERT INTO `sys_role_menu` VALUES (5325, 4, 93);
INSERT INTO `sys_role_menu` VALUES (5326, 4, 149);
INSERT INTO `sys_role_menu` VALUES (5327, 4, 165);
INSERT INTO `sys_role_menu` VALUES (5328, 4, 166);
INSERT INTO `sys_role_menu` VALUES (5329, 4, 167);
INSERT INTO `sys_role_menu` VALUES (5330, 4, 171);
INSERT INTO `sys_role_menu` VALUES (5331, 4, 172);
INSERT INTO `sys_role_menu` VALUES (5332, 4, 112);
INSERT INTO `sys_role_menu` VALUES (5333, 4, 117);
INSERT INTO `sys_role_menu` VALUES (5334, 4, 118);
INSERT INTO `sys_role_menu` VALUES (5335, 4, 119);
INSERT INTO `sys_role_menu` VALUES (5336, 4, 144);
INSERT INTO `sys_role_menu` VALUES (5337, 4, 145);
INSERT INTO `sys_role_menu` VALUES (5338, 4, 146);
INSERT INTO `sys_role_menu` VALUES (5339, 4, 147);
INSERT INTO `sys_role_menu` VALUES (5340, 4, 148);
INSERT INTO `sys_role_menu` VALUES (5341, 4, 150);
INSERT INTO `sys_role_menu` VALUES (5342, 4, 151);
INSERT INTO `sys_role_menu` VALUES (5343, 4, 152);
INSERT INTO `sys_role_menu` VALUES (5344, 4, 159);
INSERT INTO `sys_role_menu` VALUES (5345, 4, 160);
INSERT INTO `sys_role_menu` VALUES (5346, 4, 58);
INSERT INTO `sys_role_menu` VALUES (5348, 4, 15);
INSERT INTO `sys_role_menu` VALUES (5349, 4, 19);
INSERT INTO `sys_role_menu` VALUES (5350, 4, 23);
INSERT INTO `sys_role_menu` VALUES (5351, 4, -666666);
INSERT INTO `sys_role_menu` VALUES (5352, 4, 32);
INSERT INTO `sys_role_menu` VALUES (5353, 4, 34);
INSERT INTO `sys_role_menu` VALUES (5354, 4, 41);
INSERT INTO `sys_role_menu` VALUES (5355, 4, 50);
INSERT INTO `sys_role_menu` VALUES (5356, 4, 72);
INSERT INTO `sys_role_menu` VALUES (5357, 4, 111);
INSERT INTO `sys_role_menu` VALUES (5359, 4, 116);
INSERT INTO `sys_role_menu` VALUES (5360, 4, 57);
INSERT INTO `sys_role_menu` VALUES (5361, 4, 126);
INSERT INTO `sys_role_menu` VALUES (5363, 4, 2);
INSERT INTO `sys_role_menu` VALUES (5364, 4, 3);
INSERT INTO `sys_role_menu` VALUES (5365, 4, 4);
INSERT INTO `sys_role_menu` VALUES (5820, 1, 33);
INSERT INTO `sys_role_menu` VALUES (5821, 1, 184);
INSERT INTO `sys_role_menu` VALUES (5822, 1, 32);
INSERT INTO `sys_role_menu` VALUES (5823, 1, 60);
INSERT INTO `sys_role_menu` VALUES (5824, 1, 61);
INSERT INTO `sys_role_menu` VALUES (5825, 1, 90);
INSERT INTO `sys_role_menu` VALUES (5826, 1, 91);
INSERT INTO `sys_role_menu` VALUES (5827, 1, 105);
INSERT INTO `sys_role_menu` VALUES (5828, 1, 161);
INSERT INTO `sys_role_menu` VALUES (5829, 1, 34);
INSERT INTO `sys_role_menu` VALUES (5830, 1, 35);
INSERT INTO `sys_role_menu` VALUES (5831, 1, 36);
INSERT INTO `sys_role_menu` VALUES (5832, 1, 37);
INSERT INTO `sys_role_menu` VALUES (5833, 1, 38);
INSERT INTO `sys_role_menu` VALUES (5834, 1, 39);
INSERT INTO `sys_role_menu` VALUES (5835, 1, 40);
INSERT INTO `sys_role_menu` VALUES (5836, 1, 173);
INSERT INTO `sys_role_menu` VALUES (5837, 1, 41);
INSERT INTO `sys_role_menu` VALUES (5838, 1, 42);
INSERT INTO `sys_role_menu` VALUES (5839, 1, 43);
INSERT INTO `sys_role_menu` VALUES (5840, 1, 69);
INSERT INTO `sys_role_menu` VALUES (5841, 1, 70);
INSERT INTO `sys_role_menu` VALUES (5842, 1, 71);
INSERT INTO `sys_role_menu` VALUES (5843, 1, 162);
INSERT INTO `sys_role_menu` VALUES (5844, 1, 163);
INSERT INTO `sys_role_menu` VALUES (5845, 1, 164);
INSERT INTO `sys_role_menu` VALUES (5846, 1, 50);
INSERT INTO `sys_role_menu` VALUES (5847, 1, 51);
INSERT INTO `sys_role_menu` VALUES (5848, 1, 52);
INSERT INTO `sys_role_menu` VALUES (5849, 1, 53);
INSERT INTO `sys_role_menu` VALUES (5850, 1, 54);
INSERT INTO `sys_role_menu` VALUES (5851, 1, 154);
INSERT INTO `sys_role_menu` VALUES (5852, 1, 155);
INSERT INTO `sys_role_menu` VALUES (5853, 1, 158);
INSERT INTO `sys_role_menu` VALUES (5854, 1, 157);
INSERT INTO `sys_role_menu` VALUES (5855, 1, 168);
INSERT INTO `sys_role_menu` VALUES (5856, 1, 169);
INSERT INTO `sys_role_menu` VALUES (5857, 1, 170);
INSERT INTO `sys_role_menu` VALUES (5858, 1, 72);
INSERT INTO `sys_role_menu` VALUES (5859, 1, 73);
INSERT INTO `sys_role_menu` VALUES (5860, 1, 74);
INSERT INTO `sys_role_menu` VALUES (5861, 1, 75);
INSERT INTO `sys_role_menu` VALUES (5862, 1, 76);
INSERT INTO `sys_role_menu` VALUES (5863, 1, 116);
INSERT INTO `sys_role_menu` VALUES (5864, 1, 117);
INSERT INTO `sys_role_menu` VALUES (5865, 1, 118);
INSERT INTO `sys_role_menu` VALUES (5866, 1, 119);
INSERT INTO `sys_role_menu` VALUES (5867, 1, 120);
INSERT INTO `sys_role_menu` VALUES (5868, 1, 185);
INSERT INTO `sys_role_menu` VALUES (5869, 1, 186);
INSERT INTO `sys_role_menu` VALUES (5870, 1, 187);
INSERT INTO `sys_role_menu` VALUES (5871, 1, 188);
INSERT INTO `sys_role_menu` VALUES (5872, 1, 189);
INSERT INTO `sys_role_menu` VALUES (5873, 1, 174);
INSERT INTO `sys_role_menu` VALUES (5874, 1, 175);
INSERT INTO `sys_role_menu` VALUES (5875, 1, 176);
INSERT INTO `sys_role_menu` VALUES (5876, 1, 177);
INSERT INTO `sys_role_menu` VALUES (5877, 1, 178);
INSERT INTO `sys_role_menu` VALUES (5878, 1, 57);
INSERT INTO `sys_role_menu` VALUES (5879, 1, 58);
INSERT INTO `sys_role_menu` VALUES (5880, 1, 59);
INSERT INTO `sys_role_menu` VALUES (5881, 1, 2);
INSERT INTO `sys_role_menu` VALUES (5882, 1, 15);
INSERT INTO `sys_role_menu` VALUES (5883, 1, 16);
INSERT INTO `sys_role_menu` VALUES (5884, 1, 17);
INSERT INTO `sys_role_menu` VALUES (5885, 1, 18);
INSERT INTO `sys_role_menu` VALUES (5886, 1, 3);
INSERT INTO `sys_role_menu` VALUES (5887, 1, 19);
INSERT INTO `sys_role_menu` VALUES (5888, 1, 20);
INSERT INTO `sys_role_menu` VALUES (5889, 1, 21);
INSERT INTO `sys_role_menu` VALUES (5890, 1, 22);
INSERT INTO `sys_role_menu` VALUES (5891, 1, 4);
INSERT INTO `sys_role_menu` VALUES (5892, 1, 23);
INSERT INTO `sys_role_menu` VALUES (5893, 1, 24);
INSERT INTO `sys_role_menu` VALUES (5894, 1, 25);
INSERT INTO `sys_role_menu` VALUES (5895, 1, 26);
INSERT INTO `sys_role_menu` VALUES (5896, 1, -666666);
INSERT INTO `sys_role_menu` VALUES (5897, 1, 126);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `inviter_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '1cc7f0f4e29524bfbfb2a299dde135e6ebad047b2c0ef70eceb7fbc198293927', 'YKJpTRPDurwTYVsIhd0P', 'root', '13612345678', 1, 1, '2016-11-11 11:11:11', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (2, 4, 1);
INSERT INTO `sys_user_role` VALUES (3, 5, 1);
INSERT INTO `sys_user_role` VALUES (5, 7, 1);
INSERT INTO `sys_user_role` VALUES (7, 9, 1);
INSERT INTO `sys_user_role` VALUES (8, 8, 1);
INSERT INTO `sys_user_role` VALUES (10, 11, 1);
INSERT INTO `sys_user_role` VALUES (20, 10, 1);
INSERT INTO `sys_user_role` VALUES (22, 2, 1);
INSERT INTO `sys_user_role` VALUES (28, 6, 1);
INSERT INTO `sys_user_role` VALUES (29, 13, 1);
INSERT INTO `sys_user_role` VALUES (31, 12, 4);
INSERT INTO `sys_user_role` VALUES (32, 14, 1);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES (1, '0dec24ddeec5b91ccd055f0438ee1f0c', '2026-04-09 23:05:00', '2026-04-09 11:05:00');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别 1男 2女',
  `age` int(4) NULL DEFAULT NULL COMMENT '年龄',
  `open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序openId',
  `wx_open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信公众号openId',
  `account_open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信App  openId',
  `balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '收益',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加密盐',
  `apple_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '苹果id',
  `sys_phone` int(11) NULL DEFAULT NULL COMMENT '手机类型 1安卓 2ios',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 1正常 2禁用',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源 APP 小程序  公众号',
  `clientid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app推送id',
  `zhi_fu_bao_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝名称',
  `zhi_fu_bao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `is_send_msg` int(11) NULL DEFAULT NULL COMMENT '是否推送消息',
  `wx_img` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信提现图片',
  `msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '最近一条信息内容',
  `user_type` int(1) NULL DEFAULT 0 COMMENT '用户类型 0未认证 1普通用户 2企业用户',
  `is_vip` int(1) NULL DEFAULT 0 COMMENT '是否是会员(0不是 1是)',
  `vip_end_time` datetime(0) NULL DEFAULT NULL COMMENT 'vip到期时间',
  `refresh_count` int(11) NULL DEFAULT 0 COMMENT '剩余刷新次数',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` date NULL DEFAULT NULL COMMENT '更新时间',
  `invitation_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `inviter_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请人的邀请码',
  `inviter_inviter_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请人的上级',
  `browse_count` int(11) NULL DEFAULT 0 COMMENT '浏览次数',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `zhi_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '直属佣金',
  `fei_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '非直属佣金',
  `total_login_days` int(11) NULL DEFAULT 0 COMMENT '累计登录天数',
  `login_date` date NULL DEFAULT NULL COMMENT '登录日期',
  `last_login_time` date NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 210 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------

-- ----------------------------
-- Table structure for user_certification
-- ----------------------------
DROP TABLE IF EXISTS `user_certification`;
CREATE TABLE `user_certification`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户实名认证id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `user_type` int(1) NULL DEFAULT NULL COMMENT '认证类型  1普通用户 2企业用户',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名|法人姓名',
  `identity_card_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件号码|法人身份证',
  `enterprise_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `enterprise_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业联系电话',
  `business_license` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '营业执照',
  `business_license_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '营业执照图片',
  `status` int(1) NULL DEFAULT NULL COMMENT '0待审核 1已通过 2未通过',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `identity_card_front` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证正面图片',
  `identity_card_verso` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证反面图片',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `enterprise_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人详细地址|企业地址',
  `is_submit` int(1) NULL DEFAULT 0 COMMENT '是否允许提交0是 1否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 130 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_certification
-- ----------------------------

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`  (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `follow_user_id` bigint(20) NULL DEFAULT NULL COMMENT '关注的用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_follow
-- ----------------------------

-- ----------------------------
-- Table structure for user_money
-- ----------------------------
DROP TABLE IF EXISTS `user_money`;
CREATE TABLE `user_money`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '钱包id',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '钱包金额',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 519 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money
-- ----------------------------

-- ----------------------------
-- Table structure for user_money_details
-- ----------------------------
DROP TABLE IF EXISTS `user_money_details`;
CREATE TABLE `user_money_details`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包详情id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `by_user_id` int(11) NULL DEFAULT NULL COMMENT '邀请用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `classify` int(11) NULL DEFAULT NULL COMMENT '用途 0余额 1购买vip 3下级分成 4退款',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 1增加 2减少',
  `state` int(11) NULL DEFAULT 1 COMMENT '状态 1待支付 2已到账 3取消',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `user_level` int(1) NULL DEFAULT NULL COMMENT '1一级 2二级',
  `relation_id` int(11) NULL DEFAULT NULL COMMENT '关联id',
  `language_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '语种',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1562 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money_details
-- ----------------------------

-- ----------------------------
-- Table structure for user_vip
-- ----------------------------
DROP TABLE IF EXISTS `user_vip`;
CREATE TABLE `user_vip`  (
  `vip_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员id',
  `vip_name_type` int(2) NULL DEFAULT NULL COMMENT '会员类型0月1季2年',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买时间',
  `end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到期时间',
  `is_vip` int(255) NULL DEFAULT NULL COMMENT '1是会员2不是',
  PRIMARY KEY (`vip_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_vip
-- ----------------------------

-- ----------------------------
-- Table structure for user_visitor
-- ----------------------------
DROP TABLE IF EXISTS `user_visitor`;
CREATE TABLE `user_visitor`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '访客id',
  `user_id` bigint(32) NULL DEFAULT NULL COMMENT '用户id',
  `by_user_id` bigint(32) NULL DEFAULT NULL COMMENT '访问用户id',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_visitor
-- ----------------------------

-- ----------------------------
-- Table structure for vip_details
-- ----------------------------
DROP TABLE IF EXISTS `vip_details`;
CREATE TABLE `vip_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vip_month_count` int(2) NULL DEFAULT NULL COMMENT '月数',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员价格',
  `vip_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `discount_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '折扣价',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_enable` int(1) NULL DEFAULT 0 COMMENT '是否启用(1启用 0未启用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_details
-- ----------------------------
INSERT INTO `vip_details` VALUES (2, 1, 0.01, '普通月卡', 0.10, '2023-07-18 10:03:40', 1);
INSERT INTO `vip_details` VALUES (3, 3, 30.00, '普通季卡', 25.00, '2023-07-14 11:15:27', 1);
INSERT INTO `vip_details` VALUES (6, 12, 100.00, '年卡', 99.99, '2024-06-25 10:39:39', 1);

-- ----------------------------
-- Table structure for vip_discount
-- ----------------------------
DROP TABLE IF EXISTS `vip_discount`;
CREATE TABLE `vip_discount`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `discount` decimal(10, 0) NULL DEFAULT NULL COMMENT '优惠金币',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_discount
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
