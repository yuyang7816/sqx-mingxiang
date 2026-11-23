<template>
	<view>
		<view class="header">
			<view class="f48 text-bold">探索</view>
			<view class="flex align-center justify-between margin-top-lg margin-bottom-xs">
				<view class="">
					<scroll-view class="scroll-view_H" scroll-x="true">
						<view v-for="item,index in tabList" :class="tabIndex == item.id?'tabList-item':'tabList-item_aic'"
							@click="changeTabIndex(item.id,index)">{{item.exploreName}}
						</view>
					</scroll-view>
				</view>
				<view class="margin-right" @click="routerGo('/pages/explore_ts/search')">
					<up-icon name="search" color="#333" size="28"></up-icon>
				</view>
			</view>
			<up-tabs :list="list1" v-if="list1.length > 0" @click="click" :current="listIndex" keyName="exploreName"
				lineColor="#4E6BCF"></up-tabs>
		</view>
		<view class="container_box">
			<view class="flex align-center justify-between flex-wrap">
				<view style="display: inline-block;margin-top: 30rpx;" v-for="item,index in dataList"
					@click="routerGo('/subpackage/courseInfo/courseDetail?title='+item.courseName+'&id='+item.id)">
					<image :src="item.cover?item.cover:'/static/logo.png'" mode=""
						style="width: 329rpx;height: 234rpx;border-radius: 12rpx;">
					</image>
					<view class="padding-left-xs padding-top-xs">
						<view class="f30">{{item.courseName || '暂无'}}</view>
						<view class="f26 c99">{{item.courseTitle}}</view>
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
	const {
		proxy
	} = getCurrentInstance();
	import empty from '@/components/empty/empty.vue';
	import list from 'uview-plus/components/u-list/list';

	const list1 = ref([]);
	// list tab点击
	const click = (item) => {
		ejIndex.value = item.id
		listIndex.value = item.index
		pageNum.value = 1
		getListByTypeId();
	};


	const listIndex = ref(0);
	//顶部tab
	const tabList = ref();
	// 顶部tab绑定index
	const tabIndex = ref(0);
	// 次顶部tab绑定index
	const ejIndex = ref(0);
	// 顶部tab切换
	const changeTabIndex = (id, index) => {
		tabIndex.value = id
		listIndex.value = 0
		if (tabList.value[index].children && tabList.value[index].children.length > 0) {
			list1.value = []
			let childreList = {
				id: 0,
				exploreName: '全部'
			}
			list1.value.push(childreList)
			tabList.value[index].children.forEach(d => {
				list1.value.push(d);
			});
			dataList.splice(0, dataList.length, ...[]);
			pageNum.value = 1
			getListByTypeId();
		} else {
			list1.value = []
			let childreList = {
				id: 0,
				exploreName: '全部'
			}
			list1.value.push(childreList)

			dataList.splice(0, dataList.length, ...[]);

			pageNum.value = 1
			getListByTypeId();
		}


		// list1.value = []
		// let childreList = {
		// 	id: 0,
		// 	exploreName: '全部'
		// }
		// list1.value.push(childreList)
		// if(){

		// }
		// tabList.value[index].children.forEach(d => {
		// 	list1.value.push(d);
		// });
		// ejIndex.value = ''
		// pageNum.value = 1
		// getListByTypeId();

	};


	// 总页数
	const totalPages = ref(1);
	// 页数
	const pageSize = ref(10);
	const pageNum = ref(1);
	//加载状态
	const status = ref('loadmore');
	const dataList = reactive([]);
	const routerGo = (url) => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url
			})
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录，请先登录账号',
				success: function(res) {
					if (res.confirm) {
						uni.navigateTo({
							url: '/pages/public/login'
						})
					}
				}
			})
		}
	};
	const getTypeList = () => {
		proxy.$http.getT('/app/courseExplore/selectByCourseExploreList').then(res => {
			if (res.code == 0) {
				// typeList.value = res.data
				if (res.data && res.data.length > 0) {
					tabList.value = res.data
					tabIndex.value = res.data[0].id
					if (res.data[0].children && res.data[0].children.length > 0) {
						list1.value = []
						let childreList = {
							id: 0,
							exploreName: '全部'
						}
						list1.value.push(childreList)
						res.data[0].children.forEach(d => {
							list1.value.push(d);
						});
						pageNum.value = 1
						getListByTypeId();
					}
				}
			}
		});
	};
	const getListByTypeId = () => {
		let exploreId = ejIndex.value
		if (exploreId == 0) {
			exploreId = ''
		}
		let data = {
			pageNum: pageNum.value,
			pageSize: pageSize.value,
			exploreId: exploreId,
			exploreParentId: tabIndex.value
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/courseInfo/selectCourseInfoByExplore', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading();
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
		})
	};
	onLoad(option => {
		getTypeList();
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {
		pageNum.value = 1;
		getListByTypeId();
	});
	onReachBottom(() => {
		if (pageNum.value < totalPages.value) {
			pageNum.value += 1
			status.value = 'loading';
			getListByTypeId();
		}
	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #FFFFFF;
	}

	.tabList-item_aic {
		width: max-content;
		height: 60rpx;
		border-radius: 37rpx;
		font-size: 30rpx;
		background-color: #EDEFFC;
		color: #4E6BCF;
		padding: 0 40rpx;
		text-align: center;
		line-height: 60rpx;
		display: inline-block;
		margin-right: 20rpx;
	}

	.tabList-item {
		width: max-content;
		height: 60rpx;
		border-radius: 37rpx;
		font-size: 30rpx;
		background-color: #4E6BCF;
		color: #FFFFFF;
		padding: 0 40rpx;
		text-align: center;
		line-height: 60rpx;
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
		margin-top: 30rpx;
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