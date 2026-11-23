<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">省钱兄冥想助眠系统</div>
      <div class="menu-wrapper">
        <el-menu :default-active="$route.path" router class="menu" background-color="#304156" text-color="#ffffff"
          active-text-color="#4f9dec" :unique-opened="true">
          <template v-for="item in menNavList" :key="item.menuId">
            <!-- 一级菜单且没有子菜单 -->
            <el-menu-item v-if="item.type === 1 && (!item.list || item.list.length === 0)" :index="'/' + item.url">

              <el-badge class="item" v-if="item.menuId == 34 && numberDt != 0" :value="numberDt">
                <el-icon>
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </el-badge>
              <el-badge class="item" v-else-if="item.menuId == 208 && numberZx != 0" :value="numberZx">
                <el-icon>
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </el-badge>
              <el-badge class="item" v-else-if="item.menuId == 154 && numberSh != 0" :value="numberSh">
                <el-icon>
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </el-badge>
              <el-badge class="item" v-else-if="item.menuId == 145 && numberFxs != 0" :value="numberFxs">
                <el-icon>
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </el-badge>
              <div v-else>
                <el-icon>
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </div>
            </el-menu-item>

            <!-- 一级菜单且有子菜单 -->
            <el-sub-menu v-else-if="item.type === 0 || (item.list && item.list.length > 0)"
              :index="item.menuId.toString()">
              <template #title>
                <el-icon>
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </template>
              <!-- 二级菜单 -->
              <el-menu-item v-for="subItem in item.list" :key="subItem.menuId" :index="'/' + subItem.url">
                <el-icon>
                  <component :is="subItem.icon" />
                </el-icon>
                <span>{{ subItem.name }}</span>
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </div>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <!-- 添加标签页 -->
          <div class="tabs-container">
            <el-tabs v-model="activeTab" closable @tab-click="handleTabClick" @tab-remove="handleTabRemove"
              class="tabs">
              <el-tab-pane v-for="tab in tabs" :key="tab.path" :label="tab.title" :name="tab.path">
              </el-tab-pane>
            </el-tabs>
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <!-- <el-avatar :size="30" /> -->
                <img src="../assets/logo.png" width="30px"
                  style="width: 30px;height: 30px;border-radius: 50%;display: inline-block;"></img>
                <span>{{ userStore.userInfo?.username || '管理员' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  onMounted,
  watch
} from 'vue';
import {
  useUserStore
} from '@/stores/user'
import {
  useRouter,
  useRoute
} from 'vue-router'

const {
  proxy
} = getCurrentInstance()
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

const menNavList = reactive([]);
// 从 sessionStorage 恢复标签页或使用默认值
const tabs = ref([]);
const activeTab = ref('/home')
const numberDt = ref(0);//提现
const numberZx = ref(0);//注销
const numberSh = ref(0);//商户
const numberFxs = ref(0);//发型师

onMounted(() => {
  // 从 sessionStorage 恢复标签页状态
  const savedTabs = sessionStorage.getItem('layout-tabs')
  if (savedTabs) {
    tabs.value = JSON.parse(savedTabs)
  } else {
    tabs.value = [{
      title: '数据中心',
      path: '/home',
      name: 'home'
    }]
  }

  // 恢复当前激活的标签页
  const savedActiveTab = sessionStorage.getItem('layout-active-tab')
  if (savedActiveTab) {
    activeTab.value = savedActiveTab
  }

  // 动态获取菜单
  getMenNavList()
  // 获取待审核数据
  getListNum();
});

// 递归查找菜单标题
const findMenuTitle = (path, menus) => {
  for (const menu of menus) {
    if (menu.url && `/${menu.url}` === path) {
      return menu.name
    }
    if (menu.list && menu.list.length > 0) {
      const title = findMenuTitle(path, menu.list)
      if (title) return title
    }
  }
  return null
}

// 获取菜单列表
const getMenNavList = () => {
  menNavList.splice(0, menNavList.length)
  proxy.$Request.get('/sys/menu/nav').then(res => {
    if (res.code == 0) {
      menNavList.splice(0, menNavList.length, ...res.menuList)
      sessionStorage.setItem('permissions', JSON.stringify(res.permissions || '[]'))
    }
  })
};
// 获取待审核数据
const getListNum = () => {
  // 提现待审核 numberDt
  proxy.$Request.get('/cash/selectAdminHelpProfit', {
    params: {
      page: 1,
      limit: 10,
      state: 0,
      type: '',
    }
  }, {
    // type: 'json'
  }).then(res => {
    if (res.code == 0) {
      numberDt.value = res.data.total
      sessionStorage.setItem('numberDt', res.data.total)
      console.log(numberDt.value, 'numberDt--------', sessionStorage.getItem('numberDt'));
    }
  })
  // 注销 numberZx
  // proxy.$Request.get('/message/selectMessageByType', {
  //   params: {
  //     page: 1,
  //     limit: 10,
  //     state: 8,
  //   }
  // }, {
  //   // type: 'json'
  // }).then(res => {
  //   if (res.code == 0) {
  //     numberZx.value = res.data.total
  //     sessionStorage.setItem('numberZx', res.data.total)
  //   }
  // })
  // 课程待审核 numberSh
  // proxy.$Request.get('/courseInfo/getCourseInfoAllList', {
  //   params: {
  //     pageNum: 1,
  //     pageSize: 10,
  //     courseStatus: 0,
  //   }
  // }, {
  //   // type: 'json'
  // }).then(res => {
  //   if (res.code == 0) {
  //     numberSh.value = res.data.total
  //     sessionStorage.setItem('numberSh', res.data.total)
  //   }
  // })
  // 订单待发货 numberFxs
  // proxy.$Request.post('/admin/omsOrder/page', {
  //   // params: {
  //   page: 1,
  //   limit: 10,
  //   status: 1,
  //   // }
  // }, {
  //   type: 'json'
  // }).then(res => {
  //   if (res.code == 0) {
  //     numberFxs.value = res.data.total
  //     sessionStorage.setItem('numberFxs', res.data.total)
  //   }
  // })
}

// 监听标签页变化，保存到 sessionStorage
watch(tabs, (newTabs) => {
  sessionStorage.setItem('layout-tabs', JSON.stringify(newTabs))
  // 获取监听 待审核数据
  // setInterval(() => {
  //   getListNum();
  // }, 6000)
}, {
  deep: true
})

// 监听当前激活标签页变化，保存到 sessionStorage
watch(activeTab, (newActiveTab) => {
  sessionStorage.setItem('layout-active-tab', newActiveTab)
})

// 监听路由变化，自动添加标签页
watch(route, (to) => {
  activeTab.value = to.path
  // 查找是否已存在该标签页
  const existingTab = tabs.value.find(tab => tab.path === to.path)
  if (!existingTab) {
    // 查找菜单项获取标题
    const menuTitle = findMenuTitle(to.path, menNavList)
    if (menuTitle) {
      tabs.value.push({
        title: menuTitle,
        path: to.path,
        name: to.name
      })
    }
  }
}, {
  immediate: true
})

// 标签页点击事件
const handleTabClick = (tab) => {
  router.push(tab.props.name)
}

// 标签页关闭事件
const handleTabRemove = (targetName) => {
  // 不能关闭当前激活的标签页和首页标签页
  if (targetName === activeTab.value) {
    // 如果关闭的是当前页，跳转到下一个或上一个标签页
    const currentIndex = tabs.value.findIndex(tab => tab.path === targetName)
    if (currentIndex > 0) {
      const nextTab = tabs.value[currentIndex + 1] || tabs.value[currentIndex - 1]
      if (nextTab) {
        router.push(nextTab.path)
      }
    }
  }

  // 过滤掉被关闭的标签页
  tabs.value = tabs.value.filter(tab => tab.path !== targetName)
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  // 清除 sessionStorage 中的标签页信息
  sessionStorage.removeItem('layout-tabs')
  sessionStorage.removeItem('layout-active-tab')
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;

  .logo {
    height: 60px;
    line-height: 60px;
    text-align: center;
    color: white;
    font-size: 20px;
    font-weight: bold;
    background-color: #304156;
  }

  .menu-wrapper {
    height: calc(100% - 60px);
    overflow-y: auto;

    // 隐藏滚动条但仍保留滚动功能
    &::-webkit-scrollbar {
      display: none;
      /* Chrome Safari */
    }

    scrollbar-width: none;
    /* Firefox */
    -ms-overflow-style: none;
    /* IE 10+ */
  }

  .menu {
    border: none;
    min-height: 100%;

    .el-menu-item {
      .item {
        :deep(.is-fixed) {
          right: 0px !important;
          top: 20px !important;

        }
      }
    }
  }

  .header {
    display: flex;
    align-items: center;
    padding: 0;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    background-color: white;
    height: 50px;

    .header-content {
      display: flex;
      align-items: center;
      width: 100%;
      height: 100%;
    }

    .tabs-container {
      flex: 1;
      height: 100%;
      width: 88%;

      .tabs {
        height: 100%;
        width: 100%;

        :deep(.el-tabs__header) {
          margin: 0;
          border-bottom: 1px solid #e4e7ed;
        }

        :deep(.el-tabs__nav) {
          border: none;
          height: 100%;
        }

        :deep(.el-tabs__item) {
          height: 100%;
          line-height: 50px;
          padding: 0 20px;
          font-size: 14px;
        }
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      padding: 0 20px;
      height: 100%;

      .user-info {
        display: flex;
        align-items: center;
        cursor: pointer;
        height: 100%;

        span {
          margin-left: 10px;
        }
      }
    }
  }

  .el-main {
    background-color: #f5f5f5;
    padding: 20px;
  }
}
</style>
