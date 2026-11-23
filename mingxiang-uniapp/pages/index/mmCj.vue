<template>
	<view class="">
		<view class="tabBox">
		</view>
		<view class="">
			<view class="glass-box">
				<image src="/static/logo.png" mode="" style="width: 100%;height: 100%;object-fit: cover;"></image>
				<!-- 图片毛玻璃效果 -->
				<view class="glass-img"></view>
				<view class="glass-text">{{cjName||'未命名混音'}}</view>
			</view>
			<view class="text_name">
				<input type="text" v-model="cjName" placeholder="未命名混音" class="text_name_input"
					placeholder-style="color:#4C6CD3;" />
			</view>
		</view>
		<view class="nextBtn" @click="routerGo('/pages/index/index','tabbar')">完成</view>



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
	// 命名场景名称
	const cjName = ref('');
	const routerGo = (url, type) => {
		if (type == 'tabbar') {
			uni.switchTab({
				url
			})
		} else {
			uni.navigateTo({
				url
			})
		}
	};


	onLoad(option => {

	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #FFFFFF;
	}

	.text_name {
		background-color: #F2F5FF;
		color: #4C6CD3;
		width: calc(100% - 60rpx);
		margin: auto;
		height: 100rpx;
		border-radius: 21rpx;
		margin-top: 60rpx;
		line-height: 100rpx;
		text-align: center;
	}

	.text_name_input {
		line-height: 100rpx;
		height: 100rpx;
		font-weight: 400;
	}

	.glass-text {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		color: white;
		font-size: 18px;
		font-weight: 500;
		text-align: center;
		z-index: 1;
	}

	.glass-box {
		position: relative;
		width: calc(100% - 400rpx);
		height: 538rpx;
		overflow: hidden;
		border-radius: 16px;
		margin: auto;
		margin-top: 65rpx;
	}

	.glass-img {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: rgba(255, 255, 255, 0.2);
		/* 半透明白 */
		backdrop-filter: blur(10px);
		/* 关键：模糊背景 */
		-webkit-backdrop-filter: blur(10px);
		/* Safari 兼容 */
		border-radius: 16px;
		pointer-events: none;
		/* 防止遮挡点击 */
	}

	.nextBtn {
		width: calc(100% - 60rpx);
		margin: auto;
		background: #4C6CD3;
		border-radius: 50rpx;
		text-align: center;
		z-index: 8;
		color: #FFFFFF;
		height: 100rpx;
		line-height: 100rpx;
		margin-top: 50rpx;
		position: fixed;
		bottom: 100rpx;
		left: 0;
		right: 0;
	}

	.container_box {
		width: calc(100% - 60rpx);
		margin: auto;
		margin-top: 20rpx;
	}

	.tabBox {
		width: calc(100% - 60rpx);
		margin: auto;
		margin-top: 20rpx;
		display: flex;
		// justify-content: space-between;
		align-items: center;
		flex-wrap: wrap;
	}
</style>