<template>
	<view class="">
		<up-navbar title="" :autoBack="true" :bgColor="bgColor">
			<template #center>
				<view class="u-nav-slot">
					<view :class="navIndex == 0?'u-nav-item_aic':'u-nav-item'" @click="changeNavIndex(0)">今日</view>
					<view :class="navIndex == 1?'u-nav-item_aic':'u-nav-item'" @click="changeNavIndex(1)">全部</view>
				</view>
			</template>
		</up-navbar>
		<view class="jr_box padding-bottom">
			<!-- 今日 -->
			<template v-if="navIndex == 0">
				<view class="flex align-center padding">
					<view class="f36 margin-right-sm">{{dqYearAndMonth}}</view>
					<!-- <up-icon name="arrow-right" color="#999999" size="12"></up-icon> -->
				</view>
				<view class="flex align-center justify-between">
					<view class=" margin-left" style="width: 28%;">
						<view class="c428 f26 text-h"><text class="text-bold">·</text>当月累计冥想</view>
						<view class="c33 f30 text-h" style="margin-left: -20rpx;">
							&nbsp;&nbsp;&nbsp;&nbsp;{{meditationMonthCount}}<text style="color: #999;font-size: 24rpx;">次</text>
						</view>
					</view>
					<view class="" style="width: 28%;margin: 0 14rpx;">
						<view class=" f26 text-h" style="color: #FDB23D;"><text class="text-bold">·</text>当月累计睡眠</view>
						<view class="c33 f30 text-h" style="margin-left: -20rpx;">&nbsp;&nbsp;&nbsp;&nbsp;{{sleepMonthCount}}<text
								style="color: #999;font-size: 24rpx;">次</text>
						</view>
					</view>
					<view class=" margin-right" style="width: 28%">
						<view class=" f26 text-h" style="color: #6DCFA0;"><text class="text-bold">·</text>当月累计专注</view>
						<view class="c33 f30 text-h" style="margin-left: -20rpx;">&nbsp;&nbsp;&nbsp;&nbsp;{{focusMonthCount}}<text
								style="color: #999;font-size: 24rpx;">次</text>
						</view>
					</view>
				</view>
				<view style="border-top: 1px solid #F5F5F5;margin: 50rpx 30rpx;"></view>
				<!-- 当前周 -->
				<view class="margin-lr-sm flex align-center justify-between margin-bottom-lg">
					<view v-for="item,index in weekDates" :key="index"
						:class="(!datesIndex?item.isToday == true:datesIndex == index ) ?'weekItem_':'weekItem'"
						@click="changeDay(item,index)">
						<view class="f34">{{item.day}}</view>
						<view class="f22 text_week">{{item.weekDay}}</view>
					</view>
				</view>
				<view class="f32 margin-right-sm margin-left margin-bottom-sm">今日数据</view>
				<view class="flex align-center justify-between ">
					<view class="jr_item margin-left" style="background-color: #ECF2FE;width: 28%;">
						<view class="c428 f26 text-h">今日冥想</view>
						<view class="c428 f30 text-h">{{toDayMxNum}}<text style="color: #A3C3FF;font-size: 24rpx;">分钟</text></view>
					</view>
					<view class="jr_item" style="background-color: #FEF8EC;width: 28%;margin: 0 14rpx;">
						<view class=" f26 text-h" style="color: #FDB23D;">今日睡眠</view>
						<view class=" f30 text-h" style="color: #FDB23D;">{{toDaySmNum}}<text
								style="color: #FFE6B5;font-size: 24rpx;">分钟</text>
						</view>
					</view>
					<view class="jr_item margin-right" style="background-color: #EAFFF4;width: 28%">
						<view class=" f26 text-h" style="color: #6DCFA0;">今日专注</view>
						<view class=" f30 text-h" style="color: #6DCFA0;">{{toDayZzNum}}<text
								style="color: #A4F1C7;font-size: 24rpx;">分钟</text>
						</view>
					</view>
				</view>
			</template>
			<!-- 全部 -->
			<template v-if="navIndex == 1">
				<view class="text-center" style="padding: 70rpx 0;">
					<view class="c33 f68 text-h">{{dlNum}}<text style="font-size: 32rpx;">天</text></view>
					<view class="f32 text-h c99">累计登录</view>
				</view>
				<view class="flex align-center justify-between">
					<view class=" margin-left" style="width: 28%;">
						<view class="c428 f26 text-h"><text class="text-bold">·</text>累计冥想</view>
						<view class="c33 f30 text-h">&nbsp;&nbsp;&nbsp;&nbsp;{{allMeditationMonthCount}}<text
								style="color: #999;font-size: 24rpx;">天</text>
						</view>
					</view>
					<view class="" style="width: 28%;margin: 0 14rpx;">
						<view class=" f26 text-h" style="color: #FDB23D;"><text class="text-bold">·</text>累计睡眠</view>
						<view class="c33 f30 text-h">&nbsp;&nbsp;&nbsp;&nbsp;{{allSleepMonthCount}}<text
								style="color: #999;font-size: 24rpx;">天</text>
						</view>
					</view>
					<view class=" margin-right" style="width: 28%">
						<view class=" f26 text-h" style="color: #6DCFA0;"><text class="text-bold">·</text>累计专注</view>
						<view class="c33 f30 text-h">&nbsp;&nbsp;&nbsp;&nbsp;{{allFocusMonthCount}}<text
								style="color: #999;font-size: 24rpx;">天</text>
						</view>
					</view>
				</view>
			</template>
		</view>
		<view class="f32 margin-tb margin-left">分类统计</view>
		<!-- 冥想统计 -->
		<view class="fl_box">
			<view class="flex align-center">
				<image src="/my/static/tj/mx.png" mode="" class="fl_img"></image>
				<view class="text-h">冥想统计</view>
			</view>
			<view class="flex align-center justify-between margin-top-lg">
				<view class="text-center">
					<view class="c33 f38 text-h">{{meditationAllCount.meditationCount}}<text
							style="color: #999;font-size: 24rpx;">次</text></view>
					<view class="f26 c99">累计冥想次数</view>
				</view>
				<view class="text-center">
					<view class="c33 f38 text-h">{{meditationAllCount.meditationTime}}<text
							style="color: #999;font-size: 24rpx;">分钟</text></view>
					<view class="f26 c99">累计冥想分钟数</view>
				</view>
				<view class="text-center">
					<view class="c33 f38 text-h">{{meditationAllCount.meditationDay}}<text
							style="color: #999;font-size: 24rpx;">天</text></view>
					<view class="f26 c99">累计冥想天数</view>
				</view>
			</view>
		</view>
		<!-- 睡眠统计 -->
		<view class="fl_box">
			<view class="flex align-center">
				<image src="/my/static/tj/sm.png" mode="" class="fl_img"></image>
				<view class="text-h">睡眠统计</view>
			</view>
			<view class="flex align-center justify-between margin-top-lg">
				<view class="text-center">
					<view class="c33 f38 text-h">{{sleepAllCount.sleepCount}}<text style="color: #999;font-size: 24rpx;">次</text>
					</view>
					<view class="f26 c99">累计睡眠次数</view>
				</view>
				<view class="text-center">
					<view class="c33 f38 text-h">{{sleepAllCount.sleepTime}}<text style="color: #999;font-size: 24rpx;">分钟</text>
					</view>
					<view class="f26 c99">累计睡眠分钟数</view>
				</view>
				<view class="text-center">
					<view class="c33 f38 text-h">{{sleepAllCount.sleepDay}}<text style="color: #999;font-size: 24rpx;">天</text>
					</view>
					<view class="f26 c99">累计睡眠天数</view>
				</view>
			</view>
		</view>
		<!-- 专注统计 -->
		<view class="fl_box">
			<view class="flex align-center">
				<image src="/my/static/tj/zz.png" class="fl_img"></image>
				<view class="text-h">专注统计</view>
			</view>
			<view class="flex align-center justify-between margin-top-lg">
				<view class="text-center">
					<view class="c33 f38 text-h">{{focusAllCount.focusCount}}<text style="color: #999;font-size: 24rpx;">次</text>
					</view>
					<view class="f26 c99">累计专注次数</view>
				</view>
				<view class="text-center">
					<view class="c33 f38 text-h">{{focusAllCount.focusTime}}<text style="color: #999;font-size: 24rpx;">分钟</text>
					</view>
					<view class="f26 c99">累计专注分钟数</view>
				</view>
				<view class="text-center">
					<view class="c33 f38 text-h">{{focusAllCount.focusDay}}<text style="color: #999;font-size: 24rpx;">天</text>
					</view>
					<view class="f26 c99">累计专注天数</view>
				</view>
			</view>
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


	const bgColor = ref('transparent');
	const navIndex = ref(0);
	const currentDate = ref(new Date()); //当前时间
	const weekDates = reactive([]); //当前周的数组
	const dqYearAndMonth = ref(''); //当前年月

	const focusMonthCount = ref(0);
	const meditationMonthCount = ref(0);
	const sleepMonthCount = ref(0);
	const toDayMxNum = ref(0);
	const toDaySmNum = ref(0);
	const toDayZzNum = ref(0);
	const dlNum = ref(0);

	const allFocusMonthCount = ref(0);
	const allMeditationMonthCount = ref(0);
	const allSleepMonthCount = ref(0);

	const focusAllCount = ref({
		focusCount: 0,
		focusDay: 0,
		focusTime: 0
	});
	const meditationAllCount = ref({
		meditationCount: 0,
		meditationDay: 0,
		meditationTime: 0
	});
	const sleepAllCount = ref({
		sleepCount: 0,
		sleepDay: 0,
		sleepTime: 0
	});

	const changeNavIndex = (index) => {
		navIndex.value = index
		if (navIndex.value == 0) {
			selectMyTodayData()
		} else {
			selectMyAllData()
		}
	};
	// 获取今日我的数据
	const selectMyTodayData = () => {
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
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/myHomePage/selectMyTodayData', datas).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading()
			if (res.code == 0) {
				focusMonthCount.value = res.data.focusMonthCount ? res.data.focusMonthCount : 0;
				meditationMonthCount.value = res.data.meditationMonthCount ? res.data.meditationMonthCount : 0;
				sleepMonthCount.value = res.data.sleepMonthCount ? res.data.sleepMonthCount : 0;
				toDaySmNum.value = res.data.todaySleep ? res.data.todaySleep : 0;
				toDayMxNum.value = res.data.todayMeditation ? res.data.todayMeditation : 0;
				toDayZzNum.value = res.data.todayFocus ? res.data.todayFocus : 0;
				datesIndex.value = null
			}
		})

		proxy.$http.getT('/app/myHomePage/selectMyAllTypeData', datas).then(res => {
			if (res.code == 0) {
				focusAllCount.value = res.data.focusAllCount ? res.data.focusAllCount : 0
				meditationAllCount.value = res.data.meditationAllCount ? res.data.meditationAllCount : 0
				sleepAllCount.value = res.data.sleepAllCount ? res.data.sleepAllCount : 0
			}
		})
	};

	const datesIndex = ref(null);
	// 切换日期获取日期数据
	const changeDay = (item, index) => {
		datesIndex.value = index
		console.log(item, "dddddddddddd")
		let datas = {
			userId: uni.getStorageSync('userId'),
			startTime: item.date
		}
		proxy.$http.getT('/app/myHomePage/selectMyTodayData', datas).then(res => {
			if (res.code == 0) {
				focusMonthCount.value = res.data.focusMonthCount ? res.data.focusMonthCount : 0;
				meditationMonthCount.value = res.data.meditationMonthCount ? res.data.meditationMonthCount : 0;
				sleepMonthCount.value = res.data.sleepMonthCount ? res.data.sleepMonthCount : 0;
				toDaySmNum.value = res.data.todaySleep ? res.data.todaySleep : 0;
				toDayMxNum.value = res.data.todayMeditation ? res.data.todayMeditation : 0;
				toDayZzNum.value = res.data.todayFocus ? res.data.todayFocus : 0;
			}
		})
	};

	// 获取全部我的数据
	const selectMyAllData = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/myHomePage/selectMyAllTypeData', data).then(res => {
			if (res.code == 0) {
				focusAllCount.value = res.data.focusAllCount
				meditationAllCount.value = res.data.meditationAllCount
				sleepAllCount.value = res.data.sleepAllCount

				allFocusMonthCount.value = res.data.focusAllCount.focusDay ? res.data.focusAllCount.focusDay : 0;
				allMeditationMonthCount.value = res.data.meditationAllCount.meditationDay ? res.data.meditationAllCount
					.meditationDay :
					0;
				allSleepMonthCount.value = res.data.sleepAllCount.sleepDay ? res.data.sleepAllCount.sleepDay : 0;
			}
		})

		// proxy.$http.getT('/app/myHomePage/selectMyTodayData', data).then(res => {
		// 	if (res.code == 0) {
		// 		allFocusMonthCount.value = res.data.focusMonthCount ? res.data.focusMonthCount : 0;
		// 		allMeditationMonthCount.value = res.data.meditationMonthCount ? res.data.meditationMonthCount :
		// 			0;
		// 		allSleepMonthCount.value = res.data.sleepMonthCount ? res.data.sleepMonthCount : 0;
		// 	}
		// })

		proxy.$http.getT('/app/myHomePage/selectMyCenter', data).then(res => {
			if (res.code == 0) {
				dlNum.value = res.data.totalLoginDays ? res.data.totalLoginDays : 0;
			}
		})
	};

	// 获取星期几的中文名称
	const getWeekDayName = (dayIndex) => {
		const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
		return weekDays[dayIndex];
	};

	// 格式化日期为 YYYY-MM-DD (纯函数，不产生副作用)
	const formatDate = (date) => {
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${year}-${month}-${day}`;
	};

	// 格式化显示用的年月 (例如：2026年03月)
	const formatYearMonth = (date) => {
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, '0');
		return `${year}年${month}月`;
	};

	// 获取本周的日期数组
	const getWeekDates = () => {
		// 确保基于当前选中的日期计算，如果没有则默认为今天
		const baseDate = currentDate.value ? new Date(currentDate.value) : new Date();

		// 获取今天是周几 (0-6, 0是周日)
		const currentDay = baseDate.getDay();

		// 计算本周一的日期
		// 逻辑：如果是周日(0)，减去6天；如果是周一(1)，减去0天；如果是周二(2)，减去1天...
		// 公式：currentDay === 0 ? 6 : currentDay - 1
		const diffToMonday = currentDay === 0 ? 6 : currentDay - 1;

		const monday = new Date(baseDate);
		monday.setDate(baseDate.getDate() - diffToMonday);
		// 重置时间部分，避免时分秒干扰日期计算
		monday.setHours(0, 0, 0, 0);

		const week = [];
		const todayStr = formatDate(new Date()); // 获取今天的 YYYY-MM-DD 用于判断

		// 更新顶部的年月显示 (只取周一的年月即可，因为一周通常在同一个月内，若跨月可单独处理)
		// 这里假设显示的是当前选中日期所在的月份
		dqYearAndMonth.value = formatYearMonth(baseDate);

		for (let i = 0; i < 7; i++) {
			const date = new Date(monday);
			date.setDate(monday.getDate() + i);

			const dateStr = formatDate(date); // YYYY-MM-DD
			const dayOfMonth = date.getDate(); // 几号

			week.push({
				day: dayOfMonth, // 用于显示大号数字 (如 19)
				weekDay: getWeekDayName(date.getDay()), // 用于显示 (如 周四)
				date: dateStr, // 完整日期字符串 (2026-03-19)
				isToday: dateStr === todayStr, // 是否今天
				// 新增：你需要的年月日格式，方便传参或排序
				ymd: {
					year: date.getFullYear(),
					month: date.getMonth() + 1,
					day: dayOfMonth
				},
				// 新增：原始 Date 对象，方便后续复杂操作
				rawDate: date
			});
		}

		// 更新响应式数组
		// 方式1: 直接替换 (推荐，更简洁)
		// weekDates.value = week;

		// 方式2: 如果你必须用 splice (为了保持引用不变)
		weekDates.splice(0, weekDates.length, ...week);

		// console.log('本周日期数据:', weekDates.value);
	};
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};


	onLoad(option => {
		getWeekDates();
		selectMyTodayData();
	});
	onShow(() => {
		datesIndex.value = null
	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background: #EAEEFE;
		// background-color: red;
	}

	.fl_box {
		width: calc(100% - 110rpx);
		margin: auto;
		border-radius: 24rpx;
		padding: 25rpx;
		background-color: #FFFFFF;
		margin-bottom: 30rpx;
	}

	.fl_img {
		width: 52rpx;
		height: 52rpx;
		margin-right: 20rpx;
	}

	.weekItem_ {
		width: calc(100%/7.5);
		text-align: center;
		background-color: #718ADD;
		border-radius: 50%;
		color: #FFFFFF !important;
		height: 90rpx;
	}

	.weekItem {
		width: calc(100%/7);
		text-align: center;

		.text_week {
			color: #999999;
		}
	}

	.jr_box {
		width: calc(100% - 60rpx);
		background-color: #FFFFFF;
		border-radius: 24rpx;
		margin: auto;
		margin-top: 105rpx;
	}

	.jr_item {
		border-radius: 16rpx;
		padding: 20rpx;
	}

	.u-nav-slot {
		background-color: rgba(41, 41, 41, 0.2);
		width: 280rpx;
		height: 65rpx;
		border-radius: 32rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.u-nav-item_aic {
		width: 50%;
		background-color: #292929;
		color: #FFFFFF;
		font-weight: bold;
		border-radius: 32rpx;
		height: 65rpx;
		line-height: 65rpx;
		text-align: center;
	}

	.u-nav-item {
		width: 50%;
		background-color: transparent;
		color: #666666;
		border-radius: 32rpx;
		height: 65rpx;
		line-height: 65rpx;
		text-align: center;
	}
</style>