<template>
	<view>
		<view class="box">
			<view class="flex align-center justify-between" style="padding: 50rpx 50rpx;">
				<view>
					<view class="margin-bottom-sm">总收益</view>
					<view style="font-size: 58rpx;">{{teamMoney?teamMoney:'0'}}</view>
				</view>
				<view class="btns1" @click="goTixian">提现</view>
			</view>
		</view>
		<view class="navbar">
			<view class="nav-item" :class="{current: type == 1}" @click="changeList(1)">
				一级
			</view>
			<view class="nav-item" :class="{current: type == 2}" @click="changeList(2)">
				二级
			</view>
		</view>
		<view class="view2"
			style="box-shadow: rgba(183, 183, 183, 0.3) 1px 1px 10px 1px;border-radius: 20rpx;margin-bottom: 50rpx;padding-bottom: 20rpx;">
			<view style="display: flex;flex-direction: row;padding: 20rpx;">
				<view style="width: 15%;text-align: center;">编号</view>
				<view style="width: 20%;text-align: center;">头像</view>
				<view style="width: 45%;text-align: center;">昵称</view>
				<view style="width: 30%;text-align: center;">奖励</view>
			</view>
			<view style="margin-bottom: 20rpx;" class="flex justify-between align-center padding"
				v-for="(item, index) in list" :key="index">
				<view style="width: 15%;text-align: center;">
					<view style="font-size: 28rpx;margin-left: 15rpx;margin-top: 6rpx;">{{ index + 1 }}
					</view>
				</view>
				<view style="width: 20%;" class="flex align-center justify-center">
					<image :src="item.avatar?item.avatar:'../../static/logo.png'" class="round"
						style="width: 50rpx;height: 50rpx;"></image>
				</view>
				<view style="width: 45%;display: flex;flex-direction: row;align-items: center;">
					<view style="font-size: 28rpx;width: 90%;overflow: hidden;text-align: center;">
						{{ item.userName?item.userName:'匿名用户' }}
					</view>
				</view>
				<view style="width: 30%;text-align: center;display: flex;justify-content: center;align-items: center;">
					<view style="font-size: 32rpx;color: #1789FD;">￥{{ item.money ? item.money : 0 }}</view>
				</view>
			</view>
			<empty v-if="list.length == 0" style="padding-bottom: 200rpx;" />
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
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	import empty from '@/components/empty/empty.vue';
	const {
		proxy
	} = getCurrentInstance();
	//收益列表
	const list = reactive([]);
	//分页
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(20);
	//收益
	const teamMoney = ref(0);
	const countChildren = ref(0);
	const countChildrenChildren = ref(0);
	const type = ref(1)
	onLoad(option => {
		getTeamMoney();
		getList();
		getExtensionData()
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getList();
		getTeamMoney()
	})
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1;
			getList();
		}
	});
	const goTixian = () => {
		uni.navigateTo({
			url: '/my/wallet/wallet'
		})
	};
	//用户推广信息
	const getExtensionData = () => {
		proxy.$http.getT('/app/invite/getExtensionData').then(res => {
			if (res.code == 0) {
				countChildren.value = res.data.countChildren
				countChildrenChildren.value = res.data.countChildrenChildren
			}
		})

	}
	const changeList = (zhishu) => {
		page.value = 1
		type.value = zhishu
		getList()
	}
	//获取列表
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			type: type.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/invite/getProfitList', data).then(res => {
			uni.stopPullDownRefresh()
			if (res.code == 0) {
				pages.value = res.data.totalPage
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records)
				} else {
					list.push(...res.data.records)
				}
			}
		});
	};
	//获取统计
	const getTeamMoney = () => {
		proxy.$http.getT('/app/invite/selectInviteMoney').then(res => {
			if (res.code == 0) {
				teamMoney.value = res.data.inviteMoney ? res.data.inviteMoney : 0;
			}
		});
	};
</script>

<style lang="less">
	page {
		background-color: #f5f5f5;
	}

	.navbar {
		display: flex;
		height: 40px;
		border-radius: 20upx;
		position: relative;
		z-index: 10;
		margin: 0 24rpx;
		border-radius: 20rpx;
		overflow: hidden;
		background-color: #FFFFFF;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;

			position: relative;

			&.current {

				background-color: #4C6CD3;
				color: #FFFFFF;

				&:after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 44px;
					height: 0;
					// border-bottom: 2px solid $base-color;
				}
			}
		}
	}

	.box {
		margin: 30rpx;

		background: #4C6CD3;
		border-radius: 24rpx;
		color: #FFFFFF;
	}

	.btns1 {
		width: 150rpx;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background: #ffffff;
		border-radius: 40rpx;
		// padding: 16rpx 45rpx;
		color: #4C6CD3;
	}

	.view2 {
		background-color: #FFFFFF;
		width: 93%;
		margin-left: 26rpx;
		border-radius: 20rpx;
		margin-top: 20rpx;
		margin-bottom: 20rpx;
	}
</style>