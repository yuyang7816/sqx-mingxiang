<template>
	<view>
		<view class="setList flex align-center justify-center">
			<view class="setList-box">
				<view @click="goNav('/settingViews/upPwd')" class="setList-box-item flex align-center justify-between">
					修改密码
					<up-icon name="arrow-right" color="#333333" size="18"></up-icon>
				</view>
				<view @click="goNav('/settingViews/logOff')" class="setList-box-item flex align-center justify-between">
					注销账户
					<up-icon name="arrow-right" color="#333333" size="18"></up-icon>
				</view>
				<view @click="goNav('/settingViews/about?type=876')" class="setList-box-item flex align-center justify-between">
					用户服务
					<up-icon name="arrow-right" color="#333333" size="18"></up-icon>
				</view>
				<view @click="goNav('/settingViews/about?type=875')" class="setList-box-item flex align-center justify-between">
					VIP权益条款
					<up-icon name="arrow-right" color="#333333" size="18"></up-icon>
				</view>
				<view @click="goNav('/settingViews/about?type=187')" class="setList-box-item flex align-center justify-between">
					关于我们
					<up-icon name="arrow-right" color="#333333" size="18"></up-icon>
				</view>
				<view @click="goNav('/settingViews/privacyPolicy')" class="setList-box-item flex align-center justify-between">
					隐私政策
					<up-icon name="arrow-right" color="#333333" size="18"></up-icon>
				</view>
				<view @click="goNav('/settingViews/cancelAgreement')" class="setList-box-item flex align-center justify-between"
					style="padding-bottom: 0;">
					注销协议
					<up-icon name="arrow-right" color="#333333" size="18"></up-icon>
				</view>
			</view>
		</view>
		<view class="outLoginBtn flex align-center justify-center">
			<view class="outLoginBtn-box flex align-center justify-center" @click="outLogin">
				退出登录
			</view>
		</view>
	</view>
</template>

<script setup>
	import audioManager from '@/utils/audioManager.js'
	import {
		ref
	} from 'vue';
	//跳转
	const goNav = (url) => {
		uni.navigateTo({
			url: url
		})
	};
	//退出登录
	const outLogin = () => {
		uni.showModal({
			title: '提示',
			content: '确认退出登录吗？',
			complete(ret) {
				if (ret.confirm) {
					// 1. 清除音频数据
					// audioManager.clearOnLogout()
					uni.clearStorageSync()
					uni.navigateBack()
				}
			}
		})
	};
</script>

<style lang="scss">
	page {
		background-color: #f5f5f5;
	}

	.setList {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.setList-box {
			width: calc(686rpx - 60rpx);
			height: 100%;
			padding: 0 30rpx;
			background-color: #ffffff;
			border-radius: 24rpx;
			font-size: 28rpx;
			color: #333333;
		}

		.setList-box-item {
			border-bottom: 1px solid #f5f5f5;
			padding-bottom: 20rpx;
			margin: 40rpx 0;
		}
	}

	.setList-box-item:last-child {
		border: none !important;
	}

	.outLoginBtn {
		width: 100%;
		height: auto;
		margin-top: 60rpx;

		.outLoginBtn-box {
			width: 686rpx;
			height: 88rpx;
			border-radius: 44rpx;
			background-color: #4C6CD3;
			font-size: 28rpx;
			font-weight: bold;
			color: #ffffff;
		}
	}
</style>