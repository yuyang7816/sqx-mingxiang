// stores/player.js
import {
	defineStore
} from 'pinia'

export const usePlayerStore = defineStore('player', {
	state: () => ({
		isPlaying: false,
		currentAudio: null,
		currentType: '', // 'music' | 'course'
		currentIndex: 0,
		playlist: [],
		duration: 0,
		currentTime: 0,
		timerClose: null,
		timerCloseId: null,
		isVip: false,
		innerAudioContext: null,

		// 记录音乐/课程各自的播放进度
		musicProgress: {}, // { musicId: currentTime }
		courseProgress: {}, // { chapterId: currentTime }
	}),

	getters: {
		formatDuration: (state) => formatTime(state.duration),
		formatCurrentTime: (state) => formatTime(state.currentTime),
		progressPercent: (state) => {
			if (state.duration === 0) return 0
			return (state.currentTime / state.duration) * 100
		},
		remainingTime: (state) => {
			if (!state.timerClose) return 0
			const now = Date.now()
			const remaining = Math.ceil((state.timerClose - now) / 1000)
			return remaining > 0 ? remaining : 0
		}
	},

	actions: {
		// 初始化音频（带互斥逻辑）
		initAudio(force = false) {
			// 如果已有音频在播放，且不是强制重新初始化，则返回
			if (this.innerAudioContext && !force) return

			// 销毁旧音频
			if (this.innerAudioContext) {
				this.saveProgress() // 保存当前进度
				this.innerAudioContext.destroy()
			}

			this.innerAudioContext = uni.createInnerAudioContext()
			this.innerAudioContext.obeyMuteSwitch = false

			// 监听事件
			this.innerAudioContext.onPlay(() => {
				this.isPlaying = true
			})
			this.innerAudioContext.onPause(() => {
				this.isPlaying = false
			})
			this.innerAudioContext.onStop(() => {
				this.isPlaying = false
				this.currentTime = 0
			})
			this.innerAudioContext.onEnded(() => {
				this.handleAudioEnded()
			})
			this.innerAudioContext.onTimeUpdate(() => {
				this.currentTime = this.innerAudioContext.currentTime
				this.duration = this.innerAudioContext.duration || this.duration
			})
			this.innerAudioContext.onCanplay(() => {
				setTimeout(() => {
					this.duration = this.innerAudioContext.duration
				}, 500)
			})
			this.innerAudioContext.onError((err) => {
				console.error('播放错误:', err)
				uni.showToast({
					title: '播放失败',
					icon: 'none'
				})
			})
		},

		// 保存当前进度
		saveProgress() {
			if (!this.currentAudio || !this.currentType) return

			const id = this.currentAudio.id || this.currentAudio.chapterId
			if (this.currentType === 'music') {
				this.musicProgress[id] = this.currentTime
			} else {
				this.courseProgress[id] = this.currentTime
			}
		},

		// 设置播放列表（核心方法，带互斥逻辑）
		setPlaylist(list, type, index = 0, autoPlay = true) {
			// 互斥：如果正在播放另一种类型，先保存并停止
			if (this.currentType && this.currentType !== type && this.isPlaying) {
				this.saveProgress()
				this.stop()
			}

			this.playlist = list
			this.currentType = type
			this.currentIndex = index

			if (list[index]) {
				this.playItem(list[index], autoPlay)
			}
		},

		// 播放指定项
		playItem(item, autoPlay = true) {
			// 保存旧进度
			if (this.currentAudio && this.currentAudio !== item) {
				this.saveProgress()
			}

			this.currentAudio = item
			this.initAudio(true) // 强制重新初始化

			// 设置音频源
			const audioUrl = item.audioUrl || item.chapterUrl || item.filePath
			const title = item.effectsName || item.title || item.name
			const cover = item.bgImg || item.coverImage || item.cover

			this.innerAudioContext.src = audioUrl
			this.innerAudioContext.title = title
			this.innerAudioContext.coverImgUrl = cover

			// 恢复上次进度（课程有用，音乐一般从头播放）
			const id = item.id || item.chapterId
			if (this.currentType === 'course' && this.courseProgress[id] > 0) {
				// 课程恢复进度
				setTimeout(() => {
					this.innerAudioContext.seek(this.courseProgress[id])
				}, 300)
			}

			if (autoPlay) {
				this.innerAudioContext.play()
			}
		},

		// 播放/暂停切换
		togglePlay() {
			if (!this.innerAudioContext) return
			if (this.isPlaying) {
				this.innerAudioContext.pause()
				this.saveProgress()
			} else {
				this.innerAudioContext.play()
			}
		},

		// 切换上一首
		prev(callback) {
			if (this.currentIndex > 0) {
				this.currentIndex--
				const item = this.playlist[this.currentIndex]
				this.playItem(item)
				if (callback) callback('switch', item, this.currentType)
			}
		},

		// 切换下一首
		next(callback) {
			if (this.currentIndex < this.playlist.length - 1) {
				this.currentIndex++
				const item = this.playlist[this.currentIndex]

				// 检查VIP
				if (item.isVip && !this.isVip) {
					if (callback) callback('vipRequired', item, this.currentType)
					return 'vipRequired'
				}

				this.playItem(item)
				if (callback) callback('switch', item, this.currentType)
			} else {
				if (callback) callback('ended', null, this.currentType)
			}
		},

		// 跳转到指定索引
		playAtIndex(index, callback) {
			if (index >= 0 && index < this.playlist.length) {
				this.currentIndex = index
				const item = this.playlist[index]

				if (item.isVip && !this.isVip) {
					if (callback) callback('vipRequired', item, this.currentType)
					return 'vipRequired'
				}

				this.playItem(item)
				if (callback) callback('switch', item, this.currentType)
			}
		},

		// 音频结束处理
		handleAudioEnded() {
			if (this.currentType === 'course') {
				const result = this.next()
				if (result === 'vipRequired') this.stop()
			} else {
				this.next()
			}
		},

		// 拖动进度
		seek(position, callback) {
			if (!this.innerAudioContext) return
			const time = (position / 100) * this.duration
			this.innerAudioContext.seek(time)
			this.currentTime = time
			if (callback) callback('seek', {
				position,
				time
			}, this.currentType)
		},

		// 设置定时关闭
		setTimerClose(minutes, callback) {
			if (this.timerCloseId) clearTimeout(this.timerCloseId)

			if (minutes === 0) {
				this.timerClose = null
				this.timerCloseId = null
				if (callback) callback('timerCancel', null, this.currentType)
				return
			}

			const ms = minutes * 60 * 1000
			this.timerClose = Date.now() + ms

			this.timerCloseId = setTimeout(() => {
				this.stop()
				if (callback) callback('timerEnd', null, this.currentType)
			}, ms)

			if (callback) callback('timerSet', {
				minutes
			}, this.currentType)
		},

		// 清除定时
		clearTimer() {
			if (this.timerCloseId) {
				clearTimeout(this.timerCloseId)
				this.timerCloseId = null
			}
			this.timerClose = null
		},

		// 停止播放
		stop() {
			this.saveProgress()
			this.innerAudioContext?.stop()
			this.isPlaying = false
			this.currentTime = 0
			this.clearTimer()
		},

		// 释放资源
		destroy() {
			this.saveProgress()
			this.stop()
			if (this.innerAudioContext) {
				this.innerAudioContext.destroy()
				this.innerAudioContext = null
			}
		},

		// 更新VIP状态
		setVipStatus(isVip) {
			this.isVip = isVip
		}
	}
})

function formatTime(seconds) {
	if (!seconds || isNaN(seconds)) return '00:00'
	const min = Math.floor(seconds / 60)
	const sec = Math.floor(seconds % 60)
	return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`
}