<template>
	<view style="text-align: left;padding-bottom: 10rpx;">
		<view v-for="(item, index) in list" :key="index" class="item">
			<view>
				<view style="margin-bottom: 8upx;display: flex;justify-content: space-between;">
					<view class="flex align-center">
						<text style="margin-bottom: 8upx;color: #557EFD;margin-right: 15rpx;" v-if="item.classify == 2"> 微信</text>
						<text style="margin-bottom: 8upx;color: #557EFD" v-if="item.state==1"> 提现成功</text>
						<text style="margin-bottom: 8upx;color: #557EFD" v-if="item.state==0"> 提现中</text>
						<text style="margin-bottom: 8upx;color: #e10a07" v-if="item.state==-1"> 提现失败</text>
					</view>

					<text style="color: #ecd4b4;font-size: 32upx;font-weight: 600"> ￥{{item.money}}</text>
				</view>
				<view style="color: #999999;font-size: 28upx;">
					<view style="margin-bottom: 8upx" v-if="item.classify == 1"> 支付宝账号：{{item.zhifubao}}</view>
					<view style="margin-bottom: 8upx" v-if="item.classify == 1"> 支付宝姓名：{{item.zhifubaoName}}</view>
					<view style="margin-bottom: 8upx"> 提现时间：{{item.createAt}}</view>
					<view style="margin-bottom: 8upx" v-if="item.state==1">成功时间 {{item.outAt}}</view>
					<view style="margin-bottom: 8upx;color: #e10a07" v-if="item.state==-1">{{item.refund}}</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length === 0" contont="暂无提现记录"></empty>
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
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	import empty from '@/components/empty/empty.vue';
	const {
		proxy
	} = getCurrentInstance();
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	onLoad(option => {
		getList();
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getList();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1;
			getList();
		}
	});
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/cash/selectPayDetails', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading()
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.list)
				} else {
					list.push(...res.data.list)
				}
			}
		})
	};
</script>

<style lang='scss'>
	page {
		background: #F5F5F5;
	}

	.item {
		background: #FFFFFF;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		/* box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1); */
		border-radius: 16upx;
	}
</style>