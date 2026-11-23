// utils/alarmManager.js
class AlarmManager {
	constructor() {
		this.alarms = new Map()
		this.audioContext = null
		this.initAudio()
	}

	/**
	 * 初始化音频
	 * @returns {void}
	 */
	initAudio() {
		this.audioContext = uni.createInnerAudioContext()
	}

	/**
	 * 创建闹钟
	 * @param {string} id - 闹钟ID
	 * @param {Object} config - 配置项
	 * @param {string} config.type - 类型 'sleep'|'focus'|'nap'
	 * @param {number} config.duration - 时长（秒）
	 * @param {string} config.sound - 提示音地址
	 * @param {Function} config.onTick - 每秒回调
	 * @param {Function} config.onComplete - 完成回调
	 * @param {Function} config.onStop - 停止回调
	 * @param {Function} config.onPause - 暂停回调
	 * @param {Function} config.onResume - 恢复回调
	 * @returns {Object} 闹钟对象
	 */
	createAlarm(id, config) {
		const {
			type,
			duration,
			sound,
			onTick,
			onComplete,
			onStop,
			onPause,
			onResume
		} = config

		// 清除已存在的同ID闹钟
		this.clearAlarm(id)

		const alarm = {
			id,
			type,
			totalTime: duration,
			remainingTime: duration,
			sound,
			timer: null,
			isRunning: false,
			isPaused: false,
			startTime: null,
			pauseTime: null,
			elapsedTime: 0, // 已运行时间
			onTick,
			onComplete,
			onStop,
			onPause,
			onResume
		}

		this.alarms.set(id, alarm)
		return alarm
	}

