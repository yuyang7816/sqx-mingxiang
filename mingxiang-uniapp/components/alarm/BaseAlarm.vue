<!-- components/alarm/BaseAlarm.vue -->
<template>
	<view class="alarm-container">
		<view class="alarm-bg" :class="alarmType">
			<view class="bg-circle"></view>
		</view>

		<view class="alarm-header">
			<text class="alarm-title">{{ title }}</text>
			<text class="alarm-subtitle">{{ subtitle }}</text>
		</view>

		<view class="time-display">
			<text class="time-text">{{ formatTime(remainingTime) }}</text>
		</view>

		<!-- 设置区域 -->
		<view v-if="!isRunning && !isFinished" class="setup-section">
			<view class="time-selector">
				<picker mode="selector" :range="timeOptions" :value="selectedIndex" @change="onTimeChange">
					<view class="picker-value">
						{{ timeOptions[selectedIndex] }}
						<up-icon name="arrow-down" size="16" color="#666"></up-icon>
					</view>
				</picker>
			</view>

			<up-button type="primary" size="large" :text="startText" @click="startAlarm"></up-button>
		</view>

		<!-- 运行中 -->
		<view v-else-if="isRunning" class="running-section">
			<view class="progress-ring">
				<view class="progress-circle" :style="{ transform: `rotate(${progress}deg)` }"></view>
				<view class="progress-inner">
					<text class="status-text">进行中</text>
				</view>
			</view>

			<up-button type="error" size="large" text="结束" @click="stopAlarm"></up-button>
		</view>

		<!-- 结束提醒弹窗（播放音频，用户点击关闭） -->
		<view v-if="showFinishModal" class="finish-modal">
			<view class="modal-overlay" @click="closeFinishModal"></view>
			<view class="modal-content">
				<view class="modal-icon">
					<up-icon name="clock-fill" size="80" color="#fff"></up-icon>
				</view>
				<text class="modal-title">时间到了</text>
				<text class="modal-desc">{{ finishText }}</text>
				<up-button type="primary" size="large" text="我知道了" @click="closeFinishModal"></up-button>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		computed,
		onUnmounted
	} from 'vue'

	const props = defineProps({
		alarmType: {
			type: String,
			required: true
		},
		title: {
			type: String,
			required: true
		},
		subtitle: {
			type: String,
			default: ''
		},
		startText: {
			type: String,
			default: '开始'
		},
		finishText: {
			type: String,
			default: '闹钟时间已结束'
		},
		timeOptions: {
			type: Array,
			default: () => ['15分钟', '30分钟', '45分钟', '60分钟']
		},
		defaultIndex: {
			type: Number,
			default: 0
		},
		// 结束提醒音频（固定）
		alarmSound: {
			type: String,
			default: '/static/sounds/alarm-bell.mp3'
		},
		onStart: {
			type: Function,
			required: true
		},
		onEnd: {
			type: Function,
			required: true
		}
	})

	const isRunning = ref(false)
	const isFinished = ref(false)
	const showFinishModal = ref(false)
	const selectedIndex = ref(props.defaultIndex)
	const remainingTime = ref(0)
	const totalTime = ref(0)
	const timerId = ref(null)
	const alarmAudioContext = ref(null) // 结束提醒音频

	const progress = computed(() => {
		if (totalTime.value === 0) return 0
		const elapsed = totalTime.value - remainingTime.value
		return (elapsed / totalTime.value) * 360
	})

	const onTimeChange = (e) => {
		selectedIndex.value = e.detail.value
	}

	// 开始闹钟
	const startAlarm = async () => {
		const minutes = parseInt(props.timeOptions[selectedIndex.value])
		totalTime.value = minutes * 60
		remainingTime.value = totalTime.value

		const res = await props.onStart({
			type: props.alarmType,
			duration: minutes
		})

		if (res === false) return

		isRunning.value = true
		startCountdown()
	}

	// 倒计时
	const startCountdown = () => {
		timerId.value = setInterval(() => {
			remainingTime.value--

			if (remainingTime.value <= 0) {
				finishAlarm()
			}
		}, 1000)
	}

	// 结束闹钟（用户主动结束）
	const stopAlarm = async () => {
		await props.onEnd({
			type: props.alarmType,
			status: 'stopped',
			elapsed: totalTime.value - remainingTime.value
		})

		cleanup()
		isRunning.value = false
		isFinished.value = false
	}

	// 完成闹钟（倒计时结束）
	const finishAlarm = async () => {
		clearInterval(timerId.value)

		// 播放提醒音频（循环播放，音量较大）
		playAlarmSound()

		// 显示结束弹窗
		showFinishModal.value = true

		// 上报结束
		await props.onEnd({
			type: props.alarmType,
			status: 'completed',
			elapsed: totalTime.value
		})

		isRunning.value = false
		isFinished.value = true
	}

	// 播放闹钟提醒音
	const playAlarmSound = () => {
		alarmAudioContext.value = uni.createInnerAudioContext()
		alarmAudioContext.value.src = props.alarmSound
		alarmAudioContext.value.loop = true // 循环播放直到用户关闭
		alarmAudioContext.value.volume = 0.8 // 音量较大
		alarmAudioContext.value.play()
	}

	// 关闭结束弹窗
	const closeFinishModal = () => {
		showFinishModal.value = false

		// 停止提醒音频
		if (alarmAudioContext.value) {
			alarmAudioContext.value.stop()
			alarmAudioContext.value.destroy()
			alarmAudioContext.value = null
		}

		// 完全重置状态
		isFinished.value = false
		remainingTime.value = 0
		totalTime.value = 0
	}

	// 清理资源
	const cleanup = () => {
		if (timerId.value) {
			clearInterval(timerId.value)
			timerId.value = null
		}
		if (alarmAudioContext.value) {
			alarmAudioContext.value.destroy()
			alarmAudioContext.value = null
		}
	}

	const formatTime = (seconds) => {
		const h = Math.floor(seconds / 3600)
		const m = Math.floor((seconds % 3600) / 60)
		const s = seconds % 60

		if (h > 0) {
			return `${h}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
		}
		return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
	}

	onUnmounted(() => {
		cleanup()
	})
</script>

<style scoped>
	.alarm-container {
		min-height: 100vh;
		position: relative;
		overflow: hidden;
		padding: 60rpx 40rpx;
	}

	.alarm-bg {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: -1;
	}

	.alarm-bg.night {
		background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
	}

	.alarm-bg.focus {
		background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
	}

	.alarm-bg.nap {
		background: linear-gradient(180deg, #f093fb 0%, #f5576c 100%);
	}

	.bg-circle {
		position: absolute;
		width: 600rpx;
		height: 600rpx;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.05);
		top: 10%;
		left: 50%;
		transform: translateX(-50%);
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
		color: rgba(255, 255, 255, 0.7);
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
	}

	.setup-section {
		margin-top: 60rpx;
	}

	.time-selector {
		background: rgba(255, 255, 255, 0.1);
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		color: #fff;
	}

	.picker-value {
		display: flex;
		align-items: center;
		gap: 10rpx;
		font-size: 32rpx;
	}

	.running-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 60rpx;
	}

	.progress-ring {
		width: 400rpx;
		height: 400rpx;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.1);
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
	}

	.progress-circle {
		position: absolute;
		width: 100%;
		height: 100%;
		border-radius: 50%;
		border: 8rpx solid transparent;
		border-top-color: #fff;
		transition: transform 1s linear;
	}

	.progress-inner {
		text-align: center;
	}

	.status-text {
		font-size: 36rpx;
		color: #fff;
	}

	/* 结束提醒弹窗 */
	.finish-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: 9999;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.modal-overlay {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.7);
	}

	.modal-content {
		position: relative;
		width: 600rpx;
		background: #fff;
		border-radius: 30rpx;
		padding: 60rpx 40rpx;
		text-align: center;
		z-index: 1;
	}

	.modal-icon {
		width: 120rpx;
		height: 120rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 0 auto 40rpx;
	}

	.modal-title {
		display: block;
		font-size: 40rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 20rpx;
	}

	.modal-desc {
		display: block;
		font-size: 28rpx;
		color: #666;
		margin-bottom: 60rpx;
	}
</style>