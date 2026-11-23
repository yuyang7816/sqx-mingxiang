<template>
	<!--  -->
	<view class="alarm-global-modal" v-if="showModal">
		<!-- <view class="modal-overlay" @click="handleOverlayClick"></view> -->
		<view class="modal-content">
			<view class="modal-icon" :class="alarmType">
				<up-icon name="clock-fill" size="80" color="#fff"></up-icon>
			</view>
			<text class="modal-title">休息时间到了</text>
			<!-- <text class="modal-desc">{{ alarmTitle }}结束</text>
			<text class="modal-sub">{{ finishText }}</text> -->

			<view class="btn-group">
				<up-button type="error" size="large" text="结束闹钟" @click="stopAlarm"
					style="width: 100%;height: 80rpx;"></up-button>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		computed,
		getCurrentInstance
	} from 'vue'
	import {
		useAlarmStore
	} from '@/stores/alarm'
	const {
		proxy
	} = getCurrentInstance();
	const alarmStore = useAlarmStore()

	// 直接读取 store 的 getter，确保响应式
	const showModal = computed(() => {
		const hasAlarm = alarmStore.hasFinishedAlarm
		console.log('[AlarmGlobalModal] showModal computed:', hasAlarm)
		return hasAlarm
	})

	const alarmType = computed(() => alarmStore.finishedAlarmType || '')
	const alarmTitle = computed(() => alarmStore.finishedAlarmTitle || '')

	const finishTexts = {
		night: '祝您有个美好的一天',
		focus: '休息一下吧',
		nap: '精神焕发'
	}

	const finishText = computed(() => finishTexts[alarmType.value] || '时间到了')

	const handleOverlayClick = () => {
		// #ifdef APP-PLUS
		plus.device.vibrate(50)
		// #endif
	}

	const closeModal = () => {
		console.log('[AlarmGlobalModal] 点击我知道了')
		alarmStore.closeFinishedModal()
	}

	const stopAlarm = async () => {
		console.log('[AlarmGlobalModal] 点击结束闹钟', alarmStore.alarms)
		const currentType = alarmStore.finishedAlarmType || alarmStore.ringingType

		if (currentType === 'night') {
			await proxy.$http.postJson('/app/courseLearn/endAlarm', {
				userId: uni.getStorageSync('userId'),
				id: uni.getStorageSync('nightId')
			}).then(res => {
				uni.removeStorageSync('nightId')
				if (res.code == 0) {
					alarmStore.stopAlarm(currentType)
					console.log('手动结束夜间睡眠')
				} else {
					alarmStore.stopAlarm(currentType)
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			})
		} else if (currentType === 'nap') {
			await proxy.$http.postJson('/app/courseLearn/endNap', {
				userId: uni.getStorageSync('userId'),
				id: uni.getStorageSync('rijianId')
			}).then(res => {
				uni.removeStorageSync('rijianId')
				if (res.code == 0) {
					alarmStore.stopAlarm(currentType)
					console.log('手动结束日间小憩')
				} else {
					alarmStore.stopAlarm(currentType)
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			})
		} else if (currentType === 'focus') {
			await proxy.$http.postJson('/app/courseLearn/endConcentrate', {
				userId: uni.getStorageSync('userId'),
				id: uni.getStorageSync('focusId')
			}).then(res => {
				uni.removeStorageSync('focusId')
				if (res.code == 0) {
					alarmStore.stopAlarm(currentType)
					console.log('手动结束专注时钟')
				} else {
					alarmStore.stopAlarm(currentType)
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			})
		}
		alarmStore.stopFinishedAlarm()
		// #ifdef APP-PLUS
		plus.runtime.setBadgeNumber(0)
		// #endif
	}
</script>

<style scoped>
	.alarm-global-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: 99999;
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
		width: 500rpx;
		background: #fff;
		border-radius: 30rpx;
		padding: 60rpx 40rpx;
		text-align: center;
		z-index: 1;
		animation: slideUp 0.3s ease;
		box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.3);
	}

	@keyframes slideUp {
		from {
			transform: translateY(100rpx);
			opacity: 0;
		}

		to {
			transform: translateY(0);
			opacity: 1;
		}
	}

	.modal-icon {
		width: 140rpx;
		height: 140rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 0 auto 40rpx;
		animation: pulse 1.5s infinite;
	}

	@keyframes pulse {

		0%,
		100% {
			transform: scale(1);
		}

		50% {
			transform: scale(1.05);
		}
	}

	.modal-icon {
		background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
	}

	/* 
	.modal-icon.focus {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	}

	.modal-icon.nap {
		background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
	} */

	.modal-title {
		display: block;
		font-size: 34rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 20rpx;
	}

	.modal-desc {
		display: block;
		font-size: 32rpx;
		color: #666;
		margin-bottom: 10rpx;
	}

	.modal-sub {
		display: block;
		font-size: 28rpx;
		color: #999;
		margin-bottom: 60rpx;
	}

	.btn-group {
		display: block;
		margin-top: 50rpx;
	}
</style>