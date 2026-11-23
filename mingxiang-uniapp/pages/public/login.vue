<template>
	<view class="login flex align-center justify-center">
		<view class="logo flex align-center justify-center">
			<image src="/static/logo.png" mode=""></image>
		</view>
		<!-- #ifndef MP-WEIXIN -->
		<view class="loginTitle flex align-center">
			<view @click="setTabs(0)" class="loginTitle-i" :class="current==0?'active':''">
				密码登录
			</view>
			<up-line direction="col" margin="0 20rpx" color="#D6D6D6" length="26rpx"></up-line>
			<view @click="setTabs(1)" class="loginTitle-i" :class="current==1?'active':''">
				验证码登录
			</view>
		</view>
		<view class="loginInp">
			<view class="cu-form-group">
				<view class="title ">手机号</view>
				<up-input v-model="phone" border="none" placeholder="请输入手机号" placeholder-class="input-placeholder"></up-input>
			</view>
		</view>
		<view v-if="current == 0" class="loginInp flex align-center justify-center">
			<view class="cu-form-group">
				<view class="title ">密码</view>
				<up-input v-model="password" border="none" type="password" placeholder="请输入账号密码"
					placeholder-class="input-placeholder"></up-input>
			</view>
		</view>
		<view v-if="current == 1" class="loginInp flex align-center justify-center">
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

		<view class="loginNo flex align-center justify-center">
			<view @click="goNav('/pages/public/register')" class="loginNo-box margin-left">
				没有账号?<text style="color: #4C6CD3;">立即注册</text>
			</view>
			<view @click="goNav('/pages/public/resetPwd')" class="loginNo-box flex align-center justify-end margin-right">
				忘记密码?
			</view>
		</view>
		<view class="loginBtn flex align-center justify-center">
			<view @click="login()" class="loginBtn-box flex align-center justify-center">
				登录
			</view>
		</view>
		<!-- #endif -->
		<!-- #ifdef MP-WEIXIN -->
		<view class="content">
			<view>申请获取以下权限</view>
			<text>获得你的公开信息(昵称，头像、地区等)</text>
		</view>
		<button v-if="weixinPhone" style="background: #6F8ADB;color: #FFFFFF;width: calc(100% - 60rpx);margin:30rpx auto;"
			class="bottom" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">
			授权手机号
		</button>
		<button v-else
			style="background: #6F8ADB;color: #FFFFFF;width: calc(100% - 60rpx);margin:30rpx auto;border-radius: 80rpx;"
			class='bottom' bindtap="getUserProfile" @click="wxGetUserInfo">
			授权登录
		</button>
		<!-- #endif -->
		<!-- 协议 -->
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
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	//登录类型
	const current = ref(0);
	//密码
	const password = ref('');
	//是否选择协议
	const check = ref(false);
	//验证码
	const code = ref('');
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const text = ref('获取验证码');

	// 小程序登录---------开始
	const weixinPhone = ref(false);
	const sessionkey = ref(null);
	// 邀请人邀请码
	const otherInvitationCode = ref(null);
	const sendDataList = reactive({});
	const phone = ref(null);
	// 微信授权登录
	const wxGetUserInfo = (e) => {
		if (check.value == false) {
			uni.showToast({
				title: '请先同意《隐私政策》和《用户服务协议》',
				icon: 'none'
			})
			return
		}
		let params = {
			nickName: '微信用户',
			avatarUrl: '',
			sex: ''
		}
		wxLogin(params);
	};
	//登录
	const wxLogin = ({
		nickName,
		avatarUrl,
		sex
	}) => {
		uni.showLoading({
			title: '正在登录中...'
		})
		// 1.wx获取登录用户code
		uni.login({
			provider: 'weixin',
			success: function(loginRes) {
				console.log(loginRes, '************')
				let data = {
					code: loginRes.code,
				}
				proxy.$http.getT('/app/Login/wxLogin', data).then(res => {
					if (res.code == 0) {
						uni.hideLoading()
						uni.setStorageSync('openId', res.data.open_id)
						uni.setStorageSync('unionId', res.data.unionId)
						sessionkey.value = res.data.session_key;
						let sendData = {
							openId: uni.getStorageSync('openId'),
							unionId: uni.getStorageSync('unionId'),
							userName: nickName,
							avatar: avatarUrl,
							sex: sex, //性别
							inviterCode: otherInvitationCode.value //别人登录进来携带你的邀请码
						};
						Object.assign(sendDataList, sendData)
						// 1---有手机号  2---没有
						// 第一次登录获取手机号
						if (Number(res.data.isPhone) == 2) {
							weixinPhone.value = true;
						} else if (Number(res.data.isPhone) == 1) {
							getWeixinInfo(sendData);
						}
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg,
							duration: 2000
						});
					}
				})
			}
		});
	};
	//获取个人信息
	const getWeixinInfo = (sendData) => {
		uni.showLoading({
			title: '登录中...'
		});
		let postData = {
			openId: uni.getStorageSync('openId'), //小程序openId
			unionId: uni.getStorageSync('unionId'), //unionId
			userName: sendData.userName, //微信名称
			avatar: sendData.avatar ||
				'https://tcxxgj.xianmaxiong.com/file/uploadPath/2024/10/22/55b5afaacb619fdec242f9b50cf25aeb.png', //头像
			sex: sendData.sex, //性别
			phone: phone.value,
			inviterCode: otherInvitationCode.value
		};
		proxy.$http.postJson('/app/Login/insertWxUser', postData).then(res => {
			uni.hideLoading();
			if (res.code == 0) {
				console.log(res.token, "resssssss")
				uni.setStorageSync('token', res.token)
				uni.setStorageSync('userInfo', res.user)
				uni.setStorageSync('userId', res.user.userId)
				uni.setStorageSync('invitation', res.user.invitationCode)
				uni.setStorageSync('openId', res.user.openId)
				uni.reLaunch({
					url: '/pages/index/index'
				})
			} else {
				uni.showModal({
					showCancel: false,
					title: '登录失败',
					content: res.msg,
				});
			}
		})
	};
	// 小程序登录获取手机号
	const getPhoneNumber = (e) => {
		if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {
			console.log('用户拒绝提供手机号');
		} else {
			console.log('用户同意提供手机号');
			console.log(e)
			setPhoneByInsert(e.detail.encryptedData, e.detail.iv);
		}
	};
	//小程序微信登录后获取手机号
	const setPhoneByInsert = (decryptData, iv) => {
		let data = {
			decryptData: decryptData,
			key: sessionkey.value,
			iv: iv
		};
		proxy.$http.postJson('/app/Login/selectPhone', data).then(res => {
			if (res.code == 0) {
				phone.value = res.data.phoneNumber;
				getWeixinInfo(sendDataList);
			}
		})
	};
	// 小程序登录----------结束




	//切换登录方式
	const setTabs = (index) => {
		current.value = index;
		//如果有定时器，清除定时器
		if (timer.value) {
			clearInterval(timer.value)
			timer.value = null;
			countDown.value = 60;
			text.value = '获取验证码'
		}
		password.value = ''
	}
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
		proxy.$http.get(`/app/Login/sendMsg/${phone.value}/1`).then(res => {
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
	const login = () => {
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
		//密码登录
		if (current.value == 0) {
			if (!password.value) {
				uni.showToast({
					title: '请输入密码',
					icon: 'none'
				});
				return
			}
		}
		//验证码登录
		if (current.value == 1) {
			if (!code.value) {
				uni.showToast({
					title: '请输入验证码',
					icon: 'none'
				});
				return
			}
		}
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私权政策》',
				icon: 'none'
			});
			return
		};
		if (current.value == 0) {
			passwordLogin();
		} else { //验证码登录
			codeLogin();
		}

	};
	//验证码登录
	const codeLogin = () => {
		let data = {
			phone: phone.value,
			msg: code.value,
			openId: uni.getStorageSync('openId') ? uni.getStorageSync('openId') : '',
			inviterCode: uni.getStorageSync('inviterCode') ? uni.getStorageSync('inviterCode') : ''
		}
		proxy.$http.post('/app/Login/registerCode', data).then(res => {
			if (res.code == 0) {
				uni.setStorageSync('token', res.token);
				uni.setStorageSync('userId', res.user.userId);
				uni.setStorageSync('phone', res.user.phone);
				uni.setStorageSync('userName', res.user.userName);
				uni.setStorageSync('invitationCode', res.user.invitationCode);
				uni.setStorageSync('avatar', res.user.avatar);
				uni.showToast({
					title: '登录成功'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	//密码登录
	const passwordLogin = () => {
		let data = {
			password: password.value,
			phone: phone.value,
			openId: uni.getStorageSync('openId')
		}
		proxy.$http.post('/app/Login/loginApp', data).then(res => {
			if (res.code == 0) {
				// return
				uni.setStorageSync('token', res.token);
				uni.setStorageSync('userId', res.user.userId);
				uni.setStorageSync('phone', res.user.phone);
				uni.setStorageSync('userName', res.user.userName);
				uni.setStorageSync('invitationCode', res.user.invitationCode);
				uni.setStorageSync('avatar', res.user.avatar);
				uni.showToast({
					title: '登录成功'
				})
				setTimeout(() => {
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
		});
	}
</script>

<style lang="scss">
	page {
		background-color: #ffffff;

	}

	.content {
		text-align: center;
		color: #999999;
		font-size: 28rpx;
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

	.logo {
		width: 100%;
		margin: 100rpx 0;

		image {
			width: 150rpx;
			height: 150rpx;
			border-radius: 50%;
		}
	}

	.activeCode {
		background-color: #999999 !important;
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
</style>