<template>
	<view class="">
		<view class="bg_img">
			<image src="/static/image/my/yqbg.jpg" mode="" class="page_img"></image>
		</view>
		<view class="bg_container">
			<view class="container_box">
				<view class="">
					<image src="/static/image/my/bgTop.png" mode="" class="top_img"></image>
					<view class="yq_text">我的邀请码</view>
				</view>
				<view class="container_cont">
					<view class="container_cont_cent" @click="copyCode(myInvitationCode)">
						<view class="f24" style="color: #B1B9D4;">点击复制邀请码</view>
						<view class="f56 text-h">{{myInvitationCode}} </view>
					</view>
				</view>
				<view class="yqBtn" @click="showShareNow = true">立即邀请</view>
			</view>
		</view>
		<!-- 邀请的好友列表 -->
		<view class="container_box" style="margin-bottom: 40rpx;padding:0 30rpx;width: calc(100% - 120rpx);"
			v-if="yqList.length > 0">
			<swiper class="vertical-swiper" vertical :autoplay="true" :loop="true" :circular="true" :duration="1000">
				<swiper-item v-for="(group, index) in yqList" :key="index">
					<view class="flex justify-between align-center" style="height: 90rpx;" v-for="(item, i) in group" :key="i">
						<view class="flex align-center" style="width: 35%;">
							<image :src="item.avatar?item.avatar:'/static/logo.png'" mode=""
								style="width: 52rpx;height: 52rpx;border-radius: 50%;"></image>
							<view class="f24 margin-left-sm text-cut" style="width: calc(100% - 60rpx);">
								{{item.userName}}
							</view>
						</view>
						<view class="f24" style="width: 30%;text-align: right;">获得
						</view>
						<view class="flex align-center" style="width: 30%;justify-content: flex-end;">
							<view class=" f24 c28 margin-left-xs text-cut" style="color: #FF3838;">
								{{item.money}}元现金
							</view>
						</view>
					</view>
				</swiper-item>
			</swiper>
		</view>
		<view class="bg_container" style="margin-top: 70rpx;">
			<view class="container_box">
				<view class="">
					<image src="/static/image/my/bgTop.png" mode="" class="top_img"></image>
					<view class="yq_text">我的邀请战绩</view>
				</view>
				<view class="flex align-center justify-between">
					<view class="margin-left-xl text-center">
						<view class="c31 f24 text-h">已邀请</view>
						<view class="c31 f38 text-h">{{inviteCount}}<text style="font-size: 28rpx;">人</text>
						</view>
					</view>
					<view class="text-center" style="margin: 0 14rpx;">
						<view class=" c31 f24 text-h">累计收益</view>
						<view class="c31 f38 text-h">{{inviteMoney}}<text style="font-size: 28rpx;">元</text>
						</view>
					</view>
					<view class="text-center margin-right-xl">
						<view class="c31 f24 text-h">已提现</view>
						<view class="c31 f38 text-h"><text style="font-size: 28rpx;">￥</text>{{cashOut}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="bg_container" style="margin-top: 70rpx;">
			<view class="container_box" style="padding-top: 50rpx;padding-bottom: 30rpx;">
				<view class="">
					<image src="/static/image/my/bgTop.png" mode="" class="top_img"></image>
					<view class="yq_text">邀请规则</view>
				</view>
				<view class="container_cont">
					<up-parse :content="gzContent"></up-parse>
					<!-- <view class="flex  justify-between f24 margin-bottom" style="align-items: baseline;">
						<view class="dian"></view>
						<view style="width: calc(100% - 20rpx);">使用该邀请码的用户将获得8元抵扣</view>
					</view>
					<view class="flex  justify-between f24 margin-bottom" style="align-items: baseline;">
						<view class="dian"></view>
						<view style="width: calc(100% - 20rpx);">当用户使用您的邀请码支付并完成订单的时候，您也
							将获得10元奖励</view>
					</view>
					<view class="flex  justify-between f24" style="align-items: baseline;">
						<view class="dian"></view>
						<view style="width: calc(100% - 20rpx);">使用您的邀请码完成支付的用户将自动成为您的下级，
							TA的每一笔已支付订单您将享有1%的抽成</view>
					</view> -->
				</view>
			</view>
		</view>
		<view style="height: 50rpx;"></view>
		<!-- 立即邀请弹框 -->
		<up-popup :show="showShareNow" @close="showShareNow = false" mode="bottom" round="12" closeable="true">
			<view class="text-center padding-top-xl" style="line-height: 120rpx;">
				<view class="" @click="sharurl()">文案推广</view>
				<view class="" @click="getShareImg()">分享海报</view>
			</view>
		</up-popup>

		<!-- 二维码 -->
		<!-- #ifdef H5 -->
		<view class="qrcodeBox" v-if="shareH5Url">
			<up-qrcode :size="200" @result="resultQr" :val="shareH5Url"></up-qrcode>
		</view>

		<canvas canvas-id="myShareImg" id="myShareImg" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}"
			class="myShareImg"></canvas>
		<!-- #endif -->

		<up-popup :show="showShareImg" @close="closeShareImg" mode="center">
			<view class="flex">
				<image :src="canvasImgSrc" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}" mode="">
				</image>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		reactive,
		computed
	} from 'vue';
	import {
		onLoad,
		onUnload
	} from '@dcloudio/uni-app';
	import configdata from '../../common/config.js';
	const {
		proxy
	} = getCurrentInstance();
	const showShareNow = ref(false);
	const backgroundImage = ref('/static/logo.png');
	// 自己的邀请码
	const myInvitationCode = ref('');
	const userName = ref('');
	const avatar = ref('');
	//推广文案
	const tuiguang1 = ref('');
	const url = ref('');
	const qrImg = ref('');
	const shareH5Url = ref('');
	//海报宽度
	const shareImgWidth = ref(uni.upx2px(686));
	//海报高度
	const shareImgHeight = ref(0);
	//canvas生成的图片
	const canvasImgSrc = ref('');
	//是否显示海报弹窗
	const showShareImg = ref(false);
	// 任务id 是否通过签到任务进入的页面
	const taskId = ref('');


	const gzContent = ref('');
	const inviteCount = ref(0);
	const inviteMoney = ref(0);
	const cashOut = ref(0);
	const yqList = ref([]);

	onLoad(option => {
		getBackImageList();
		if (uni.getStorageSync('token')) {
			getUserInfo();
		}
		if (uni.getStorageSync('taskId')) {
			taskId.value = uni.getStorageSync('taskId')
		}
		console.log(shareImgWidth.value)
		getMoneyDetail();
		getYqList();
	});
	onUnload(() => {
		uni.removeStorageSync('taskId')
	});
	const getYqList = () => {
		proxy.$http.getT('/app/invite/getProfitList?page=0&limit=20').then(res => {
			if (res.code === 0) {

				if (res.data.records.length > 0) {
					var datanew = chunk(res.data.records, 10)
					yqList.value = datanew;
				} else {
					var datanew = chunk(res.data.records, 10)
					yqList.value = datanew;
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	// 传进数组和指定个数，进行拆分
	const chunk = (array, size) => {
		//获取数组的长度，如果你传入的不是数组，那么获取到的就是undefined
		const length = array.length
		//判断不是数组，或者size没有设置，size小于1，就返回空数组
		if (!length || !size || size < 1) {
			return []
		}
		//核心部分
		let index = 0 //用来表示切割元素的范围start
		let resIndex = 0 //用来递增表示输出数组的下标

		//根据length和size算出输出数组的长度，并且创建它。
		let result = new Array(Math.ceil(length / size))
		//进行循环
		while (index < length) {
			//循环过程中设置result[0]和result[1]的值。该值根据array.slice切割得到。
			result[resIndex++] = array.slice(index, (index += size))
		}
		//输出新数组
		return result
	};
	const getMoneyDetail = () => {
		uni.showLoading({
			title: "加载中..."
		})
		proxy.$http.getT('/app/invite/selectInviteMoney').then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				inviteCount.value = res.data.inviteCount ? res.data.inviteCount : 0;
				inviteMoney.value = res.data.inviteMoney ? res.data.inviteMoney : 0;
				cashOut.value = res.data.cashOut ? res.data.cashOut : 0;
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};

	const copyCode = (data) => {
		uni.setClipboardData({
			data: data,
			success: function() {
				console.log('success');
				uni.showToast({
					title: '复制成功',
					icon: 'none'
				})
			}
		});
	};
	//关闭海报弹窗
	const closeShareImg = () => {
		showShareImg.value = false
	};
	const getShareImg = async () => {
		showShareNow.value = false
		//如果已经生成了海报，则直接显示
		if (canvasImgSrc.value) {
			showShareImg.value = true
		} else { //没有则去用canvas生成
			await shareImg();
		}
	};
	//生成海报
	const shareImg = async () => {
		uni.showLoading({
			title: '生成中...'
		})
		let _imgInfo = await getImageInfo(backgroundImage.value);
		console.log(_imgInfo)
		//获取canvas的上下文对象
		const ctx = uni.createCanvasContext('myShareImg');
		//设置画布的高度（根据图片的宽高比）
		// shareImgHeight.value = (shareImgWidth.value * _imgInfo.height) / _imgInfo.width;
		console.log(shareImgHeight.value)
		ctx.setFillStyle('#ffffff'); //canvas背景颜色
		ctx.fillRect(0, 0, shareImgWidth.value, shareImgHeight.value); //canvas画布大小
		//绘制背景图片
		ctx.drawImage(_imgInfo.path, 0, 0, shareImgWidth.value, shareImgHeight.value);
		// 在底部绘制个人信息与二维码
		//绘制头像
		const _avatarInfo = ref('');
		//头像宽高
		const avatarWidthOrHeight = ref(60);
		//边距
		const paddingNum = ref(20);
		//如果有头像
		if (avatar.value) {
			_avatarInfo.value = await getImageInfo(avatar.value)
		} else { //没有头像使用本地logo绘制
			_avatarInfo.value = await getImageInfo('/static/logo.png')
			console.log(_avatarInfo.value.path)
		}
		ctx.save();
		ctx.beginPath();
		//计算头像的中心坐标
		const avatarCenterX = paddingNum.value + avatarWidthOrHeight.value / 2;
		const avatarCenterY = shareImgHeight.value - paddingNum.value - avatarWidthOrHeight.value / 2;
		//绘制圆形头像
		ctx.arc(avatarCenterX, avatarCenterY, avatarWidthOrHeight.value / 2, 0, 2 * Math.PI);
		ctx.clip();
		//绘制头像
		ctx.drawImage(_avatarInfo.value.path, paddingNum.value, shareImgHeight.value - paddingNum.value -
			avatarWidthOrHeight.value, avatarWidthOrHeight.value,
			avatarWidthOrHeight.value);
		ctx.restore();
		//绘制用户昵称
		ctx.setFontSize(18);
		ctx.setFillStyle('#333333');
		ctx.fillText(userName.value, paddingNum.value + paddingNum.value / 2 + avatarWidthOrHeight.value,
			shareImgHeight.value - (
				avatarWidthOrHeight.value / 2) - paddingNum.value - paddingNum.value / 2);
		//绘制用户邀请码
		if (taskId.value) {
			ctx.fillText('ID:' + myInvitationCode.value, '任务ID:' + taskId.value, paddingNum.value + paddingNum.value / 2 +
				avatarWidthOrHeight
				.value, shareImgHeight
				.value - avatarWidthOrHeight.value / 2);
		} else {
			ctx.fillText('ID:' + myInvitationCode.value, paddingNum.value + paddingNum.value / 2 + avatarWidthOrHeight
				.value, shareImgHeight
				.value - avatarWidthOrHeight.value / 2);
		}

		//绘制二维码
		ctx.drawImage(qrImg.value, shareImgWidth.value - paddingNum.value - avatarWidthOrHeight.value,
			shareImgHeight.value - paddingNum
			.value -
			avatarWidthOrHeight.value, avatarWidthOrHeight.value,
			avatarWidthOrHeight.value);

		ctx.draw(false, () => {
			console.log('绘制完成')
			uni.hideLoading()
			getCanvasNewImage();
		});
	};
	//把canvas画布转为图片
	const getCanvasNewImage = () => {
		uni.canvasToTempFilePath({
			canvasId: 'myShareImg',
			quality: 1,
			complete: res => {
				console.log(res.tempFilePath)
				canvasImgSrc.value = res.tempFilePath;
				showShareImg.value = true;
			}
		});
	};
	//异步获取图片信息
	const getImageInfo = async (imgSrc) => {
		return new Promise((resolve, errs) => {
			uni.getImageInfo({
				src: imgSrc,
				success: function(image) {
					resolve(image);
				},
				fail(err) {
					errs(err);
					uni.showToast({
						title: '海报生成失败'
					})
					uni.hideLoading()
				}
			});
		});
	};
	//生成二维码回掉的图片（base64）
	const resultQr = (e) => {
		qrImg.value = e;
	};
	//文案推广
	const getTuiguang1 = () => {
		proxy.$http.getT('/app/common/type/276').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					tuiguang1.value = res.data.value;
				}
			}
		});
		// #ifndef H5
		proxy.$http.getT('/app/common/type/25').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					url.value = res.data.value;
				}
			}
		});
		//#endif
		// #ifdef H5
		proxy.$http.getT('/app/common/type/19').then(ress => {
			if (ress.code === 0) {
				if (ress.data && ress.data.value) {
					url.value = ress.data.value;
					shareH5Url.value = url.value
				} else {
					url.value = proxy.$publicYuMing();
					shareH5Url.value = url.value
				}
			}
		});
		//#endif
	};
	// 获取用户信息
	const getUserInfo = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		};
		proxy.$http.getT('/app/user/selectInfoByUserId', data).then(res => {
			if (res.code == 0) {
				myInvitationCode.value = res.data.invitationCode
				userName.value = res.data.userName
				avatar.value = res.data.avatar
				uni.setStorageSync('avatar', res.data.avatar)
				getTuiguang1();
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	// //获取分享海报的图片
	const getBackImageList = () => {
		proxy.$http.getT('/app/common/type/277').then(async res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					backgroundImage.value = res.data.value;
					let _imgInfo = await getImageInfo(backgroundImage.value);
					shareImgHeight.value = (shareImgWidth.value * _imgInfo.height) / _imgInfo.width;
				}
			}
		});

		proxy.$http.getT('/app/common/type/874').then(async res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					gzContent.value = res.data.value;
				}
			}
		});
	};
	const sharurl = () => {
		showShareNow.value = false
		let relationId = myInvitationCode.value;
		let modalContent = '';
		if (taskId.value) {
			modalContent = tuiguang1.value + relationId + '\n' + url.value + '/?inviterCode=' + relationId + '&taskId=' +
				taskId.value
		} else {
			modalContent = tuiguang1.value + relationId + '\n' + url.value + '/?inviterCode=' + relationId
		}
		uni.showModal({
			title: '文案推广',
			content: modalContent,
			showCancel: true,
			cancelText: '关闭',
			confirmText: '一键复制',
			confirmColor: '#3152CA',
			success: res => {
				if (res.confirm) {
					uni.setClipboardData({
						data: modalContent,
						success: function() {
							console.log('success');
							uni.showToast({
								title: '复制成功',
								icon: 'none'
							})
						}
					});
				}
			}
		});
	};
