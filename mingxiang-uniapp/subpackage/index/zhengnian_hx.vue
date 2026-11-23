<template>
	<view class="alarm-page nap">
		<!-- <view class="sz_picker" v-if="!isRunning">
			<up-picker :show="szShow" :columns="columns" :pageInline="true" :showToolbar="false" :itemHeight="75"
				:visibleItemCount="3" @change="onConfirm"></up-picker>
			<view class="cFF sz_picker_unit">分钟</view>
		</view> -->
		<view style="height: 120rpx;"></view>
		<view class="circle-picker" v-if="!isRunning">
			<CustomPicker :list="columns" :defaultIndex="defaultIndex" unit="分钟" :itemHeight="80" @change="onTimeChange" />
		</view>

		<view class="sz_picker" v-if="isRunning">
			<view class="breathe-stage" :class="{ running: isRunning }">
				<view class="wave-border"></view>
				<view class="stage_">
					<view class="stage-label" :style="{ transform: 'scale(' + scaleValue + ')' }">{{ phaseLabel }}</view>
					<view class="stage-count">{{ phaseCountdown }}<text class="unit">秒</text></view>
				</view>
			</view>
			<view class="total-remaining">{{ formattedTime }}</view>
		</view>
		<view class="setup-section" @click="startBreathe">
			<image :src="btnText == '结束呼吸'?'../static/js.png':'/static/image/index/hx.png'" mode="" class="ksimg">
			</image>
			<view class="ksText">{{ btnText }}</view>
		</view>
		<SubPagePlayer :playlist="musicList" :on-play-pause="handlePlayPause" :on-switch="handleSwitch"
			v-if="playerStore.isPlaying||showMusic == true" />
		<!-- 底部-->
		<view class="footer_fixed" v-if="!playerStore.isPlaying&&showMusic == false">
			<view class="flex align-center margin-lr-xl justify-between" style="margin-top: 250rpx;">
				<view class="" @click="zzMsShow = true">
					<image src="/subpackage/static/zhuanzhu/ms.png" mode="" class="ksimg"></image>
					<view class="f26 cFF text-center margin-top-sm">模式</view>
				</view>
				<view class="" @click="routerGo('/subpackage/index/sleep_music')">
					<image src="/subpackage/static/yy.png" mode="" class="ksimg"></image>
					<view class="f26 cFF text-center margin-top-sm">音乐</view>
				</view>
			</view>
		</view>
		<!-- 呼吸模式弹窗 -->
		<up-popup :show="zzMsShow" @close="zzMsShow = false" round="12" :closeable="true">
			<view class="dsBox" style="background-color: #FFFFFF; padding-bottom: 20rpx;">
				<view class="f30 c66 padding-left padding-top" style="font-size: 32rpx; font-weight: bold;">呼吸模式</view>
				<view class="margin-lr padding-bottom padding-top">
					<up-radio-group v-model="radiovalue1" placement="column" @change="groupChange" iconPlacement="right">
						<up-radio :customStyle="{marginBottom: '16px'}" v-for="(item, index) in radiolist2" :key="index"
							:label="item.name" :name="item.name" @change="radioChange" activeColor="#4E6BA8" iconColor="#FFFFFF"
							inactiveColor="#4E6BA8">
							<template #label>
								<view class="flex align-center" style="padding: 10rpx 0;">
									<image :src="item.img" mode="aspectFit" style="width: 66rpx; height: 66rpx; border-radius: 8rpx;">
									</image>
									<view class="f30 margin-left-xs" style="font-size: 30rpx; color: #333;">
										{{ item.label }}
									</view>
								</view>
							</template>
						</up-radio>
					</up-radio-group>
				</view>
			</view>
		</up-popup>
	</view>

</template>

