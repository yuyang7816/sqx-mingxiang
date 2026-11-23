<!-- components/player/MiniPlayer.vue -->
<template>
	<view v-if="playerStore.currentAudio" class="mini-player" @click="handleClick">
		<image class="cover" :src="coverImage" mode="aspectFill" />
		<view class="info">
			<text class="name">{{ title }}</text>
			<!-- <text class="type">{{ typeText }}</text> -->
		</view>
		<view class="controls" @click.stop="handlePlayPause">
			<up-icon :name="playerStore.isPlaying ? 'pause' : 'play-right-fill'" size="15" color="#333"></up-icon>
		</view>
	</view>
</template>

<script setup>
	import {
		computed
	} from 'vue'
	import {
		usePlayerStore
	} from '@/stores/player'

	const props = defineProps({
		onPlayPause: {
			type: Function,
			required: true
		},
		onClick: {
			type: Function,
			default: null
		}
	})

	const playerStore = usePlayerStore()

	// 封面图
	const coverImage = computed(() => {
		const item = playerStore.currentAudio
		if (!item) return ''
		return item.bgImg || item.coverImage || item.cover || ''
	})

	// 标题
	const title = computed(() => {
		const item = playerStore.currentAudio
		if (!item) return ''
		return item.effectsName || item.title || item.name || ''
	})

	// 类型文本
	const typeText = computed(() => {
		return playerStore.currentType === 'music' ? '音乐' : '课程'
	})

	// 播放/暂停
	const handlePlayPause = async () => {
		const action = playerStore.isPlaying ? 'pause' : 'play'
		const res = await props.onPlayPause(action, playerStore.currentAudio, playerStore.currentType)
		if (res !== false) {
			playerStore.togglePlay()
		}
	}

	// 点击跳转
	const handleClick = () => {
		if (props.onClick) {
			props.onClick(playerStore.currentAudio, playerStore.currentType)
		}
	}
</script>

<style scoped>
	.mini-player {
		position: fixed;
		/* #ifdef MP-WEIXIN */
		bottom: 0rpx;
		/* #endif */
		/* #ifndef MP-WEIXIN */
		bottom: 88rpx;
		/* #endif */
		left: 0;
		right: 0;
		height: 100rpx;
		background: #F7F8FF;
		display: flex;
		align-items: center;
		padding: 0 30rpx;
		box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
		z-index: 999;
	}

	.cover {
		width: 70rpx;
		height: 70rpx;
		border-radius: 50%;
		margin-right: 20rpx;
	}

	.info {
		flex: 1;
		display: flex;
		flex-direction: column;
	}

	.name {
		font-size: 26rpx;
		color: #333;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.type {
		font-size: 22rpx;
		color: #999;
		margin-top: 4rpx;
	}

	.controls {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>