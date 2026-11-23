<template>
	<view>
		<view class="flex align-center justify-between padding-lr margin-top-sm">
			<view style="width: 88%;">
				<up-search placeholder="休息冥想" v-model="courseName" :showAction="false" shape="square" height="40"
					bgColor="#F2F5FF" @clear="inputClear" @change="inputChange"></up-search>
			</view>
			<view class="c66 f30" @click="searchInput">搜索</view>
		</view>
		<view class="padding-lr margin-top-lg">
			<view class="f28">热门搜索</view>
			<view class="margin-top flex align-center flex-wrap" v-if="hotList.length > 0">
				<view class="tag" v-for="tag in hotList">{{tag}}</view>
			</view>
			<view class="margin-top text-center c99 f28" v-else>暂无热门搜索</view>
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


	const hotList = reactive([]);
	// 获取热门搜索
	const getHotList = () => {
		proxy.$http.getT('/app/explore/selectByHOT').then(res => {
			if (res.code == 0) {
				hotList.splice(0, hotList.length, ...res.data);
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const searchList = reactive([]);
	const courseName = ref('');
	// 顶部搜索
	const searchInput = () => {
		let data = {
			courseName: courseName.value
		}
		proxy.$http.getT('/app/explore/selectBySearch', data).then(res => {
			if (res.code == 0) {
				searchList.splice(0, searchList.length, ...res.data);
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	// 搜索框清除
	const inputClear = () => {
		courseName.value = '';
		searchInput()
	};
	const inputChange = (e) => {
		if (e == '') {
			courseName.value = '';
			searchInput()
		}
	};
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};


	onLoad(option => {
		getHotList()
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
		background-color: #FFFFFF !important;
	}

	.tag {
		font-size: 24rpx;
		border-radius: 8rpx;
		background-color: #F2F5FF;
		width: max-content;
		padding: 0 20rpx;
		text-align: center;
		line-height: 60rpx;
		margin-bottom: 25rpx;
		margin-right: 25rpx;
	}
</style>