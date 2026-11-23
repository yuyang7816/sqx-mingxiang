<template>
	<view class="login flex align-center justify-center">
		<view class="logo flex align-center justify-center">
			<image src="/static/logo.png" mode=""></image>
		</view>
		<view class="loginInp">
			<!-- <view class="loginInp-box">
				<up-input style="height: 64rpx;" v-model="phone" maxlength="11" type="number" border="bottom"
					placeholder="请输入手机号" prefixIcon="/static/image/login/phone.png"></up-input>
			</view> -->
			<view class="cu-form-group">
				<view class="title ">手机号</view>
				<up-input v-model="phone" maxlength="11" type="number" border="none" placeholder="请输入手机号"
					placeholder-class="input-placeholder"></up-input>
			</view>
		</view>
		<view class="loginInp">
			<!-- <view class="loginInp-box flex align-center" style="border-bottom: 0.5px solid #dadbde;">
				<up-input style="height: 64rpx;" v-model="code" border="bottom" placeholder="请输入验证码"
					prefixIcon="/static/image/login/password.png"></up-input>
				<view @click="getCode()" :class="timer?'activeCode':''" class="loginInpCode">
					{{text}}
				</view>
			</view> -->
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
		</view>
		<view class="loginInp">
			<!-- 			<view class="loginInp-box">
				<up-input style="height: 64rpx;" type="password" v-model="password" border="bottom" placeholder="请输入新密码"
					prefixIcon="/static/image/login/password.png"></up-input>
			</view> -->
			<view class="cu-form-group">
				<view class="title ">设置密码</view>
				<up-input v-model="password" maxlength="20" border="none" type="password" placeholder="请输入新密码"
					placeholder-class="input-placeholder"></up-input>
			</view>
		</view>
		<view class="loginBtn flex align-center justify-center">
			<view @click="rustPwd()" class="loginBtn-box flex align-center justify-center">
				重置密码
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	//手机号
	const phone = ref('');
	//密码
	const password = ref('');
	//验证码
	const code = ref('');
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const text = ref('获取验证码')
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
		proxy.$http.get(`/app/Login/sendMsg/${phone.value}/forget`).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '验证码已发送',
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

	}
	//登录
	const validatePhone = (phone) => {
		// 中国手机号码正则表达式
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};
	//重置密码
	const rustPwd = () => {
		if (!phone.value) {
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
		let data = {
			phone: phone.value,
			msg: code.value,
			pwd: password.value
		}
		uni.showLoading({
			title: '重置中...'
		})
		proxy.$http.post('/app/Login/forgetPwd', data).then(res => {
			if (res.code == 0) {
				uni.hideLoading();
				uni.showToast({
					title: '已重置，请重新登录',
					icon: 'none'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 1000)
			} else {
				uni.hideLoading()
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
</script>

<style lang="scss">
	page {
		background-color: #ffffff;

	}

	.send-msg {
		font-family: Source Han Sans CN;
		font-weight: 400;
		font-size: 26rpx;
		color: #000000;
	}

	.cu-form-group {
		background: #FFFFFF;
		padding: 0px 30rpx;
		height: 90rpx;
		display: flex;
		align-items: center;
		margin-bottom: 20px;
		// border-radius: 30px;
		border-bottom: 0.5px solid #dadbde;
		width: calc(100% - 120rpx);
		margin: auto;

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

	.activeCode {
		background-color: #999999 !important;
	}

	.logo {
		width: 100%;

		image {
			width: 150rpx;
			height: 150rpx;
			border-radius: 50%;
		}
	}

	.login {
		width: 100%;
		height: 100vh;
		flex-direction: column;
	}

	.loginTitle {
		width: calc(100% - 60rpx);
		padding: 0 30rpx;

		.loginTitle-i {
			color: #999999;
			font-size: 32rpx;
		}
	}

	.active {
		color: #333333 !important;
		font-weight: bold;
	}

	.loginInp {
		width: 100%;
		height: auto;
		margin-top: 70rpx;

		.loginInp-box {
			width: 686rpx;
		}

		.loginInpCode {
			background-color: #4C6CD3;
			color: #ffffff;
			padding: 8rpx 20rpx;
			border-radius: 24rpx;
			font-size: 24rpx;

		}
	}

	.loginNo {
		width: 100%;
		height: auto;
		margin-top: 26rpx;

		.loginNo-box {
			width: 686rpx;
			color: #999999;
			font-size: 26rpx;
		}
	}

	.loginBtn {
		width: 100%;
		height: auto;
		margin-top: 200rpx;

		.loginBtn-box {
			width: 646rpx;
			height: 92rpx;
			background-color: #4C6CD3;
			border-radius: 46rpx;
			color: #FFFFFF;
			font-size: 30rpx;
			font-weight: bold;
		}
	}
</style>