<script setup>
	import {
		ref,
		reactive,
		computed,
		onMounted,
		onUnmounted,
		getCurrentInstance
	} from 'vue'
	import {
		onLoad,
		onShow,
		onHide
	} from '@dcloudio/uni-app';
	import {
		useAlarmStore
	} from '@/stores/alarm'
	import {
		usePlayerStore
	} from '@/stores/player'
	import SubPagePlayer from '@/components/player/SubPagePlayer.vue'
	import CustomPicker from '@/components/CustomPicker.vue'
	const {
		proxy
	} = getCurrentInstance();


	// 默认选中时间下标
	const defaultIndex = ref(0);
	const selectedValue = ref(10);
	// 时间选择
	const onTimeChange = ({
		index,
		value
	}) => {
		selectedIndex.value = index
		selTime.value = value
		countdownTotal.value = parseInt(selTime.value) * 60;
	};

	// 音乐播放器逻辑

	const showMusic = ref(false);

	const playerStore = usePlayerStore()
	const musicList = ref([])
	// 获取音乐列表
	const getList = () => {
		uni.showLoading({
			title: "加载中..."
		})
		proxy.$http.getT('/app/soundEffects/selectSoundEffectsList?pageNum=1&pageSize=50').then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				musicList.value = res.data.list.map(item => ({
					id: item.id,
					effectsName: item.effectsName,
					bgImg: item.bgImg,
					audioUrl: item.filePath,
					...item,
				}))
				// 如果当前正在播放音乐，检查是否在列表中
				if (playerStore.currentType === 'music' && playerStore.currentAudio) {
					const index = musicList.value.findIndex(
						item => item.id === playerStore.currentAudio.id
					)
					// 如果找到了，更新播放列表（保持当前播放）
					if (index !== -1) {
						playerStore.playlist = musicList.value
					}
				} else {
					playerStore.playlist = musicList.value
					playerStore.setPlaylist(musicList.value, 'music', 0)
				}
			}
		});
	};
	// 是否是当前播放的音乐
	const isCurrentMusic = (item) => {
		return playerStore.currentType === 'music' &&
			playerStore.currentAudio?.id === item.id
	}

	// 点击播放音乐
	const playMusic = (item, index) => {
		// 互斥：如果正在播放课程，会停止课程
		playerStore.setPlaylist(musicList.value, 'music', index)
	}

	const handlePlayPause = (action, item, type) => {}

	const handleSwitch = (action, item, type) => {}

	const selectedIndex = ref(0);

	const bgColor = ref('transparent');

	const szShow = ref(true);

	// 平衡呼吸
	const balanceConfig = [{
			name: 'inhale',
			label: '吸气',
			duration: 8,
			tip: '用鼻子缓慢吸气...'
		},
		{
			name: 'exhale',
			label: '呼气',
			duration: 8,
			tip: '用鼻子缓慢呼气...'
		}
	];

	const mode478Config = [{
			name: 'inhale',
			label: '吸气',
			duration: 4,
			tip: '用鼻子静静吸气'
		},
		{
			name: 'hold',
			label: '憋气',
			duration: 7,
			tip: '屏住呼吸，保持放松'
		},
		{
			name: 'exhale',
			label: '呼气',
			duration: 8,
			tip: '用嘴缓缓吐气，发出嘶嘶声'
		}
	];


	const radiolist2 = [{ //吸气跟呼气时间一样
			name: 'balance',
			label: '平衡呼吸法',
			img: '/subpackage/static/kc/ph.png',
			config: balanceConfig
		},
		{
			name: '478',
			label: '4-7-8 呼吸法',
			img: '/subpackage/static/kc/noph.png',
			config: mode478Config
		}
	];


	const zzMsShow = ref(false);
	const radiovalue1 = ref('balance');

	const columns = reactive(['10', '15', '20', '25', '30', '35', '40']);
	const selTime = ref(10);
	const countdownTotal = ref(parseInt(selTime.value) * 60);

	const isRunning = ref(false);
	const btnText = ref('开始呼吸');

	const currentPhaseIndex = ref(0);
	const phaseCountdown = ref(0);
	const phaseLabel = ref('');
	const phaseTip = ref('准备开始');

	let timer = null;
	let currentPattern = balanceConfig;

	// 当前缩放比例
	const scaleValue = ref(1);
	// 动画 ID
	let animationFrameId = null;
	// 当前阶段开始时间戳
	let phaseStartTime = 0;
	// 当前阶段总时长 (ms)
	let phaseDuration = 0;
	// 起始缩放
	let startScale = 1;
	// 目标缩放
	let endScale = 1;

	const formattedTime = computed(() => {
		const totalSeconds = countdownTotal.value;
		const minutes = Math.floor(totalSeconds / 60);
		const seconds = totalSeconds % 60;
		return `${minutes < 10 ? '0' + minutes : minutes}:${seconds < 10 ? '0' + seconds : seconds}`;
	});

	const resetPhaseState = () => {
		if (!currentPattern || currentPattern.length === 0) return;
		const firstStep = currentPattern[0];
		currentPhaseIndex.value = 0;
		phaseLabel.value = firstStep.label;
		phaseTip.value = firstStep.tip;
		phaseCountdown.value = firstStep.duration;

		// 重置时也将缩放归位
		scaleValue.value = 1;
	};

	const groupChange = (e) => {
		const selectedName = e;
		const selectedItem = radiolist2.find(item => item.name === selectedName);
		if (selectedItem) {
			currentPattern = selectedItem.config;
			resetPhaseState();
		}
	};

	const changePickerTime = (e) => {
		if (isRunning.value) {
			uni.showToast({
				title: '呼吸进行中，无法切换时间',
				icon: 'none'
			});
			return;
		}
		const val = e.value || e;
		selTime.value = String(val);
		countdownTotal.value = parseInt(val) * 60;
		resetPhaseState();
	};

	/**
	 * 初始化当前阶段的动画参数
	 * 在阶段切换时调用
	 */
	const initPhaseAnimation = () => {
		if (!isRunning.value) return;

		const currentPhase = currentPattern[currentPhaseIndex.value];
		phaseDuration = currentPhase.duration * 1000; // 转毫秒
		phaseStartTime = Date.now();

		// 定义缩放的起点和终点
		if (currentPhase.name === 'inhale') {
			// 吸气：从 0.8 (小) -> 1.4 (大) [持续变大]
			startScale = 0.8;
			endScale = 1.4;
		} else if (currentPhase.name === 'exhale') {
			// 呼气：从 1.4 (大) -> 0.8 (小) [持续变小]
			startScale = 1.4;
			endScale = 0.8;
		} else {
			// 其他（如憋气）：保持当前值
			startScale = scaleValue.value;
			endScale = scaleValue.value;
		}

		// 动画循环
		if (animationFrameId) cancelAnimationFrame(animationFrameId);
		animateScale();
	};

	/**
	 * 动画循环函数
	 */
	const animateScale = () => {
		if (!isRunning.value) return;

		const now = Date.now();
		const elapsed = (now - phaseStartTime) / 1000; // 已过秒数

		// 计算进度 (0.0 ~ 1.0)
		let progress = elapsed / (phaseDuration / 1000);
		if (progress > 1) progress = 1;

		// 【线性插值公式】：当前值 = 起点 + (终点 - 起点) * 进度
		scaleValue.value = startScale + (endScale - startScale) * progress;

		// 如果阶段还没结束，请求下一帧
		if (progress < 1) {
			animationFrameId = requestAnimationFrame(animateScale);
		}
	};

	// 开始呼吸
	const startBreathe = () => {
		console.log(isRunning.value, "isRunning.value")
		if (isRunning.value) {
			stopBreatheLogic(false);
			return;
		}

		if (!selTime.value) {
			uni.showToast({
				title: '请选择练习时长',
				icon: 'none'
			});
			return;
		}

		let data = {
			userId: uni.getStorageSync('userId'),
			// duration: parseInt(selTime.value), 
			// mode: radiovalue1.value
		};
		proxy.$http.postJson('/app/courseLearn/startBreathe', data).then(res => {
			if (res.code == 0) {
				isRunning.value = true;
				btnText.value = '结束呼吸';
				uni.setStorageSync('breatheId', res.data)
				resetPhaseState();
				startTimer();
				initPhaseAnimation(); // 初始化动画
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		});
	};

	const startTimer = () => {
		if (timer) clearInterval(timer);
		timer = setInterval(() => {
			if (countdownTotal.value > 0) {
				countdownTotal.value--;
			} else {
				finishBreatheLogic();
				return;
			}

			if (phaseCountdown.value > 0) {
				phaseCountdown.value--;
			}

			if (phaseCountdown.value === 0) {
				switchNextPhase();
			}
		}, 1000);
	};

	const switchNextPhase = () => {
		let nextIndex = currentPhaseIndex.value + 1;
		if (nextIndex >= currentPattern.length) {
			nextIndex = 0;
		}
		currentPhaseIndex.value = nextIndex;
		const step = currentPattern[nextIndex];
		phaseLabel.value = step.label;
		phaseTip.value = step.tip;
		phaseCountdown.value = step.duration;

		// 重新初始化动画参数
		initPhaseAnimation();
	};

	// 结束呼吸
	const stopBreatheLogic = (isFinished) => {
		let data = {
			userId: uni.getStorageSync('userId'),
			id: uni.getStorageSync('breatheId')
		};
		proxy.$http.postJson('/app/courseLearn/endBreathe', data).then(res => {
			if (res.code == 0) {
				if (timer) {
					clearInterval(timer);
					timer = null;
				}
				// 清除动画
				if (animationFrameId) {
					cancelAnimationFrame(animationFrameId);
					animationFrameId = null;
				}

				isRunning.value = false;
				btnText.value = '开始呼吸';
				countdownTotal.value = parseInt(selTime.value) * 60;
				scaleValue.value = 1; // 重置大小
				resetPhaseState();

				uni.showToast({
					title: isFinished ? '呼吸练习完成' : '已停止',
					icon: isFinished ? 'success' : 'none'
				});
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		});
	};

	const finishBreatheLogic = () => {
		stopBreatheLogic(true);
	};

	const routerGo = (url) => {
		uni.navigateTo({
			url
		});
	};

	// 页面卸载时清理
	onHide(() => {
		if (timer) clearInterval(timer);
		if (animationFrameId) cancelAnimationFrame(animationFrameId);
	});

	onLoad(() => {
		console.log(playerStore.isPlaying, "playerStore")
		getList()
	});
	onShow(() => {
		resetPhaseState();
	});
</script>

<style lang="scss" scoped>
	.circle-picker {
		width: 500rpx;
		height: 500rpx;
		border-radius: 50%;
		border: 1px solid rgba(255, 255, 255, 0.3);
		margin: auto;
	}

	/* 呼吸展示区 */
	.breathe-stage {
		border-radius: 50%;
		border: solid 0.0625rem #FFFFFF;
		width: 15.625rem;
		height: 15.625rem;
		margin: auto;
		position: relative;
		/* 防止放大时溢出容器，如果需要文字超出圆圈显示，可改为 visible */
		overflow: visible;
	}

	.stage_ {
		margin: auto;
		width: max-content;
		text-align: center;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		/* 确保子元素变换原点正确 */
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	/* 【核心修改】文字样式 */
	.stage-label {
		font-size: 50rpx;
		font-weight: bold;
		color: #FFFFFF;
		margin-bottom: 20rpx;

		/* 关键：设置变换原点为中心 */
		transform-origin: center center;

		/* 
		   注意：这里去掉了 transition！
		   因为我们用 requestAnimationFrame 每一帧都在强制更新 scale 值。
		   如果加了 transition，JS 的更新会和 CSS 过渡冲突，导致动画卡顿或滞后。
		*/
		will-change: transform;
		/* 性能优化：告诉浏览器这个属性会频繁变化 */
	}

	.stage-count {
		font-size: 36rpx;
		font-weight: 500;
		color: #FFFFFF;
		line-height: 1;
	}

	.unit {
		font-size: 36rpx;
		font-weight: 500;
		margin-left: 5rpx;
	}

	.stage-tip {
		text-align: center;
		margin-top: 30rpx;
		font-size: 28rpx;
		color: #FFFFFF;
		padding: 0 40rpx;
		line-height: 1.5;
	}

	.total-remaining {
		color: #FFFFFF;
		font-size: 40rpx;
		text-align: center;
		margin-top: 50rpx;
	}

	.alarm-page {
		min-height: 100vh;
		padding: 0rpx 40rpx;
	}

	.footer_fixed {
		position: fixed;
		bottom: 95rpx;
		width: calc(100% - 130rpx);
		left: 0;
		right: 0;
		margin: auto;
	}

	.ksimg {
		width: 120rpx;
		height: 120rpx;
		margin: auto;
		display: block;
	}

	.ksText {
		font-size: 26rpx;
		color: #9FA7C5;
		text-align: center;
		margin-top: 20rpx;
	}

	.sz_picker_unit {
		position: absolute;
		right: 28%;
		font-size: 40rpx;
		top: 59%;
		/* #ifdef MP-WEIXIN */
		top: 54%;
		/* #endif */
		font-size: 40rpx;
	}

	.sz_picker {
		position: relative;
		// padding-top: 170rpx;
	}

	.timePicker {
		margin: 0 150rpx;
		// padding-top: 200rpx;
		// position: relative;
		position: fixed;
		top: 200rpx;
		z-index: 99;
		width: 60%;
	}



	:deep(.uni-picker-view-mask) {
		background-image: none;
	}

	:deep(.u-picker-wraper) {}

	:deep(.u-popup__content) {
		background: transparent !important;
	}

	:deep(.u-picker__view) {
		height: 264px !important;
	}

	:deep(.uni-picker-view-wrapper) {
		border-radius: 50%;
		border: solid 2rpx #FFFFFF;
		width: 500rpx;
		height: 500rpx;
		margin: auto;
	}

	/* #ifdef MP-WEIXIN */
	.u-picker-wraper .u-picker__view ::before,
	.u-picker-wraper .u-picker__view ::after {
		background: none !important;
	}

	/* 小程序 - 覆盖 picker-view 遮罩 */
	::v-deep .u-picker__view ::before,
	::v-deep .u-picker__view ::after {
		background: none !important;
	}

	/* 或更精确 */
	.u-picker-wraper .u-picker__view .u-picker__mask {
		background: none !important;
	}

	/* #endif */
	/* 覆盖 picker-view 遮罩 */
	::v-deep .u-picker__view .u-picker__mask {
		background: none !important;
	}

	::v-deep .u-picker__view ::before,
	::v-deep .u-picker__view ::after {
		background: none !important;
	}

	/* #ifdef MP-WEIXIN */
	:deep(.u-picker__view) {
		background: transparent !important;
		border-radius: 50%;
		border: solid 2rpx #FFFFFF;
		width: 500rpx;
		height: 500rpx;
		margin: auto;
		overflow: hidden;
	}

	:deep(.u-picker__view__column) {
		background: transparent !important;
		width: 100%;
		height: 100%;
	}

	:deep(.u-picker__view__column__item) {
		color: #7F93BD !important;
		font-size: 86rpx !important;
	}

	:deep(.u-picker__view__column__item--selected) {
		font-size: 86rpx !important;
		font-weight: 100 !important;
		color: #FFFFFF !important;
	}

	:deep(.uni-picker-view-indicator:before) {
		border: none !important;
	}

	/* #endif */


	:deep(.u-picker__view__column__item) {
		color: #7F93BD;
		font-size: 86rpx;
		font-family: Arial, Helvetica, sans-serif;
	}

	:deep(.u-picker__view__column__item--selected) {
		font-size: 86rpx;
		font-weight: 100 !important;
		color: #FFFFFF;
		font-family: Arial, Helvetica, sans-serif;
	}

	:deep(.uni-picker-view-indicator:before) {
		border: none;
	}


	:deep(.uni-picker-view-indicator:after) {
		border: none;
	}

	/* 根据注释修改背景色：日间小憩用粉色渐变 */
	.nap {
		background: linear-gradient(126deg, #234585, #151F45);
	}

	.alarm-header {
		text-align: center;
		margin-bottom: 60rpx;
	}

	.alarm-title {
		display: block;
		font-size: 48rpx;
		font-weight: bold;
		color: #fff;
		margin-bottom: 20rpx;
	}

	.alarm-subtitle {
		font-size: 28rpx;
		color: rgba(255, 255, 255, 0.8);
	}

	.time-display {
		text-align: center;
		margin: 80rpx 0;
	}

	.time-text {
		font-size: 120rpx;
		font-weight: 200;
		color: #fff;
		font-variant-numeric: tabular-nums;
		text-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.2);
	}

	.setup-section {
		padding-top: 170rpx;
	}

	.time-selector {
		background: rgba(255, 255, 255, 0.2);
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		color: #fff;
		backdrop-filter: blur(10rpx);
	}

	.picker-value {
		display: flex;
		align-items: center;
		gap: 10rpx;
		font-size: 32rpx;
	}

	.running-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 60rpx;
		padding-top: 170rpx;
	}

	.progress-ring {
		width: 400rpx;
		height: 400rpx;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.1);
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
		backdrop-filter: blur(10rpx);
	}

	.progress-circle {
		position: absolute;
		width: 100%;
		height: 100%;
		border-radius: 50%;
		border: 8rpx solid transparent;
		border-top-color: #fff;
		border-right-color: rgba(255, 255, 255, 0.3);
		transition: transform 0.5s linear;
	}

	.progress-inner {
		text-align: center;
	}

	.status-text {
		font-size: 32rpx;
		color: rgba(255, 255, 255, 0.8);
		display: block;
		margin-bottom: 10rpx;
	}

	.remaining-text {
		font-size: 48rpx;
		color: #fff;
		font-weight: bold;
	}
</style>