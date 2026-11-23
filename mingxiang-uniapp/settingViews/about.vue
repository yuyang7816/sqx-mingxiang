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
	const type = ref('');
	onLoad((option) => {
		if (option.type) {
			type.value = option.type
			getContent()
		}
	});
	const getContent = () => {
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.get('/app/common/type/' + type.value).then(res => {
			uni.hideLoading()
			contont.value = res.data.value;
			uni.setNavigationBarTitle({
				title: res.data.min
			})
		})
	}
</script>

<style lang="scss">

</style>