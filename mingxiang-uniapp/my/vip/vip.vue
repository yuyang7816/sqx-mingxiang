<template>
	<view style="padding-bottom: 300rpx;">
		<!-- #ifndef H5 -->
		<up-navbar title="会员中心" :autoBack="true" :bgColor="bgColor" leftIconColor="#FDE3C0" titleStyle="color:#FDE3C0;">
		</up-navbar>
		<!-- #endif -->

		<view class="vipTop">
			<view class="padding-top padding-left flex">
				<image src="/my/static/vip/vip.png" mode="" style="width: 41rpx;height: 35rpx;"></image>
				<view class="text-bold" style="color: #AC6A4C;margin-top: -4rpx;margin-left: 10rpx;">
					{{isVip == false?'开通会员':'已开通会员'}}
				</view>
			</view>
			<view class="f24 padding-left" style="color: #AC6A4C;">{{isVip == false?'成为会员，每月多重好礼送给你':'恭喜您成为会员，已获得以下等级特权'}}
			</view>
			<view class="f24 padding-left" style="color: #AC6A4C;" v-if="isVip == true">
				会员到期时间：{{vipEndTime.slice(0,10)}}</view>
		</view>
		<view class="tqBox" style="margin-top: 30rpx;">
			<view class="margin-bottom-xl flex align-center justify-center">
				<view class="aa_"></view>
				<view class="text-color f28 text-center margin-lr-xs">会员特权</view>
				<view class="aa"></view>
			</view>
			<swiper class="swiper-box" :indicator-dots="false" :circular="false" style="height: 300rpx;">
				<swiper-item v-for="(pageData, pageIndex) in paginatedList" :key="pageIndex">
					<up-grid :border="false" col="3" align="center" gap="30">
						<up-grid-item v-for="item in pageData" :key="item.memberId">
							<view class="grid-item-content margin-bottom-xl">
								<image :src="item.memberImg" mode="aspectFill"
									style="width: 45rpx;height: 45rpx;display: block;margin: auto;" class="item-icon" />
								<view class="f26 text-color_f8 margin-top-xs">{{item.memberName}}</view>
							</view>
						</up-grid-item>
					</up-grid>
				</swiper-item>
			</swiper>
		</view>

		<view class="tqBox" style="margin-top: 30rpx;" v-if="vipList.length > 0">
			<view class="margin-bottom-xl flex align-center justify-center">
				<view class="aa_"></view>
				<view class="text-color f28 text-center margin-lr-xs">会员套餐选择</view>
				<view class="aa"></view>
			</view>

			<scroll-view class="scroll-view_H margin-top padding-bottom" scroll-x="true">
				<view style="display: inline-block;" v-for="item,index in vipList" :key="index" @click="changeVipTc(item,index)"
					:class="vipTcIndex  == index?'tc_item':'tc_item_aic'">
					<view class="f30 padding-top">{{item.vipName}}</view>
					<view class="f30 text-center" style="padding-top: 10rpx;padding-bottom: 40rpx;">￥<text
							class="f46">{{item.money}}</text></view>
				</view>
			</scroll-view>
		</view>
		<view class="paybox" v-if="vipList.length > 0">
			<view class="cFF text-bold">支付方式</view>
			<view class="flex justify-between" style="margin-top: 20rpx;width: 100%;" v-for="(item, index) in payList"
				:key="index">
				<image :src="item.image" mode="" style="width: 60rpx;height: 60rpx;margin-right: 20rpx;border-radius: 50%;">
				</image>
				<up-radio-group v-model="payWay" iconPlacement="right" @change="radioChange">
					<up-radio :customStyle="{marginBottom: '8px'}" :label="item.name" :name="item.name" activeColor="#DCB57C"
						inactiveColor="#272833" labelColor="#FFFFFF">
					</up-radio>
				</up-radio-group>
			</view>
		</view>
		<view class="flex align-center flex-wrap padding-left margin-top-sm">
			<up-checkbox label="" name="agree" usedAlone shape="circle" v-model:checked="aloneChecked">
			</up-checkbox>
			<view style="color: #FFF9F0;font-size: 24rpx;">
				请认真阅读并同意<text @click="routerGo('/settingViews/xieyi?id=873&text=VIP权益条款')">《VIP权益条款》</text><text
					@click="routerGo('/settingViews/about?type=876')">《用户协议》</text>
			</view>
		</view>
		<view class="openVip">
			<view class="openVipBtn" @tap="submitOrderPay">立即开通</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		computed
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


	const bgColor = ref('#0A1227');
	// 协议是否选择
	const aloneChecked = ref(false);
	const vipList = ref([]);
	const tqList = ref([]);
	const payList = ref([]);
	const payWay = ref('');

	// 自动分页计算属性
	const paginatedList = computed(() => {
		const result = [];
		if (!tqList.value || tqList.value.length === 0) return result;

		// 无论多长，这里都会自动切分
		for (let i = 0; i < tqList.value.length; i += pageSize) {
			result.push(tqList.value.slice(i, i + pageSize));
		}
		return result;
	});

	// 切换会员方法和定义值
	const vipTcIndex = ref(0); // 会员套餐绑定的index
	const changeVipTc = (item, index) => {
		vipTcIndex.value = index
		Object.assign(vipInfo, item)
	};

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	//支付
	const submitOrderPay = () => {
		if (aloneChecked.value == false) {
			uni.showToast({
				title: '请先阅读并同意《VIP权益条款》',
				icon: 'none',
				duration: 2000
			})
			return
		}
		let data = {
			id: vipInfo.id,
			classify: 0
		}
		// 0余额支付 1app  2 二维码支付  3小程序 公众号支付 4支付宝APP支付 5支付宝H5支付
		uni.showLoading({
			title: '支付中...'
		})
		if (payWay.value == '微信支付') { //：微信支付
			// #ifdef H5
			data.classify = 3
			proxy.$http.postJson('/app/vipDetails/userBuyVip', data).then(red => {
				callPay(red);
			});
			// #endif
			// #ifdef APP-PLUS
			data.classify = 1
			proxy.$http.postJson('/app/vipDetails/userBuyVip', data).then(red => {
				setPayment('wxpay', JSON.stringify(red));
			});
			// #endif
			// #ifdef MP-WEIXIN
			data.classify = 3
			proxy.$http.postJson('/app/vipDetails/userBuyVip', data).then(red => {
				uni.requestPayment({
					provider: 'wxpay',
					timeStamp: red.timestamp,
					nonceStr: red.noncestr,
					package: red.package,
					signType: red.signType,
					paySign: red.sign,
					success: function(ress) {
						uni.showLoading({
							title: '支付成功'
						});
						uni.hideLoading();
						setTimeout(() => {
							getUserInfo()
						}, 1000);
					},
					fail: function(err) {
						uni.hideLoading();
						uni.showToast({
							title: '支付失败',
							icon: 'none'
						})
					}
				});
			});
			// #endif
		} else if (payWay.value == '支付宝支付') { //4：支付宝支付
			// #ifdef H5
			data.classify = 5
			proxy.$http.postJson('/app/vipDetails/userBuyVip', data).then(red => {
				const div = document.createElement('div')
				div.innerHTML = red.data //此处form就是后台返回接收到的数据
				document.body.appendChild(div)
				document.forms[0].submit()
			});
			// #endif
			// #ifdef APP-PLUS
			data.classify = 4
			proxy.$http.postJson('/app/vipDetails/userBuyVip', data).then(red => {
				setPayment('alipay', red.data);
			});
			// #endif
		} else { //余额支付
			data.classify = 0
			proxy.$http.postT('/app/vipDetails/userBuyVip', data).then(red => {
				if (red.code == 0) {
					uni.showToast({
						title: '支付成功',
						icon: "none",
						duration: 2000
					})
					uni.hideLoading();
					setTimeout(() => {
						getUserInfo()
					}, 1000);
				} else {
					uni.showToast({
						title: red.msg,
						icon: "none",
						duration: 3000
					})
				}
			});
		}
	};
	const setPayment = (name, order) => {
		console.log('*-*-*')
		uni.requestPayment({
			provider: name,
			orderInfo: order, //微信、支付宝订单数据
			success: function(res) {
				console.log(res)
				uni.hideLoading();
				uni.showLoading({
					title: '支付成功'
				});
				getUserInfo()
			},
			fail: function(err) {
				console.log(err)
				uni.hideLoading();
			},
			complete() {
				uni.hideLoading();
			}
		});
	};
	const callPay = (response) => {
		if (typeof WeixinJSBridge === "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(response), false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(response));
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(response));
			}
		} else {
			onBridgeReady(response);
		}
	};
	const onBridgeReady = (response) => {
		if (!response.package) {
			return;
		}
		console.log(response, '++++++++')
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				"appId": response.appid, //公众号名称，由商户传入
				"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
				"nonceStr": response.noncestr, //随机串
				"package": response.package,
				"signType": response.signType, //微信签名方式：
				"paySign": response.sign //微信签名
			},
			function(res) {
				console.log(res, '/*-/*-/*-')
				if (res.err_msg === "get_brand_wcpay_request:ok") {
					// 使用以上方式判断前端返回,微信团队郑重提示：
					//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
					uni.hideLoading();
					uni.showLoading({
						title: '支付成功'
					});
					getUserInfo()
				} else {
					uni.hideLoading();
				}
				WeixinJSBridge.log(response.err_msg);
			}
		);
	};
	const radioChange = (e) => {
		console.log(e, "eeeeeeeeeeee")
		payWay.value = e
	};
	const memberList = ref([]);
	const pageSize = 6; // 固定每页 6 个
	const isVip = ref(false);
	const vipInfo = reactive({});
	const vipEndTime = ref('');
	const getUserInfo = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/user/selectInfoByUserId', data).then(res => {
			if (res.code == 0) {
				vipEndTime.value = res.data.vipEndTime
				if (res.data.isVip && res.data.isVip == 1) {
					isVip.value = true;
				} else {
					isVip.value = false;
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	const getVipList = () => {
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/vipDetails/getVipDetailsList').then(res => {
			if (res.code == 0) {
				vipList.value = res.data.records;
				Object.assign(vipInfo, res.data.records[0])
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})

		proxy.$http.getT('/app/member/getMemberList').then(res => {
			if (res.code == 0) {
				tqList.value = res.data.records;
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
		uni.hideLoading()
	};
	onLoad(option => {
		getVipList()
		getUserInfo()
		// #ifdef APP
		let data = [{
			name: '微信支付',
			id: 1,
			image: '/my/static/tj/wx.png',
			isShow: true
		}, {
			name: '余额支付',
			id: 3,
			image: '/my/static/tj/jb.png',
			isShow: true
		}]
		payWay.value = '微信支付';
		payList.splice(0, payList.length, ...data)
		// #endif
		// #ifdef MP-WEIXIN
		let data = [{
			name: '微信支付',
			id: 1,
			image: '/my/static/tj/wx.png'
		}, {
			name: '支付宝支付',
			id: 2,
			image: '/my/static/tj/zhifubao.png'
		}, {
			name: '余额支付',
			id: 3,
			image: '/my/static/tj/jb.png'
		}]
		payWay.value = '微信支付';
		payList.splice(0, payList.length, ...data)
		// #endif
		// #ifdef H5
		let target = navigator.userAgent.toLowerCase();
		let isWeixin = (/micromessenger/.test(target)) ? true : false;
		console.log(payList, "payList")
		if (isWeixin) {
			let data = [{
				name: '微信支付',
				id: 1,
				image: '/my/static/tj/wx.png'
			}, {
				name: '余额支付',
				id: 3,
				image: '/my/static/tj/jb.png'
			}]
			payWay.value = '微信支付';
			payList.value.splice(0, payList.length, ...data)
		} else {
			let data = [{
				name: '支付宝支付',
				id: 2,
				image: '/my/static/tj/zhifubao.png'
			}, {
				name: '余额支付',
				id: 3,
				image: '/my/static/tj/jb.png'
			}]
			payWay.value = '支付宝支付';
			payList.value.splice(0, payList.length, ...data)
		}
		// #endif
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background: #0A1227;
		// background-color: red;
	}

	:deep().u-radio {
		// justify-content: space-between;
		width: 100%;
	}

	:deep().u-radio__icon-wrap {
		// background-color: #272833 !important;
		border: solid #CCCCCC 1rpx !important;
		border-color: #CCCCCC !important;
	}

	.vipTop {
		width: calc(100% - 60rpx);
		border-radius: 32rpx;
		height: 300rpx;
		background-image: url('/my/static/vip/vipbg.png');
		background-size: 100% 100%;
		margin: auto;
		background-repeat: no-repeat;
		margin-top: 30rpx;
	}

	.aa_ {
		width: 95rpx;
		height: 3rpx;
		border-top: 3rpx solid transparent;
		/* 设置透明边框 */
		border-image: linear-gradient(to left, #F2CC93, #2A2E37) 1;
		/* 渐变边框 */
		box-sizing: border-box;
	}

	.aa {
		width: 95rpx;
		height: 3rpx;
		border-top: 3rpx solid transparent;
		/* 设置透明边框 */
		border-image: linear-gradient(to right, #F2CC93, #2A2E37) 1;
		/* 渐变边框 */
		box-sizing: border-box;
	}

	.paybox {
		background-color: #2A2E37;
		width: calc(100% - 120rpx);
		margin: auto;
		border-radius: 16rpx;
		margin-top: 30rpx;
		padding: 30rpx;
	}

	.openVip {
		background: #0A1227;
		width: 100%;
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0rpx;
		margin: auto;
		padding: 30rpx 0;
	}

	.openVipBtn {
		background: linear-gradient(122deg, #EFDCB5, #DDB27D);
		border-radius: 44rpx;
		height: 88rpx;
		width: calc(100% - 60rpx);
		font-size: 30rpx;
		color: #78521E;
		text-align: center;
		line-height: 88rpx;
		margin: auto;
	}

	.tc_item {
		width: 140rpx;
		height: 186rpx;
		background: linear-gradient(122deg, #EFDCB5, #DDB27D);
		border-radius: 14rpx;
		color: #6B4716;
		text-align: center;
		padding: 20rpx;
		font-weight: 500;
		margin-right: 20rpx;
	}

	.tc_item_aic {
		width: 140rpx;
		height: 186rpx;
		background: #1F1F2F;
		border-radius: 14rpx;
		color: #EFC991;
		text-align: center;
		padding: 20rpx;
		font-weight: 500;
		margin-right: 20rpx;
		border: solid 1rpx #EEC991;
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
	}

	.tqBox_item {
		text-align: center;
		width: 28%;
		margin-bottom: 45rpx;
	}


	.text-color_f8 {
		color: #F8D8B2;
	}

	.text-color {
		color: #EEC991;
	}

	.tqBox {
		background-color: #2A2E37;
		width: calc(100% - 120rpx);
		margin: auto;
		border-radius: 16rpx;
		margin-top: 88rpx;
		padding: 30rpx;
	}
</style>