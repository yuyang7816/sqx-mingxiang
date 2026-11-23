<template>
	<view class="">
		<!-- #ifndef H5 -->
		<up-navbar :title="titlePage" :autoBack="true" :bgColor="bgColor"></up-navbar>
		<!-- #endif -->
		<view class="topBox">
			<image :src="detailInfo.backgroundImage" mode="aspectFill" class="topImg"></image>
		</view>
		<view class="containerBox">
			<view class="flex align-center justify-between">
				<view class=" flex align-center flex-wrap" style="width: calc(100% - 80rpx);">
					<view class="tag margin-bottom-xs" v-for="(item,index) in detailInfo.labelNames">{{item}}</view>
				</view>
				<image src="/subpackage/static/aixin_1.png" @tap="saveLove" v-if="detailInfo.isLike && detailInfo.isLike == 1"
					style="width: 48rpx;height: 44rpx;"></image>
				<image src="/subpackage/static/aixin.png" @tap="saveLove" v-else style="width: 48rpx;height: 44rpx;">
				</image>
			</view>
			<view class="margin-top-xs">
				<view class="f42 text-bold">{{detailInfo.courseName}}</view>
				<!-- {{detailInfo.countChapter}}· -->
				<view class="f24 c99 margin-top-xs">{{detailInfo.courseTitle}}</view>
			</view>
			<view class="f26 c66 margin-top">
				{{detailInfo.courseDesc}}
			</view>
			<!-- 导师信息 -->
			<view class="flex align-center justify-between ds_Msg" @click="dsMsgShow = true">
				<view class="flex align-center">
					<image :src="detailInfo.mentorPhoto ? detailInfo.mentorPhoto : '/static/logo.png'" mode=""
						style="width: 80rpx;height: 80rpx;border-radius: 50%;">
					</image>
					<view class="margin-left">
						<view style="color: #B1B2BE;font-size: 26rpx;">{{detailInfo.mentorTitle}}</view>
						<view style="color: #6F8ADB;">{{detailInfo.mentor}}</view>
					</view>
				</view>
				<up-icon name="arrow-right" color="#6E87D7" size="12"></up-icon>
			</view>
			<view style="border-top: 1px solid #ECEDF4;margin: 40rpx 0;"></view>
			<!-- 课程列表 -->
			<view class="">
				<view class="flex align-center justify-between margin-bottom-lg" @click="goChapterInfo(item,index)"
					v-for="(item,index) in detailInfo.chapterInfoList">
					<view class="flex align-center">
						<view class="">{{index + 1}}</view>
						<view class="margin-left">
							<view class=" f28">{{item.chapterName}}</view>
							<view class="c99 f24">{{item.chapterDuration}}分钟</view>
						</view>
					</view>
					<image src="/subpackage/static/wjs.png" v-if="item.chapterCharge && item.chapterCharge == 1&&isVip == false"
						mode="" style="width: 52rpx;height: 52rpx;"></image>
					<!-- 当前播放且正在播放：显示暂停图标 -->
					<up-icon name="pause-circle-fill" size="30" color="#6F8ADB"
						v-else-if="isCurrentChapter(item) && playerStore.isPlaying"></up-icon>
					<!-- 当前播放但暂停：显示播放图标 -->
					<up-icon name="play-circle-fill" size="30" color="#6F8ADB" v-else-if="isCurrentChapter(item)"></up-icon>
					<!-- 其他：显示默认图标 -->
					<image src="/subpackage/static/kbf.png" mode="" style="width: 52rpx;height: 52rpx;" v-else></image>
				</view>
			</view>
			<!-- 相关推荐 -->
			<view class="text-bold">相关推荐</view>
			<scroll-view class="scroll-view_H margin-top" scroll-x="true">
				<view class="flex align-center ">
					<view class="xiangguan" style="margin-right: 15rpx;" v-for="item,index in courseInfoList" :key="index"
						@click="goCourseInfo(item)">
						<image :src="item.cover" mode="aspectFill" style="width: 245rpx;height: 227rpx;border-radius: 12rpx;">
						</image>
						<view class="padding-left-xs">
							<view class="f28 text-h">{{item.courseName||'\u00A0'}}</view>
							<view class="f24 c99">{{item.courseTitle||'\u00A0'}}</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<view style="height: 150rpx;"></view>
		<view class="footer">
			<!-- 底部播放按钮 - 与store同步 -->
			<view v-if="hasCurrentChapter" class="footer_btn" @click="handleFooterClick">
				<up-icon :name="playerStore.isPlaying ? 'pause' : 'play-right-fill'" size="12" color="#fff"></up-icon>
				<view class="">{{playerStore.isPlaying ? '正在练习' : '继续练习'}}{{currentChapterName}}</view>
			</view>
			<view class="footer_btn" @click="goChapterInfo(chapterInfo, 0)" v-else>
				<up-icon name="play-right-fill" color="#FFFFFF" size="12"></up-icon>
				<view class="">开始练习{{chapterInfo.chapterName}}</view>
			</view>
		</view>

		<!-- 导师信息弹窗 -->
		<up-popup :show="dsMsgShow" @close="dsMsgShow = false" :closeable="true" round="12">
			<view>
				<view class="f32 c99 text-center padding-top-lg margin-bottom-xl">{{detailInfo.mentorTitle}}</view>
				<view class="ds_msg_tk">
					<view style="width: 200rpx;height: 200rpx;border-radius: 50%;margin:0 auto;padding: 45rpx 0;">
						<image :src="detailInfo.mentorPhoto ? detailInfo.mentorPhoto : '/static/logo.png'"
							style="width: 100%;height: 100%;border-radius: 50%;">
						</image>
					</view>
					<view style="font-size: 34rpx;color: #4E6BCF;text-align: center;">{{detailInfo.mentor}}</view>
					<view class="f26 c99 padding-lr padding-tb-lg">
						{{detailInfo.mentorIntroduction}}
					</view>
				</view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		computed,
		watch
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	import {
		usePlayerStore
	} from '@/stores/player'

	const playerStore = usePlayerStore()

	const images = reactive(['/subpackage/static/tp.png', '/subpackage/static/tp.png', '/subpackage/static/tp.png',
		'/subpackage/static/tp.png', '/subpackage/static/gd_tx.png'
	])
	const bgColor = ref('transparent');
	const titlePage = ref('');
	const detailInfo = ref({
		id: '',
		chapterInfoList: []
	})
	const id = ref(''); //课程id
	const typeId = ref(''); //课程id
	const dsMsgShow = ref(false);
	const chapterInfoIndex = ref(0);
	const isDataLoaded = ref(false) // 数据加载完成标志

	// ========== 播放器状态同步 ==========

	// 监听store变化，强制刷新UI
	watch(() => playerStore.isPlaying, (newVal) => {
		console.log('播放状态变化:', newVal)
	})

	watch(() => playerStore.currentAudio, (newVal) => {
		console.log('当前音频变化:', newVal)
	})

	// 判断item是否是当前播放的章节（使用chapterId匹配，因为store里存的是chapterId）
	const isCurrentChapter = (item) => {
		if (!playerStore.currentAudio) return false
		if (playerStore.currentType !== 'course') return false

		// 使用chapterId匹配（store里存的是chapterId）
		const storeId = playerStore.currentAudio.chapterId || playerStore.currentAudio.id
		return storeId === item.id
	}

	// 判断当前播放的章节是否属于本课程
	const isCurrentCourseChapter = () => {
		if (!playerStore.currentAudio) return false
		if (playerStore.currentType !== 'course') return false
		// 数据还没加载完，直接返回true（因为onShow时已经知道有currentAudio）
		if (!isDataLoaded.value || !detailInfo.value.chapterInfoList) {
			// 尝试用courseId匹配（如果store里有courseId）
			const storeCourseId = playerStore.currentAudio.courseId || playerStore.playlist[0]?.courseId
			if (storeCourseId && storeCourseId == typeId.value) return true
			return false
		}

		// 检查当前播放的chapterId是否在课程列表中
		const storeId = playerStore.currentAudio.chapterId || playerStore.currentAudio.id
		const result = detailInfo.value.chapterInfoList.some(chapter => chapter.id === storeId)
		console.log('isCurrentCourseChapter check:', storeId, 'in list:', result)
		return result
	}

	// 计算属性：底部按钮是否显示当前播放的章节
	const hasCurrentChapter = computed(() => {
		const has = playerStore.currentType === 'course' &&
			playerStore.currentAudio &&
			isCurrentCourseChapter()
		console.log('hasCurrentChapter:', has, 'isDataLoaded:', isDataLoaded.value)
		return has
	})

	// 计算属性：当前章节名称
	const currentChapterName = computed(() => {
		if (playerStore.currentAudio) {
			return playerStore.currentAudio.chapterName || playerStore.currentAudio.title || ''
		}
		return chapterInfo.chapterName || ''
	})

	// 底部按钮点击处理
	const handleFooterClick = () => {
		if (!playerStore.currentAudio) return

		const storeId = playerStore.currentAudio.chapterId || playerStore.currentAudio.id

		// 如果数据已加载，找对应的index
		if (detailInfo.value.chapterInfoList && detailInfo.value.chapterInfoList.length > 0) {
			const index = detailInfo.value.chapterInfoList.findIndex(item => item.id === storeId)
			if (index !== -1) {
				goChapterInfo(detailInfo.value.chapterInfoList[index], index)
				return
			}
		}

		// 如果找不到，直接用store的数据跳转
		uni.navigateTo({
			url: '/subpackage/courseInfo/course_bf?id=' + typeId.value + '&chapterId=' + storeId
		})
	}

	// 进入课程详情
	const goChapterInfo = (item, index) => {
		chapterInfoIndex.value = index
		Object.assign(chapterInfo, item)

		// 如果点击的是当前正在播放的章节，直接跳转详情页（保持播放状态）
		if (isCurrentChapter(item)) {
			uni.navigateTo({
				url: '/subpackage/courseInfo/course_bf?id=' + typeId.value + '&chapterId=' + item.id
			})
			return
		}

		// VIP检查
		if (item.chapterCharge && item.chapterCharge == 1 && isVip.value == false) {
			uni.showModal({
				title: '提示',
				content: '需开通会员解锁课程',
				confirmText: '立即开通',
				success: function(res) {
					if (res.confirm) {
						uni.navigateTo({
							url: '/my/vip/vip'
						})
					}
				}
			})
			return
		}

		// 准备播放列表数据（使用原始数据格式，保持id一致）
		const playlist = detailInfo.value.chapterInfoList.map(ch => ({
			id: ch.id, // 原始id（用于匹配）
			chapterId: ch.id, // 兼容字段（store主要用这个）
			courseId: typeId.value, // 课程id，用于判断所属课程
			title: ch.chapterName, // 映射标题
			chapterName: ch.chapterName, // 保持原始字段
			teacherName: detailInfo.value.mentor,
			coverImage: detailInfo.value.coverImage || ch.chapterCover,
			audioUrl: ch.chapterUrl || ch.filePath, // 音频地址
			isVip: ch.chapterCharge == 1,
			duration: ch.chapterDuration
		}))

		// 设置播放列表并播放（互斥：会停止音乐）
		playerStore.setPlaylist(playlist, 'course', index, true)

		uni.navigateTo({
			url: '/subpackage/courseInfo/course_bf?id=' + typeId.value + '&chapterId=' + item.id
		})
	};



	const saveLove = () => {
		let data = {
			courseId: id.value
		}
		if (detailInfo.value.isLike && detailInfo.value.isLike == 1) {
			proxy.$http.postJson('/app/CourseLike/delete', data).then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '已取消喜欢！',
						icon: 'none'
					})
					detailInfo.value.isLike = 0
				}
			});
		} else {
			proxy.$http.postJson('/app/CourseLike/add', data).then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '添加成功！',
						icon: 'none'
					})
					detailInfo.value.isLike = 1
				}
			});
		}
	};

	const chapterInfo = reactive({});

	const getDetail = () => {
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/courseInfo/selectCourseInfoByDetail?id=' + typeId.value).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				detailInfo.value = res.data;
				id.value = res.data.id
				Object.assign(chapterInfo, res.data.chapterInfoList[0])
				chapterInfoIndex.value = 0
				isDataLoaded.value = true // 标记数据加载完成
				console.log('数据加载完成，chapterInfoList:', detailInfo.value.chapterInfoList)
				getCourseInfo()
			}
		});
	};

	const goCourseInfo = (item) => {
		typeId.value = item.id
		getDetail()
	}

	const courseInfoList = reactive([]);
	// 获取相关推荐课程
	const getCourseInfo = () => {
		proxy.$http.get('/app/courseInfo/selectRecommendCourseInfo?id=' + detailInfo.value.id).then(res => {
			if (res.code == 0) {
				courseInfoList.splice(0, courseInfoList.length, ...res.data)
			}
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
			}
		})
	}

	onLoad(option => {
		titlePage.value = option.title
		typeId.value = option.id
	});

	onShow(() => {
		getDetail();
		getUserInfo()
		// 每次显示页面时，检查并同步播放状态
		console.log('onShow - currentAudio:', playerStore.currentAudio)
		console.log('onShow - isPlaying:', playerStore.isPlaying)
		console.log('onShow - currentType:', playerStore.currentType)
	});

	onPullDownRefresh(() => {

	});

	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #FFFFFF;
	}

	// .xiangguan:nth-child(3n) {
	// 	margin-right: 0 !important;
	// }

	.ds_msg_tk {
		width: calc(100% - 100rpx);
		margin: auto;
		background-color: #F7F8FF;
		border-radius: 30rpx;
		margin-bottom: 30rpx;
	}

	.footer_btn {
		background-color: #6F8ADB;
		border-radius: 14rpx;
		height: 90rpx;
		line-height: 90rpx;
		text-align: center;
		color: #FFFFFF;
		width: calc(100% - 60rpx);
		margin: auto;
		display: flex;
		align-items: center;
		margin-right: 30rpx;
		justify-content: center;
	}

	.footer {
		position: fixed;
		width: 100%;
		bottom: 0;
		background-color: #FFFFFF;
		padding: 30rpx 0;
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
	}

	.ds_Msg {
		border: solid #6F8ADB 1rpx;
		border-radius: 24rpx;
		background: #F7F8FF;
		padding: 20rpx 30rpx;
		margin-top: 45rpx;
	}

	.tag {
		background-color: #EBEFFA;
		color: #6E87D7;
		font-size: 24rpx;
		border-radius: 10rpx;
		margin-right: 15rpx;
		text-align: center;
		line-height: 50rpx;
		height: 50rpx;
		width: max-content;
		padding: 0 15rpx;
	}

	.containerBox {
		border-radius: 30rpx 30rpx 0rpx 0rpx;
		background: #FFFFFF;
		width: calc(100% - 60rpx);
		margin: auto;
		padding: 0 30rpx;
		margin-top: -10%;
		position: relative;
		padding-top: 40rpx;
	}

	.image-item {
		width: 50rpx;
		height: 50rpx;
		border-radius: 50%;
	}

	.topBox {
		position: relative;
	}

	.topImg {
		width: 100%;
		height: 493rpx;
	}

	.topMsg {
		position: absolute;
		bottom: 95rpx;
		z-index: 2;
		margin: 0 0rpx 0rpx 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		width: 686rpx;
	}
</style>