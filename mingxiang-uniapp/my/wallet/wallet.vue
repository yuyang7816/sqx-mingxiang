<template>
	<view>
		<view class="padding">
			<view class="bg radius text-white padding">
				<view class="text-lg">可提金额</view>
				<view class="flex margin-top-sm">
					<view class="text-xxl">{{money}}元</view>
				</view>
			</view>
			<view class="bg radius text-white padding margin-top">
				<view class="text-lg margin-bottom">提现金额</view>
				<input type="number" placeholder="请输入提现金额" v-model="moneyNum">
			</view>
			<view class="paybox bg radius">
				<view class=" text-bold">支付方式</view>
				<view class="flex justify-between" style="margin-top: 20rpx;width: 100%;" v-for="(item, index) in payList"
					:key="index">
					<image :src="item.image" mode="" style="width: 60rpx;height: 60rpx;margin-right: 20rpx;border-radius: 50%;">
					</image>
					<up-radio-group v-model="payWay" iconPlacement="right" @change="radioChange">
						<up-radio :customStyle="{marginBottom: '8px'}" :label="item.name" :name="item.name" activeColor="#4C6CD3"
							inactiveColor="#272833">
						</up-radio>
					</up-radio-group>
				</view>
			</view>
			<view class="text-center text-grey margin-top">{{placeholder}}</view>
			<view v-if="tipText" class="text-center margin-top" style="color: red;">*{{tipText}}</view>
			<view class="btn" @click="cashMoney">立即提现</view>
		</view>
		<view class="flex justify-around margin-top text-white">
			<view @click="goNav('/my/wallet/zhifubao')">提现账号</view>
			<view @click="show=true">微信收款码</view>
			<view @click="goNav('/my/wallet/mymoneydetail')">钱包明细</view>
			<view @click="goNav('/my/wallet/cashList')">提现记录</view>
		</view>
		<up-popup :show="show" mode="center" round="12">
			<view class="padding-xl">
				<view class="text-center text-lg text-bold flex justify-between">
					<view></view>
					<view>添加微信收款码</view>
					<view @click="show=false">X</view>
				</view>
				<image :src="wxImg?wxImg:erweima" @click.stop="weixin"
					style="order-radius: 16rpx;overflow: hidden;width: 400rpx;height: 400rpx;border-radius: 12rpx;margin-top: 40rpx;padding: 0 40rpx;">
				</image>
			</view>
		</up-popup>
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
		onShow
	} from '@dcloudio/uni-app';
	import config from '../../common/config.js'
	const {
		proxy
	} = getCurrentInstance();
	const show = ref(false);
	//可提现金额
	const money = ref(0);
	//提现金额
	const moneyNum = ref('');
	//提现说明
	const placeholder = ref('');
	const tipText = ref('');
	const charge = ref('');
	const minMoney = ref(0);
	const maxMoney = ref(0);
	const ratio = ref(0);
	const zhiFuBao = ref('');
	const zhiFuBaoName = ref('');
	const payList = ref([]);
	const payWay = ref('');
	const erweima = ref('https://xianyu.xianmaxiong.com/file/uploadPath/2024/11/14/f537b096d4357ccb68437ee8b8b56cae.png');
	//提现提示
	const sp = ref('');
	onLoad(option => {
		let data = [{
			name: '微信',
			id: 2,
			image: '/my/static/tj/wx.png',
		}, {
			name: '支付宝',
			id: 1,
			image: '/my/static/tj/zhifubao.png',
		}]
		payWay.value = '微信';
		payList.value.splice(0, payList.length, ...data)
		// getMoney();
		getCharge();
		getMinMoney();
		getMaxMoney();
	});
	onShow(() => {
		getUserInfo();
	});
	const radioChange = (e) => {
		console.log(e, "eeeeeeeeeeee")
		payWay.value = e
	};
	const wxImg = ref('');
	const getUserInfo = () => {
		proxy.$http.getT("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				if (res.data.zhiFuBao) {
					zhiFuBao.value = res.data.zhiFuBao;
				}
				if (res.data.zhiFuBaoName) {
					zhiFuBaoName.value = res.data.zhiFuBaoName;
				}
				wxImg.value = res.data.wxImg ? res.data.wxImg : ''
				money.value = res.data.balance ? res.data.balance : 0
			}
		});
	};
	const goNav = (url) => {
		uni.navigateTo({
			url: url
		})
	};
	//获取可提现余额
	const getMoney = () => {
		proxy.$http.get('/app/common/type/327').then(res => {
			if (res.code == 0) {
				tipText.value = res.data.value
			}
		});
	};
	// 提现手续费
	const getCharge = () => {
		proxy.$http.get("/app/common/type/152").then(res => {
			if (res.code == 0) {
				charge.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	// 最低提现额度
	const getMinMoney = () => {
		proxy.$http.get("/app/common/type/112").then(res => {
			if (res.code == 0) {
				minMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	// 最高提现额度
	const getMaxMoney = () => {
		proxy.$http.get("/app/common/type/153").then(res => {
			if (res.code == 0) {
				maxMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	//提现
	const cashMoney = () => {
		if (!moneyNum.value) {
			uni.showToast({
				icon: 'none',
				title: '请输入提现金额'
			})
			return
		}
		if (moneyNum.value > money.value * 1) {
			uni.showToast({
				icon: 'none',
				title: '您的余额不足'
			})
			return
		}


		if ((!zhiFuBao.value || !zhiFuBaoName.value) && payWay.value == '支付宝') {
			uni.navigateTo({
				url: '/my/wallet/zhifubao'
			})
			return
		}
		if (!wxImg.value && payWay.value == '微信') {
			uni.showToast({
				title: '请上传微信收款码',
				icon: 'none'
			})
			return
		}
		sp.value = (moneyNum.value * charge.value).toFixed(2)
		uni.showModal({
			title: '提示',
			content: '本次提现' + moneyNum.value + '元，服务费' + sp.value + '元，是否确认提现？',
			success: function(res) {
				if (res.confirm) {
					proxy.$http.get("/app/cash/cashMoney", {
						money: moneyNum.value,
						classify: payWay.value == '微信' ? 2 : 1
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
							moneyNum.value = ''
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
						}
						getUserInfo();
					});
				} else if (res.cancel) {
					console.log('用户点击取消');
				}
			}
		});
	};
	const weixin = () => {
		uni.chooseImage({
			count: 1, //默认9
			sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
			sourceType: ['album', 'camera'], //从相册选择
			success: (res) => {
				uni.showLoading({
					title: '上传中...'
				})
				for (let i = 0; i < 1; i++) {
					uni.uploadFile({ // 上传接口
						// url: config.APIHOST + "/alioss/upload", //真实的接口地址
						url: "https://mingxiang.xianmaxiong.com/sqx_fast/alioss/upload", //真实的接口地址
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							let data = {
								wxImg: JSON.parse(uploadFileRes.data).data
							}
							proxy.$http.postJson('/app/user/updateUser',
								data).then(
								res => {
									if (res.code == 0) {
										getUserInfo()
										setTimeout(function() {
											uni.showToast({
												title: '上传成功',
												icon: 'none'
											})
										}, 1000)
										getUserInfo()
										show.value = false
									}
								});
							uni.hideLoading();

						}
					});
				}

			}
		});
	}
</script>

<style lang="scss" scoped>
	.bg {
		background-color: #FFFFFF;
	}

	page {
		background-color: #f5f5f5;
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

	.paybox {
		width: calc(100% - 60rpx);
		margin: auto;
		border-radius: 16rpx;
		margin-top: 30rpx;
		padding: 30rpx;
		color: #333333;
	}

	.btn {
		width: 100%;
		height: 88upx;
		background: #4C6CD3;
		border-radius: 44upx;
		text-align: center;
		line-height: 88upx;
		margin-top: 40upx;
		font-size: 28upx;
		color: #FFF;
	}
</style>