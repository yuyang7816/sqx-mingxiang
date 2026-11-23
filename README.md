<div align="center">

# **省钱兄 JAVA 冥想助眠项目全套开源系统（JAVA + Uni-app + Vue）**

**服务端 · 管理端 · 用户端**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-1.8-orange.svg)](docs/服务端-功能说明.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.11-6DB33F.svg)](docs/服务端-功能说明.md)
[![Vue](https://img.shields.io/badge/Vue-3-42b883.svg)](mingxiang-admin/package.json)
[![uni-app](https://img.shields.io/badge/uni--app-用户端-2b9939.svg)](mingxiang-uniapp/manifest.json)

八年沉淀，分批次开源由 AI 辅助开发的系统源码。  
开源为生态，服务才是根基。欢迎各位开发者一同交流。

</div>

---
⭐ 如果觉得项目不错，请点击右上角 Star 收藏支持！您的 Star 是我们持续开源迭代的最大动力！

## **1. 项目简介**

**省钱兄冥想助眠项目全套开源系统**是一套面向冥想练习、助眠放松与专注训练的三端系统，内置 **课程播放、探索检索、夜间睡眠、专注时钟、日间小憩、正念呼吸、会员权益、邀请推广**，采用前后端分离架构，服务端、管理端、用户端全套代码开源。

服务端基于 **Spring Boot 2.6.11**（**Java 8**）与 **MyBatis-Plus** 构建，负责接口、数据库，以及短信、对象存储、支付等外部能力。  
管理端基于 **Vue 3 + Element Plus + Vite** 开发，供运营人员在电脑浏览器中维护课程、音效场景、会员、订单、用户、财务与系统设置。  
用户端基于 **uni-app（Vue 3）** 开发，覆盖 H5 与微信小程序。用户可浏览冥想课程、播放章节、助眠与专注；同一套用户端里还可开通会员、查看练习数据、邀请好友与钱包提现。

本仓库为服务端、管理端、用户端全套源码。线上 H5 演示见 [https://mingxiang.xianmaxiong.com](https://mingxiang.xianmaxiong.com)（与仓库用户端同源）。

- **官方网站开源计划**：[https://www.xiansqx.com](https://www.xiansqx.com)
- **开源计划**：[https://opensource.xiansqx.cn](https://opensource.xiansqx.cn)
- **官方文档**：[功能说明与运行教程](docs/服务端-运行教程.md)
- **Gitee 仓库**：[https://gitee.com/maxiaodong1996/sqx-mingxiang.git](https://gitee.com/maxiaodong1996/sqx-mingxiang.git)
- **GitHub 仓库**：[https://github.com/yuyang7816/sqx-mingxiang.git](https://github.com/yuyang7816/sqx-mingxiang.git)


## **2. 移动端界面预览**

截图来自本项目线上 H5 演示站 [https://mingxiang.xianmaxiong.com](https://mingxiang.xianmaxiong.com)（与 `mingxiang-uniapp` 构建产物一致），手机视口截取。

<div align="center">

| 首页 | 探索 | 我的 | 登录 |
| :---: | :---: | :---: | :---: |
| <img src="https://mingxiang.xianmaxiong.com/file/uploadPath/2026/11/06/329e63e8046f0f37a45cc65b311a3985.png" width="160" alt="首页" /> | <img src="https://mingxiang.xianmaxiong.com/file/uploadPath/2026/11/06/5dcb1ce2e9679f78fb5cd66768c7d4da.png" width="160" alt="探索" /> | <img src="https://mingxiang.xianmaxiong.com/file/uploadPath/2026/11/06/bdabd54bd7015adccec04724ae4f9af6.png" width="160" alt="我的" /> | <img src="https://mingxiang.xianmaxiong.com/file/uploadPath/2026/11/06/c80433d168ce38829586e7bf42d719c5.png" width="160" alt="登录" /> |

</div>

## **3. 开源说明**

> **【省钱兄科技｜开源计划启动】**
>
> 八年沉淀，我们将分批次开源全套由 AI 辅助开发的系统源码。  
> 本项目采用 **Apache 2.0** 许可协议，源码供学习、技术研究、商业使用、二次开发与参考使用。  
> **开源为生态，服务才是根基；** 欢迎各位开发者一同交流。

## **4. 快速开始**

建议顺序：**先服务端，再管理端，最后用户端。** 管理端和用户端都要等服务端已经启动，才能登录和加载数据。

**说明：** 仓库里**不包含** `node_modules`（体积大、随系统生成）。克隆或下载后，在 **`mingxiang-admin`** 目录下**必须先执行一次** `npm install`，再启动开发命令。用户端可用 HBuilderX 打开，也可用命令行运行 H5；若 uView 等组件样式异常，可在 `mingxiang-uniapp` 目录执行一次 `npm install`。详细步骤见下方与 [docs](docs/管理端-运行教程.md) 中的运行教程。

### **4.1 服务端**

安装 **JDK 8**、**Maven**、**MySQL 8**。新建数据库 `mingxiang`，导入 [`mingxiang-server/db/mingxiang.sql`](mingxiang-server/db/mingxiang.sql)。

本地开发时，将 [`mingxiang-server/src/main/resources/application.yml`](mingxiang-server/src/main/resources/application.yml) 中的 `spring.profiles.active` 保持为 **`prod`**（仓库默认），并按本机环境修改 [`application-prod.yml`](mingxiang-server/src/main/resources/application-prod.yml) 里的数据库账号密码（库名保持 `mingxiang`）。

在 `mingxiang-server` 目录执行 Maven 启动。默认 **端口 9120**，上下文路径 **`/sqx_fast`**。接口文档：`http://127.0.0.1:9120/sqx_fast/swagger-ui.html`（Swagger）。

Redis 在当前开发配置中默认关闭（`sqx.redis.open: false`）。若你自行打开 Redis 缓存，请同步修改 `application.yml` 中的 `spring.redis` 段。

更完整的模块划分与接口约定见 [服务端功能说明](docs/服务端-功能说明.md)。

### **4.2 管理端**

安装 **Node.js 20+**（见 `mingxiang-admin/package.json` 的 `engines`）。进入 `mingxiang-admin` 目录，**首次运行前**安装依赖并启动：

```bash
cd mingxiang-admin
npm install
npm run dev
```

`npm install` 会根据 `package.json` / `package-lock.json` 在本机生成 `node_modules`，只需在依赖变更或新克隆后执行。

开发站点默认端口 **8888**。请将接口地址指向本机服务端（`http://127.0.0.1:9120`，路径 `/sqx_fast`），配置文件见 [`mingxiang-admin/comment/httpUrl.js`](mingxiang-admin/comment/httpUrl.js)。

演示管理端账号：`admin` / `123456`（以库中数据为准）。登录页名称是「省钱兄冥想助眠系统」。

### **4.3 用户端**

**HBuilderX（推荐）：** 打开 `mingxiang-uniapp` 目录，运行到浏览器或微信开发者工具。应用名称是「码兄冥想」，底部三个入口：**首页、探索、我的**。

**命令行 H5（若已配置对应脚本）：**

```bash
cd mingxiang-uniapp
npm install
```

开发时请确认 [`mingxiang-uniapp/common/config.js`](mingxiang-uniapp/common/config.js) 中的接口地址指向本机或已部署的服务端（线上演示为 `https://mingxiang.xianmaxiong.com/sqx_fast`）。本地联调可改为 `http://127.0.0.1:9120/sqx_fast`。

短信、微信、支付、对象存储等密钥需自行申请，并在管理端相关配置页填写。

## **5. 文档**

| 想做的事 | 打开哪一份 |
|---|---|
| 把服务端跑起来 | [服务端运行教程](docs/服务端-运行教程.md) |
| 看服务端能做什么 | [服务端功能说明](docs/服务端-功能说明.md) |
| 把管理端跑起来 | [管理端运行教程](docs/管理端-运行教程.md) |
| 看管理端能做什么 | [管理端功能说明](docs/管理端-功能说明.md) |
| 把用户端跑起来 | [用户端运行教程](docs/用户端-运行教程.md) |
| 看用户端能做什么 | [用户端功能说明](docs/用户端-功能说明.md) |

## **6. 目录结构**

仓库根目录只放三端代码、说明和协议。编译出来的 `target`、`dist`、`unpackage`、`node_modules` 是运行时生成的，不需要改。

### **6.1 仓库根目录**

| 路径 | 是什么 |
|---|---|
| `mingxiang-server` | **服务端**。接口、数据库脚本、支付、短信、音效处理等能力都在这里 |
| `mingxiang-admin` | **管理端**。运营人员在电脑浏览器里打开的后台 |
| `mingxiang-uniapp` | **用户端**。练习冥想 / 助眠 / 专注用的手机应用，也可在浏览器 H5 预览 |
| `docs` | 功能说明、运行教程与 README 预览截图 |
| `LICENSE` | Apache License 2.0 全文 |

### **6.2 服务端 `mingxiang-server`**

| 路径 | 是什么 |
|---|---|
| `src/main/java/com/sqx/SqxApplication.java` | 启动入口。启动成功后控制台会打印「冥想项目启动成功」 |
| `src/main/java/com/sqx/modules/course` | 课程、章节、探索分类、点赞、学习记录 |
| `src/main/java/com/sqx/modules/effect` | 音效与混音场景 |
| `src/main/java/com/sqx/modules/app` | C 端用户、会员、钱包 |
| `src/main/java/com/sqx/modules/pay` | 支付明细、提现申请 |
| `src/main/java/com/sqx/modules/invite` | 邀请推广与收益 |
| `src/main/java/com/sqx/modules/member` | 会员套餐配置 |
| `src/main/java/com/sqx/modules/sys` | 系统模块：管理员、角色、菜单、配置 |
| `src/main/java/com/sqx/modules/banner` | 首页轮播 |
| `src/main/resources` | `application.yml` 与 `application-prod.yml` |
| `db/mingxiang.sql` | 要导入的数据库脚本，库名建议是 `mingxiang` |

### **6.3 管理端 `mingxiang-admin`**

| 路径 | 是什么 |
|---|---|
| `src/pages/Home` | 数据中心 / 运营看板 |
| `src/pages/courseManagement` | 课程管理、类型配置 |
| `src/pages/vipPrivilege` | 会员特权、会员配置、音乐场景配置 |
| `src/pages/orderCenter` | 订单中心 |
| `src/pages/userList` | C 端用户 |
| `src/pages/financeList` | 财务中心 |
| `src/pages/bannerList` | 首页装修（轮播） |
| `src/pages/allocationList` | 系统配置 |
| `src/pages/message` | 消息中心、注销信息 |
| `src/pages/Users` / `Role` / `Menu` | 管理员、角色、菜单 |
| `src/router` | 页面地址和菜单对应关系 |
| `comment/httpUrl.js` | 请求封装与接口根地址 |

### **6.4 用户端 `mingxiang-uniapp`**

| 路径 | 是什么 |
|---|---|
| `pages/index` | 首页：问候语、轮播、夜间睡眠 / 专注时钟 / 日间小憩 / 正念呼吸、课程推荐 |
| `pages/explore_ts` | 探索：按冥想 / 睡眠 / 声音分类浏览课程，支持搜索 |
| `pages/my` | 我的：会员、今日数据、消息、最近播放、邀请、钱包、设置 |
| `pages/public` | 登录、注册、找回密码 |
| `subpackage` | 睡眠、专注、小憩、呼吸、课程详情与播放器 |
| `my` | 会员中心、钱包、团队收益、我的喜欢、消息等子包 |
| `settingViews` | 设置中心、协议、关于我们、客服、注销 |
| `common` | 接口地址、登录态、请求封装等公共逻辑 |
| `components` | 播放器、闹钟、空状态等可复用组件 |
| `static` | 图标与默认图片 |
| `pages.json` | 页面路由与底部导航（首页 / 探索 / 我的） |
| `manifest.json` | 应用名称「码兄冥想」、小程序 AppId、H5 等配置 |

界面组件使用 **uView Plus**。课程播放、助眠、专注、会员与钱包均在用户端完成。

## **7. 核心特性与技术栈**

### **7.1 核心特性**

· **冥想课程分类推荐、章节播放与学习时长统计**  
· **探索页按冥想 / 睡眠 / 声音检索课程，支持搜索与标签**  
· **夜间睡眠：倒计时助眠、闹钟提醒、助眠音乐**  
· **专注时钟、日间小憩、正念呼吸四种练习入口**  
· **音效混音场景，可自定义场景名称**  
· **课程喜欢 / 收藏、最近播放、今日冥想 / 睡眠 / 专注数据**  
· **会员中心：开通会员解锁付费章节与权益**  
· **邀请好友、推广团队与收益、钱包提现**  
· **管理端课程 / 音效 / 会员 / 订单 / 财务 / 系统配置**  
· **短信登录、微信登录、在线客服**

业务细节见 [服务端功能说明](docs/服务端-功能说明.md) 与 Swagger `swagger-ui.html`。

版本以仓库里的配置文件为准。没写死版本号的，按上文快速开始安装对应大版本即可。

### **7.2 服务端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 语言 | **Java** | **1.8** | 整个服务端。不能换成 11 或 17 |
| 框架 | **Spring Boot** | **2.6.11** | 提供网页接口，端口 `9120`，路径 `/sqx_fast` |
| 持久化 | **MyBatis-Plus** | **3.2.0** | 读写 MySQL |
| 构建 | **Maven** | 3.6 及以上 | 下载依赖、编译、启动 |
| 数据库 | **MySQL** | 8.0 及以上 | 业务数据。库名 `mingxiang`，脚本是 `mingxiang-server/db/mingxiang.sql` |
| 连接池 | **Druid** | 随工程 | 管理数据库连接 |
| 缓存 | **Redis** | 可选 | 默认关闭；开启后用于登录态、系统配置缓存等 |
| 接口文档 | **Swagger** | 2.7.0 | 浏览器打开 `swagger-ui.html` |
| 登录验证码 | **Kaptcha** | 0.0.9 | 管理端登录页上的图片验证码 |
| 对象存储 | **阿里云 OSS** 等（可选） | 随工程 | 不填密钥时图片可存本机路径 |
| 支付 | 微信支付、支付宝等 | 随工程 | 会员开通、订单等。密钥要自己申请 |
| 音频 | JAVE / mp3spi | 随工程 | 助眠音乐、混音场景等音频处理 |

### **7.3 管理端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 运行环境 | **Node.js** | **20+** | 安装依赖、启动开发网站 |
| 页面框架 | **Vue** | **3.5** | 后台每一个页面 |
| 构建 | **Vite** | **7.x** | 开发时热更新，默认端口 `8888` |
| 界面组件 | **Element Plus** | **2.x** | 表格、表单、弹窗、菜单 |
| 页面跳转 | **Vue Router** | **4.x** | 登录页、看板和各业务页的地址 |
| 状态 | **Pinia** | **3.x** | 记住登录等信息 |
| 请求 | **Axios** | **1.x** | 把操作发给服务端 |

### **7.4 用户端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 框架 | **uni-app** | Vue 3 | 同一套页面可运行到浏览器、微信小程序 |
| 开发工具 | **HBuilderX** 或 CLI | 正式版 | 编译并运行到 Chrome 或微信开发者工具 |
| 界面组件 | **uView Plus** | 3.x | 按钮、输入框、轮播、弹窗等 |
| 状态 | **Pinia** | 3.x | 播放器、闹钟与登录状态 |
| 浏览器预览 | H5 | 以控制台为准 | 电脑上看用户端 |
| 微信小程序 | 微信开发者工具 | 可选 | 要自己的小程序 AppId 才能真机预览 |

## **8. 开源协议**

<div align="center">

本仓库使用 **Apache License 2.0**

全文见 [LICENSE](LICENSE)

</div>

## **9. 社区与支持**

运行、二次开发或商务合作，可以联系 **省钱兄科技**。

使用过程中如果接口起不来、页面打不开，先按本文 **4. 快速开始** 和 `docs` 里的运行教程排查。短信、微信、支付、对象存储等密钥需要自行申请，并在管理端系统配置中填写。仍解决不了时，可通过官方渠道说明你的环境和报错。

当前项目正在测试中，如遇到 BUG，可自行修复或者留言反馈，我们将第一时间进行版本迭代。  
欢迎一起交流学习。开源是把源码开放出来，具体落地、定制和长期维护仍由服务来承接。

<div align="center">

<img src="docs/images/community-qr.jpg" alt="省钱兄科技二维码" width="220" />

**省钱兄科技**

</div>
