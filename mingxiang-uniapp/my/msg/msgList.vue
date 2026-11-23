<template>
	<view class="">
		<view class="container_box">
			<view class="msgList" v-for="item,index in list" :key="index">
				<view class="margin-left-xs">
					<view class="flex align-center justify-between">
						<view class="f32">{{item.title || ''}}</view>
						<view class="f28 c99">{{item.createAt ||' '}}</view>
					</view>
					<view class="f28 c99" style="margin-top: 10rpx;">{{item.content||''}}</view>
				</view>
			</view>
			<up-loadmore :status="status" v-if="list.length>0" />
			<empty v-if="list.length==0"></empty>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		computed
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
	// 搜索框绑定值
	const keyword = ref('');

	const list = reactive([]);
	// 总页数
	const totalPages = ref(1);
	// 页数
	const page = ref(1);
	const limit = ref(10);
	//加载状态
	const status = ref('loadmore');
	//通知消息
	const getTzMsg = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId'),
			// type: 3
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.get("/app/message/getUserMessageInfoList", data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				totalPages.value = res.data.pages;
				if (page.value < totalPages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.records);
				} else {
					list.push(...res.data.records);
				}
				// clearUnread()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	const clearUnread = () => {
		proxy.$http.get("/app/messages/clearUnread").then(res => {
			if (res.code == 0) {} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	onLoad(option => {
		getTzMsg()
	})
	onPullDownRefresh(() => {
		page.value = 1;
		getTzMsg();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1
			status.value = 'loading';
			getTzMsg();
		}
	});
</script>

<style lang="scss" scoped>
	.searchBox {
		position: fixed;
		top: 0%;
		background-color: #FFFFFF;
		width: 100%;
		padding: 25rpx 0;
		z-index: 9;
	}

	.msgList {
		width: calc(100% - 100rpx);
		margin: auto;
		background-color: #FFFFFF;
		border-radius: 18rpx;
		margin-bottom: 20rpx;
		// display: flex;
		// align-items: center;
		padding: 20rpx;
	}

	.u-search {
		width: calc(100% - 60rpx);
		margin: auto !important;
	}

	.container_box {
		margin-top: 20rpx;
	}
</style>