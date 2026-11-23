<!-- pages/player/course.vue -->
<template>
	<view class="container_box">
		<FullPlayer :course-list="courseList" :current-index="currentIndex" :is-vip="isVip" :on-play-pause="handlePlayPause"
			:on-switch="handleSwitch" :on-seek="handleSeek" :on-timer="handleTimer" :on-open-vip="handleOpenVip" />
	</view>
</template>


<script setup>
	import {
		ref,
		onMounted,
		computed,
		onUnmounted,
		getCurrentInstance,
		reactive
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance();
	import {
		usePlayerStore
	} from '@/stores/player'
	import FullPlayer from '@/components/player/FullPlayer.vue'

	const playerStore = usePlayerStore()
	const courseList = reactive([])
	const currentIndex = ref(0)
	// 获取正在播放内容和播放列表
	const getDetail = () => {
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/courseInfo/selectCourseInfoByDetail?id=' + courseId.value).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				id.value = res.data.id

				res.data.chapterInfoList.map((item, index) => {
					if (chapterId.value == item.id) {
						currentIndex.value = index
					} else {
						// 匹配当前章节
						const matchedIndex = courseList.findIndex(
							item => item.chapterId == chapterId
						)
						currentIndex.value = matchedIndex !== -1 ? matchedIndex : 0
					}
					courseList.push({
						chapterId: item.id, // 映射 chapterId
						title: item.chapterName, // 映射标题
						teacherName: res.data.mentorTitle, // 映射导师
						coverImage: res.data.mentorPhoto,
						audioUrl: item.chapterUrl,
						isVip: item.chapterCharge == 1 && isVip.value == false ? true : false
						// duration: 0
					})
					console.log(item.chapterCharge == 1 && isVip.value == false,
						"item.chapterCharge == 1 && isVip.value == false")
				})


				console.log(courseList, "courseListcourseList")

				// 互斥播放课程（会自动停止音乐）
				playerStore.setPlaylist(courseList, 'course', currentIndex.value)
				playerStore.setVipStatus(isVip.value)
				handlePlayPause()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};

	const id = ref('');
	const handlePlayPause = async (action, item, type) => {
		console.log(item, "itemitem")
		let url = '';
		if (action == 'pause') { //暂停
			url = '/app/courseLearn/endMeditation'
		} else {
			url = '/app/courseLearn/startMeditation'
		}
		await proxy.$http.postJson(url, {
			chapterId: chapterId.value,
			id: action == 'pause' ? uni.getStorageSync('courseId') : ''
		}).then(res => {
			if (res.code == 0) {
				if (action != 'pause') {
					uni.setStorageSync('courseId', res.data)
				}
				return true
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	const handleSwitch = async (action, item, type) => {
		// if (action === 'vipRequired') return
		currentIndex.value = playerStore.currentIndex
	}

	const handleSeek = async (action, data, type) => {
		// 拖动进度条
	}

	const handleTimer = async (action, data, type) => {
		const url = action === 'timerCancel' ?
			'/api/course/timer-cancel' :
			'/api/course/timer'
		// await uni.request({
		// 	url: `https://your-api.com${url}`,
		// 	method: 'POST',
		// 	header: {
		// 		'Authorization': uni.getStorageSync('token')
		// 	},
		// 	data: {
		// 		chapterId: playerStore.currentAudio.chapterId,
		// 		minutes: data?.minutes || 0
		// 	}
		// })
	}

	const handleOpenVip = () => {
		uni.navigateTo({
			url: '/my/vip/vip'
		})
	}

	const isVip = ref(false);
	const getUserInfo = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/user/selectInfoByUserId', data).then(res => {
			if (res.code == 0) {
				if (res.data.isVip && res.data.isVip == 1) {
					isVip.value = true;
				} else {
					isVip.value = false;
				}
				getDetail()
			}
		})
	}


	const courseId = ref('');
	const chapterId = ref('');
	onLoad((options) => {
		courseId.value = options.id
		chapterId.value = options.chapterId
		getUserInfo()
	})
</script>


<style scoped>
	.container_box {
		width: 100%;
		position: relative;
		height: 100vh;
		background-image: url('/subpackage/static/kc/bg.png');
		background-repeat: no-repeat;
		background-size: 100% 100%;
	}
</style>