	/**
	 * 开始闹钟
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	startAlarm(id) {
		const alarm = this.alarms.get(id)
		if (!alarm) {
			console.warn(`闹钟 ${id} 不存在`)
			return false
		}

		if (alarm.isRunning) {
			console.warn(`闹钟 ${id} 已在运行`)
			return false
		}

		alarm.isRunning = true
		alarm.isPaused = false
		alarm.startTime = Date.now()

		alarm.timer = setInterval(() => {
			alarm.remainingTime--
			alarm.elapsedTime++

			// 触发tick回调
			if (alarm.onTick) {
				try {
					alarm.onTick({
						remainingTime: alarm.remainingTime,
						totalTime: alarm.totalTime,
						elapsedTime: alarm.elapsedTime,
						progress: ((alarm.totalTime - alarm.remainingTime) / alarm.totalTime) * 100
					})
				} catch (error) {
					console.error('Tick回调错误:', error)
				}
			}

			// 时间到
			if (alarm.remainingTime <= 0) {
				this.completeAlarm(id)
			}
		}, 1000)

		return true
	}

	/**
	 * 暂停闹钟
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	pauseAlarm(id) {
		const alarm = this.alarms.get(id)
		if (!alarm) {
			console.warn(`闹钟 ${id} 不存在`)
			return false
		}

		if (!alarm.isRunning) {
			console.warn(`闹钟 ${id} 未在运行`)
			return false
		}

		clearInterval(alarm.timer)
		alarm.timer = null
		alarm.isRunning = false
		alarm.isPaused = true
		alarm.pauseTime = Date.now()

		if (alarm.onPause) {
			try {
				alarm.onPause({
					remainingTime: alarm.remainingTime,
					elapsedTime: alarm.elapsedTime
				})
			} catch (error) {
				console.error('Pause回调错误:', error)
			}
		}

		return true
	}

	/**
	 * 恢复闹钟
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	resumeAlarm(id) {
		const alarm = this.alarms.get(id)
		if (!alarm) {
			console.warn(`闹钟 ${id} 不存在`)
			return false
		}

		if (alarm.isRunning) {
			console.warn(`闹钟 ${id} 已在运行`)
			return false
		}

		if (!alarm.isPaused) {
			console.warn(`闹钟 ${id} 未被暂停`)
			return false
		}

		alarm.isRunning = true
		alarm.isPaused = false
		alarm.pauseTime = null

		alarm.timer = setInterval(() => {
			alarm.remainingTime--
			alarm.elapsedTime++

			if (alarm.onTick) {
				try {
					alarm.onTick({
						remainingTime: alarm.remainingTime,
						totalTime: alarm.totalTime,
						elapsedTime: alarm.elapsedTime,
						progress: ((alarm.totalTime - alarm.remainingTime) / alarm.totalTime) * 100
					})
				} catch (error) {
					console.error('Tick回调错误:', error)
				}
			}

			if (alarm.remainingTime <= 0) {
				this.completeAlarm(id)
			}
		}, 1000)

		if (alarm.onResume) {
			try {
				alarm.onResume({
					remainingTime: alarm.remainingTime
				})
			} catch (error) {
				console.error('Resume回调错误:', error)
			}
		}

		return true
	}

	/**
	 * 停止闹钟
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	stopAlarm(id) {
		const alarm = this.alarms.get(id)
		if (!alarm) {
			console.warn(`闹钟 ${id} 不存在`)
			return false
		}

		clearInterval(alarm.timer)
		alarm.timer = null
		alarm.isRunning = false
		alarm.isPaused = false
		alarm.remainingTime = alarm.totalTime
		alarm.elapsedTime = 0

		// 停止音频
		this.stopSound()

		if (alarm.onStop) {
			try {
				alarm.onStop({
					totalTime: alarm.totalTime,
					elapsedTime: alarm.elapsedTime
				})
			} catch (error) {
				console.error('Stop回调错误:', error)
			}
		}

		return true
	}

	/**
	 * 重置闹钟
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	resetAlarm(id) {
		const alarm = this.alarms.get(id)
		if (!alarm) {
			console.warn(`闹钟 ${id} 不存在`)
			return false
		}

		clearInterval(alarm.timer)
		alarm.timer = null
		alarm.isRunning = false
		alarm.isPaused = false
		alarm.remainingTime = alarm.totalTime
		alarm.elapsedTime = 0
		alarm.startTime = null
		alarm.pauseTime = null

		this.stopSound()

		return true
	}

	/**
	 * 完成闹钟
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	completeAlarm(id) {
		const alarm = this.alarms.get(id)
		if (!alarm) {
			console.warn(`闹钟 ${id} 不存在`)
			return false
		}

		clearInterval(alarm.timer)
		alarm.timer = null
		alarm.isRunning = false
		alarm.isPaused = false
		alarm.remainingTime = 0

		// 播放提示音
		if (alarm.sound) {
			this.playSound(alarm.sound)
		}

		if (alarm.onComplete) {
			try {
				alarm.onComplete({
					totalTime: alarm.totalTime,
					elapsedTime: alarm.elapsedTime,
					type: alarm.type
				})
			} catch (error) {
				console.error('Complete回调错误:', error)
			}
		}

		// 延迟清理
		setTimeout(() => {
			this.clearAlarm(id)
		}, 5000)

		return true
	}

	/**
	 * 延长闹钟时间
	 * @param {string} id - 闹钟ID
	 * @param {number} additionalSeconds - 增加的秒数
	 * @returns {boolean}
	 */
	extendAlarm(id, additionalSeconds) {
		const alarm = this.alarms.get(id)
		if (!alarm) {
			console.warn(`闹钟 ${id} 不存在`)
			return false
		}

		alarm.totalTime += additionalSeconds
		alarm.remainingTime += additionalSeconds

		return true
	}

	/**
	 * 缩短闹钟时间
	 * @param {string} id - 闹钟ID
	 * @param {number} reduceSeconds - 减少的秒数
	 * @returns {boolean}
	 */
	reduceAlarm(id, reduceSeconds) {
		const alarm = this.alarms.get(id)
		if (!alarm) {
			console.warn(`闹钟 ${id} 不存在`)
			return false
		}

		alarm.remainingTime = Math.max(0, alarm.remainingTime - reduceSeconds)

		// 如果时间到了，立即完成
		if (alarm.remainingTime === 0) {
			this.completeAlarm(id)
		}

		return true
	}

	/**
	 * 播放提示音
	 * @param {string} soundSrc - 音频地址
	 * @returns {void}
	 */
	playSound(soundSrc) {
		if (!soundSrc) return

		try {
			this.audioContext.src = soundSrc
			this.audioContext.loop = false
			this.audioContext.play()
		} catch (error) {
			console.error('播放提示音失败:', error)
		}
	}

	/**
	 * 停止提示音
	 * @returns {void}
	 */
	stopSound() {
		try {
			this.audioContext.stop()
		} catch (error) {
			// 忽略停止错误
		}
	}

	/**
	 * 设置提示音循环
	 * @param {boolean} loop - 是否循环
	 * @returns {void}
	 */
	setSoundLoop(loop) {
		this.audioContext.loop = loop
	}

