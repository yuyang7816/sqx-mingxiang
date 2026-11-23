<template>
	<view>
		<view class="icon flex align-center justify-center">
			<u-icon name="info-circle-fill" color="#4C6CD3" size="100"></u-icon>
		</view>
		<view class="title">
			注销码兄冥想服务
		</view>
		<view class="shuoming">
			码兄冥想注销后，您将放弃以下权益且无法找回
		</view>
		<view class="list flex align-center justify-center">
			<view class="list-box">
				<view class="list-box-item">
					·<text style="margin-left: 10rpx;">您将无法通过该帐号登录、使用码兄冥想平台；</text>
				</view>
				<view class="list-box-item">
					·<text style="margin-left: 10rpx;">您将无法访问帐号的个人信息（包括帐号名称、昵称、头像等）；</text>
				</view>
				<view class="list-box-item">
					·<text style="margin-left: 10rpx;">您帐号的所有权益，将视为主动放弃</text>
				</view>
			</view>
		</view>
		<view class="tishi">
			点击【确认注销】即代表您已同意<text @click="goXieOff()">《注销协议》</text>
		</view>
		<view class="submit flex align-center justify-center">
			<view class="submit-box flex align-center justify-center" @click="submit()">
				确认注销
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance()
	//去注销协议
	const goXieOff = () => {
		uni.navigateTo({
			url: '/settingViews/cancelAgreement'
		})
	};
	//确认注销
	const submit = () => {
		uni.showModal({
			title: '温馨提示',
			content: '注销后您的账号信息与会员权益将会永久清除且无法恢复，继续注销将在三日内清除完毕，确认继续注销吗？',
			showCancel: true,
			cancelText: '暂不注销',
			confirmText: '继续注销',
			confirmColor: '#4C6CD3',
			success: res => {
				if (res.confirm) {
					uni.showLoading({
						title: '注销中...'
					})
					proxy.$http.postJson('/app/message/insertMessage', {
						state: 3,
						title: uni.getStorageSync('phone') ? uni.getStorageSync('phone') : '注销账号',
						content: '我要注销账号！',
						phone: uni.getStorageSync('phone'),
						userId: uni.getStorageSync('userId')
					}).then(res => {
						if (res.code === 0) {
							uni.showToast({
								title: '申请成功！'
							});
							logout();
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/index/index'
								})
							}, 1000);
						} else {
							uni.hideLoading();
							uni.showModal({
								showCancel: false,
								title: '注销失败',
								content: res.msg
							});
						}
					}).catch(err => {
						uni.hideLoading()
						uni.showToast({
							title: '网络异常，请重试',
							icon: 'none'
						})
					})
				}
			}
		});
	};
	//退出登录
	const logout = () => {
		uni.removeStorageSync('token')
		uni.removeStorageSync('userId')
	};
</script>

<style lang="scss">
	.icon {
		width: 100%;
		padding-top: 100rpx;
	}

	.title {
		width: 100%;
		text-align: center;
		margin-top: 20rpx;
		font-weight: bold;
		font-size: 40rpx;
	}

	.shuoming {
		font-size: 24rpx;
		color: #333333;
		margin-top: 20rpx;
		text-align: center;
	}

	.list {
		width: 100%;
		// height: 100rpx;
		margin-top: 20rpx;

		.list-box {
			padding: 20rpx;
			width: 686rpx;
			height: 100%;
			border-radius: 24rpx;
			background: #ffffff;

			.list-box-item {
				margin-top: 10rpx;
				font-size: 24rpx;
				color: #333333;
			}
		}
	}

	.tishi {
		width: 100%;
		text-align: center;
		color: #999999;
		margin-top: 40rpx;
		font-size: 24rpx;

		text {
			color: #4C6CD3;
		}
	}

	.submit {
		width: 100%;
		height: 88rpx;
		margin-top: 60rpx;

		.submit-box {
			width: 686rpx;
			height: 88rpx;
			border-radius: 24rpx;
			color: #ffffff;
			background: #4C6CD3;
		}
	}
</style>