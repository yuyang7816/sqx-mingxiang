<!-- components/player/FullPlayer.vue -->
<template>
	<view class="full-player" v-if="currentCourse">

		<view class="info-section">
			<text class="course-name">{{ currentCourse.title }}</text>
			<text class="teacher-name">{{ currentCourse.teacherName }}</text>
		</view>

		<view class="cover-section">
			<image class="cover" :src="currentCourse.coverImage" mode="aspectFill" />
		</view>



		<view class="controls-section">
			<view class="" @click="showPlaylist = true">
				<image src="/subpackage/static/kc/yy.png" mode="" style="width: 46rpx;height: 46rpx;"></image>
			</view>

			<view class="play-btn" @click="handlePlayPause">
				<!-- <up-icon :name="playerStore.isPlaying ? 'pause-circle-fill' : 'play-circle-fill'" size="80"
					color="#2979ff"></up-icon> -->
				<view class="bf_btn">
					<up-icon :name="playerStore.isPlaying ? 'pause' : 'play-right-fill'" size="25" color="#FFFFFF"
						style="display: flex;justify-content: center;padding-top: 30rpx;"></up-icon>
				</view>
			</view>

			<view class="" @click="showTimer = true">
				<image src="/subpackage/static/kc/nz.png" mode="" style="width: 42rpx;height: 46rpx;"></image>
				<!-- <up-icon name="clock" size="32" color="#333"></up-icon>
				<text class="btn-text">
					{{ playerStore.remainingTime > 0 ? formatCountdown(playerStore.remainingTime) : '定时' }}
				</text> -->
			</view>
		</view>

		<view class="progress-section">
			<slider :value="playerStore.progressPercent" @change="handleSeek" block-size="10" activeColor="#FFFFFF"
				backgroundColor="rgba(255, 255, 255, 0.3)" />
			<view class="time-display">
				<text class="time">{{ playerStore.formatCurrentTime }}</text>
				<text class="time">{{ playerStore.formatDuration }}</text>
			</view>
		</view>

		<!-- 课程列表弹窗 -->
		<up-popup :show="showPlaylist" mode="bottom" @close="showPlaylist = false" round="24" bgColor="transparent">
			<view class="popup-content">
				<view class="popup-header">
					<text class="title">课程列表</text>
					<up-icon name="close" size="20" color="#FFFFFF" @click="showPlaylist = false"></up-icon>
				</view>
				<scroll-view scroll-y class="popup-scroll">
					<view v-for="(item, index) in courseList" :key="item.chapterId" class="course-item" :class="{ 
              active: index === currentIndex,
              locked: item.isVip && !isVip 
            }" @click="handleSwitchCourse(index)">
						<view class="item-left">
							<text class="item-index">{{ index + 1 }}</text>
							<view class="item-info">
								<text class="item-name">{{ item.title }}</text>
								<text class="item-teacher">{{ item.teacherName }}</text>
							</view>
						</view>
						<!-- <view class="item-right">
							<text v-if="item.isVip && !isVip" class="lock-tag">🔒</text>
							<up-icon v-else-if="index === currentIndex" name="volume" size="16" color="#2979ff"></up-icon>
						</view> -->
						<view class="item-right">
							<image src="/subpackage/static/wjs.png" v-if="item.isVip && !isVip" mode=""
								style="width: 50rpx;height: 50rpx;"></image>
							<!-- <text v-if="item.isVip && !isVip" class="lock-tag">🔒</text> -->
							<!-- <up-icon v-else-if="index === currentIndex" name="volume" size="16" color="#2979ff"></up-icon> -->
						</view>
					</view>
				</scroll-view>
			</view>
		</up-popup>

		<!-- 定时弹窗 -->
		<up-popup :show="showTimer" mode="bottom" @close="showTimer = false" round="24" bgColor="transparent">
			<view class="popup-content">
				<view class="popup-header">
					<text class="title">设置定时</text>
					<up-icon name="close" size="20" color="#FFFFFF" @click="showTimer = false"></up-icon>
				</view>
				<view class="timer-list">
					<view v-for="time in timerOptions" :key="time.value" class="timer-item"
						:class="{ active: selectedTimer === time.value }" @click="handleSetTimer(time.value)">
						<text>{{ time.label }}</text>
						<view class="isSel" v-if="selectedTimer === time.value">
							<up-icon name="checkmark" size="15" color="#333333"></up-icon>
						</view>
						<view class="noSel" v-else></view>
					</view>
					<view class="timer-item cancel" @click="handleSetTimer(0)">
						<text>取消定时</text>
					</view>
				</view>
			</view>
		</up-popup>

		<!-- VIP提示弹窗 -->
		<up-popup :show="showVipTip" mode="center" :round="10" bgColor="#FFFFFF">
			<view class="vip-popup">
				<text class="vip-title">该课程需VIP</text>
				<text class="vip-desc">开通VIP即可畅听所有课程</text>
				<view class="vip-actions">
					<up-button text="取消" type="info" @click="showVipTip = false"></up-button>
					<up-button text="立即开通" type="primary" @click="handleOpenVip"></up-button>
				</view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		computed,
		watch
	} from 'vue'
	import {
		usePlayerStore
	} from '@/stores/player'

	const props = defineProps({
		courseList: {
			type: Array,
			required: true
		},
		currentIndex: {
			type: Number,
			default: 0
		},
		isVip: {
			type: Boolean,
			default: false
		},
		onPlayPause: {
			type: Function,
			required: true
		},
		onSwitch: {
			type: Function,
			required: true
		},
		onSeek: {
			type: Function,
			required: true
		},
		onTimer: {
			type: Function,
			required: true
		},
		onOpenVip: {
			type: Function,
			required: true
		}
	})

	const playerStore = usePlayerStore()

	const showPlaylist = ref(false)
	const showTimer = ref(false)
	const showVipTip = ref(false)
	const selectedTimer = ref(0)

	const currentCourse = computed(() => props.courseList[props.currentIndex])

	const timerOptions = [{
			label: '不停止',
			value: 0
		},
		{
			label: '1分钟',
			value: 1
		},
		{
			label: '20分钟',
			value: 20
		},
		{
			label: '30分钟',
			value: 30
		},
		{
			label: '60分钟',
			value: 60
		}
	]

	watch(() => props.isVip, (newVal) => {
		playerStore.setVipStatus(newVal)
	})

	playerStore.setVipStatus(props.isVip)

	const handlePlayPause = async () => {
		const action = playerStore.isPlaying ? 'pause' : 'play'
		const res = await props.onPlayPause(action, currentCourse.value, 'course')
		if (res !== false) {
			playerStore.togglePlay()
		}
	}

	const handleSwitchCourse = async (index) => {
		const item = props.courseList[index]
		if (item.isVip && !props.isVip) {
			showVipTip.value = true
			return
		}

		await props.onSwitch('switch', item, 'course')
		playerStore.setPlaylist(props.courseList, 'course', index)
		showPlaylist.value = false
	}

	const handleSeek = async (e) => {
		await playerStore.seek(e.detail.value, props.onSeek)
	}

	const handleSetTimer = async (minutes) => {
		selectedTimer.value = minutes
		await playerStore.setTimerClose(minutes, props.onTimer)
		showTimer.value = false

		if (minutes > 0) {
			uni.showToast({
				title: `已设置${minutes}分钟后关闭`,
				icon: 'none'
			})
		} else {
			uni.showToast({
				title: '已取消定时',
				icon: 'none'
			})
		}
	}

	const handleOpenVip = () => {
		showPlaylist.value = false
		showVipTip.value = false
		props.onOpenVip()
	}

	const formatCountdown = (seconds) => {
		const min = Math.floor(seconds / 60)
		const sec = seconds % 60
		return `${min}:${sec.toString().padStart(2, '0')}`
	}