</script>

<style lang="scss" scoped>
	page {
		background-color: #c1dcfd;
		height: 100vh;
	}

	.dian {
		width: 12rpx;
		height: 12rpx;
		border-radius: 50%;
		background-color: #3152CA;
	}

	.c31 {
		color: #3152CA;
	}

	.vertical-swiper {
		// width: calc(100% - 0rpx);
		// margin: auto;
	}

	.bg_container {
		margin-bottom: 40rpx;
	}

	.yq_text {
		position: absolute;
		top: -30rpx;
		left: 0;
		right: 0;
		text-align: center;
		color: #ffffff;
	}

	.yqBtn {
		width: calc(100% - 60rpx);
		margin: auto;
		background: linear-gradient(-90deg, #4E70ED 0%, #72A0FF 100%);
		text-align: center;
		color: #ffffff;
		height: 88rpx;
		border-radius: 88rpx;
		line-height: 88rpx;
		margin-top: 44rpx;
	}

	.container_box {
		width: calc(100% - 60rpx);
		margin: auto;
		border-radius: 30rpx;
		background-color: #eef5ff;
		box-shadow: 4rpx 3rpx 4rpx 0rpx rgba(44, 111, 203, 0.09), -3rpx -2rpx 4rpx 0rpx rgba(44, 111, 203, 0.21);
		position: relative;
		padding: 80rpx 0 44rpx 0;

		.top_img {
			width: 300rpx;
			height: 57rpx;
			position: absolute;
			top: -30rpx;
			left: 0;
			right: 0;
			margin: auto;
		}

		.container_cont {}

		.container_cont_cent {
			background-color: #E3E9FF;
			width: 450rpx;
			height: 156rpx;
			text-align: center;
			margin: auto;
			border-radius: 31rpx;
			padding-top: 50rpx;
		}
	}

	.bg_img {
		width: 100%;
		height: 700rpx;
		position: relative;

		.page_img {
			width: 100%;
			height: 100%;
		}

	}

	.backgroundImg {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.backgroundImg-box {
			width: 686rpx;
			height: 100%;

			image {
				width: 100%;
				height: 800rpx;
				border-radius: 24rpx 24rpx 0 0;
			}
		}
	}

	.userInfo {
		width: 100%;
		height: auto;

		.userInfo-box {
			width: calc(682rpx - 40rpx);
			height: 100%;
			border-left: 1px solid #d6d7d9;
			border-right: 1px solid #d6d7d9;
			border-bottom: 1px solid #d6d7d9;
			border-radius: 0 0 24rpx 24rpx;
			padding: 20rpx;

			image {
				width: 100rpx;
				height: 100rpx;
				border-radius: 50%;
			}

			.userInfo-box-info {
				margin-left: 20rpx;
				color: #333333;
			}

			.userInfo-box-info-name {
				font-size: 30rpx;
				font-weight: bold;
			}

			.userInfo-box-info-code {
				font-size: 24rpx;
			}
		}
	}

	.shareBtn {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.shareBtn-box {
			width: 686rpx;
			height: 100%;
		}

		.shareBtn-box-items {
			background-color: #ffffff !important;
			color: #4BB6FF !important;
			border: 1px solid #4BB6FF;
		}

		.shareBtn-box-item {
			width: 333rpx;
			height: 80rpx;
			background-color: #4BB6FF;
			color: #ffffff;
			border-radius: 14rpx;
		}
	}

	.qrcodeBox {
		position: fixed;
		left: -1000px;
	}

	.myShareImg {
		position: fixed;
		left: -1000px;
	}
</style>