<template>
	<view>
		<!-- <view class="header">
			<view class="flex align-center justify-between margin-top-lg margin-bottom-xs">
				<view class="">
					<scroll-view class="scroll-view_H" scroll-x="true">
						<view v-for="item,index in tabList" :class="tabIndex == index?'tabList-item':'tabList-item_aic'"
							@click="changeTabIndex(index)">{{item}}
						</view>
					</scroll-view>
				</view>
			</view>
			<up-tabs :list="list1" @click="click" lineColor="#4E6BCF"></up-tabs>
		</view> -->
		<view class="container_box" v-for="item,index in dataList">
			<view class="f28 padding-left margin-bottom-xs margin-top">{{item.date}}</view>
			<view class="flex align-center justify-between flex-wrap">
				<view style="display: inline-block;margin-bottom: 20rpx;" v-for="(ites,indes) in item.list">
					<image :src="ites.coverImage?ites.coverImage:'/static/logo.png'" mode=""
						style="width: 329rpx;height: 234rpx;border-radius: 12rpx;">
					</image>
					<view class="padding-left-xs">
						<view class="f26">{{ites.typeName || '暂无'}}</view>
						<view class="f24 c99">{{ites.chapterNum}}·{{ites.remark}}</view>
					</view>
				</view>
			</view>
		</view>

		<up-loadmore :status="status" v-if="dataList.length>0" />
		<empty v-if="dataList.length==0"></empty>
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
	import empty from '@/components/empty/empty.vue';
	const {
		proxy
	} = getCurrentInstance();



	// 总页数
	const totalPages = ref(1);
	// 页数
	const pageSize = ref(10);
	const pageNum = ref(1);
	const dataList = reactive([]);
	//顶部tab
	const tabList = ref(['冥想', '睡眠', '声音']);
	// 顶部tab绑定index
	const tabIndex = ref(0);
	// 顶部tab切换
	const changeTabIndex = (index) => {
		tabIndex.value = index
	}
	//加载状态
	const status = ref('loadmore');
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	const getList = () => {
		let data = {
			pageNum: pageNum.value,
			pageSize: pageSize.value
		}
		uni.showLoading({
			title: "加载中..."
		})
		proxy.$http.getT('/app/courseLearn/selectMyPlayed', data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				totalPages.value = res.data.pages;
				if (pageNum.value < totalPages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (pageNum.value == 1) {
					dataList.splice(0, dataList.length, ...res.data.list);
				} else {
					dataList.push(...res.data.list);
				}
			}
		});
	};
	onLoad(option => {
		getList();
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {
		pageNum.value = 1;
		getList();
	});
	onReachBottom(() => {
		if (pageNum.value < totalPages.value) {
			pageNum.value += 1
			status.value = 'loading';
			getList();
		}
	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #FFFFFF;
	}

	.tabList-item_aic {
		width: max-content;
		height: 74rpx;
		border-radius: 37rpx;
		font-size: 30rpx;
		background-color: #EDEFFC;
		color: #4E6BCF;
		padding: 0 40rpx;
		text-align: center;
		line-height: 74rpx;
		display: inline-block;
		margin-right: 20rpx;
	}

	.tabList-item {
		width: max-content;
		height: 74rpx;
		border-radius: 37rpx;
		font-size: 30rpx;
		background-color: #4E6BCF;
		color: #FFFFFF;
		padding: 0 40rpx;
		text-align: center;
		line-height: 74rpx;
		display: inline-block;
		margin-right: 20rpx;
	}

	.container_box {
		width: calc(100% - 60rpx);
		margin: auto;

	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
	}

	.header {
		width: calc(100% - 60rpx);
		margin: auto;
		/* #ifdef H5 */
		margin-top: 30rpx;
		/* #endif */
		/* #ifndef H5 */
		margin-top: 88rpx;
		/* #endif */
	}

	.swiper_ {
		height: 240rpx;
		width: calc(100% - 55rpx);
		margin: auto;
		margin-top: 30rpx;
	}

	.tabBox {
		width: calc(100% - 60rpx);
		margin: auto;
		margin-top: 95rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
</style>