<!-- components/TimePicker.vue -->
<template>
	<view class="time-picker">
		<!-- 上方遮罩 -->
		<view class="mask-top"></view>

		<!-- 手势控制区域 -->
		<view class="picker-content">
			<!-- 小时列 -->
			<view class="column" @touchstart="onTouchStart($event, 'hour')" @touchmove="onTouchMove($event, 'hour')"
				@touchend="onTouchEnd('hour')">
				<view class="items-wrapper" :style="getWrapperStyle('hour')">
					<view v-for="(item, index) in hours" :key="index" class="item" :class="{ active: selectedHour === index }"
						:style="{ height: itemHeight + 'px' }">
						<view class="num-box">
							<text class="num">{{ formatNumber(item) }}</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 分隔符 -->
			<view class="separator">
				<text class="colon" :class="{ active: isActive }">:</text>
			</view>

			<!-- 分钟列 -->
			<view class="column" @touchstart="onTouchStart($event, 'minute')" @touchmove="onTouchMove($event, 'minute')"
				@touchend="onTouchEnd('minute')">
				<view class="items-wrapper" :style="getWrapperStyle('minute')">
					<view v-for="(item, index) in minutes" :key="index" class="item" :class="{ active: selectedMinute === index }"
						:style="{ height: itemHeight + 'px' }">
						<view class="num-box">
							<text class="num">{{ formatNumber(item) }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 下方遮罩 -->
		<view class="mask-bottom"></view>
	</view>
</template>

<script>
	export default {
		name: 'TimePicker',
		props: {
			defaultHour: {
				type: Number,
				default: 8
			},
			defaultMinute: {
				type: Number,
				default: 0
			},
			itemHeight: {
				type: Number,
				default: 70
			}
		},
		data() {
			return {
				hours: Array.from({
					length: 24
				}, (_, i) => i),
				minutes: Array.from({
					length: 60
				}, (_, i) => i),
				selectedHour: 0,
				selectedMinute: 0,
				currentTranslateY: {
					hour: 0,
					minute: 0
				},
				startY: {
					hour: 0,
					minute: 0
				},
				startTranslateY: {
					hour: 0,
					minute: 0
				},
				isDragging: {
					hour: false,
					minute: false
				},
				containerHeight: 0
			}
		},
		computed: {
			isActive() {
				return true
			}
		},
		mounted() {
			this.selectedHour = this.defaultHour
			this.selectedMinute = this.defaultMinute
			this.getContainerHeight()
		},
		watch: {
			defaultHour(val) {
				this.selectedHour = val
			},
			defaultMinute(val) {
				this.selectedMinute = val
			}
		},
		methods: {
			getContainerHeight() {
				const query = uni.createSelectorQuery().in(this)
				query.select('.time-picker').boundingClientRect((rect) => {
					if (rect) this.containerHeight = rect.height
				}).exec()
			},

			formatNumber(num) {
				return num < 10 ? '0' + num : num.toString()
			},

			getWrapperStyle(type) {
				const centerPosition = (this.containerHeight - this.itemHeight) / 2
				const selectedIndex = type === 'hour' ? this.selectedHour : this.selectedMinute
				const selectedItemTop = selectedIndex * this.itemHeight
				const offset = centerPosition - selectedItemTop
				const translateY = this.currentTranslateY[type]

				return {
					transform: `translateY(${offset + translateY}px)`,
					transition: this.isDragging[type] ? 'none' : 'transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1)'
				}
			},

			onTouchStart(e, type) {
				this.isDragging[type] = true
				this.startY[type] = e.touches[0].clientY
				this.startTranslateY[type] = this.currentTranslateY[type]
			},

			onTouchMove(e, type) {
				if (!this.isDragging[type]) return
				const currentY = e.touches[0].clientY
				const deltaY = currentY - this.startY[type]
				this.currentTranslateY[type] = this.startTranslateY[type] + deltaY
			},

			onTouchEnd(type) {
				this.isDragging[type] = false
				const totalMove = this.currentTranslateY[type]
				const moveCount = Math.round(-totalMove / this.itemHeight)

				if (type === 'hour') {
					let newIndex = this.selectedHour + moveCount
					newIndex = Math.max(0, Math.min(newIndex, 23))
					this.selectedHour = newIndex
				} else {
					let newIndex = this.selectedMinute + moveCount
					newIndex = Math.max(0, Math.min(newIndex, 59))
					this.selectedMinute = newIndex
				}

				this.currentTranslateY[type] = 0

				this.$emit('change', {
					hour: this.selectedHour,
					minute: this.selectedMinute,
					time: `${this.formatNumber(this.selectedHour)}:${this.formatNumber(this.selectedMinute)}`
				})
			}
		}
	}
</script>

<style scoped>
	.time-picker {
		position: relative;
		width: 100%;
		height: 200px;
		overflow: hidden;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.picker-content {
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
		z-index: 1;
	}

	.column {
		width: 70px;
		height: 220px;
		overflow: hidden;
		position: relative;
	}

	.items-wrapper {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.item {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
	}

	/* 数字容器 */
	.num-box {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 50px;
		height: 44px;
		line-height: 1;
		position: relative;
	}

	/* 数字 */
	.num {
		font-size: 33px;
		font-variant-numeric: tabular-nums;
		font-feature-settings: "tnum";
		font-weight: bold;
		color: rgba(255, 255, 255, 0.4);
		transition: color 0.3s ease;
	}

	.item.active .num {
		color: #fff;
	}

	/* 分隔符容器 - 关键修复：相对定位 */
	.separator {
		position: relative;
		width: 20px;
		height: 44px;
	}

	/* 冒号 - 关键修复：绝对定位精确居中 */
	.colon {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		font-size: 33px;
		font-weight: bold;
		color: rgba(255, 255, 255, 0.4);
		line-height: 1;
		transition: color 0.3s ease;
		top: 18rpx;
	}

	.colon.active {
		color: #fff;
	}

	/* 遮罩 */
	.mask-top,
	.mask-bottom {
		position: absolute;
		left: 0;
		right: 0;
		height: 65px;
		z-index: 2;
		pointer-events: none;
	}
/* 
	.mask-top {
		top: 0;
		background: linear-gradient(to bottom, rgba(58, 90, 159, 1) 0%, transparent 100%);
	}

	.mask-bottom {
		bottom: 0;
		background: linear-gradient(to top, rgba(58, 90, 159, 1) 0%, transparent 100%);
	} */
</style>