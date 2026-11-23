<template>
  <div class="dashboard">

    <el-row :gutter="20" style="margin-top: 40px;" v-loading="menuLoading">
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.totalUsers ? tableData.totalUsers : 0 }}</div>
            <div class="stat-label">总用户数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.newToday ? tableData.newToday : 0 }}</div>
            <div class="stat-label">今日新增</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.newMonth ? tableData.newMonth : 0 }}</div>
            <div class="stat-label">本月新增</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.newYear ? tableData.newYear : 0 }}</div>
            <div class="stat-label">本年新增</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.totalRevenue ? tableData.totalRevenue : 0 }}</div>
            <div class="stat-label">总收入</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.todayRevenue ? tableData.todayRevenue : 0 }}</div>
            <div class="stat-label">今日收入</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.monthRevenue ? tableData.monthRevenue : 0 }}</div>
            <div class="stat-label">本月收入</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.yearRevenue ? tableData.yearRevenue : 0 }}</div>
            <div class="stat-label">本年收入</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div style="margin-top: 20px;">
      <el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="orderfenxi()">
        <el-option v-for="(item, index) in flags" :key="index" :label="item.label" :value="item.value">
        </el-option>
      </el-select>&nbsp;&nbsp;&nbsp;
      <el-date-picker v-model="info" type="date" placeholder="选择时间" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
        @change="orderfenxi()" />
    </div>
    <el-tabs v-model="activeName" @tab-click="handleClick" style="margin-left: 10px;">

      <el-tab-pane label="总用户统计" name="first">
        <el-row :gutter="20" style="margin-top: 20px;" v-loading="menuLoadingUser">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ userStat.totalNumber ? userStat.totalNumber : 0 }}</div>
                <div class="stat-label">总用户数</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ userStat.userNumber ? userStat.userNumber : 0 }}</div>
                <div class="stat-label">普通用户</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ userStat.vipUserNumber ? userStat.vipUserNumber : 0 }}</div>
                <div class="stat-label">vip用户</div>
              </div>
            </el-card>
          </el-col>

        </el-row>
      </el-tab-pane>
      <el-tab-pane label="支付统计" name="sixth">
        <el-row :gutter="20" style="margin-top: 40px;" v-loading="menuLoadingUser">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ zongMoney.sumMoney ? zongMoney.sumMoney : 0 }}</div>
                <div class="stat-label">支付总金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ zongMoney.weiXinAppMoney ? zongMoney.weiXinAppMoney : 0 }}</div>
                <div class="stat-label">微信APP支付金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ zongMoney.weixinGZHMoney ? zongMoney.weiXinGZHMoney : 0 }}</div>
                <div class="stat-label">微信公众号支付金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ zongMoney.weixinXcXMoney ? zongMoney.weiXinXCXMoney : 0 }}</div>
                <div class="stat-label">微信小程序支付金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ zongMoney.zhifubaoAppMoney ? zongMoney.zhifubaoAppMoney : 0 }}</div>
                <div class="stat-label">支付宝APP支付金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ zongMoney.zhifubaoH5Money ? zongMoney.zhifubaoH5Money : 0 }}</div>
                <div class="stat-label">支付宝H5支付金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ zongMoney.yueMoney ? zongMoney.yueMoney : 0 }}</div>
                <div class="stat-label">余额支付金额</div>
              </div>
            </el-card>
          </el-col>
          <!-- <el-col :span="6" class="stat-col" v-for="(item, index) in withdrawDataZf" :key="index">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ item.amount ? item.amount : '0' }}</div>
                <div class="stat-label">{{ item.name }}</div>
              </div>
            </el-card>
          </el-col> -->
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="课程统计" name="kecheng">
      </el-tab-pane>
      <el-tab-pane label="分类统计" name="fenlei">
        <div style="font-size: 18px;color: #999;font-weight: 600;padding-left: 20px;">冥想统计</div>
        <el-row :gutter="20" style="margin-top: 20px;" v-loading="tableDataLoading">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.meditationAllCount ?
                  tongjiData.meditationAllCount.meditationCount : 0
                  }}</div>
                <div class="stat-label">次数统计</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.meditationAllCount ?
                  tongjiData.meditationAllCount.meditationTime : 0
                  }}</div>
                <div class="stat-label">睡眠时长(分钟)统计</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.meditationAllCount ?
                  tongjiData.meditationAllCount.meditationDay : 0
                  }}</div>
                <div class="stat-label">睡眠天数统计</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <div style="font-size: 18px;color: #999;font-weight: 600;padding-left: 20px;">睡眠统计</div>
        <el-row :gutter="20" style="margin-top: 20px;" v-loading="tableDataLoading">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.sleepAllCount ?
                  tongjiData.sleepAllCount.sleepCount : 0
                  }}</div>
                <div class="stat-label">次数统计</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.sleepAllCount ?
                  tongjiData.sleepAllCount.sleepTime : 0
                  }}</div>
                <div class="stat-label">冥想时长(分钟)统计</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.sleepAllCount ?
                  tongjiData.sleepAllCount.sleepDay : 0
                  }}</div>
                <div class="stat-label">冥想天数统计</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <div style="font-size: 18px;color: #999;font-weight: 600;padding-left: 20px;">专注统计</div>
        <el-row :gutter="20" style="margin-top: 20px;" v-loading="tableDataLoading">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.focusAllCount ?
                  tongjiData.focusAllCount.focusCount : 0
                  }}</div>
                <div class="stat-label">次数统计</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.focusAllCount ?
                  tongjiData.focusAllCount.focusTime : 0
                  }}</div>
                <div class="stat-label">专注时长(分钟)统计</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;">{{ tongjiData.focusAllCount ?
                  tongjiData.focusAllCount.focusDay : 0
                  }}</div>
                <div class="stat-label">专注天数统计</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="待审核统计" name="diashenhe">
        <el-row :gutter="20" style="margin-top: 40px;" v-loading="menuLoading">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;" @click="Btns(1)">{{ numberDt ? numberDt : 0
                  }}</div>
                <div class="stat-label">提现待审核</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;" @click="Btns(2)">{{ numberZx ? numberZx : 0
                  }}</div>
                <div class="stat-label">注销待审核</div>
              </div>
            </el-card>
          </el-col>
          <!-- <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;" @click="Btns(3)">{{ numberSh ? numberSh : 0
                  }}</div>
                <div class="stat-label">课程待审核</div>
              </div>
            </el-card>
          </el-col> -->
          <!-- <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number" style="color: red;cursor: pointer;" @click="Btns(4)">{{ numberFxs ? numberFxs :
                  0 }}</div>
                <div class="stat-label">订单待发货</div>
              </div>
            </el-card>
          </el-col> -->
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <!-- 图表 -->
    <el-row type="flex" justify="space-between" style="margin: 30px 0;">
      <el-col :span="12">
        <div style="margin-left: 20px;margin-right: 5px;padding: 0;" class="grid-content bg-purple">
          <el-card class="box-card" shadow="never" :body-style="bodyStyle" style="padding: 20px;">
            <div slot="header" style="height: 15px;display: flex;justify-content: space-between;align-items: center;">
              <div style="height: 100%;display: flex;align-items: center;width: 50%;">类型增长折线图</div>
              <!-- <el-date-picker v-model="selectYear" :picker-options="pickerOptions" type="daterange" range-separator="至"
                start-placeholder="开始日期" @change="selectYears" end-placeholder="结束日期">
              </el-date-picker> -->
              <el-date-picker v-model="selectYear" type="daterange" range-separator="到" start-placeholder="开始时间"
                end-placeholder="结束时间" :size="size" :shortcuts="shortcuts" @change="selectYears" />
            </div>
            <div id="Chart1" class="Chart1" v-loading="tableDataLoading1">
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="12">
        <div style="margin-right: 10px;margin-left: 15px;" class="grid-content bg-purple-light">
          <el-card class="box-card" shadow="never" :body-style="bodyStyle" style="padding: 20px;">
            <div slot="header" style="height: 15px;display: flex;justify-content: space-between;align-items: center;">
              <div style="height: 100%;display: flex;align-items: center;width: 50%;">用户增长折线图</div>
              <!-- <el-date-picker v-model="selectDayDate" :picker-options="pickerOptions" type="daterange"
                range-separator="至" start-placeholder="开始日期" @change="selectTime" end-placeholder="结束日期">
              </el-date-picker> -->
              <el-date-picker v-model="selectDayDate" type="daterange" range-separator="到" start-placeholder="开始时间"
                end-placeholder="结束时间" :size="size" :shortcuts="shortcuts" @change="selectTime" />
            </div>
            <div id="Chart2" class="Chart1" v-loading="tableDataLoading2">
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
//按需加载echarts
import * as echarts from 'echarts/core';
import {
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';
import {
  LineChart
} from 'echarts/charts';
import {
  UniversalTransition
} from 'echarts/features';
import {
  CanvasRenderer
} from 'echarts/renderers';

import {
  reactive,
  ref,
  getCurrentInstance,
  onMounted
} from 'vue';
import {
  ElMessageBox,
  ElMessage
} from 'element-plus';
import { useRouter } from 'vue-router';
const {
  proxy
} = getCurrentInstance();
//数据是否加载中
const menuLoading = ref(false);
const menuLoadingUser = ref(false);
const router = useRouter();
// 用户统计
const tableData = ref({});
// 用户分析
const userStat = ref({});
// 支付统计
const zongMoney = ref({});
// 支付统计合集
const withdrawDataZf = reactive([])
// 绑定当前时间
const info = ref(null);
// 首次选择的绑定值
const activeName = ref('first');
// 时间类型
const flag = ref(1);
// 时间类型集合
const flags = reactive([
  {
    value: 1,
    label: '按天查询'
  }, {
    value: 2,
    label: '按月查询'
  }, {
    value: 3,
    label: '按年查询'
  }
]);

const numberDt = ref(0);//提现
const numberZx = ref(0);//注销
const numberSh = ref(0);//商户
const numberFxs = ref(0);//发型师
const tongjiData = ref({}); //数据统计
const tableDataLoading = ref(false);
// 图表----------
const choiceDate = ref('')
const tableDataLoading1 = ref(true);
const tableDataLoading2 = ref(true);
const orderEcharts = ref({}); //订单折线图
const increaseEcharts = ref({}); //用户增长折线图
const selectDayDate = ref('');
const pickerOptions = ref({
  onPick: ({
    maxDate,
    minDate
  }) => {
    // 把选择的第一个日期赋值给一个变量。
    choiceDate.value = minDate.getTime()
    // 如何你选择了两个日期了，就把那个变量置空
    if (maxDate) choiceDate.value = ''
  },
  disabledDate: time => {
    // 如何选择了一个日期
    if (choiceDate.value) {
      // 7天的时间戳
      const one = 6 * 24 * 3600 * 1000
      // 当前日期 - one = 7天之前
      const minTime = choiceDate.value - one
      // 当前日期 + one = 7天之后
      const maxTime = choiceDate.value + one
      return (
        time.getTime() < minTime ||
        time.getTime() > maxTime
      )
    } else {
      return false
    }
  }
})
const shortcuts = [
  {
    text: '上周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: '上月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: '过去3个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]
const bodyStyle = ref({
  padding: '0'
})
const selectYear = ref('');
const qiehuan_type = ref(1);
const lirunStat = ref({});
// ===========
//  初始加载
onMounted(() => {
  info.value = getNowTime()
  getMenuList();
  getNowTime();
  userData();

  getUserOrderEcharts();
  getUserIncreaseEcharts()
});
// 获取当前时间
const getNowTime = () => {
  const now = new Date();
  const year = now.getFullYear(); //得到年份
  const month = now.getMonth(); //得到月份
  const date = now.getDate(); //得到日期
  // const hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
  // const mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
  // const ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
  console.log(month)
  const month1 = month + 1;
  const month2 = month1.toString().padStart(2, "0");
  const date1 = date.toString().padStart(2, "0");
  var defaultDate = `${year}-${month2}-${date1}`;
  // var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;

  return defaultDate;
}
//获取用户统计
const getMenuList = () => {
  menuLoading.value = true
  proxy.$Request.get('/user/homeMessage').then(res => {
    // console.log(res)
    tableData.value = res.data
    menuLoading.value = false
  })
};
// 获取 用户分析
const userData = () => {
  const datas = {
    findDate: info.value,
    type: flag.value
  }
  menuLoadingUser.value = true
  proxy.$Request.get('/user/userMessage', {
    params: datas
  }).then(res => {
    if (res.code == 0) {
      userStat.value = res.data;
      console.log(userStat)
    }
    menuLoadingUser.value = false
  })

};
// 获取 支付统计
const rechSelect = () => {
  const datas = {
    findDate: info.value,
    findType: flag.value
  }
  menuLoadingUser.value = true
  withdrawDataZf.splice(0, withdrawDataZf.length)
  proxy.$Request.get('/cash/payMember',
    {
      params: {
        time: info.value,
        flag: flag.value
      }
    }
  ).then(res => {
    if (res.code == 0) {
      // withdrawDataZf.value = []
      zongMoney.value = res.data

      // for (const i in res.data) {
      //   console.log('Number(withdrawDataZf[i].amount)', Number(res.data[i].amount));

      //   if (Number(res.data[i].amount) > 0) {
      //     zongMoney.value = zongMoney.value + Number(res.data[i].amount)
      //   }
      // }

    }
    // withdrawDataZf.splice(0, withdrawDataZf.length, ...res.data)
    menuLoadingUser.value = false
  })


}
// 类型、时间筛选
const orderfenxi = () => {
  userData()
  rechSelect()
  qingqiuSelect()
}
//tab 切换
const handleClick = (tab, event) => {
  console.log(event.srcElement.innerText)
  if (event.srcElement.innerText == '用户分析') {
    info.value = getNowTime()
    userData()
  }
  if (event.srcElement.innerText == '支付统计') {
    info.value = getNowTime()
    rechSelect()
  }

  if (event.srcElement.innerText == '支付统计') {
    info.value = getNowTime()
    rechSelect()
  }
  if (event.srcElement.innerText == '分类统计') {
    info.value = getNowTime()
    qingqiuSelect()
  }

  if (event.srcElement.innerText == '待审核统计') {
    numberDt.value = sessionStorage.getItem('numberDt')
    numberZx.value = sessionStorage.getItem('numberZx')
    numberSh.value = sessionStorage.getItem('numberSh')
    numberFxs.value = sessionStorage.getItem('numberFxs')
  }
};
const Btns = (index) => {
  if (index == 1) {
    router.push({
      name: 'financeList',
      query: {
        state: 0,
      },

    });
  }
  if (index == 2) {
    router.push({
      name: 'messageZx',
      query: {
        status: 1,
      },

    });
  }
  if (index == 3) {
    router.push({
      name: 'courseManagement',
      query: {
        status: 0,
      },

    });
  }
  // if (index == 4) {
  //   router.push({
  //     name: 'orderCenter',
  //     query: {
  //       status: 1,
  //     },

  //   });
  // }
}
// 数据统计 列表
const qingqiuSelect = () => {
  tableDataLoading.value = true
  proxy.$Request.get('/user/selectAllTypeData', {
    params: {
      startTime: info.value,
      type: flag.value,

    }
  }, {}).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    tongjiData.value = res.data;
  })
}



//类型折线图
const getUserOrderEcharts = (startTime, endTime) => {
  tableDataLoading1.value = true
  console.log(info.value);

  // var times = info.stockDate.slice(0, 10)
  var times = info.value
  if (startTime) {
    var startTime = startTime
    var endTime = endTime
  } else {
    // var now = Date.parse(new Date())
    // var oldD = new Date(now)
    // // var year = oldD.getFullYear(); //得到年份
    // var year = this.info2.stockDate
    // // this.selectYear = year
    var now = Date.parse(new Date())
    // var choiceDate = (new Date(now)).getTime() / 1000;
    var choiceDate = Date.parse(new Date())
    var one = 6 * 24 * 3600 * 1000
    // 当前日期 - one = 7天之前
    var minTime = choiceDate - one
    var oldD = new Date(minTime)
    var year = oldD.getFullYear(); //得到年份
    var month = oldD.getMonth(); //得到月份
    var date = oldD.getDate(); //得到日期
    var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
    var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
    var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    var defaultDate = `${year}-${month}-${date}`;

    var startTime = defaultDate
    var endTime = times
    // selectYear.splice(0, withdrawDataZf.length, ...startTime, ...endTime)
    selectYear.value = [startTime, endTime]
  }

  proxy.$Request.get('/user/selectMeditationStatisticsByTime', {
    params: {
      startTime: startTime ? startTime : '',
      endTime: endTime ? endTime : ''
    }
  }, {
    // type: 'json',
  }).then(res => {
    if (res.code == 0) {
      //使用数据初始化图表
      tableDataLoading1.value = false
      orderEcharts.value = res.data;
      setEcharts()
    }


  })

};
//用户增长折线图
const getUserIncreaseEcharts = (startTime, endTime) => {
  tableDataLoading2.value = true
  // var times = info.stockDate.slice(0, 10)
  var times = info.value
  console.log('times', times)
  if (startTime) {
    var startTime = startTime
    var endTime = endTime
  } else {
    var now = Date.parse(new Date())
    // var choiceDate = (new Date(now)).getTime() / 1000;
    var choiceDate = Date.parse(new Date())
    var one = 6 * 24 * 3600 * 1000
    // 当前日期 - one = 7天之前
    var minTime = choiceDate - one
    var oldD = new Date(minTime)
    var year = oldD.getFullYear(); //得到年份
    var month = oldD.getMonth(); //得到月份
    var date = oldD.getDate(); //得到日期
    var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
    var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
    var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    var defaultDate = `${year}-${month}-${date}`;

    var startTime = defaultDate
    var endTime = times
    selectDayDate.value = [startTime, endTime]
    // selectDayDate.splice(0, withdrawDataZf.length, ...startTime, ...endTime)
  }


  proxy.$Request.get('/user/selectUserCountStatisticsByTime', {
    params: {
      startTime: startTime ? startTime : '',
      endTime: endTime ? endTime : ''
    }
  }, {
    // type: 'json',
  }).then(res => {
    if (res.code == 0) {
      //使用数据初始化图表
      increaseEcharts.value = res.data;
      tableDataLoading2.value = false
      setEcharts2()
    }


  })

};
//使用数据初始化图表
const setEcharts = () => {
  echarts.use([
    ToolboxComponent,
    TooltipComponent,
    GridComponent,
    LegendComponent,
    LineChart,
    CanvasRenderer,
    UniversalTransition
  ]);

  var chartDom = document.getElementById('Chart1');
  var myChart = echarts.init(chartDom);
  var option;

  option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['冥想总量', '睡眠总量', '专注总量',],
      top: '5%'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      top: '3%',
      feature: {
        saveAsImage: {},

      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: orderEcharts.value.year
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      name: '冥想总量',
      type: 'line',
      stack: 'Total',
      data: orderEcharts.value.meditationCountList
    }, {
      name: '睡眠总量',
      type: 'line',
      stack: 'Total',
      data: orderEcharts.value.sleepCountList
    }, {
      name: '专注总量',
      type: 'line',
      stack: 'Total',
      data: orderEcharts.value.focusCountList
    },


    ]
  };

  option && myChart.setOption(option);
};
//使用数据初始化图表
const setEcharts2 = () => {
  echarts.use([
    ToolboxComponent,
    TooltipComponent,
    GridComponent,
    LegendComponent,
    LineChart,
    CanvasRenderer,
    UniversalTransition
  ]);
  var chartDom = document.getElementById('Chart2');
  var myChart = echarts.init(chartDom);
  var option;
  option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['用户增长量'],
      top: '5%'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      top: '3%',
      feature: {
        saveAsImage: {},

      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: increaseEcharts.value.year
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      name: '用户增长量',
      type: 'line',
      stack: 'Total',
      data: increaseEcharts.value.userCountList
    },]
  };

  option && myChart.setOption(option);
};
//根据年份筛选
const selectYears = (e) => {
  // console.log(e, '`````````', this.info.stockDate)
  if (e == null) {
    getUserOrderEcharts()
  } else {
    // var now = new Date();
    // // var year = now.getFullYear(); //得到年份
    // var year = this.info2.stockDate
    // let startTime = (new Date(e)).getTime() / 1000;
    // this.getUserOrderEcharts(year)
    var now = new Date(e[0]);
    var year = now.getFullYear(); //得到年份
    var month = now.getMonth(); //得到月份
    var date = now.getDate(); //得到日期
    var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
    var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
    var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    // var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
    var defaultDate = `${year}-${month}-${date}`;

    var now1 = new Date(e[1]);
    var year1 = now1.getFullYear(); //得到年份
    var month1 = now1.getMonth(); //得到月份
    var date1 = now1.getDate(); //得到日期
    var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
    var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
    var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
    month1 = month + 1;
    month1 = month.toString().padStart(2, "0");
    date1 = date1.toString().padStart(2, "0");
    var defaultDate1 = `${year1}-${month1}-${date1}`;

    let startTime = defaultDate
    let endTime = defaultDate1
    console.log(startTime, '`````````', endTime)
    getUserOrderEcharts(startTime, endTime)
  }
};
//根据日期区间筛选
const selectTime = (e) => {
  console.log(e)
  if (e == null) {
    getUserIncreaseEcharts()
  } else {
    var now = new Date(e[0]);
    var year = now.getFullYear(); //得到年份
    var month = now.getMonth(); //得到月份
    var date = now.getDate(); //得到日期
    var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
    var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
    var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    // var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
    var defaultDate = `${year}-${month}-${date}`;

    var now1 = new Date(e[1]);
    var year1 = now1.getFullYear(); //得到年份
    var month1 = now1.getMonth(); //得到月份
    var date1 = now1.getDate(); //得到日期
    var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
    var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
    var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
    month1 = month + 1;
    month1 = month.toString().padStart(2, "0");
    date1 = date1.toString().padStart(2, "0");
    var defaultDate1 = `${year1}-${month1}-${date1}`;

    let startTime = defaultDate
    let endTime = defaultDate1

    // let startTime = (new Date(e[0])).getTime() / 1000;
    // let endTime = (new Date(e[1])).getTime() / 1000;
    getUserIncreaseEcharts(startTime, endTime)
  }
};
// --------------------------------


</script>

<style lang="scss" scoped>
.Chart1 {
  width: 100%;
  height: 300px;
}

.dashboard {
  .welcome-card {
    .content {
      p {
        font-size: 16px;
        color: #666;
      }
    }
  }

  .stat-col {
    margin-bottom: 20px;
  }

  .stat-card {
    text-align: center;

    .stat-content {
      padding: 30px 0;

      .stat-number {
        font-size: 24px;
        font-weight: bold;
        color: #409eff;
      }

      .stat-label {
        margin-top: 10px;
        color: #666;
      }
    }
  }
}
</style>
