// stores/alarm.js
import {
	defineStore
} from 'pinia'

const DEFAULT_ALARM_SOUND =
	'https://dzht.xianmaxiong.com/file/uploadPath/2026/03/30/33d7af68a2ae38e3f6fe5f9262f79fc7.mp3'

export const useAlarmStore = defineStore('alarm', {
	state: () => ({
		alarms: {
			night: {
				isRunning: false,
				remainingTime: 0,
				totalTime: 0,
				endTime: 0,
				timerId: null,
				title: '夜间睡眠',
				sound: DEFAULT_ALARM_SOUND
			},
			focus: {
				isRunning: false,
				remainingTime: 0,
				totalTime: 0,
				endTime: 0,
				timerId: null,
				title: '专注时钟',
				sound: DEFAULT_ALARM_SOUND
			},
			nap: {
				isRunning: false,
				remainingTime: 0,
				totalTime: 0,
				endTime: 0,
				timerId: null,
				title: '日间小憩',
				sound: DEFAULT_ALARM_SOUND
			}
		},
		audioContexts: {},
		ringingType: '',
		h5Audio: null,
		showFinishedModal: false,
		finishedAlarmType: '',
		finishedAlarmTitle: ''
	}),

	getters: {
		getFormatTime: (state) => (type) => {
			const seconds = state.alarms[type].remainingTime
			console.log(seconds,"seconds")
			const h = Math.floor(seconds / 3600)
			const m = Math.floor((seconds % 3600) / 60)
			const s = seconds % 60
			if (h > 0) return `${h}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
			return `${m.toString().padStart(2, '0')} : ${s.toString().padStart(2, '0')}`
		},
		getProgress: (state) => (type) => {
			const alarm = state.alarms[type]
			if (alarm.totalTime === 0) return 0
			return ((alarm.totalTime - alarm.remainingTime) / alarm.totalTime) * 100
		},
		hasRunningAlarm: (state) => Object.values(state.alarms).some(a => a.isRunning),
		runningAlarms: (state) => Object.entries(state.alarms)
			.filter(([_, alarm]) => alarm.isRunning)
			.map(([type, alarm]) => ({
				type,
				...alarm
			})),
		isRinging: (state) => !!state.ringingType,
		ringingAlarm: (state) => state.ringingType ? {
			type: state.ringingType,
			...state.alarms[state.ringingType]
		} : null,
		hasFinishedAlarm: (state) => state.showFinishedModal,
		finishedAlarmInfo: (state) => state.showFinishedModal ? {
			type: state.finishedAlarmType,
			title: state.finishedAlarmTitle
		} : null
	},

	actions: {
		startAlarm(type, data) {
			const minutes = parseInt(data.duration)
			const totalSeconds = minutes * 60

			this.closeFinishedModal()
			this.stopAlarm(type)

			this.alarms[type] = {
				...this.alarms[type],
				isRunning: true,
				remainingTime: totalSeconds,
				totalTime: totalSeconds,
				endTime: Date.now() + (totalSeconds * 1000),
				timerId: null,
				title: data.title || this.alarms[type].title,
				sound: DEFAULT_ALARM_SOUND
			}

			this.startCountdown(type)
			this.setBackgroundTask(type)

			console.log(`[startAlarm] 开始${minutes}分钟${data.title}`)
			return true
		},

		startCountdown(type) {
			const alarm = this.alarms[type]
			if (alarm.timerId) clearInterval(alarm.timerId)

			alarm.timerId = setInterval(() => {
				const now = Date.now()
				const remaining = Math.ceil((alarm.endTime - now) / 1000)
				alarm.remainingTime = remaining > 0 ? remaining : 0

				uni.$emit(`alarmTick_${type}`, {
					remainingTime: alarm.remainingTime,
					formatTime: this.getFormatTime(type)
				})

				if (remaining <= 0) {
					this.finishAlarm(type)
				}
			}, 1000)
		},

		stopAlarm(type) {
			const alarm = this.alarms[type]
			if (alarm.timerId) {
				clearInterval(alarm.timerId)
				alarm.timerId = null
			}
			this.stopAudio(type)
			this.clearBackgroundTask(type)

			alarm.isRunning = false
			alarm.remainingTime = 0

			if (this.ringingType === type) {
				this.ringingType = ''
			}

			uni.$emit(`alarmStopped_${type}`, {
				type
			})
		},

		finishAlarm(type) {
			const alarm = this.alarms[type]
			console.log(`[finishAlarm] 类型: ${type}, 标题: ${alarm.title}`)

			this.stopCountdown(type)
			this.ringingType = type

			// 播放完成音频
			this.playFinishSound(type)

			// 设置弹窗状态
			console.log('[finishAlarm] 设置弹窗状态为 true')
			this.showFinishedModal = true
			this.finishedAlarmType = type
			this.finishedAlarmTitle = alarm.title

			// 发送本地通知（息屏时唤醒）
			this.sendLocalNotification(type, alarm.title)

			// 震动提醒
			this.vibrateAlarm()

			console.log('[finishAlarm] 当前状态:', {
				showFinishedModal: this.showFinishedModal,
				finishedAlarmType: this.finishedAlarmType,
				finishedAlarmTitle: this.finishedAlarmTitle
			})

			uni.$emit('alarmFinished', {
				type,
				title: alarm.title
			})

			alarm.isRunning = false
		},

		sendLocalNotification(type, title) {
			// #ifdef APP-PLUS
			const options = {
				title: '⏰ 闹钟提醒',
				content: `${title}时间到了！`,
				payload: JSON.stringify({
					type
				}),
				sound: 'system',
				cover: true,
				vibrate: true
			}

			plus.push.createMessage(`${title}时间到了！`, JSON.stringify({
				type
			}), options)

			let vibrateCount = 0
			const vibrateInterval = setInterval(() => {
				vibrateCount++
				if (vibrateCount > 5 || !this.ringingType) {
					clearInterval(vibrateInterval)
					return
				}
				plus.device.vibrate(1000)
			}, 1500)
			// #endif

			// #ifdef H5
			if ('Notification' in window && Notification.permission === 'granted') {
				new Notification('闹钟提醒', {
					body: `${title}时间到了！`,
					icon: '/static/logo.png',
					requireInteraction: true
				})
			}
			// #endif
		},

		vibrateAlarm() {
			// #ifdef APP-PLUS
			plus.device.vibrate(2000)
			// #endif

			// #ifdef H5
			if (navigator.vibrate) {
				navigator.vibrate([500, 200, 500, 200, 500])
			}
			// #endif
		},

		stopCountdown(type) {
			const alarm = this.alarms[type]
			if (alarm.timerId) {
				clearInterval(alarm.timerId)
				alarm.timerId = null
			}
		},

		playFinishSound(type) {
			// #ifdef H5
			this.playFinishSoundH5()
			// #endif
			// #ifndef H5
			this.playFinishSoundApp(type)
			// #endif
		},

		playFinishSoundH5() {
			if (this.h5Audio) {
				this.h5Audio.pause()
				this.h5Audio.currentTime = 0
			}
			try {
				const audio = new Audio()
				audio.src = DEFAULT_ALARM_SOUND
				audio.loop = true
				audio.volume = 0.9
				audio.onerror = (e) => {
					console.error('H5 音频加载失败:', e)
					this.fallbackNotification()
				}
				audio.oncanplay = () => {
					audio.play().catch(err => {
						console.error('H5 音频播放失败:', err)
					})
				}
				this.h5Audio = audio
			} catch (e) {
				console.error('创建 Audio 失败:', e)
				this.fallbackNotification()
			}
		},

		// 修复：使用 try-catch 包裹 obeyMuteSwitch 设置
		playFinishSoundApp(type) {
			if (this.audioContexts[type]) {
				this.audioContexts[type].destroy()
			}

			const audio = uni.createInnerAudioContext()

			// 先设置 src 和其他属性
			audio.src = DEFAULT_ALARM_SOUND
			audio.loop = true
			audio.volume = 0.9

			// 使用 try-catch 设置 obeyMuteSwitch，某些平台它是只读的
			try {
				// #ifdef APP-PLUS
				audio.obeyMuteSwitch = false
				// #endif
			} catch (err) {
				console.warn('[playFinishSoundApp] obeyMuteSwitch 设置失败（只读属性）:', err.message)
			}

			// 尝试使用 uni.setInnerAudioOption（如果可用）
			// #ifdef APP-PLUS
			if (uni.setInnerAudioOption) {
				uni.setInnerAudioOption({
					obeyMuteSwitch: false,
					success: () => console.log('[playFinishSoundApp] setInnerAudioOption 设置成功'),
					fail: (err) => console.warn('[playFinishSoundApp] setInnerAudioOption 设置失败:', err)
				})
			}
			// #endif

			audio.onError((err) => {
				console.error('音频播放错误:', err)
				// #ifdef APP-PLUS
				plus.device.vibrate(2000)
				// #endif
			})

			audio.onCanplay(() => {
				audio.play()
			})

			this.audioContexts[type] = audio
		},

		fallbackNotification() {
			// #ifdef APP-PLUS
			plus.device.vibrate(2000)
			// #endif
			if (navigator.vibrate) {
				navigator.vibrate(2000)
			}
		},

		stopAudio(type) {
			// #ifdef H5
			if (this.h5Audio) {
				this.h5Audio.pause()
				this.h5Audio.currentTime = 0
				this.h5Audio = null
			}
			// #endif
			// #ifndef H5
			if (this.audioContexts[type]) {
				this.audioContexts[type].stop()
				this.audioContexts[type].destroy()
				delete this.audioContexts[type]
			}
			// #endif
		},

		stopRinging() {
			if (this.ringingType) {
				const type = this.ringingType
				this.stopAudio(type)
				this.ringingType = ''
				if (this.alarms[type]) {
					this.alarms[type].isRunning = false
					this.alarms[type].remainingTime = 0
				}
			}
		},

		setBackgroundTask(type) {
			// #ifdef APP-PLUS
			// 申请后台运行权限（Android）
			if (plus.os.name === 'Android') {
				const Context = plus.android.importClass('android.content.Context')
				const PowerManager = plus.android.importClass('android.os.PowerManager')
				const main = plus.android.runtimeMainActivity()
				const pm = main.getSystemService(Context.POWER_SERVICE)

				if (!pm.isIgnoringBatteryOptimizations(main.getPackageName())) {
					console.log('[setBackgroundTask] 申请后台运行权限')
				}
			}
			console.log(`[setBackgroundTask] ${type} 已设置后台任务`)
			// #endif
		},

		clearBackgroundTask(type) {
			// #ifdef APP-PLUS
			const hasOthers = Object.entries(this.alarms).some(([t, a]) => t !== type && a.isRunning)
			if (!hasOthers) {
				console.log('[clearBackgroundTask] 清除后台任务')
			}
			// #endif
		},

		closeFinishedModal() {
			console.log('[closeFinishedModal] 关闭弹窗')
			this.showFinishedModal = false
			this.finishedAlarmType = ''
			this.finishedAlarmTitle = ''
		},

		stopFinishedAlarm() {
			console.log('[stopFinishedAlarm] 结束闹钟')
			const type = this.finishedAlarmType || this.ringingType
			this.closeFinishedModal()
			this.stopRinging()
			if (type) {
				this.stopAlarm(type)
			}
		},

		getAlarmState(type) {
			return this.alarms[type]
		}
	}
})