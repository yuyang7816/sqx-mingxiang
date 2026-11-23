<template>
	<view class="container_back">

		<!-- 闹钟循环滚动 -->
		<view class="timePicker">
			<view class="circle-picker" v-if="!alarmState.isRunning">
				<TimePicker :defaultHour="8" :defaultMinute="30" @change="onTimeChange" />
			</view>


			<view class="progress-ring" v-else
				style="background-color: transparent;display: block;width: max-content;text-align: center;color: #FFFFFF;margin: auto;">
				<view class="f30 cFF">倒计时</view>
				<text class="remaining-text" style="font-size: 86rpx;">{{ alarmStore.getFormatTime(alarmType) }}</text>
			</view>
			<view class="yjTime" v-if="!alarmState.isRunning">预计睡眠：{{hourPicker}}小时{{minutePicker}}分钟</view>

			<view class="ks_box" :style="{paddingTop:!alarmState.isRunning?'100rpx':'300rpx'}">
				<image src="/subpackage/static/ks.png" mode="" class="ksimg" @click="startAlarm" v-if="!alarmState.isRunning">
				</image>
				<image src="/subpackage/static/js.png" mode="" class="ksimg" @click="stopAlarm" v-else>
				</image>
				<view class="ksText">{{!alarmState.isRunning?'开始睡眠':'结束睡眠'}}</view>
			</view>
		</view>


		<!-- 底部 -->
		<view class="fixedBox">
			<view class="flex align-center margin-lr-xl justify-between" v-if="!playerStore.isPlaying&&showMusic == false">
				<view class="">
					<image src="/subpackage/static/nz.png" mode="" class="ksimg"></image>
					<view class="f26 cFF text-center margin-top-sm">闹钟</view>
				</view>
				<view class="" @click="showMusic = true">
					<image src="/subpackage/static/yy.png" mode="" class="ksimg"></image>
					<view class="f26 cFF text-center margin-top-sm">音乐</view>
				</view>
			</view>
			<SubPagePlayer :playlist="musicList" :on-play-pause="handlePlayPause" :on-switch="handleSwitch"
				v-if="playerStore.isPlaying||showMusic == true" />
		</view>

		<!-- 设置定时弹窗 -->
		<!-- <up-popup :show="dsShow" @close="dsShow = false" round="12" :closeable="true">
			<view class="dsBox">
				<view class="dsBoxTitle">设置定时</view>
				<view class="margin-lr padding-bottom">
					<scroll-view :scroll-y="true" class="scrollView">
						<up-radio-group v-model="radiovalue1" placement="column" @change="groupChange" iconPlacement="right">
							<up-radio :customStyle="{marginBottom: '8px'}" v-for="(item, index) in radiolist1" :key="index"
								labelColor="#FFFFFF" :label="item.name" :name="item.name" @change="radioChange" activeColor="#FFFFFF"
								iconColor="#333333" inactiveColor="#131313">
							</up-radio>
						</up-radio-group>
					</scroll-view>
				</view>
			</view>
		</up-popup> -->

		<!-- 底部播放器（带播放列表） -->


		<AlarmGlobalModal />
	</view>
