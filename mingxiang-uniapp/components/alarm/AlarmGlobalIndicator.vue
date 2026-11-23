<template>
	<!-- 全局闹钟指示器页面 -->
	<!-- 多个闹钟悬浮窗 -->
	<view v-if="runningAlarms.length > 0" class="alarm-container">
		<view v-for="alarm in runningAlarms" :key="alarm.type" class="alarm-mini" :class="alarm.type"
			@click="expandAlarm(alarm.type)">
			<text class="mini-title">{{ alarm.title }}</text>
			<text class="mini-time">{{ formatTime(alarm) }}</text>
		</view>
	</view>

	<!-- 展开详情 -->
	<view v-if="expandedType" class="alarm-expanded" @click="closeExpand">
		<view class="expanded-content" @click.stop>
			<view class="expanded-header">
				<text class="expanded-title">{{ expandedAlarm.title }}</text>
				<up-icon name="close" size="24" color="#999" @click="closeExpand"></up-icon>
			</view>
			<view class="expanded-time">{{ alarmStore.getFormatTime(expandedType) }}</view>
			<view class="progress-bar">
				<view class="progress-fill" :style="{ width: alarmStore.getProgress(expandedType) + '%' }"></view>
			</view>
			<up-button type="error" size="normal" text="结束闹钟" @click="stopAlarm(expandedType)"></up-button>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		computed
	} from 'vue'
	import {
		useAlarmStore
	} from '@/stores/alarm'

	const alarmStore = useAlarmStore()
	const expandedType = ref('')

	const runningAlarms = computed(() => alarmStore.runningAlarms)

	const formatTime = (alarm) => {
		const seconds = alarm.remainingTime
		const m = Math.floor(seconds / 60)
		const s = seconds % 60
		return `${m}:${s.toString().padStart(2, '0')}`
	}

	const expandedAlarm = computed(() => {
		if (!expandedType.value) return {}
		return alarmStore.alarms[expandedType.value]
	})

	const expandAlarm = (type) => {
		expandedType.value = type
	}

	const closeExpand = () => {
		expandedType.value = ''
	}

	const stopAlarm = (type) => {
		alarmStore.stopAlarm(type)
		closeExpand()
	}
</script>

<style scoped>
	.alarm-container {
		position: fixed;
		top: 200rpx;
		right: 30rpx;
		z-index: 999;
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.alarm-mini {
		padding: 20rpx 30rpx;
		border-radius: 16rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.2);
	}

	.alarm-mini.night {
		background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
	}

	.alarm-mini.focus {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	}

	.alarm-mini.nap {
		background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
	}

	.mini-title {
		font-size: 22rpx;
		color: rgba(255, 255, 255, 0.8);
		margin-bottom: 8rpx;
	}

	.mini-time {
		font-size: 28rpx;
		color: #fff;
		font-weight: bold;
	}

	.alarm-expanded {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: 9999;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.expanded-content {
		width: 600rpx;
		background: #fff;
		border-radius: 30rpx;
		padding: 40rpx;
	}

	.expanded-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
	}

	.expanded-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.expanded-time {
		font-size: 80rpx;
		font-weight: 200;
		text-align: center;
		margin-bottom: 30rpx;
	}

	.progress-bar {
		height: 12rpx;
		background: #f0f0f0;
		border-radius: 6rpx;
		overflow: hidden;
		margin-bottom: 30rpx;
	}

	.progress-fill {
		height: 100%;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		transition: width 1s linear;
	}
</style>