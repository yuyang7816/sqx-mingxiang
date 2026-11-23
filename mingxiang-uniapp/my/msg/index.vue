<template>
	<view>
		<view class="margin-topW">
			<view class="margin-top-sm">
				<view class="flex list">
					<view>
						<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;" src="/my/static/kf.png">
						</image>
					</view>
					<view class="flex-sub margin-left-sm" @click="selectKeFu">
						<view class="flex justify-between">
							<view class="f26">在线客服</view>
						</view>
						<view style="margin-top: 10rpx;">
							<view class="f26 c99">有问题请联系在线客服~</view>
						</view>
					</view>
				</view>
			</view>
			<view class="margin-top-sm" @click="routerGo('/my/msg/msgList')">
				<view class="flex radius bg list">
					<view>
						<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;" src="/static/image/my/msgtz.png"></image>
					</view>
					<view class="flex-sub margin-left-sm">
						<view class="flex justify-between">
							<view class="f26">{{newMessagesInfo ?newMessagesInfo.title:'系统消息'}}</view>
							<up-badge bgColor="#f56c6c" max="99" :value="messageCount" v-if="messageCount > 0"
								style="position: absolute;right: 30rpx;"></up-badge>
						</view>
						<view>
							<view class="f26 c99" style="margin-top: 10rpx;">{{newMessagesInfo ?newMessagesInfo.content:'暂无消息'}}
							</view>
						</view>
					</view>
				</view>
			</view>

		</view>
		<!-- <view v-if="chatList.length" class="margin-top-sm  content ">
			<view class="radius padding-lr-sm bg" style="margin-top: 4rpx;" @click="goIM(item)"
				v-for="(item,index) in chatList" :key='index'>
				<view class="flex padding-tb " v-if="userId == item.userId">
					<view>
						<u-image shape="circle" width='80rpx' height="80rpx" :src="item.byAvatar"></u-image>
					</view>
					<view class="flex-sub margin-left-sm">
						<view class="flex justify-between">
							<view class="text-white">{{item.byUserName}}</view>
							<view class="text-grey">{{item.messageTime}}</view>
						</view>
						<view class="flex justify-between">
							<view class="text-grey" v-if="item.messageType == 1">{{ item.content }}</view>
							<view class="text-grey" v-if="item.messageType == 2">[图片]</view>
							<view class="text-grey" v-if="item.messageType == 3">[语音]</view>
							<view class="text-grey" v-if="item.messageType == 4">[表情]</view>
							<view class="text-grey" v-if="item.messageType == 5">[地图]</view>
							<view class="text-grey" v-if="item.messageType == 6">[订单]</view>
							<view class="text-grey" v-if="item.messageType == 7">[服务]</view>
							<view class="item_dian" v-if="item.contentCount>0">
								{{item.contentCount < 99 ?item.contentCount:item.contentCount+'+'}}
							</view>
						</view>
					</view>
				</view>
				<view class="flex padding-tb" v-else>
					<view>
						<u-image shape="circle" width='80rpx' height="80rpx" :src="item.avatar"></u-image>
					</view>
					<view class="flex-sub margin-left-sm">
						<view class="flex justify-between">
							<view class="text-white">{{item.userName}}</view>
							<view class="text-grey">{{item.messageTime}}</view>
						</view>
						<view class="flex justify-between">
							<view class="text-grey" v-if="item.messageType == 1">{{ item.content }}</view>
							<view class="text-grey" v-if="item.messageType == 2">[图片]</view>
							<view class="text-grey" v-if="item.messageType == 3">[语音]</view>
							<view class="text-grey" v-if="item.messageType == 4">[表情]</view>
							<view class="text-grey" v-if="item.messageType == 5">[地图]</view>
							<view class="text-grey" v-if="item.messageType == 6">[订单]</view>
							<view class="text-grey" v-if="item.messageType == 7">[服务]</view>
							<view v-if="item.contentCount"
								style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
								{{item.contentCount}}
							</view>
						</view>
					</view>
				</view>
			</view>
		</view> -->
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
		onReachBottom,
		onReady,
		onPageScroll
	} from '@dcloudio/uni-app';
	// import shopContainer from "/pages/index/components/shopContainer.vue";
	// import empty from '@/components/empty/empty.vue';
	const {
		proxy
	} = getCurrentInstance();


	// 总页数
	const totalPages = ref(1);
	// 页数
	const page = ref(1);
	const limit = ref(10);
	//加载状态
	const status = ref('loadmore');
	const newMessagesInfo = reactive([]);
	const messageCount = ref(10);
	const getNewMessages = () => {
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.get('/app/message/getUserMessageInfoListLimit1', {
			userId: uni.getStorageSync('userId')
		}).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				Object.assign(newMessagesInfo, res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};
	const selectKeFu = () => {
		let SelKeFu = uni.getStorageSync('SelKeFu')
		if (SelKeFu + '' == '1') { //手机号
			uni.makePhoneCall({
				phoneNumber: uni.getStorageSync('kefuPhone')
			});
		} else if (SelKeFu + '' == '2') { //企业微信
			// #ifdef MP-WEIXIN
			wx.openCustomerServiceChat({
				extInfo: {
					url: uni.getStorageSync('kefu')
				},
				corpId: uni.getStorageSync('kefuAppId'),
				success(res) {}
			})
			// #endif
			// #ifdef H5
			window.location.href = uni.getStorageSync('kefu')
			// #endif
			// #ifdef APP
			let kefu = uni.getStorageSync('kefu')
			console.log(kefu)
			plus.runtime.openURL(kefu, function(res) {});
			// #endif
		} else { //客服二维码页面
			uni.navigateTo({
				url: '/settingViews/kefu'
			})
		}
	};
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	onLoad(option => {

	});
	onShow(() => {
		if (uni.getStorageSync('token')) {
			getNewMessages()
			messageCount.value = uni.getStorageSync('messageCount')
		}
	});
</script>

<style>
	page {
		background-color: #F7F7F7;
	}

	.list {
		position: relative;
		background-color: #FFFFFF;
		width: calc(100% - 120rpx);
		margin: auto;
		border-radius: 24rpx;
		padding: 30rpx;
	}

	.item_dian {
		position: absolute;
		right: 4%;
		top: 45rpx;
		z-index: 9;
		/* padding: 0rpx 2rpx; */
		background: #4CB6FF;
		border-radius: 50rpx;
		width: 40rpx;
		height: 40rpx;
		color: #fff;
		text-align: center;
		line-height: 40rpx;
		font-size: 22rpx;
	}

	.bg {
		background: #FFFFFF;
		/* width: calc(100% - 60rpx);
		margin: auto;
		border-radius: 18rpx;
		margin-top: 15rpx; */
	}

	.searchBox {
		width: 100%;
		background-color: #FFFFFF;
		padding-bottom: 35rpx;
		padding-top: 20rpx;
	}

	/deep/ .u-border-bottom:after {
		border: none !important;
	}
</style>