<template>
	<view style="padding: 32rpx;">
		<up-parse :content="contont"></up-parse>
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
	const contont = ref('');
	onLoad((option) => {
		getContent()
	});
	const getContent = () => {
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.get('/app/common/type/176').then(res => {
			uni.hideLoading()
			contont.value = res.data.value;
		})
	}
</script>

<style lang="scss">

</style>