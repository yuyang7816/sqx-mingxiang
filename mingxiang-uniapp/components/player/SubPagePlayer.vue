<!-- components/player/SubPagePlayer.vue -->
<template>
	<view v-if="playerStore.currentAudio && playerStore.currentType === 'music'" class="sub-page-player">
		<view class="player-bar" @click="handleClick">
			<image class="cover" :src="playerStore.currentAudio.bgImg" mode="aspectFill" />
			<view class="info">
				<text class="name">{{ playerStore.currentAudio.effectsName }}</text>
				<text class="sub">背景音乐</text>
			</view>
			<view class="actions">
				<view class="btn" @click.stop="handlePlayPause">
					<up-icon :name="playerStore.isPlaying ? 'pause' : 'play-right-fill'" size="20"
						color="rgba(165, 175, 200, 1)"></up-icon>
				</view>
				<view class="btn" @click.stop="showPlaylist = true">
					<!-- <up-icon name="grid-fill" size="22" color="rgba(165, 175, 200, 1)"></up-icon> -->
					<image src="/subpackage/static/lb.png" mode="" style="width: 40rpx;height: 38rpx;"></image>
				</view>
			</view>
		</view>

		<!-- 播放列表 -->
		<up-popup :show="showPlaylist" mode="bottom" @close="showPlaylist = false" bgColor="transparent">
			<view class="playlist-popup">
				<view class="popup-header">
					<text class="title">播放列表({{ playlist.length }})</text>
					<up-icon name="close" size="20" @click="showPlaylist = false"></up-icon>
				</view>
				<scroll-view scroll-y class="playlist-scroll">
					<view v-for="(item, index) in playlist" :key="item.id" class="playlist-item"
						:class="{ active: isCurrentItem(item) }" @click="handleSwitchItem(index)">
						<view class="item-info">
							<image class="item-cover" :src="item.bgImg" mode="aspectFill" />

						</view>
						<view class="flex align-center justify-center">
							<view class="item-name">{{ item.effectsName }}</view>
							<!-- 当前选中的展示这个 -->
							<!-- <up-icon v-if="isCurrentItem(item)" name="volume" size="16" color="#2979ff"></up-icon> -->
						</view>
					</view>
				</scroll-view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		computed
	} from 'vue'
	import {
		usePlayerStore
	} from '@/stores/player'

	const props = defineProps({
		playlist: {
			type: Array,
			default: () => []
		},
		onPlayPause: {
			type: Function,
			required: true
		},
		onSwitch: {
			type: Function,
			required: true
		}
	})

	const playerStore = usePlayerStore()
	const showPlaylist = ref(false)

	// 是否是当前项
	const isCurrentItem = (item) => {
		return playerStore.currentAudio?.id === item.id
	}

	// 播放/暂停
	const handlePlayPause = async () => {
		const action = playerStore.isPlaying ? 'pause' : 'play'
		const res = await props.onPlayPause(action, playerStore.currentAudio, 'music')
		if (res !== false) {
			playerStore.togglePlay()
		}
	}

	// 切换音乐
	const handleSwitchItem = async (index) => {
		const item = props.playlist[index]

		// 如果点击的是当前项，不做切换
		if (isCurrentItem(item)) {
			showPlaylist.value = false
			return
		}

		await props.onSwitch('switch', item, 'music')
		playerStore.setPlaylist(props.playlist, 'music', index)
		showPlaylist.value = false
	}

	const handleClick = () => {
		// 音乐页面不需要跳转
	}
</script>

<style>
	/* 样式保持不变 */
	.sub-page-player {
		position: fixed;
		bottom: 65rpx;
		left: 0;
		right: 0;
		z-index: 99999;
		width: calc(100% - 60rpx);
		margin: auto;
		border-radius: 24rpx;
	}

	.player-bar {
		height: 100rpx;
		background: rgba(255, 255, 255, 0.3);
		display: flex;
		align-items: center;
		padding: 20rpx 30rpx;
		box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
		border-radius: 24rpx;
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
		font-size: 28rpx;
		color: #FFF;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.sub {
		font-size: 22rpx;
		color: rgba(255, 255, 255, 0.3);
		margin-top: 4rpx;
	}

	.actions {
		display: flex;
		gap: 30rpx;
	}

	.btn {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.playlist-popup {
		max-height: 60vh;
		background: rgba(1, 3, 11, 0.5);
		border-radius: 20rpx 20rpx 0 0;
	}

	.popup-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		/* border-bottom: 1rpx solid #eee; */
	}

	.title {
		font-size: 32rpx;
		/* font-weight: bold; */
		color: #FFFFFF;
	}

	.playlist-scroll {
		max-height: 50vh;
		width: calc(100% - 60rpx);
		margin: auto;
	}

	.playlist-item {
		display: inline-block;
		width: calc((100% / 3) - 20rpx);
		margin-right: 20rpx;
		margin-bottom: 20rpx;
		/* display: flex;
		justify-content: space-between;
		align-items: center; */
		/* padding: 20rpx 30rpx; */
		/* border-bottom: 1rpx solid #f5f5f5; */
	}

	.playlist-item:nth-child(3n) {
		margin-right: 0 !important;
	}

	.playlist-item.active {
		/* background: #f0f7ff; */
	}

	.item-info {
		/* 	display: flex;
		align-items: center; */
		gap: 20rpx;
	}

	.item-cover {
		width: 100%;
		height: 200rpx;
		border-radius: 12rpx;
	}

	.item-name {
		font-size: 28rpx;
		color: #FFFFFF;
		text-align: center;
		font-weight: 400 !important;
	}
</style>