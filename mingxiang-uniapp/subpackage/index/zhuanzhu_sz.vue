<template>
	<view class="alarm-page nap">
		<view style="height: 120rpx;"></view>
		<view class="circle-picker" v-if="!alarmState.isRunning">
			<CustomPicker :list="columns" :defaultIndex="defaultIndex" unit="分钟" :itemHeight="80" @change="onTimeChange" />
		</view>
		<!-- <view class="time-display">
			<text class="time-text">{{ displayTime }}</text>
		</view> -->

		<view v-if="!alarmState.isRunning" class="setup-section">
			<image src="/subpackage/static/ks.png" mode="" class="ksimg" @click="startAlarm"></image>
			<view class="ksText">开始专注</view>
		</view>

		<view v-else class="running-section">
			<view class="progress-ring"
				style="background-color: transparent;display: block;width: max-content;text-align: center;">
				<view class="f30 cFF">倒计时</view>
				<text class="remaining-text" style="font-size: 86rpx;">{{ alarmStore.getFormatTime(alarmType) }}</text>
			</view>
			<view class="setup-section">
				<image src="/subpackage/static/js.png" mode="" class="ksimg" @click="stopAlarm">
				</image>
				<view class="ksText">结束专注</view>
			</view>

		</view>
		<SubPagePlayer :playlist="musicList" :on-play-pause="handlePlayPause" :on-switch="handleSwitch"
			v-if="playerStore.isPlaying||showMusic == true" />
		<!-- 底部-->
		<view class="footer_fixed" v-if="!playerStore.isPlaying&&showMusic == false">
			<view class="flex align-center justify-between">
				<view class="" @click="routerGo('/subpackage/index/sleep_yj')">
					<image src="/subpackage/static/nz.png" mode="" class="ksimg"></image>
					<view class="f26 cFF text-center margin-top-sm">闹钟</view>
				</view>
				<view class="" @click="showMusic = true">
					<image src="/subpackage/static/yy.png" mode="" class="ksimg"></image>
					<view class="f26 cFF text-center margin-top-sm">音乐</view>
				</view>
			</view>
		</view>
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
	} from 'vue'
	import {
		onLoad,
		onShow,
		onHide
	} from '@dcloudio/uni-app';
	import {
		useAlarmStore
	} from '@/stores/alarm'
	import AlarmGlobalModal from '@/components/alarm/AlarmGlobalModal.vue'
	import {
		usePlayerStore
	} from '@/stores/player'
	import SubPagePlayer from '@/components/player/SubPagePlayer.vue'
	import CustomPicker from '@/components/CustomPicker.vue'
	const {
		proxy
	} = getCurrentInstance();

	// 默认选中时间下标
	const defaultIndex = ref(0);
	// 时间选择
	const onTimeChange = ({
		index,
		value
	}) => {
		selectedIndex.value = index
		selectedValue.value = value
	};


	// 音乐播放器逻辑

	const showMusic = ref(false);

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

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




	// 下面都是闹钟逻辑
	const alarmStore = useAlarmStore()
	const alarmType = 'focus'
	const selectedIndex = ref(1) // 默认15分钟

	const alarmState = computed(() => alarmStore.alarms[alarmType])

	const displayTime = computed(() => {
		if (alarmState.value.isRunning) {
			return alarmStore.getFormatTime(alarmType)
		}
		const minutes = parseInt(selectedValue.value)
		return `${minutes}:00`
	})

	// 进度圈旋转角度
	const progressRotate = computed(() => {
		const progress = alarmStore.getProgress(alarmType)
		return (progress / 100) * 360
	})

	// 原始数据
	const rawOptions = ['20', '25', '30']

	// 重复 5 遍（奇数，确保中间有完整一组）
	const columns = reactive(Array(5).fill(null).flatMap(() => [...rawOptions]))

	const selectedValue = ref('20');


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
		const minutes = parseInt(selectedValue.value)
		await proxy.$http.postJson('/app/courseLearn/startConcentrate', {
			userId: uni.getStorageSync('userId')
		}).then(res => {
			if (res.code == 0) {
				uni.setStorageSync('focusId', res.data)
				alarmStore.startAlarm(alarmType, {
					title: '专注时钟',
					duration: minutes
				})

				console.log(`开始${minutes}分钟专注时钟`)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	const stopAlarm = async () => {
		await proxy.$http.postJson('/app/courseLearn/endConcentrate', {
			userId: uni.getStorageSync('userId'),
			id: uni.getStorageSync('focusId')
		}).then(res => {
			uni.removeStorageSync('focusId')
			if (res.code == 0) {
				alarmStore.stopAlarm(alarmType)
				console.log('手动结束专注时钟')
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}
</script>

<style lang="scss" scoped>
	.circle-picker {
		width: 500rpx;
		height: 500rpx;
		border-radius: 50%;
		border: 1px solid rgba(255, 255, 255, 0.3);
		margin: auto;
	}

	.running-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 60rpx;
		padding-top: 170rpx;
	}

	.progress-ring {
		background: rgba(255, 255, 255, 0.1);
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
		backdrop-filter: blur(10rpx);
	}

	.alarm-page {
		min-height: 100vh;
		padding: 0rpx 40rpx;
	}

	.footer_fixed {
		position: fixed;
		bottom: 95rpx;
		width: calc(100% - 200rpx);
		left: 0;
		right: 0;
		margin: auto;
	}

	.ksimg {
		width: 120rpx;
		height: 120rpx;
		margin: auto;
		display: block;
	}

	.ksText {
		font-size: 26rpx;
		color: #9FA7C5;
		text-align: center;
		margin-top: 20rpx;
	}

	.sz_picker_unit {
		position: absolute;
		right: 28%;
		font-size: 40rpx;
		top: 59%;
		font-size: 40rpx;
	}

	.sz_picker {
		position: relative;
		padding-top: 170rpx;
	}

	.timePicker {
		margin: 0 150rpx;
		// padding-top: 200rpx;
		// position: relative;
		position: fixed;
		top: 200rpx;
		z-index: 99;
		width: 60%;
	}



	:deep(.uni-picker-view-mask) {
		background-image: none;
	}

	:deep(.u-picker-wraper) {}

	:deep(.u-popup__content) {
		background: transparent !important;
	}

	:deep(.u-picker__view) {
		height: 264px !important;
	}

	:deep(.uni-picker-view-wrapper) {
		// height: 75%;
		// top: 50rpx;
		// // padding-top: 34rpx;
		border-radius: 50%;
		border: solid 2rpx #FFFFFF;
		width: 500rpx;
		height: 500rpx;
		margin: auto;
	}

	:deep(.u-picker__view__column__item) {
		color: #7F93BD;
		font-size: 86rpx;
		font-family: Arial, Helvetica, sans-serif;
	}

	:deep(.u-picker__view__column__item--selected) {
		font-size: 86rpx;
		font-weight: 100 !important;
		color: #FFFFFF;
		font-family: Arial, Helvetica, sans-serif;
	}

	:deep(.uni-picker-view-indicator:before) {
		border: none;
	}


	:deep(.uni-picker-view-indicator:after) {
		border: none;
	}

	/* 根据注释修改背景色：日间小憩用粉色渐变 */
	.nap {
		background: linear-gradient(126deg, #234585, #151F45);
	}

	.alarm-header {
		text-align: center;
		margin-bottom: 60rpx;
	}

	.alarm-title {
		display: block;
		font-size: 48rpx;
		font-weight: bold;
		color: #fff;
		margin-bottom: 20rpx;
	}

	.alarm-subtitle {
		font-size: 28rpx;
		color: rgba(255, 255, 255, 0.8);
	}

	.time-display {
		text-align: center;
		margin: 80rpx 0;
	}

	.time-text {
		font-size: 120rpx;
		font-weight: 200;
		color: #fff;
		font-variant-numeric: tabular-nums;
		text-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.2);
	}

	.setup-section {
		padding-top: 170rpx;
	}

	.time-selector {
		background: rgba(255, 255, 255, 0.2);
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		color: #fff;
		backdrop-filter: blur(10rpx);
	}

	.picker-value {
		display: flex;
		align-items: center;
		gap: 10rpx;
		font-size: 32rpx;
	}





	.progress-circle {
		position: absolute;
		width: 100%;
		height: 100%;
		border-radius: 50%;
		border: 8rpx solid transparent;
		border-top-color: #fff;
		border-right-color: rgba(255, 255, 255, 0.3);
		transition: transform 0.5s linear;
	}

	.progress-inner {
		text-align: center;
	}

	.status-text {
		font-size: 32rpx;
		color: rgba(255, 255, 255, 0.8);
		display: block;
		margin-bottom: 10rpx;
	}

	.remaining-text {
		font-size: 48rpx;
		color: #fff;
		font-weight: bold;
	}
</style>