</script>

<style lang="scss" scoped>
	uni-slider {
		margin: 0 !important;
	}

	.isSel {
		width: 30rpx;
		height: 30rpx;
		border-radius: 50%;
		border: solid 2rpx #FFFFFF;
		background-color: #FFFFFF;
	}

	.noSel {
		width: 30rpx;
		height: 30rpx;
		border-radius: 50%;
		border: solid 2rpx rgba(255, 255, 255, 0.5);
	}

	.bf_btn {
		background-color: rgba(255, 255, 255, 0.3);
		width: 112rpx;
		height: 112rpx;
		border-radius: 50%;
	}

	/* 样式保持不变 */
	.full-player {
		/* height: 100vh; */
		/* background: linear-gradient(180deg, #f5f7fa 0%, #fff 100%); */
		display: flex;
		flex-direction: column;
		/* padding: 40rpx; */
	}

	.cover-section {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 590rpx;
		height: 590rpx;
		margin: 70rpx auto 60rpx auto;
	}

	.cover {
		width: 590rpx;
		height: 590rpx;
		border-radius: 20rpx;
		box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.15);
		display: block;
		margin: 0 !important;
	}

	.info-section {
		margin-top: 120rpx;
		text-align: center;
	}

	.course-name {
		display: block;
		font-size: 40rpx;
		font-weight: bold;
		color: #FFF;
		margin-bottom: 15rpx;
	}

	.teacher-name {
		font-size: 28rpx;
		color: #FFF;
	}

	.progress-section {
		width: calc(100% - 60rpx);
		margin: 40rpx auto;
	}

	.time-display {
		display: flex;
		justify-content: space-between;
		margin-top: 10rpx;
	}

	.time {
		font-size: 24rpx;
		color: #999;
	}

	.controls-section {
		width: 100%;
		display: flex;
		justify-content: space-around;
		align-items: center;
		padding: 40rpx 0;
	}

	.control-btn {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 10rpx;
	}

	.btn-text {
		font-size: 24rpx;
		color: #666;
	}

	.play-btn {
		transform: scale(1.2);
	}

	.popup-content {
		max-height: 60vh;
		background: rgba(1, 3, 11, 0.5);
		border-radius: 24rpx 24rpx 0 0;
	}

	.popup-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		// border-bottom: 1rpx solid #eee;
		color: #FFFFFF;
	}

	.title {
		font-size: 30rpx;
	}

	.popup-scroll {
		max-height: 50vh;
	}

	.course-item {
		display: flex;
		justify-content: space-between;
		align-items: start;
		padding: 30rpx;
		// border-bottom: 1rpx solid #f5f5f5;
	}

	.course-item.active {
		// background: #f0f7ff;
	}

	.course-item.locked {
		opacity: 0.6;
	}

	.item-left {
		display: flex;
		align-items: start;
		gap: 20rpx;
	}

	.item-index {
		// width: 40rpx;
		// height: 40rpx;
		// background: #f0f0f0;
		// border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		color: #FFF;
	}

	.item-info {
		display: flex;
		flex-direction: column;
	}

	.item-name {
		font-size: 28rpx;
		color: #FFF;
		margin-bottom: 8rpx;
	}

	.item-teacher {
		font-size: 24rpx;
		color: #FFF;
	}

	.lock-tag {
		font-size: 28rpx;
	}

	.timer-list {
		padding: 20rpx 0;
	}

	.timer-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		// border-bottom: 1rpx solid #f5f5f5;
		color: #FFFFFF;
		font-size: 28rpx;
		font-weight: 400;
	}

	.timer-item.active {
		color: #FFFFFF;
	}

	.timer-item.cancel {
		justify-content: center;
		color: #999;
	}

	.vip-popup {
		width: 560rpx;
		padding: 40rpx;
		text-align: center;
	}

	.vip-title {
		display: block;
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 20rpx;
	}

	.vip-desc {
		display: block;
		font-size: 28rpx;
		color: #666;
		margin-bottom: 40rpx;
	}

	.vip-actions {
		display: flex;
		gap: 20rpx;
	}
</style>