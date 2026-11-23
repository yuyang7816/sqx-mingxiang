<script setup>
	import {
		onMounted,
		onUnmounted
	} from 'vue'
	import {
		useAlarmStore
	} from '@/stores/alarm'

	const alarmStore = useAlarmStore()

	onMounted(() => {
		console.log('[App] 应用启动，全局弹窗组件已挂载')

		// #ifdef APP-PLUS
		// 监听应用从后台恢复
		document.addEventListener('resume', onAppResume)

		// 监听本地通知点击
		plus.push.addEventListener('click', onNotificationClick, false)

		// 监听本地通知接收（前台时）
		plus.push.addEventListener('receive', onNotificationReceive, false)

		// 请求通知权限
		requestNotificationPermission()
		// #endif
	})

	onUnmounted(() => {
		// #ifdef APP-PLUS
		document.removeEventListener('resume', onAppResume)
		plus.push.removeEventListener('click', onNotificationClick)
		plus.push.removeEventListener('receive', onNotificationReceive)
		// #endif
	})

	// #ifdef APP-PLUS
	// 应用从后台恢复
	const onAppResume = () => {
		console.log('[App] 应用从后台恢复')

		// 检查是否有正在响铃的闹钟，显示弹窗
		if (alarmStore.isRinging) {
			console.log('[App] 恢复时发现响铃，显示弹窗')
			const type = alarmStore.ringingType
			const alarm = alarmStore.alarms[type]
			alarmStore.showFinishedModal = true
			alarmStore.finishedAlarmType = type
			alarmStore.finishedAlarmTitle = alarm.title
		}
	}

	// 通知点击处理
	const onNotificationClick = (msg) => {
		console.log('[App] 通知点击:', msg)
		try {
			const payload = JSON.parse(msg.payload || '{}')
			if (payload.type && alarmStore.alarms[payload.type]) {
				// 显示闹钟完成弹窗
				alarmStore.showFinishedModal = true
				alarmStore.finishedAlarmType = payload.type
				alarmStore.finishedAlarmTitle = alarmStore.alarms[payload.type].title
			}
		} catch (e) {
			console.error('[App] 通知解析失败:', e)
		}
	}

	// 通知接收处理（前台时）
	const onNotificationReceive = (msg) => {
		console.log('[App] 通知接收:', msg)
		// 前台收到通知时也显示弹窗
		onNotificationClick(msg)
	}

	// 请求通知权限
	const requestNotificationPermission = () => {
		// Android 自动有权限，iOS 需要请求
		if (plus.os.name === 'iOS') {
			// iOS 推送权限申请
			const UIApplication = plus.ios.importClass('UIApplication')
			const app = UIApplication.sharedApplication()
			const settings = app.currentUserNotificationSettings()
			if (!settings || settings.alertStyle() === 0) {
				console.log('[App] 需要申请iOS通知权限')
				// 实际项目中引导用户开启
			}
		}
	}
	// #endif
</script>

<style lang="scss">
	/*每个页面公共css */
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-plus/index.scss";

	//自定义公共样式
	@import "@/static/css/main.css";
	@import "@/style/main.scss";

	page {
		// background-color: #F5F7FF;
	}
</style>