<!-- components/alarm/AlarmIndicator.vue -->
<template>
	<!-- 小悬浮窗 -->
	<view v-if="alarmStore.isRunning && !isExpanded" class="alarm-mini" @click="expand">
		<view class="mini-ring" :style="{ background: getBgColor() }">
			<text class="mini-time">{{ alarmStore.formatTime }}</text>
		</view>
	</view>

	<!-- 展开弹窗 -->
	<view v-if="isExpanded" class="alarm-expanded" @click="closeExpand">
		<view class="expanded-content" @click.stop>
			<view class="expanded-header">
				<text class="expanded-title">{{ alarmStore.title }}</text>
				<up-icon name="close" size="24" color="#999" @click="closeExpand"></up-icon>
			</view>
			<view class="expanded-time" :style="{ color: getBgColor() }">{{ alarmStore.formatTime }}</view>
			<view class="expanded-progress">
				<view class="progress-bar">
					<view class="progress-fill" :style="{ width: alarmStore.progress + '%', background: getBgColor() }"></view>
				</view>
			</view>
			<up-button type="error" size="normal" text="结束闹钟" @click="stopAlarm"></up-button>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue'
	import {
		useAlarmStore
	} from '@/stores/alarm'

	const alarmStore = useAlarmStore()
	const isExpanded = ref(false)

	const getBgColor = () => {
		const colors = {
			night: '#667eea',
			focus: '#764ba2',
			nap: '#f5576c'
		}
		return colors[alarmStore.type] || '#667eea'
	}

	const expand = () => {
		isExpanded.value = true
	}

	const closeExpand = () => {
		isExpanded.value = false
	}

	const stopAlarm = () => {
		alarmStore.stopAlarm()
		closeExpand()
	}
</script>

<style scoped>
	/* 样式与之前相同，省略... */
	.alarm-mini {
		position: fixed;
		top: 200rpx;
		right: 30rpx;
		z-index: 999;
	}

	.mini-ring {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.2);
	}

	.mini-time {
		font-size: 24rpx;
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
		transition: width 1s linear;
	}
</style>