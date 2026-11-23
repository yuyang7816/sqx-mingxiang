<template>
	<view class="container_back">
		<!-- #ifndef H5 -->
		<up-navbar title="夜间睡眠" :autoBack="true" :bgColor="bgColor" leftIconColor="#FFFFFF" titleStyle="color:#FFFFFF;">
		</up-navbar>
		<!-- #endif -->
		<SubPagePlayer :playlist="musicList" :on-play-pause="handlePlayPause" :on-switch="handleSwitch" />

	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		onMounted,
		onBeforeUnmount,
		onUnmounted,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onHide
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	import {
		usePlayerStore
	} from '@/stores/player'
	import SubPagePlayer from '@/components/player/SubPagePlayer.vue'

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


	const bgColor = ref('transparent');
	const nowTime = ref(''); //页面展示时间
	const endTime = ref('')
	const percentageValue = ref(0);
	const islong = ref(false);
	let progressTimer = null;

	// 模拟增加速度：每 50 毫秒增加 1% (可根据需求调整)
	const stop = ref(2);
	const time = ref(50);

	// 开始长按/触摸
	const handleTouchStart = () => {
		// 如果已经达到100%，先重置（可选逻辑，看需求）
		if (percentageValue.value >= 100) {
			percentageValue.value = 0;
		}

		// 防止重复启动定时器
		if (progressTimer) return;

		islong.value = true;
		console.log('长按开始');

		progressTimer = setInterval(() => {
			if (percentageValue.value < 100) {
				percentageValue.value += stop.value;

				// 检查是否达到或超过 100
				if (percentageValue.value >= 100) {
					percentageValue.value = 100; // 修正为准确的 100
					completeProgress();
				}
			}
		}, time.value);
	};

	// 结束长按/触摸（用户中途松手）
	const handleTouchEnd = () => {
		// 如果还没到 100%，用户松手了，我们停止计时器
		// 根据你的需求：如果希望松手后继续跑完，这里就不要调用 stopTimer
		if (percentageValue.value < 100) {
			stopTimer();
			islong.value = false;
			console.log('用户中途松手，停止增加');

			// 可选：中途松手是否重置进度？
			// percentageValue.value = 0; 
		}
	};

	// 完成进度（达到 100%）
	const completeProgress = () => {
		stopTimer();
		islong.value = false; // 核心需求：达到百分百 islong 改为 false
		console.log('已达到 100%，长按状态结束');
		uni.vibrateShort({
			type: 'heavy', // 可选值：heavy(重), medium(中), light(轻)。默认通常是 medium
			success: () => {
				console.log('震动成功');
			},
			fail: (err) => {
				// 部分模拟器或不支持的设备可能会失败，忽略即可
				console.log('震动失败或不支持', err);
			}
		});
		// 可以在这里触发后续逻辑，比如提交表单、跳转等
		// uni.showToast({ title: '加载完成', icon: 'success' });
	};

	// 清除定时器公用方法
	const stopTimer = () => {
		if (progressTimer) {
			clearInterval(progressTimer);
			progressTimer = null;
		}
	};

	// 页面卸载时清理定时器，防止内存泄漏
	onUnmounted(() => {
		stopTimer();
	});
	// 长按
	const onLongPress = () => {
		islong.value = true
		console.log('长按成功')
	};



	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	onMounted(() => {});
	// 页面卸载或 onHide 时清除
	onBeforeUnmount(() => {
		clearTimer();
	});
	onLoad((options) => {
		if (options.endTime) {
			endTime.value = options.endTime
		}
		getList()
	});
	onHide(() => {
		clearTimer();
	});
	onShow(() => {});
</script>


<style lang="scss" scoped>
	.container_back {
		background: linear-gradient(-31deg, #151F45 0%, #234585 100%);
		min-height: 100vh;
	}

	.popup-box_min {
		background-color: rgba(1, 3, 11, 0.5);
		border-radius: 24rpx 24rpx 0rpx 0rpx;
		width: calc(100% - 60rpx);
		margin: auto;
		padding: 30rpx;
		// background: #01030B;

		.popup-title_min {
			font-size: 31rpx;
			font-weight: bold;
			color: #FFFFFF;
			padding: 30rpx 0 0 0rpx;
			margin-bottom: 70rpx;
		}

		.popup-content_min {
			max-height: 600rpx;
			width: 100%;
		}

		.scroll-view_min {
			max-height: 500rpx;
		}

		/* 播放列表弹窗项 */
		.popup-item_min {
			margin-right: 15rpx;
			margin-bottom: 15rpx;
		}

		.popup-item_min:nth-child(3n) {
			margin-right: 0rpx !important;
		}
	}

	.footer {
		position: fixed;
		bottom: 80rpx;
		width: 100%;
		z-index: 99;
	}

	.music_tp {
		width: 81rpx;
		height: 81rpx;
		border-radius: 50%;
	}

	.music_box {
		background-color: #444C6A;
		width: calc(100% - 120rpx);
		border-radius: 24rpx;
		margin: auto;
		padding: 30rpx 25rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 150rpx;
	}

	.timePicker_yy {
		font-size: 86rpx;
		font-weight: bold;
		color: #FFFFFF;
		text-align: center;
	}

	.ks_box {
		width: 120rpx;
		height: 120rpx;
		margin: auto;
	}

	.ksimg {
		width: 120rpx;
		height: 120rpx;
	}

	.ksText {
		font-size: 26rpx;
		color: #9FA7C5;
		text-align: center;
	}

	.yjTime {
		background-color: #4C618F;
		color: #A2AECC;
		font-size: 26rpx;
		display: flex;
		align-items: center;
		width: max-content;
		padding: 15rpx 25rpx;
		border-radius: 50rpx;
		margin: auto;
		margin-top: 80rpx;

		image {
			width: 35rpx;
			height: 35rpx;
			margin-right: 15rpx;
		}
	}
</style>