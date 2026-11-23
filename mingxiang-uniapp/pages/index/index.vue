<template>
	<view>
		<view class="header">
			<view class="c99 f28">hi，{{userInfo.userName||'小暴燥'}}</view>
			<view class="flex align-center justify-between">
				<view class="f55 text-h">{{greeting||''}}</view>
				<view class="" v-if="userId">
					<!-- #ifndef MP-WEIXIN -->
					<image src="/static/image/index/fx.png" mode="" style="width: 44rpx;height: 44rpx;" @click="showShare = true">
					</image>
					<!-- #endif -->
					<!-- #ifdef MP-WEIXIN -->
					<button open-type="share" class="" style="text-align: center;">
						<image src="/static/image/index/fx.png" mode="" style="width: 44rpx;height: 44rpx;"></image>
					</button>
					<!-- #endif -->
				</view>
			</view>
		</view>
		<view class=" swiper_">
			<up-swiper :list="list" keyName="imageUrl" height="160" radius="12" @click="goClick"></up-swiper>
		</view>
		<view class="tabBox">
			<view class="text-center" @click="routerGo('/subpackage/index/sleep_yj')">
				<image src="/static/image/index/sm.png" mode="" style="width: 85rpx;height: 85rpx;border-radius: 50%;">
				</image>
				<view class="f26">夜间睡眠</view>
			</view>
			<view class="text-center" @click="routerGo('/subpackage/index/zhuanzhu_sz')">
				<image src="/static/image/index/sz.png" mode="" style="width: 85rpx;height: 85rpx;border-radius: 50%;">
				</image>
				<view class="f26 ">专注时钟</view>
			</view>
			<view class="text-center" @click="routerGo('/subpackage/index/rijian_xx')">
				<image src="/static/image/index/xq.png" mode="" style="width: 85rpx;height: 85rpx;border-radius: 50%;">
				</image>
				<view class="f26">日间小憩</view>
			</view>
			<view class="text-center" @click="routerGo('/subpackage/index/zhengnian_hx')">
				<image src="/static/image/index/hx.png" mode="" style="width: 85rpx;height: 85rpx;border-radius: 50%;">
				</image>
				<view class="f26">正念呼吸</view>
			</view>
		</view>

		<template v-if="dataList.length > 0">
			<view class="container_box" v-for="item,index in dataList" :key="index">
				<view class="flex align-center justify-between" v-if="item.courseInfoList.length > 0"
					@click="routerGo('/subpackage/index/indexMore?title='+item.typeName+'&id='+item.id + '&remark=' + item.remark)">
					<view class="">
						<view class="f32 text-h">{{item.typeName || '暂无'}}</view>
						<view class="c99 f24">{{item.remark || '暂无'}}</view>
					</view>
					<view class="flex align-center">
						<view class="f24 c668">更多</view>
						<image src="/static/image/index/fh.png" mode="" style="width: 10rpx;height: 20rpx;margin-left: 15rpx;">
						</image>
					</view>
				</view>
				<scroll-view class="scroll-view_H margin-top" scroll-x="true">
					<view style="display: inline-block;margin-right: 25rpx;"
						@click="routerGo('/subpackage/courseInfo/courseDetail?title='+children.typeName+'&id='+children.id)"
						v-for="children,childrenIndex in item.courseInfoList" :key="childrenIndex">
						<view style="width: 329rpx;height: 234rpx;border-radius: 24rpx;overflow: hidden;display: block;">
							<image :src="children.cover?children.cover:'/static/logo.png'" mode="aspectFill"
								style="width: 100%;height: 100%;display: block;">
							</image>
						</view>
						<view class="padding-left-xs padding-top-sm">
							<view class="f30">{{children.courseName||'暂无'}}</view>
							<view class="f26 c99" style="margin-top: 6rpx;">{{children.courseTitle||'暂无'}}</view>
						</view>
					</view>
				</scroll-view>
			</view>
		</template>
		<up-loadmore :status="status" v-if="dataList.length>0" />
		<empty v-if="dataList.length==0"></empty>


		<!-- 迷你播放器 -->
		<MiniPlayer :on-play-pause="handlePlayPause" :on-click="handlePlayerClick" />
		<!-- 分享弹框 -->
		<up-modal :show="showShare" title="分享好友" :content='shareContent' @confirm="confirmBtn"></up-modal>

		<AlarmGlobalModal />
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom,
		onShareAppMessage,
		onShareTimeline
	} from '@dcloudio/uni-app';
	import AlarmGlobalModal from '@/components/alarm/AlarmGlobalModal.vue'
	const {
		proxy
	} = getCurrentInstance();
	import empty from '@/components/empty/empty.vue';
	import MiniPlayer from '@/components/player/MiniPlayer.vue'

	const handlePlayPause = async (action, item, type) => {
		// const url = type === 'music' ?
		// 	`https://your-api.com/api/music/${action}` :
		// 	`https://your-api.com/api/course/${action}`
		console.log(item, 'item')
		if (type === 'course') {
			let url = '';
			if (action == 'pause') { //暂停
				url = '/app/courseLearn/endMeditation'
			} else {
				url = '/app/courseLearn/startMeditation'
			}
			await proxy.$http.postJson(url, {
				chapterId: item.chapterId,
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
		} else {
			return true
		}
	}

	const handlePlayerClick = (item, type) => {
		if (type === 'music') {
			uni.navigateTo({
				url: '/subpackage/index/sleep_music'
			})
		} else {
			uni.navigateTo({
				url: `/pages/course-detail/index?chapterId=${item.chapterId}`
			})
		}
	}


	const showShare = ref(false);
	//顶部轮播图
	const list = ref([]);
	// 分享内容
	const shareContent = ref('');

	// 总页数
	const totalPages = ref(1);
	// 页数
	const pageSize = ref(10);
	const pageNum = ref(1);
	//加载状态
	const status = ref('loadmore');
	const dataList = reactive([]);
	// 获取首页展示数据
	const getIndexList = () => {
		let data = {
			pageNum: pageNum.value,
			pageSize: pageSize.value
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/courseInfo/selectCourseInfoList', data).then(res => {
			console.log(res, "res")
			uni.stopPullDownRefresh()
			uni.hideLoading();
			if (res.code == 0) {
				totalPages.value = res.data.pages;
				if (pageNum.value < totalPages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (pageNum.value === 1) {
					dataList.splice(0, dataList.length, ...res.data.list);
				} else {
					dataList.push(...res.data.list);
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const userInfo = reactive({});
	const myInvitationCode = ref('');

	// 获取用户信息
	const getUserInfo = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/user/selectInfoByUserId', data).then(res => {
			if (res.code == 0) {
				Object.assign(userInfo, res.data)
				myInvitationCode.value = res.data.invitationCode
				console.log(myInvitationCode.value, "myInvitationCode.value")
				uni.setStorageSync('myInvitationCode', res.data.invitationCode)
			}
		})
	};

	const routerGo = (url) => {
		// uni.navigateTo({
		// 	url
		// })
		// return
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url
			})
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录，请先登录账号',
				success: function(res) {
					if (res.confirm) {
						uni.navigateTo({
							url: '/pages/public/login'
						})
					}
				}
			})
		}
	};
	const goClick = (e) => {
		console.log(e)
	};
	const getBannerList = () => {
		proxy.$http.getT('/app/banner/selectBannerList?classify=1').then(res => {
			if (res.code == 0) {
				list.value = res.data
			}
		});
		proxy.$http.getT('/app/banner/selectBannerList?classify=2').then(res => {
			if (res.code == 0) {
				// cateList.value = res.data
			}
		});

	};

	const greeting = ref('');
	// 
	const updateGreeting = () => {
		const now = new Date();
		const hour = now.getHours(); // 获取当前小时 (0-23)
		if (hour >= 5 && hour < 11) {
			greeting.value = '早上好';
		} else if (hour >= 11 && hour < 13) {
			greeting.value = '中午好';
		} else if (hour >= 13 && hour < 18) {
			greeting.value = '下午好';
		} else {
			greeting.value = '晚上好';
		}
	};

	const confirmBtn = () => {
		showShare.value = false
		uni.setClipboardData({
			data: shareContent.value,
			success: function() {
				console.log('success');
				uni.showToast({
					title: '复制成功',
					icon: 'none'
				})
			}
		});
	};

	onShareAppMessage(res => {
		return {
			path: shareMessage.value, //这是为了传参   onload(data){let id=data.id;} 
			title: wxShareText.value,
			imageUrl: ''
		}
	});
	// 分享到朋友圈
	onShareTimeline(res => {
		return {
			path: shareMessage.value, //这是为了传参   onload(data){let id=data.id;} 
			title: wxShareText.value,
			imageUrl: ''
		}
	});
	// 音乐播放列表
	const getList = () => {
		uni.showLoading({
			title: "加载中..."
		})
		proxy.$http.getT('/app/soundEffects/selectSoundEffectsList?pageNum=1&pageSize=50').then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				musicList.splice(0, musicList.length, ...res.data.list.map((item) => ({
					id: item.id,
					effectsName: item.effectsName,
					bgImg: item.bgImg,
					audioUrl: item.filePath,
					...item,
				})))
				// 默认设置第一个为当前播放（不自动播放）
				if (musicList.length > 0) {
					currentMusic.value = musicList[0]
				}
			}
		});
	};

	const userId = ref('');
	const shareUrl = ref('');
	const gfinviteCode = ref('');
	const inviteText = ref('');
	const wxShareText = ref('');
	const shareMessage = ref('');
	onLoad(option => {
		console.log(option, "optionoption")
		getIndexList()
		getBannerList();
		updateGreeting()
		getList()
		userId.value = uni.getStorageSync('userId')
		if (uni.getStorageSync('token')) {
			getUserInfo()
		}
		// h5域名
		proxy.$http.get('/app/common/type/19').then(res => {
			if (res.code == 0 && res.data.value) {
				shareUrl.value = res.data.value
				shareContent.value = shareUrl.value + '?otherInviteCode=' + myInvitationCode.value
				console.log(shareUrl.value, "shareUrl.value")
			}
		})
		// 微信分享邀请语
		proxy.$http.get('/app/common/type/103').then(res => {
			if (res.code == 0 && res.data.value) {
				wxShareText.value = res.data.value
			}
		})
		if (option.otherInviterCode) {
			uni.setStorageSync('otherInviterCode', option.otherInviterCode)
		} else if (option.inviterCode) {
			uni.setStorageSync('otherInviterCode', option.inviterCode)
		}
		shareMessage.value = '?otherInviterCode=' + myInvitationCode.value
		console.log(shareMessage.value, "shareMessage.value")
	});
	onShow(() => {
		let token = uni.getStorageSync('token');
		if (token) {
			proxy.$http.getT('/app/Login/launchApp').then(res => {
				if (res.code == 0) {

				}
			});
		}
	});
	onPullDownRefresh(() => {
		pageNum.value = 1;
		getIndexList();
	});
	onReachBottom(() => {
		if (pageNum.value < totalPages.value) {
			pageNum.value += 1
			status.value = 'loading';
			getIndexList();
		}
	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #FFFFFF;
		padding-bottom: 140rpx;
		/* 为迷你播放器预留空间 */
	}

	wx-button {
		background-color: transparent !important;
	}

	wx-button:after {
		border: none !important;
	}

	.container_box {
		width: calc(100% - 60rpx);
		margin: auto;
		margin-top: 40rpx;
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
	}

	.header {
		width: calc(100% - 60rpx);
		margin: auto;
		/* #ifdef H5 */
		margin-top: 30rpx;
		/* #endif */
		/* #ifndef H5 */
		margin-top: 30rpx;
		/* #endif */
	}

	.swiper_ {
		height: 240rpx;
		width: calc(100% - 55rpx);
		margin: auto;
		margin-top: 30rpx;
	}

	.tabBox {
		width: calc(100% - 60rpx);
		margin: auto;
		margin-top: 95rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
</style>