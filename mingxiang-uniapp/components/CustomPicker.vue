<!-- components/CustomPicker.vue -->
<template>
	<view class="custom-picker">
		<view class="mask-top"></view>

		<view class="gesture-area" @touchstart="onTouchStart" @touchmove="onTouchMove" @touchend="onTouchEnd">
			<view class="items-wrapper" :style="wrapperStyle">
				<view v-for="(item, index) in list" :key="index" class="item" :class="{ active: selectedIndex === index }"
					:style="{ height: itemHeight + 'px' }">
					<view class="num-box">
						<text class="num">{{ item }}</text>
						<!-- 单位绝对定位，不挤数字 -->
						<text v-if="index === selectedIndex" class="unit">{{ unit }}</text>
					</view>
				</view>
			</view>
		</view>
		<view class="mask-bottom"></view>
	</view>
</template>

<script>
	export default {
		name: 'CustomPicker',
		props: {
			list: {
				type: Array,
				default: () => []
			},
			defaultIndex: {
				type: Number,
				default: 0
			},
			itemHeight: {
				type: Number,
				default: 60
			},
			unit: {
				type: String,
				default: '分钟'
			}
		},
		data() {
			return {
				selectedIndex: this.defaultIndex,
				currentTranslateY: 0,
				startY: 0,
				startTranslateY: 0,
				isDragging: false,
				containerHeight: 0
			}
		},
		computed: {
			wrapperStyle() {
				const centerPosition = (this.containerHeight - this.itemHeight) / 2
				const selectedItemTop = this.selectedIndex * this.itemHeight
				const offset = centerPosition - selectedItemTop
				return {
					transform: `translateY(${offset + this.currentTranslateY}px)`,
					transition: this.isDragging ? 'none' : 'transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1)'
				}
			}
		},
		mounted() {
			this.getContainerHeight()
		},
		methods: {
			getContainerHeight() {
				const query = uni.createSelectorQuery().in(this)
				query.select('.custom-picker').boundingClientRect((rect) => {
					if (rect) this.containerHeight = rect.height
				}).exec()
			},
			onTouchStart(e) {
				this.isDragging = true
				this.startY = e.touches[0].clientY
				this.startTranslateY = this.currentTranslateY
			},
			onTouchMove(e) {
				if (!this.isDragging) return
				const currentY = e.touches[0].clientY
				const deltaY = currentY - this.startY
				this.currentTranslateY = this.startTranslateY + deltaY
			},
			onTouchEnd(e) {
				this.isDragging = false
				const totalMove = this.currentTranslateY
				const moveCount = Math.round(-totalMove / this.itemHeight)
				let newIndex = this.selectedIndex + moveCount
				newIndex = Math.max(0, Math.min(newIndex, this.list.length - 1))
				this.selectedIndex = newIndex
				this.currentTranslateY = 0
				this.$emit('change', {
					index: this.selectedIndex,
					value: this.list[this.selectedIndex]
				})
			}
		}
	}
</script>

<style scoped>
	.custom-picker {
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
	}

	.gesture-area {
		width: 100%;
		height: 100%;
		position: relative;
	}

	.items-wrapper {
		display: flex;
		flex-direction: column;
		align-items: center;
		width: 100%;
	}

	.item {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
	}

	/* 关键：数字容器固定宽高，相对定位 */
	.num-box {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 60px;
		/* 固定宽度，只放数字 */
		height: 44px;
		line-height: 1;
		position: relative;
		/* 单位定位参考 */
	}

	/* 数字：统一大小 */
	.num {
		font-size: 44px;
		font-variant-numeric: tabular-nums;
		font-feature-settings: "tnum";
		font-weight: 400;
		color: rgba(255, 255, 255, 0.4);
		transition: color 0.3s ease;
	}

	.item.active .num {
		color: #fff;
	}

	/* 关键：单位绝对定位，不挤数字 */
	.unit {
		position: absolute;
		left: 100%;
		/* 数字右侧 */
		top: 50%;
		transform: translateY(-50%);
		margin-left: 4px;
		font-size: 20px;
		color: rgba(255, 255, 255, 0.8);
		white-space: nowrap;
	}

	/* 遮罩 */
	.mask-top,
	.mask-bottom {
		position: absolute;
		left: 0;
		right: 0;
		height: calc((100% - 180px) / 2);
		z-index: 2;
		pointer-events: none;
	}

</style>