	/**
	 * 设置提示音音量
	 * @param {number} volume - 音量 0-1
	 * @returns {void}
	 */
	setSoundVolume(volume) {
		this.audioContext.volume = Math.max(0, Math.min(1, volume))
	}

	/**
	 * 清除闹钟
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	clearAlarm(id) {
		const alarm = this.alarms.get(id)
		if (!alarm) return false

		clearInterval(alarm.timer)
		this.alarms.delete(id)
		return true
	}

	/**
	 * 获取闹钟状态
	 * @param {string} id - 闹钟ID
	 * @returns {Object|null}
	 */
	getAlarmStatus(id) {
		const alarm = this.alarms.get(id)
		if (!alarm) return null

		return {
			id: alarm.id,
			type: alarm.type,
			remainingTime: alarm.remainingTime,
			totalTime: alarm.totalTime,
			elapsedTime: alarm.elapsedTime,
			isRunning: alarm.isRunning,
			isPaused: alarm.isPaused,
			progress: alarm.totalTime > 0 ?
				((alarm.totalTime - alarm.remainingTime) / alarm.totalTime) * 100 :
				0
		}
	}

	/**
	 * 获取所有闹钟状态
	 * @returns {Array}
	 */
	getAllAlarmsStatus() {
		const statuses = []
		this.alarms.forEach((alarm, id) => {
			statuses.push(this.getAlarmStatus(id))
		})
		return statuses
	}

	/**
	 * 检查闹钟是否存在
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	hasAlarm(id) {
		return this.alarms.has(id)
	}

	/**
	 * 检查闹钟是否运行中
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	isAlarmRunning(id) {
		const alarm = this.alarms.get(id)
		return alarm ? alarm.isRunning : false
	}

	/**
	 * 检查闹钟是否暂停
	 * @param {string} id - 闹钟ID
	 * @returns {boolean}
	 */
	isAlarmPaused(id) {
		const alarm = this.alarms.get(id)
		return alarm ? alarm.isPaused : false
	}

	/**
	 * 获取运行中的闹钟数量
	 * @returns {number}
	 */
	getRunningCount() {
		let count = 0
		this.alarms.forEach(alarm => {
			if (alarm.isRunning) count++
		})
		return count
	}

	/**
	 * 暂停所有闹钟
	 * @returns {void}
	 */
	pauseAllAlarms() {
		this.alarms.forEach((alarm, id) => {
			if (alarm.isRunning) {
				this.pauseAlarm(id)
			}
		})
	}

	/**
	 * 停止所有闹钟
	 * @returns {void}
	 */
	stopAllAlarms() {
		this.alarms.forEach((alarm, id) => {
			this.stopAlarm(id)
		})
	}

	/**
	 * 清除所有闹钟
	 * @returns {void}
	 */
	clearAllAlarms() {
		this.alarms.forEach((alarm, id) => {
			this.clearAlarm(id)
		})
		this.alarms.clear()
	}

	/**
	 * 格式化时间
	 * @param {number} seconds - 秒数
	 * @returns {string}
	 */
	formatTime(seconds) {
		if (!seconds || isNaN(seconds) || seconds < 0) return '00:00'

		const hours = Math.floor(seconds / 3600)
		const mins = Math.floor((seconds % 3600) / 60)
		const secs = seconds % 60

		if (hours > 0) {
			return `${hours}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
		}
		return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
	}

	/**
	 * 格式化时间（带单位）
	 * @param {number} seconds - 秒数
	 * @returns {string}
	 */
	formatTimeWithUnit(seconds) {
		if (seconds < 60) {
			return `${seconds}秒`
		} else if (seconds < 3600) {
			const mins = Math.floor(seconds / 60)
			const secs = seconds % 60
			return secs > 0 ? `${mins}分${secs}秒` : `${mins}分钟`
		} else {
			const hours = Math.floor(seconds / 3600)
			const mins = Math.floor((seconds % 3600) / 60)
			return mins > 0 ? `${hours}小时${mins}分` : `${hours}小时`
		}
	}

	/**
	 * 清理所有资源
	 * @returns {void}
	 */
	cleanup() {
		this.stopAllAlarms()
		this.clearAllAlarms()
		this.stopSound()
		try {
			this.audioContext.destroy()
		} catch (error) {
			console.error('销毁音频失败:', error)
		}
	}
}

export default new AlarmManager()