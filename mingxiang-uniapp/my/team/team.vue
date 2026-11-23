<template>
	<view class="content">
		<view class="view1"
			style="box-shadow: rgba(183, 183, 183, 0.3) 1px 1px 10px 1px;border-radius: 20rpx;margin-bottom: 20rpx;">
			<view style="display: flex;">
				<view style="margin: 70rpx 0 0 20rpx;height: 100rpx; width: 300rpx; text-align: center;">
					<view style="font-size: 40rpx;">{{ teamCount?teamCount:0 }}</view>
					<view style="font-size: 28rpx;margin-left: 20rpx;">邀请好友（人）</view>
				</view>
				<view style="margin: 70rpx 0 0 75rpx;height: 100rpx; width: 300rpx;text-align: center;" @click="goDet">
					<view style="font-size: 40rpx;">{{ teamMoney?teamMoney:0 }}</view>
					<view style="font-size: 28rpx;">我的收益</view>
				</view>
			</view>
			<view style="margin: 20rpx 30rpx;font-size: 28rpx;color: #4C6CD3;" v-if="yijiyongjin">
				*可得一级好友消费奖励的{{yijiyongjin*100}}%</view>
			<view style="margin: 20rpx 30rpx;font-size: 28rpx;color: #4C6CD3;" v-if="erjiyongjin">
				*可得二级好友消费奖励的{{erjiyongjin*100}}%</view>
			<button class="yaoqing_btn" @click="goYao">邀请好友</button>
		</view>
		<view class="navbar">
			<view class="nav-item" :class="{current: type == 1}" @click="changeList(1)">
				一级( {{countChildren}} )
			</view>
			<view class="nav-item" :class="{current: type == 2}" @click="changeList(2)">
				二级( {{countChildrenChildren}} )
			</view>
		</view>
		<view class="view2"
			style="box-shadow: rgba(183, 183, 183, 0.3) 1px 1px 10px 1px;border-radius: 20rpx;margin-bottom: 50rpx;padding-bottom: 20rpx;">
			<view style="display: flex;flex-direction: row;padding: 20rpx;">
				<view style="width: 15%;text-align: center;">编号</view>
				<view style="width: 20%;text-align: center;">头像</view>
				<view style="width: 40%;text-align: center;">昵称</view>
				<view style="width: 35%;text-align: center;">时间</view>
			</view>
			<view class="flex justify-between align-center padding" v-if="list.length > 0" v-for="(item, index) in list"
				:key="index" style="margin-bottom: 16rpx;">
				<view style="width: 15%;text-align: center;">
					<view style="font-size: 28rpx;margin-left: 15rpx;margin-top: 6rpx;">{{ index + 1 }}
					</view>
				</view>
				<view style="width: 20%;" class="flex align-center justify-center">
					<image :src="item.avatar?item.avatar:'../../static/logo.png'" class="round"
						style="width: 50rpx;height: 50rpx;"></image>
				</view>
				<view style="width: 40%;display: flex;flex-direction: row;align-items: center;">
					<view style="font-size: 28rpx;width: 90%;overflow: hidden;text-align: center;">
						{{ item.userName?item.userName:'匿名用户' }}
					</view>
				</view>
				<view style="width: 35%;text-align: center;">
					<view style="font-size: 28rpx;">
						{{ item.createTime.substr(0,10) }}
					</view>
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
	const YaoqingShangJin = ref(0);
	const setting = ref(1);
	//邀请列表
	const list = reactive([]);
	//分页
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(20);
	//邀请人数
	const teamCount = ref(0);
	const teamMoney = ref(0);
	//一级
	const yijiyongjin = ref('');
	const erjiyongjin = ref('');
	const countChildren = ref(0);
	const countChildrenChildren = ref(0);
	const type = ref(1)
	onLoad(option => {
		getUserInfo();
		getList();
		getTeamMoney()
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
	const changeList = (zhishu) => {
		page.value = 1
		type.value = zhishu
		getList()
	}
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				yijiyongjin.value = res.data.zhiRate ? res.data.zhiRate : 0;
				erjiyongjin.value = res.data.feiRate ? res.data.feiRate : 0;
				uni.setStorageSync('avatar', res.data.avatar)
				uni.setStorageSync('invitationCode', res.data.invitationCode)
				uni.setStorageSync('zhiFuBao', res.data.zhiFuBao)
				uni.setStorageSync('zhiFuBaoName', res.data.zhiFuBaoName)
			}
		});
	};
	//获取列表
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			type: type.value,
		}
		proxy.$http.getT('/app/invite/getExtensionList', data).then(res => {
			uni.stopPullDownRefresh()
			if (res.code == 0) {
				pages.value = res.data.total
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records)
				} else {
					list.push(...res.data.records)
				}
			}
		});
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
	//获取统计
	const getTeamMoney = () => {
		proxy.$http.getT('/app/invite/selectInviteMoney').then(res => {
			if (res.code == 0) {
				teamCount.value = res.data.inviteCount ? res.data.inviteCount : 0;
				teamMoney.value = res.data.inviteMoney ? res.data.inviteMoney : 0;
			}
		});
	};
	//邀请好友
	const goYao = () => {
		uni.navigateTo({
			url: '/pages/my/invitationUser'
		})
	};
	//我的收益
	const goDet = () => {
		uni.navigateTo({
			url: '/my/team/earnings'
		})
	};
</script>

<style lang="scss">
	// @import '../../static/css/index.css';
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

	.view1 {
		background-color: #FFFFFF;
		width: 93%;
		// height: 300rpx;
		margin-left: 26rpx;
		border-radius: 20rpx;
		margin-top: 20rpx;
		padding-bottom: 32rpx;
	}

	.view2 {
		background-color: #FFFFFF;
		width: 93%;
		// height: 100%;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;

		margin-left: 26rpx;
		margin-top: 20rpx;

	}



	.yaoqing_btn {
		// width: 80%;

		line-height: 80rpx;
		margin-top: 30rpx;
		height: 85rpx;
		color: #FFFFFF;
		background: #4C6CD3;
		margin-left: 32rpx;
		margin-right: 32rpx;

		background-size: 100%;
	}
</style>