<template>
	<view>
		<view class="flex align-center justify-between margin-top-lg margin-left margin-bottom">
			<view class="">
				<view class="f32 text-bold">{{titleName}}</view>
				<view class="c99 f24">{{remark}}</view>
			</view>
		</view>
		<view class="container_box flex flex-wrap justify-between" v-if="dataList.length > 0">
			<view class="list"
				@click="routerGo('/subpackage/courseInfo/courseDetail?title='+children.typeName+'&id='+children.id)"
				v-for="children,childrenIndex in dataList" :key="childrenIndex">
				<view style="width: 329rpx;height: 234rpx;border-radius: 24rpx;overflow: hidden;display: block;">
					<image :src="children.cover?children.cover:'/static/logo.png'" mode="aspectFill"
						style="width: 100%;height: 100%;display: block;">
					</image>
				</view>
			<!-- 	<view class="padding-left-xs">
					<view class="f26">{{children.courseName||'暂无'}}</view>
					<view class="f24 c99 " style="margin-top: 6rpx;">{{children.courseTitle||'暂无'}}</view>
				</view> -->
				<view class="padding-left-xs padding-top-xs">
					<view class="f30">{{children.courseName || '暂无'}}</view>
					<view class="f26 c99" style="margin-top: 6rpx;">{{children.courseTitle}}</view>
				</view>
			</view>
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
	// list tab点击
	const click = (item) => {
		console.log('当前值：' + item)
	};
	//顶部tab
	const tabList = ref(['冥想', '睡眠', '声音']);
	// 顶部tab绑定index
	const tabIndex = ref(0);
	const titleName = ref('')
	const remark = ref('')
	const id = ref('');
	// 总页数
	const totalPages = ref(1);
	// 页数
	const pageSize = ref(10);
	const pageNum = ref(1);
	//加载状态
	const status = ref('loadmore');
	const dataList = reactive([]);


	// 顶部tab切换
	const changeTabIndex = (index) => {
		tabIndex.value = index
	}
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	const getList = () => {
		let data = {
			pageNum: pageNum.value,
			pageSize: pageSize.value,
			typeId: id.value
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/courseInfo/selectCourseListByTypeId', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading();
			if (res.code == 0) {
				totalPages.value = res.data.pages;
				if (pageNum.value < totalPages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (pageNum.value === 1) {
					dataList.splice(0, dataList.length, ...res.data.list);
				} else {
					dataList.push(...res.data.list);
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};

	onLoad(option => {
		id.value = option.id
		titleName.value = option.title
		remark.value = option.remark
		if (option.title) {
			uni.setNavigationBarTitle({
				title: option.title
			})
		}

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

	.list {
		margin-right: 25rpx;
	}

	.list:nth-child(2n) {
		margin-right: 0 !important;
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
		// margin-top: 30rpx;
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