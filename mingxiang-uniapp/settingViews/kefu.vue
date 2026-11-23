<template>
	<view style="height: 100vh;margin: 32upx;">
		<view style="text-align: center;background: #FFFFFF;padding: 40upx;border-radius: 32upx;">
			<view style="font-size: 38upx;color: #000">添加客服微信咨询</view>
			<view style="font-size: 32upx;margin-top: 32upx;color: #000">微信号：{{weixin}}</view>
			<view @click="copyHref"
				style="background: #4BB6FF;width:200upx;margin-top: 32upx;font-size: 30upx;color: #FFFFFF;padding: 4upx 20upx;border-radius: 24upx;margin: 20rpx auto;">
				一键复制</view>

			<image @click="prewImg(image)" mode="aspectFit" style="margin-top: 32upx" :src="image"></image>
			<!-- <image src="/static/logo.png" mode=""></image> -->
			<view style="font-size: 28upx;color: #000;margin-top: 32upx" v-if="isWeiXin">
				{{ isWeiXin ? '长按识别上方二维码' : '' }}
			</view>
			<view v-if="isWeiXin" style="font-size: 24upx;color: #FFFFFF;margin-top: 80upx" @click="rests">无法识别？</view>
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
	const image = ref('');
	const isWeiXin = ref(false);
	const weixin = ref('710070994');
	const webviewStyles = reactive({
		progress: {
			color: '#1A1929 '
		}
	});
	const SelKeFu = ref('2');
	onLoad(option => {
		SelKeFu.value = uni.getStorageSync('SelKeFu');
		// #ifdef H5
		let ua = navigator.userAgent.toLowerCase();
		if (ua.indexOf('micromessenger') !== -1) {
			isWeiXin.value = true;
		}
		// #endif
		proxy.$http.get('/app/common/type/1').then(res => {
			if (res.code == 200) {
				if (res.data && res.data.value) {
					image.value = res.data.value;
				}
			}
		})
		proxy.$http.get('/app/common/type/436').then(res => {
			if (res.code == 200) {
				if (res.data && res.data.value) {
					weixin.value = res.data.value;
				}
			}
		})
	});
	//邀请码复制
	const copyHref = () => {
		uni.setClipboardData({
			data: weixin.value,
			success: r => {
				uni.showToast({
					title: '复制成功'
				})
			}
		});
	};
	const prewImg = (img) => {
		uni.previewImage({
			urls: [img],
			current: 0
		})
	};
	const rests = () => {
		uni.showToast({
			title: '已刷新请再次长按识别',
			mask: false,
			duration: 1500,
			icon: 'none'
		});
		window.location.reload();
	};
</script>

<style>

</style>