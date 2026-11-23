<template>
	<view>
		<view style="background: linear-gradient(-30deg,rgba(218,237,251,0.1) 0%,#D5E0F4 100%);">
			<view class="header">
				<view class="flex align-center userMsg" @click="routerGo('/pages/my/userinfo')" v-if="userLogin">
					<image :src="userAvater" mode="" class="toux"></image>
					<view class="margin-left-sm">
						<view class="text-bold">{{userName}}</view>
						<view class="f24 c99" v-if="userLogin">{{isVip == true?'已开通会员':'未开通会员'}}</view>
					</view>
				</view>
				<view class="flex align-center userMsg" @click="routerGo('/pages/public/login')" v-else>
					<image src="/static/logo.png" mode="" class="toux"></image>
					<view class="margin-left-sm">
						<view class="text-bold">登录</view>
					</view>
				</view>
				<view class="flex align-center justify-between margin-top-lg">
					<view class="text-center">
						<view class="f38 ">{{dlNum}}<text class="f24">天</text></view>
						<view class="f24 c99">累计登录</view>
					</view>
					<view class="text-center">
						<view class="f38 ">{{ formatDurationStat(mxNum).value }}<text
								class="f24">{{ formatDurationStat(mxNum).unit }}</text></view>
						<view class="f24 c99">累计冥想</view>
					</view>
					<view class="text-center" @click="routerGo('/my/tongji/myLike')">
						<view class="f38 ">{{xhNum}}<text class="f24">个</text></view>
						<view class="f24 c99">我的喜欢</view>
					</view>
				</view>
			</view>
			<view class="vipBox">
				<view class="cFF" v-if="!isVip">加入会员 权益升级 多重好礼送给你</view>
				<view v-if="isVip">
					<view class="cFF">正在享受会员多重权限</view>
					<view class="cFF " style="font-size: 26rpx;margin-top: 8rpx;">会员到期时间：{{userInfo.vipEndTime.slice(0,10)}}
					</view>
				</view>

				<view class="openVip" v-if="!isVip" @click="routerGo('/my/vip/vip')">开通会员</view>
				<view class="openVip" v-if="isVip" @click="routerGo('/my/vip/vip')">查看权益</view>
			</view>
			<view class="userSetting">
				<view class="jr_box padding-bottom">
					<view class="flex align-center justify-between padding" @click="routerGo('/my/toadyDataTj/index')">
						<view class="">今日数据</view>
						<up-icon name="arrow-right" color="#999999" size="12"></up-icon>
					</view>
					<view class="flex align-center justify-between ">
						<view class="jr_item margin-left" style="background-color: #ECF2FE;width: 28%;">
							<view class="c428 f26 text-h">今日冥想</view>
							<view class="c428 f30 text-h">{{ formatDurationStat(toDayMxNum).value }}<text
									style="color: #A3C3FF;font-size: 24rpx;">{{ formatDurationStat(toDayMxNum).unit }}</text>
							</view>
						</view>
						<view class="jr_item" style="background-color: #FEF8EC;width: 28%;margin: 0 14rpx;">
							<view class=" f26 text-h" style="color: #FDB23D;">今日睡眠</view>
							<view class=" f30 text-h" style="color: #FDB23D;">{{ formatDurationStat(toDaySmNum).value }}<text
									style="color: #FFE6B5;font-size: 24rpx;">{{ formatDurationStat(toDaySmNum).unit }}</text></view>
						</view>
						<view class="jr_item margin-right" style="background-color: #EAFFF4;width: 28%">
							<view class=" f26 text-h" style="color: #6DCFA0;">今日专注</view>
							<view class=" f30 text-h" style="color: #6DCFA0;">{{ formatDurationStat(toDayZzNum).value }}<text
									style="color: #A4F1C7;font-size: 24rpx;">{{ formatDurationStat(toDayZzNum).unit }}</text></view>
						</view>
					</view>
				</view>

				<view class="jr_box padding-bottom" style="margin-top: 30rpx;">
					<view class="flex align-center justify-between padding">
						<view class="">其他服务</view>
					</view>
					<view class="flex align-center justify-between padding-lr margin-bottom-xl" style="position: relative;"
						@click="routerGo('/my/msg/index')">
						<view class="flex align-center">
							<image src="/static/image/my/xx.png" mode="" style="width: 40rpx;height: 40rpx;margin-right: 20rpx;">
							</image>
							<view class="f26">消息提醒</view>
						</view>
						<up-icon name="arrow-right" color="#333" size="12"></up-icon>
						<up-badge bgColor="#f56c6c" max="99" :value="messageCount" v-if="messageCount > 0"
							style="position: absolute;right: 90rpx;"></up-badge>
					</view>
					<view class="flex align-center justify-between padding-lr margin-bottom-xl"
						@click="routerGo('/my/tongji/zj_bofang')">
						<view class="flex align-center">
							<image src="/static/image/my/zj.png" mode="" style="width: 40rpx;height: 40rpx;margin-right: 20rpx;">
							</image>
							<view class="f26">最近播放</view>
						</view>
						<up-icon name="arrow-right" color="#333" size="12"></up-icon>
					</view>
					<view class="flex align-center justify-between padding-lr margin-bottom-xl"
						@click="routerGo('/pages/my/invitationUser')">
						<view class="flex align-center">
							<image src="/static/image/my/yq.png" mode="" style="width: 40rpx;height: 40rpx;margin-right: 20rpx;">
							</image>
							<view class="f26">邀请好友</view>
						</view>
						<up-icon name="arrow-right" color="#333" size="12"></up-icon>
					</view>
					<view class="flex align-center justify-between padding-lr margin-bottom-xl" v-if="userInfo.zhiRate > 0"
						@click="routerGo('/my/team/team')">
						<view class="flex align-center">
							<image src="/static/image/my/td.png" mode="" style="width: 40rpx;height: 40rpx;margin-right: 20rpx;">
							</image>
							<view class="f26">我的团队</view>
						</view>
						<up-icon name="arrow-right" color="#333" size="12"></up-icon>
					</view>
					<view class="flex align-center justify-between padding-lr margin-bottom-xl"
						@click="routerGo('/my/wallet/wallet')">
						<view class="flex align-center">
							<image src="/static/image/my/qb.png" mode="" style="width: 40rpx;height: 40rpx;margin-right: 20rpx;">
							</image>
							<view class="f26">我的钱包</view>
						</view>
						<up-icon name="arrow-right" color="#333" size="12"></up-icon>
					</view>
					<view class="flex align-center justify-between padding-lr margin-bottom-xl"
						@click="routerGo('/settingViews/about?type=187')">
						<view class="flex align-center">
							<image src="/static/image/my/gy.png" mode="" style="width: 40rpx;height: 40rpx;margin-right: 20rpx;">
							</image>
							<view class="f26">关于我们</view>
						</view>
						<up-icon name="arrow-right" color="#333" size="12"></up-icon>
					</view>
					<view class="flex align-center justify-between padding-lr " @click="routerGo('/settingViews/setting')">
						<view class="flex align-center">
							<image src="/static/image/my/sz.png" mode="" style="width: 40rpx;height: 40rpx;margin-right: 20rpx;">
							</image>
							<view class="f26">设置中心</view>
						</view>
						<up-icon name="arrow-right" color="#333" size="12"></up-icon>
					</view>
				</view>
				<view style="height: 50rpx;width: 100%;"></view>
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

	const userAvater = ref('/static/logo.png');
	const userLogin = ref(false);
	const isVip = ref(false);
	const userName = ref('游客');

	const dlNum = ref('0');
	const mxNum = ref('0');
	const xhNum = ref('0');

	const toDayMxNum = ref('0');
	const toDaySmNum = ref('0');
	const toDayZzNum = ref('0');

	const userInfo = reactive({});

	const {
		proxy
	} = getCurrentInstance();

	const bgColor = ref('transparent');

	const formatDurationStat = (value) => {
		const totalMinutes = Number(value) || 0

		if (totalMinutes >= 1440) {
			return {
				value: formatDurationValue(totalMinutes / 1440),
				unit: '天'
			}
		}

		if (totalMinutes >= 60) {
			return {
				value: formatDurationValue(totalMinutes / 60),
				unit: '小时'
			}
		}

		return {
			value: formatDurationValue(totalMinutes),
			unit: '分钟'
		}
	}

	const formatDurationValue = (value) => {
		const roundedValue = Math.round(value * 10) / 10
		return Number.isInteger(roundedValue) ? String(roundedValue) : roundedValue.toFixed(1)
	}

	// 获取我的数据统计
	const getMyData = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/myHomePage/selectMyCenter', data).then(res => {
			if (res.code == 0) {
				dlNum.value = res.data.totalLoginDays ? res.data.totalLoginDays : 0;
				mxNum.value = res.data.allMeditation ? res.data.allMeditation : 0;
				xhNum.value = res.data.courseLikeCount ? res.data.courseLikeCount : 0;
			}
		})

		proxy.$http.getT('/app/user/selectInfoByUserId', data).then(res => {
			if (res.code == 0) {
				userAvater.value = res.data.avatar ? res.data.avatar : '/static/logo.png'
				userName.value = res.data.userName ? res.data.userName : '用户'
				userLogin.value = true
				Object.assign(userInfo, res.data)
				if (res.data.isVip && res.data.isVip == 1) {
					isVip.value = true;
				} else {
					isVip.value = false;
				}
			}
		})
		// 获取当前时间
		var now = new Date();
		// 获取年份
		var year = now.getFullYear();
		// 获取月份，需要注意月份是从0开始计算的，所以需要+1
		var month = now.getMonth() + 1;
		if (month < 10) {
			month = '0' + month;
		}
		// 获取日期
		var date = now.getDate();
		if (date < 10) {
			date = '0' + date;
		}

		let startTime = year + '-' + month + '-' + date

		let datas = {
			userId: uni.getStorageSync('userId'),
			startTime: startTime
		}
		proxy.$http.getT('/app/myHomePage/selectMyTodayData', datas).then(res => {
			if (res.code == 0) {
				toDaySmNum.value = res.data.todaySleep ? res.data.todaySleep : 0;
				toDayMxNum.value = res.data.todayMeditation ? res.data.todayMeditation : 0;
				toDayZzNum.value = res.data.todayFocus ? res.data.todayFocus : 0;
			}
		})
	}

	const routerGo = (url) => {
		// uni.navigateTo({
		// 	url
		// })
		// uni.navigateTo({
		// 	url: '/pages/public/login'
		// })
		// return
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url
			})
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
			isVip.value = false;
			dlNum.value = 0;
			mxNum.value = 0;
			xhNum.value = 0;
			userAvater.value = '/static/logo.png'
			userName.value = '游客'
		}
	};
	const messageCount = ref(0);
	onLoad(option => {});
	onShow(() => {
		let userId = uni.getStorageSync('userId');
		if (userId) {
			getMyData()
			messageCount.value = uni.getStorageSync('messageCount')
		} else {
			userName.value = ''
			userLogin.value = false
			userAvater.value = '/static/logo.png';
			dlNum.value = 0;
			mxNum.value = 0;
			xhNum.value = 0;
		}
	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	.jr_item {
		border-radius: 16rpx;
		padding: 20rpx;
	}

	.jr_box {
		width: calc(100% - 60rpx);
		background-color: #FFFFFF;
		border-radius: 24rpx;
		margin: auto;
	}

	.userSetting {
		// background-color: #F2F4FF;
		background-color: #F2F5FF;
		width: 100%;
		// padding: 30rpx;
		border-radius: 24rpx 30rpx 0rpx 0rpx;
		// position: relative;
		padding-top: 90rpx;
	}

	.vipBox {
		width: calc(100% - 120rpx);
		margin: auto;
		background: linear-gradient(90deg, #272A33 0%, #545253 100%);
		border-radius: 24rpx;
		font-size: 26rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;
		margin-top: -52rpx;
		position: absolute;
		left: 30rpx;
	}

	.openVip {
		background: linear-gradient(122deg, #EFDCB5, #DDB27D);
		color: #32343A;
		width: 138rpx;
		height: 52rpx;
		text-align: center;
		line-height: 52rpx;
		text-align: center;
		border-radius: 26rpx;
	}

	.header {
		height: 325rpx;
		width: calc(100% - 80rpx);
		margin: auto;
		padding-top: 36rpx;
	}

	.userMsg {
		margin-top: 20rpx;
	}

	.toux {
		width: 96rpx;
		height: 96rpx;
		border-radius: 50%;
	}
</style>