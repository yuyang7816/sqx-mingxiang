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
	const contont = ref(
		''
	);
	onLoad((option) => {
		getContent(option.id)
		uni.setNavigationBarTitle({
			title: option.text
		})
	});
	const getContent = (id) => {
		proxy.$http.get('/app/common/type/' + id).then(res => {
			contont.value = res.data.value;
		})
	}
</script>

<style lang="scss">

</style>