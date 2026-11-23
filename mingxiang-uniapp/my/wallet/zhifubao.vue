<template>
	<view class="container" v-if="XCXIsSelect != '否'">
		<view class="inputItem flex align-center">
			<text>
				收款人姓名
			</text>
			<up-input placeholder="请输入支付宝收款人姓名" border="none" type="text" v-model="zhiFuBaoName"></up-input>
		</view>
		<view class="inputItem flex align-center">
			<text>支付宝账号</text>
			<up-input placeholder="请输入要绑定的支付宝手机号" border="none" type="text" v-model="zhiFuBao"></up-input>
		</view>
		<view class="btn flex align-center justify-center" @click="toLogin()">
			绑定账户
		</view>
		<!-- <list-cell title="收款人姓名" type="text" placeholder="请输入支付宝收款人姓名" v-model="zhiFuBaoName" ></list-cell> -->

		<!-- <list-cell title="支付宝账号" type="number" placeholder="请输入要绑定的支付宝手机号" v-model="zhiFuBao"></list-cell> -->

		<!-- <wButton text="绑定账户" :rotate="logining" @click.native="toLogin()"></wButton> -->
		<view style="padding: 32upx 64upx;font-size: 24upx;color: #999999;">提示：请正确填写收款人的支付宝账户和真实的收款人姓名，否则将无法正常收款</view>
		<!-- </view> -->
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const XCXIsSelect = ref('否');
	const zhiFuBao = ref('');
	const zhiFuBaoName = ref('');
	onLoad(option => {
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect');
		if (XCXIsSelect.value == '否') {
			uni.setNavigationBarTitle({
				title: '隐私协议'
			})
		}
		getUserInfo();
	});
	const getUserInfo = () => {
		proxy.$http.getT("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				if (res.data.zhiFuBao) {
					zhiFuBao.value = res.data.zhiFuBao;
				}
				if (res.data.zhiFuBaoName) {
					zhiFuBaoName.value = res.data.zhiFuBaoName;
				}
			}
		});
	};
	const toLogin = () => {
		if (!zhiFuBaoName.value) {
			uni.showToast({
				title: '请输入收款人姓名',
				icon: 'none'
			})
			return
		}
		if (!zhiFuBao.value) {
			uni.showToast({
				title: '请输入支付宝账号',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '提交中...'
		})
		let data = {
			zhiFuBao: zhiFuBao.value,
			zhiFuBaoName: zhiFuBaoName.value
		}
		proxy.$http.postJson('/app/user/updateUser', data).then(res => {
			if (res.code === 0) {
				uni.hideLoading();
				uni.setStorageSync('zhiFuBao', data.zhiFuBao)
				uni.setStorageSync('zhiFuBaoName', data.zhiFuBaoName)
				uni.showToast({
					title: '已提交',
					icon: 'none',
					complete() {
						setTimeout(function() {
							uni.navigateBack();
						}, 1000)
					}
				})
			} else {
				uni.hideLoading();
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
</script>

<style lang='scss'>
	page {
		background-color: #FFFFFF;
	}

	.container {
		padding-top: 32upx;
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
		background: #FFFFFF;
	}

	.confirm-btn {
		width: 300px;
		height: 42px;
		line-height: 42px;
		border-radius: 30px;
		margin-top: 70upx;
		background: #e10a07;
		color: #fff;
		font-size: 32rpx;

		&:after {
			border-radius: 60px;
		}
	}

	.inputItem {
		width: calc(686rpx - 40rpx);
		border: 1px solid #d6d7d9;
		margin: 40rpx auto;
		padding: 20rpx 20rpx;
		border-radius: 40rpx;

		text {
			margin-right: 10rpx;
		}
	}

	.btn {
		width: 686rpx;
		height: 88rpx;
		background-color: #4C6CD3;
		color: #FFFFFF;
		border-radius: 44rpx;
		margin: 0 auto;
	}
</style>