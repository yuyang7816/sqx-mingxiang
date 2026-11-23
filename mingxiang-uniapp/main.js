import App from './App'
//引入 uview plus
import uviewPlus from 'uview-plus'
import httpPlugin from '/common/httpPlugin.js';
// 导入pinia
import {
	createPinia
} from 'pinia'
// 创建应用
const pinia = createPinia()

import {
	getWxAppid,
	getInvitation,
	getAppDownUrl,
	publicYuMing
} from '/common/queue.js';
// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'

const app = new Vue({
	...App
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'



export function createApp() {
	const app = createSSRApp(App)
	app.config.globalProperties.$getWxAppid = getWxAppid;
	app.config.globalProperties.$getInvitation = getInvitation;
	app.config.globalProperties.$getAppDownUrl = getAppDownUrl;
	app.config.globalProperties.$publicYuMing = publicYuMing;

	app.use(pinia)
	app.use(uviewPlus)
	app.use(httpPlugin)
	return {
		app,
		pinia
	}
}
// #endif