</template>
<script setup>
	import {
		ref,
		reactive,
		computed,
		onMounted,
		onUnmounted,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom,
		onReady
	} from '@dcloudio/uni-app';
	import SubPagePlayer from '@/components/player/SubPagePlayer.vue'
	import AlarmGlobalModal from '@/components/alarm/AlarmGlobalModal.vue'
	import TimePicker from '@/components/TimePicker.vue'
	const {
		proxy
	} = getCurrentInstance();
	import {
		useAlarmStore
	} from '@/stores/alarm'
	import {
		usePlayerStore
	} from '@/stores/player'

	// 时间选择
	const onTimeChange = ({
		hour,
		minute,
		time
	}) => {
		hourPicker.value = hour
		minutePicker.value = minute
		selectedValue.value = ((hour % 24 * 60) + minute % 60).toFixed(2)
		console.log('选中时间：', time, hour, minute) // 08:30
	};



	// 音乐播放器逻辑

	const showMusic = ref(false);

	onLoad((options) => {
		console.log(playerStore.isPlaying, "playerStore")
		getList()
	});

	const playerStore = usePlayerStore()
	const musicList = ref([])
	// 获取音乐列表
	const getList = () => {
		uni.showLoading({
			title: "加载中..."
		})
		proxy.$http.getT('/app/soundEffects/selectSoundEffectsList?pageNum=1&pageSize=50').then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				musicList.value = res.data.list.map(item => ({
					id: item.id,
					effectsName: item.effectsName,
					bgImg: item.bgImg,
					audioUrl: item.filePath,
					...item,
				}))
				// 如果当前正在播放音乐，检查是否在列表中
				if (playerStore.currentType === 'music' && playerStore.currentAudio) {
					const index = musicList.value.findIndex(
						item => item.id === playerStore.currentAudio.id
					)
					// 如果找到了，更新播放列表（保持当前播放）
					if (index !== -1) {
						playerStore.playlist = musicList.value
					}
				} else {
					playerStore.playlist = musicList.value
					playerStore.setPlaylist(musicList.value, 'music', 0)
				}
			}
		});
	};
	// 是否是当前播放的音乐
	const isCurrentMusic = (item) => {
		return playerStore.currentType === 'music' &&
			playerStore.currentAudio?.id === item.id
	}

	// 点击播放音乐
	const playMusic = (item, index) => {
		// 互斥：如果正在播放课程，会停止课程
		playerStore.setPlaylist(musicList.value, 'music', index)
	}

	const handlePlayPause = (action, item, type) => {}

	const handleSwitch = (action, item, type) => {}
	// 结束

	// 下面都是闹钟逻辑
	const alarmStore = useAlarmStore()
	const alarmType = 'night'
	const selectedIndex = ref(1) // 默认15分钟

	const alarmState = computed(() => alarmStore.alarms[alarmType])

	const displayTime = computed(() => {
		if (alarmState.value.isRunning) {
			return alarmStore.getFormatTime(alarmType)
		}
		const minutes = parseInt(selectedValue.value)
		return `${minutes}:00`
	})

	const selectedValue = ref('510.00');

	onMounted(() => {
		// 监听倒计时更新（用于刷新UI）
		uni.$on(`alarmTick_${alarmType}`, handleTick)
	})

	onUnmounted(() => {
		uni.$off(`alarmTick_${alarmType}`, handleTick)
	})

	const handleTick = () => {
		// 触发UI更新
	}

	const startAlarm = async () => {
		if ((hourPicker.value == 0 && minutePicker.value == 0) || selectedValue.value == 0) {
			uni.showToast({
				title: '请选择时长',
				icon: 'none'
			})
			return
		}
		const minutes = parseInt(selectedValue.value)
		await proxy.$http.postJson('/app/courseLearn/startAlarm', {
			userId: uni.getStorageSync('userId')
		}).then(res => {
			if (res.code == 0) {
				uni.setStorageSync('nightId', res.data)
				alarmStore.startAlarm(alarmType, {
					title: '夜间睡眠',
					duration: minutes
				})

				console.log(`开始${minutes}分钟夜间睡眠`)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	const stopAlarm = async () => {
		await proxy.$http.postJson('/app/courseLearn/endAlarm', {
			userId: uni.getStorageSync('userId'),
			id: uni.getStorageSync('nightId')
		}).then(res => {
			uni.removeStorageSync('nightId')
			if (res.code == 0) {
				hourPicker.value = 0
				minutePicker.value = 0
				selectedValue.value = 0
				alarmStore.stopAlarm(alarmType)
				console.log('手动结束夜间睡眠')
			} else {
				alarmStore.stopAlarm(alarmType)
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	// 赋值页面展示数据
	const hours = ref(8);
	const minutes = ref(30);
	const hourPicker = ref('08');
	const minutePicker = ref('30');
</script>

<style lang="scss" scoped>
	.circle-picker {
		// width: 280px;
		// height: 280px;
		// border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		overflow: hidden;
	}



	.container_back {
		background: linear-gradient(126deg, #234585, #151F45);
		min-height: 100vh;
	}

	.setup-section {
		padding-top: 170rpx;
	}

	page {
		// background: linear-gradient(-31deg, #3A4984 0%, #5373B0 100%);
	}

	.fixedBox {
		position: fixed;
		bottom: 50rpx;
		width: calc(100% - 130rpx);
		z-index: 99;
		left: 0;
		right: 0;
		margin: auto;
	}

	.scrollView {
		height: 700rpx;
	}

	.dsBoxTitle {
		font-size: 31rpx;
		font-weight: bold;
		color: #FFFFFF;
		padding: 30rpx 0 0 30rpx;
		margin-bottom: 70rpx;
	}

	.dsBox {
		background-color: rgba(19, 19, 19, 0.6);
		border-radius: 24rpx 24rpx 0rpx 0rpx;
	}

	.ks_box {
		// position: absolute;
		// bottom: 50rpx;
		// left: 0;
		// right: 0;
		width: 120rpx;
		height: 120rpx;
		margin: auto;
		padding-top: 100rpx;
	}

	.ksText {
		font-size: 26rpx;
		color: #9FA7C5;
		text-align: center;
		margin-top: 20rpx;
	}

	.ksimg {
		width: 120rpx;
		height: 120rpx;
	}

	.yjTime {
		color: #A2AECC;
		font-size: 26rpx;
		text-align: center;
		margin-top: 50rpx;
	}

	.timePicker {
		margin: 0 150rpx;
		// padding-top: 200rpx;
		// position: relative;
		position: fixed;
		top: 200rpx;
		z-index: 9;
		width: 60%;
	}


	:deep(.uni-picker-view-mask) {
		background-image: none;
	}

	:deep(.u-popup__content) {
		background: transparent !important;
	}

	:deep(.u-picker__view__column__item) {
		color: #7F93BD;
		font-size: 68rpx;
		font-weight: bold;
		font-family: Arial, Helvetica, sans-serif;
	}

	:deep(.u-picker__view__column__item--selected) {
		font-size: 68rpx;
		font-weight: bold;
		color: #FFFFFF;
		font-family: Arial, Helvetica, sans-serif;
	}

	:deep(.uni-picker-view-indicator:before) {
		border: none;
	}


	:deep(.uni-picker-view-indicator:after) {
		border: none;
	}





	.picker-view {
		width: 100%;
		height: 330rpx;
		background: transparent;
	}

	.picker-item {
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 68rpx;
		font-weight: bold;
		color: #7F93BD;
		font-family: Arial, Helvetica, sans-serif;
	}

	.picker-item_ {
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 68rpx;
		font-weight: bold;
		color: #F6F6F6;
		font-family: Arial, Helvetica, sans-serif;
	}

	.timePicker_d {
		position: fixed;
		font-size: 50rpx;
		font-weight: bold;
		color: #FFFFFF;
		top: 330rpx;
		left: 0;
		right: 0;
		text-align: center;
		margin: auto;
	}
</style>
