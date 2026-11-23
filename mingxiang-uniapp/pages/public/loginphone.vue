<template>
	<view class="container">
		<!-- 自定义顶部导航栏 -->
		<up-navbar title="" :placeholder="true" :bgColor="bgColor" leftIconColor="#FFFFFF" :autoBack="true">
		</up-navbar>

		<view class="bgBot">
			<image src="https://mydz.xianmxkj.com/travel/loginbg.png" style="width: 100%;height: 100vh;"></image>
		</view>

		<view class="wrapper">

			<view class="cu-form-group">
				<view class="title ">账号</view>
				<up-input v-model="phone" maxlength="11" type="number" border="none" placeholder="请输入手机号"
					placeholder-class="input-placeholder"></up-input>
			</view>
			<view class="cu-form-group" v-if="type == 1">
				<view class="title ">密码</view>
				<view class="flex align-center justify-between flex-sub">
					<up-input v-model="password" maxlength="20" border="none" type="password" placeholder="请输入密码"
						placeholder-class="input-placeholder"></up-input>
					<text class="send-msg" @click="forget">忘记密码</text>
				</view>
			</view>
			<view class="cu-form-group" v-if="type == 2">
				<view class="title ">验证码</view>
				<view class="flex align-center justify-between flex-sub">
					<up-input v-model="code" maxlength="20" border="none" type="number" placeholder="请输入验证码"
						placeholder-class="input-placeholder"></up-input>
					<view @click="getCode()" :class="timer?'activeCode':''" class="send-msg">
						{{text}}
					</view>
				</view>
			</view>
			<view class="confirm-btn" @click="toLogin">登录</view>

			<view class="text-center margin-top-xl">
				<view style="font-size: 26rpx;">
					<text style="color: #FFFFFF;">没有账号？</text>
					<text style="color: #2FD7C8" @click="goRegister">立即注册</text>
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
	// 顶部导航栏背景色
	const bgColor = ref('transparent');
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const text = ref('获取验证码')
	//手机号
	const phone = ref('');
	//密码
	const password = ref('');
	// 1账号密码登录 2验证码登录
	const type = ref(1);
	// 验证码
	const code = ref('');
	// 跳转注册
	const goRegister = () => {
		uni.navigateTo({
			url: '/pages/public/register'
		});
	};
	// 跳转忘记密码
	const forget = () => {
		uni.navigateTo({
			url: '/pages/public/forgetPwd'
		});
	};
	//手机号效验
	const validatePhone = (phone) => {
		// 中国手机号码正则表达式
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};
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
		proxy.$http.postJson(`/app/verifyCode/sendPhoneCode`, {
			phone: phone.value,
			bizCode: 'register'
		}).then(res => {
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
	// 登录
	const toLogin = () => {
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

		if (!password.value && type.value == 1) {
			uni.showToast({
				title: '请输入密码',
				icon: 'none'
			});
			return
		}
		if (!code.value && type.value == 2) {
			uni.showToast({
				title: '请输入验证码',
				icon: 'none'
			});
			return
		}
		uni.showLoading({
			title: '正在登录中...'
		})
		let url = '';
		let data = {
			phoneNo: phone.value,
		}
		if (type.value == 1) {
			data.password = password.value
			data.openId = uni.getStorageSync('openId')
			url = "/app/auth/phonePasswordLogin"
		}
		if (type.value == 2) {
			data.verifyCode = code.value
			url = "/app/auth/phoneCodeLogin"
		}
		proxy.$http.postJson(url, data).then(res => {
			if (res.code == 200) {
				uni.setStorageSync('token', res.data.token);
				uni.setStorageSync('userId', res.data.user.userId);
				uni.setStorageSync('phone', res.data.user.phone);
				uni.setStorageSync('userName', res.data.user.userName);
				uni.setStorageSync('myInvitationCode', res.data.user.invitationCode);
				uni.setStorageSync('avatar', res.data.user.avatar);
				uni.showToast({
					title: '登录成功'
				})
				setTimeout(() => {
					uni.switchTab({
						url: '/pages/my/index'
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
		if (option.type) {
			type.value = option.type
		}

	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
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
			margin-top: 150px;
			padding: 16px 25px;

			.cu-form-group {
				background: #FFFFFF;
				padding: 0px 30rpx;
				height: 90rpx;
				display: flex;
				align-items: center;
				margin-bottom: 20px;
				border-radius: 30px;

				.title {
					padding-right: 15px;
					font-size: 28rpx;
					font-family: Source Han Sans CN;
					font-weight: 400;

				}

				.send-msg {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 26rpx;
					color: #000000;
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