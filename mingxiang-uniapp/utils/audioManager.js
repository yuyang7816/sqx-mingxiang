// utils/audioManager.js
class AudioManager {
	constructor() {
		this.audioContext = null
		this.isBackgroundAudio = false
		this.listeners = {}
		this.init()
	}

	/**
	 * 初始化音频管理器
	 * @returns {void}
	 */
	init() {
		// #ifdef MP-WEIXIN
		this.audioContext = uni.getBackgroundAudioManager()
		this.isBackgroundAudio = true
		// #endif

		// #ifndef MP-WEIXIN
		this.audioContext = uni.createInnerAudioContext()
		this.isBackgroundAudio = false
		// #endif

		this.setupListeners()
	}

	/**
	 * 设置音频事件监听
	 * @returns {void}
	 */
	setupListeners() {
		if (!this.audioContext) return

		// 播放中
		this.audioContext.onPlay(() => {
			this.emit('onPlay')
		})

		// 暂停
		this.audioContext.onPause(() => {
			this.emit('onPause')
		})

		// 停止
		this.audioContext.onStop(() => {
			this.emit('onStop')
		})

		// 播放结束
		this.audioContext.onEnded(() => {
			this.emit('onEnded')
		})

		// 播放进度更新
		this.audioContext.onTimeUpdate(() => {
			this.emit('onTimeUpdate', {
				currentTime: this.audioContext.currentTime,
				duration: this.audioContext.duration
			})
		})

		// 播放错误
		this.audioContext.onError((err) => {
			console.error('音频播放错误:', err)
			this.emit('onError', err)
		})

		// 背景音频特有事件
		if (this.isBackgroundAudio) {
			this.audioContext.onPrev(() => this.emit('onPrev'))
			this.audioContext.onNext(() => this.emit('onNext'))
			this.audioContext.onWaiting(() => this.emit('onWaiting'))
			this.audioContext.onCanplay(() => this.emit('onCanplay'))
		}
	}

	/**
	 * 注册事件监听
	 * @param {string} event - 事件名称
	 * @param {Function} callback - 回调函数
	 * @returns {void}
	 */
	on(event, callback) {
		if (!this.listeners[event]) this.listeners[event] = []
		this.listeners[event].push(callback)
	}

	/**
	 * 移除事件监听
	 * @param {string} event - 事件名称
	 * @param {Function} callback - 回调函数
	 * @returns {void}
	 */
	off(event, callback) {
		if (!this.listeners[event]) return
		const index = this.listeners[event].indexOf(callback)
		if (index > -1) {
			this.listeners[event].splice(index, 1)
		}
	}

	/**
	 * 触发事件
	 * @param {string} event - 事件名称
	 * @param {*} data - 事件数据
	 * @returns {void}
	 */
	emit(event, data) {
		if (this.listeners[event]) {
			this.listeners[event].forEach(cb => cb(data))
		}
	}

	/**
	 * 播放音频
	 * @param {string} src - 音频地址
	 * @param {Object} options - 播放选项
	 * @param {string} options.title - 标题
	 * @param {string} options.artist - 艺术家
	 * @param {string} options.cover - 封面图
	 * @returns {void}
	 */
	play(src, options = {}) {
		if (!this.audioContext || !src) {
			console.error('音频上下文未初始化或音频地址为空')
			return
		}

		try {
			if (this.isBackgroundAudio) {
				this.audioContext.title = options.title || '冥想音频'
				this.audioContext.singer = options.artist || ''
				this.audioContext.coverImgUrl = options.cover || ''
				this.audioContext.epname = options.album || ''
				this.audioContext.src = src
				this.audioContext.play()
			} else {
				this.audioContext.src = src
				this.audioContext.play()
			}
		} catch (error) {
			console.error('播放音频失败:', error)
			this.emit('onError', error)
		}
	}

	/**
	 * 暂停播放
	 * @returns {void}
	 */
	pause() {
		if (this.audioContext) {
			try {
				this.audioContext.pause()
			} catch (error) {
				console.error('暂停音频失败:', error)
			}
		}
	}

	/**
	 * 继续播放
	 * @returns {void}
	 */
	resume() {
		if (this.audioContext) {
			try {
				this.audioContext.play()
			} catch (error) {
				console.error('继续播放失败:', error)
			}
		}
	}

	/**
	 * 停止播放
	 * @returns {void}
	 */
	stop() {
		if (this.audioContext) {
			try {
				this.audioContext.stop()
			} catch (error) {
				console.error('停止音频失败:', error)
			}
		}
	}

	/**
	 * 跳转到指定位置
	 * @param {number} position - 目标位置（秒）
	 * @returns {void}
	 */
	seek(position) {
		if (this.audioContext) {
			try {
				this.audioContext.seek(position)
			} catch (error) {
				console.error('跳转进度失败:', error)
			}
		}
	}

	/**
	 * 设置音量
	 * @param {number} volume - 音量 0-1
	 * @returns {void}
	 */
	setVolume(volume) {
		if (this.audioContext) {
			try {
				this.audioContext.volume = Math.max(0, Math.min(1, volume))
			} catch (error) {
				console.error('设置音量失败:', error)
			}
		}
	}

	/**
	 * 设置播放速率
	 * @param {number} rate - 播放速率 0.5-2.0
	 * @returns {void}
	 */
	setPlaybackRate(rate) {
		if (this.audioContext) {
			try {
				this.audioContext.playbackRate = Math.max(0.5, Math.min(2.0, rate))
			} catch (error) {
				console.error('设置播放速率失败:', error)
			}
		}
	}

	/**
	 * 设置是否循环播放
	 * @param {boolean} loop - 是否循环
	 * @returns {void}
	 */
	setLoop(loop) {
		if (this.audioContext) {
			try {
				this.audioContext.loop = loop
			} catch (error) {
				console.error('设置循环播放失败:', error)
			}
		}
	}

	/**
	 * 销毁音频实例
	 * @returns {void}
	 */
	destroy() {
		if (this.audioContext && !this.isBackgroundAudio) {
			try {
				this.audioContext.destroy()
				this.audioContext = null
				this.listeners = {}
			} catch (error) {
				console.error('销毁音频失败:', error)
			}
		}
	}

	/**
	 * 获取当前播放状态
	 * @returns {Object} 播放状态
	 */
	getStatus() {
		if (!this.audioContext) {
			return {
				currentTime: 0,
				duration: 0,
				paused: true,
				volume: 1
			}
		}
		return {
			currentTime: this.audioContext.currentTime || 0,
			duration: this.audioContext.duration || 0,
			paused: this.audioContext.paused !== false,
			volume: this.audioContext.volume || 1
		}
	}

	/**
	 * 获取音频总时长
	 * @returns {number} 总时长（秒）
	 */
	getDuration() {
		return this.audioContext?.duration || 0
	}

	/**
	 * 获取当前播放位置
	 * @returns {number} 当前位置（秒）
	 */
	getCurrentTime() {
		return this.audioContext?.currentTime || 0
	}

	/**
	 * 检查是否正在播放
	 * @returns {boolean} 是否播放中
	 */
	isPlaying() {
		return this.audioContext?.paused === false
	}

	/**
	 * 检查是否暂停
	 * @returns {boolean} 是否暂停
	 */
	isPaused() {
		return this.audioContext?.paused !== false
	}
}

export default new AudioManager()