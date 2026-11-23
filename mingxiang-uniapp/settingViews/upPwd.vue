<template>
	<view class="container">
		<view class="wrapper">
			<view class="input-content">
				<view class="cu-form-group flex align-center"
					style="border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px;padding: 20rpx;">
					<text class="title">旧密码</text>
					<input type="password" v-model="oldPwd" placeholder="请输入旧密码" placeholder-class="input-empty"
						maxlength="20" minlength="6" data-key="oldpassword" @confirm="toLogin" />
				</view>
				<view class="cu-form-group flex align-center"
					style="border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px;padding: 20rpx;">
					<text class="title">新密码</text>
					<input type="password" v-model="pwd" placeholder="请设置新密码" placeholder-class="input-empty"
						maxlength="20" minlength="6" data-key="password" @confirm="toLogin" />
				</view>
			</view>
			<button class="confirm-btn" @click="toLogin">修改密码</button>
		</view>
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
	const oldPwd = ref('');
	const pwd = ref('');
	const toLogin = () => {
		if (!oldPwd.value) {
			uni.showToast({
				title: '请输入旧密码',
				icon: 'none'
			})
			return
		}
		if (oldPwd.value.length < 6) {
			uni.showToast({
				title: '旧密码位数必须大于六位',
				icon: 'none'
			})
			return
		}
		if (!pwd.value) {
			uni.showToast({
				title: '请设置新密码',
				icon: 'none'
			})
			return
		}
		if (pwd.value.length < 6) {
			uni.showToast({
				title: '新密码位数必须大于六位',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '正在修改密码中...'
		})
		proxy.$http.post("/app/user/updatePwd", {
			pwd: pwd.value,
			oldPwd: oldPwd.value,
		}).then(res => {
			uni.hideLoading();
			if (res.code === 0) {
				uni.showToast({
					title: '密码修改成功！下次请使用新密码登录！',
					icon: 'none'
				})
				setTimeout(function() {
					uni.navigateBack()
				}, 1000)
			} else {
				uni.showModal({
					showCancel: false,
					title: '密码修改失败',
					content: res.msg,
				});
			}
		});
	};
</script>

<style lang='scss'>
	page {
		height: 100%;
		background: #ffffff !important;
	}

	.title {
		margin-right: 20rpx;
	}

	.container {
		padding-top: 32upx;
		position: relative;
		width: 100%;
		/* height: 100%; */
		/* overflow: hidden; */
		/* background: #F5F5F5 !important; */
	}

	.wrapper {
		position: relative;
		z-index: 90;
		background: #ffffff;
		padding-bottom: 20px;
	}


	.input-content {
		padding: 32upx 80upx;
	}


	.confirm-btn {
		width: 600upx;
		height: 80upx;
		line-height: 80upx;
		border-radius: 60upx;
		margin-top: 32upx;
		background: #4C6CD3;
		color: #fff;
		font-size: 32upx;

		&:after {
			border-radius: 60px;
		}
	}
</style>