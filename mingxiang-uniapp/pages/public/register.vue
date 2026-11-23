<template>
	<view class="container">
		<!-- 自定义顶部导航栏 -->
		<up-navbar title="" :placeholder="true" :bgColor="bgColor" leftIconColor="#FFFFFF" :autoBack="true">
		</up-navbar>

		<view class="logo flex align-center justify-center">
			<image src="/static/logo.png" mode=""></image>
		</view>
		<view class="wrapper">
			<view class="cu-form-group">
				<view class="title ">用户名</view>
				<up-input v-model="userName" border="none" placeholder="请输入用户名"
					placeholder-class="input-placeholder"></up-input>
			</view>

			<view class="cu-form-group">
				<view class="title ">手机号</view>
				<up-input v-model="phone" maxlength="11" type="number" border="none" placeholder="请输入手机号"
					placeholder-class="input-placeholder"></up-input>
			</view>
			<view class="cu-form-group">
				<view class="title ">验证码</view>
				<view class="flex align-center justify-between flex-sub">
					<up-input v-model="code" maxlength="20" border="none" type="number" placeholder="请输入验证码"
						placeholder-class="input-placeholder"></up-input>
					<view @click="getCode()" :class="timer?'activeCode':''" class="send-msg">
						{{text}}
					</view>
				</view>
			</view>
			<view class="cu-form-group">
				<view class="title ">设置密码</view>
				<up-input v-model="password" maxlength="20" border="none" type="password" placeholder="请设置新密码"
					placeholder-class="input-placeholder"></up-input>
			</view>

			<view class="cu-form-group">
				<view class="title ">邀请码</view>
				<up-input v-model="invitation" maxlength="6" border="none" placeholder="请输入邀请码"
					placeholder-class="input-placeholder"></up-input>
			</view>

			<view class="confirm-btn" @click="toLogin">立即注册</view>
			<view class="loginXy flex align-center justify-center">
				<view class="loginXy-box flex align-center justify-center">
					<up-checkbox activeColor="#4C6CD3" shape="circle" label="" name="agree" usedAlone v-model:checked="check">
					</up-checkbox>
					<view @click="check = !check" class="flex align-center">
						同意
						<text @click.stop.prevent="goNav('/settingViews/about?type=876')">《用户服务协议》</text>
						及
						<text @click.stop.prevent="goNav('/settingViews/privacyPolicy')">《隐私权政策》</text>
					</view>
				</view>
			</view>

		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	// 手机类型 1安卓 2ios
	const sysphone = ref('1');
	// 顶部导航栏背景色
	const bgColor = ref('transparent');
	// 用户名
	const userName = ref('');
	//手机号
	const phone = ref('');
	//密码
	const password = ref('');
	// 验证码
	const code = ref('');
	// 邀请码
	const invitation = ref('');
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const text = ref('获取验证码')

	//是否选择协议
	const check = ref(false);
	//手机号效验
	const validatePhone = (phone) => {
		// 中国手机号码正则表达式
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};
	//跳转
	const goNav = (url) => {
		uni.navigateTo({
			url: url
		})
	}
	//获取验证码
	const getCode = () => {
		if (timer.value) {
			return
		}
		if (phone.value == '') {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		proxy.$http.getT(`/app/Login/sendMsg/${phone.value}/3`).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '验证码发送成功请注意查收',
					icon: 'none'
				})
				timer.value = setInterval(() => {
					countDown.value--;
					if (countDown.value == 0) {
						clearInterval(timer.value)
						timer.value = null;
						countDown.value = 60;
						text.value = '获取验证码'
					} else {
						text.value = countDown.value + 's后重新获取'
					}
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})

	};
	// 找回密码
	const toLogin = () => {
		
		if (!userName.value) {
			uni.showToast({
				title: '请输入用户名',
				icon: 'none'
			});
			return
		};
		if (!phone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			});
			return
		};

		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		if (!code.value) {
			uni.showToast({
				title: '请输入验证码',
				icon: 'none'
			});
			return
		}
		if (!password.value) {
			uni.showToast({
				title: '请输入密码',
				icon: 'none'
			});
			return
		}
		if (check.value == false) {
			uni.showToast({
				title: '请先同意《隐私政策》和《用户服务协议》',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '正在注册中...'
		})

		// #ifdef APP
		let platform = 'APP'
		// #endif
		// #ifdef H5
		let platform = 'H5'
		// #endif
		// #ifdef MP-WEIXIN
		let platform = '小程序 '
		// #endif
		proxy.$http.postT('/app/Login/registerCode', {
			password: password.value,
			phone: phone.value,
			msg: code.value,
			userName: userName.value,
			openId: uni.getStorageSync('openId') ? uni.getStorageSync('openId') : '',
			inviterCode: invitation.value,
			platform: platform,
			avatar: 'https://waposs.wjhzm.cn/20250827/c7223ff7df564fe280b931b8482a9baf.png',
			sysPhone: sysphone.value
		}).then(res => {
			if (res.code == 0) {
				uni.setStorageSync('token', res.token);
				uni.setStorageSync('userId', res.user.userId);
				uni.setStorageSync('phone', res.user.phone);
				uni.setStorageSync('userName', res.user.userName);
				uni.setStorageSync('myInvitationCode', res.user.invitationCode);
				uni.setStorageSync('avatar', res.user.avatar);
				uni.showToast({
					title: '注册成功'
				})
				setTimeout(function() {
					uni.switchTab({
						url: '/pages/my/my'
					})
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	onLoad(option => {
		invitation.value = uni.getStorageSync('otherInviterCode') ? uni.getStorageSync('otherInviterCode') : ''
		// #ifdef APP
		if (plus.os.name.toLowerCase() === 'android') {
			sysphone.value = 1;
		} else {
			sysphone.value = 2;
		}
		// #endif
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background-color: #ffffff;
	}

	.loginXy {
		width: 100%;
		margin-top: 26rpx;

		.loginXy-box {
			width: 646rpx;
			font-size: 24rpx;
			color: #999999;

			text {
				color: #4C6CD3;
			}
		}
	}

	.logo {
		width: 100%;
		margin-top: 200rpx;

		image {
			width: 150rpx;
			height: 150rpx;
			border-radius: 50%;
		}
	}

	.input-placeholder {
		font-family: Microsoft YaHei;
		font-weight: 400;
		font-size: 26rpx;
		color: #868484 !important;
	}

	.container {
		padding-bottom: 0;
		padding-bottom: constant(safe-area-inset-bottom);
		/* 兼容 IOS<11.2 */
		padding-bottom: env(safe-area-inset-bottom);
		/* 兼容 IOS>=11.2 */
		background: transparent;
		position: relative;
		width: 100%;
		height: 100vh;
		overflow: hidden;

		.bgBot {
			width: 100%;
			position: fixed;
			top: 0;
			left: 0;
			right: 0;
			z-index: 9;
		}

		.wrapper {
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			z-index: 9;
			margin-top: 250px;
			padding: 16px 25px;

			.cu-form-group {
				background: #FFFFFF;
				padding: 0px 30rpx;
				height: 90rpx;
				display: flex;
				align-items: center;
				margin-bottom: 20px;
				// border-radius: 30px;
				border-bottom: 0.5px solid #dadbde;
				width: calc(100% - 60rpx);

				.title {
					padding-right: 15px;
					font-size: 28rpx;
					font-family: Source Han Sans CN;
					font-weight: 400;

				}

				.send-msg {
					padding: 0 20rpx;
					border-radius: 30px;
					background: #4C6CD3;
					color: #fff;
					height: 30px;
					font-size: 14px;
					display: flex;
					align-items: center;
					justify-content: center;
				}
			}

			.confirm-btn {
				width: 100%;
				height: 90rpx;
				border-radius: 30px;
				font-size: 32rpx;
				background: #4C6CD3;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}

	}
</style>