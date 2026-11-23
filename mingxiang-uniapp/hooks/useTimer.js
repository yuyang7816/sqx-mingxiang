// src/hooks/useTimer.js

import {
	ref,
	onUnmounted
} from 'vue'

export function useTimer() {
	// 剩余时间（秒）
	const remainingTime = ref(0)
	// 定时器ID
	let timer = null

	// 开始倒计时
	// seconds: 总秒数
	// onEndCallback: 倒计时结束时的回调函数
	const startTimer = (seconds, onEndCallback) => {
		// 如果已有定时器，先清除，防止冲突
		clearTimer()

		remainingTime.value = seconds

		timer = setInterval(() => {
			if (remainingTime.value > 0) {
				remainingTime.value--
			} else {
				// 时间到了
				clearTimer()
				if (onEndCallback) onEndCallback()
			}
		}, 1000)
	}

	// 清除定时器
	const clearTimer = () => {
		if (timer) {
			clearInterval(timer)
			timer = null
		}
		// 注意：这里不清空 remainingTime，以便UI上保留最后的时间显示，
		// 如果需要重置显示，可以加上 remainingTime.value = 0
	}

	// 组件卸载时自动清除定时器，防止内存泄漏
	onUnmounted(() => {
		clearTimer()
	})

	return {
		remainingTime, // 响应式的时间数据，可以在组件里用
		startTimer, // 开启方法
		clearTimer // 清除方法
	}
}