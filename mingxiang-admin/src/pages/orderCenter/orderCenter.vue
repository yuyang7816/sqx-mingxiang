<template>
  <div class="menu">
    <div>
      <div style="display: inline-block;margin: 5px;">
        <span>用户名称 ：</span>
        <el-input v-model="userNameT" placeholder="请输入用户名称" style="width: 150px;" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>订单号 ：</span>
        <el-input v-model="phoneT" placeholder="请输入订单号" style="width: 150px;" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>状态 ：</span>
        <el-select v-model="stateT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="getUserList">
          <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
        </el-select>
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>充值方式 ：</span>
        <el-select v-model="classifyT" class="m-2" placeholder="请选择充值方式" style="width: 150px;" @change="getUserList">
          <el-option v-for="(item, index) in classifyData" :key="index" :label="item.label" :value="item.value" />
        </el-select>
      </div>&nbsp;&nbsp;
      <el-button type="primary" size="small" @click="getUserList">刷新</el-button>
      <el-button type="warning" size="small" @click="clearBtn">重置</el-button>
    </div>

    <el-row class="el-rows">
      <el-col :span="24">
        <div class="menu-list">
          <el-table :data="userList" v-loading="menuLoading">
            <el-table-column prop="id" align="center" label="编号" width="60" fixed="left" />
            <el-table-column label="用户名称" align="center" min-width="170" prop="userName" fixed="left">
              <template #default="scope">
                <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{ scope.row.userName ?
                  scope.row.userName : '未绑定' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="phone" label="手机号"></el-table-column>
            <el-table-column prop="money" label="充值金额" width="80"></el-table-column>
            <el-table-column prop="orderId" label="订单号"></el-table-column>
            <el-table-column prop="classify" label="支付方式">
              <template #default="scope">
                <div v-if="scope.row.classify == 1">app微信</div>
                <div v-if="scope.row.classify == 2">微信公众号</div>
                <div v-if="scope.row.classify == 3">微信小程序</div>
                <div v-if="scope.row.classify == 4">支付宝</div>
                <div v-if="scope.row.classify == 5">5--</div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
            <el-table-column prop="state" label="状态" fixed="right">
              <template #default="scope">
                <div v-if="scope.row.state == 0">待支付</div>
                <div v-if="scope.row.state == 1">支付成功</div>
                <div v-if="scope.row.state == 2">支付失败</div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <el-row class="el-rows">
      <el-col :span="24">
        <div class="topBtn flex align-center justify-end">
          <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="getUserList()" />
        </div>
      </el-col>
    </el-row>
    <!-- 添加/修改菜单 -->
    <!-- <EditDataDialog v-model="isShowEditData" v-if="isShowEditData" :menuId="menuId" @refresh="getMenuList" /> -->
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  onMounted
} from 'vue';
import { useRouter } from 'vue-router';
import pagination from '@/components/pagination/pagination.vue'
import {
  ElMessageBox,
  ElMessage
} from 'element-plus';
const {
  proxy
} = getCurrentInstance();
const router = useRouter();
//用户列表
const userList = reactive([]);
//列表是否加载中
const menuLoading = ref(false);
//是否显示添加/修改弹窗
const userNameT = ref(''); //用户昵称
const phoneT = ref(''); //手机号
const stateT = ref('');
const classifyT = ref('');
//当前页
const page = ref(1);
//每页条数
const limit = ref(10);
//总条数
const total = ref(0);
// classify ：1 app微信 2 微信公众号 3 微信小程序 4 支付宝
const classifyData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: 'app微信',
    value: 1
  },
  {
    label: '微信公众号',
    value: 2
  },
  {
    label: '微信小程序',
    value: 3
  },
  {
    label: '支付宝',
    value: 4
  }
]);
// 状态类型 ： 0 待支付 1 支付成功 2 支付失败
const statusData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '待支付',
    value: 0
  },
  {
    label: '支付成功',
    value: 1
  },
  {
    label: '支付失败',
    value: 2
  }
]);
onMounted(() => {
  getUserList();
});
//跳转用户详情
const updates = (userId) => {
  router.push({
    name: 'userDetail',
    query: {
      userId: userId,
    },

  });
  // userId.value = item.userId
  // isShowEditData.value = true
};
// 修改用户状态
const changeBtn = (rows) => {
  proxy.$Request.get('/user/updateUserStatusByUserId', {
    params: {
      userId: rows.userId,
      // status: rows.status
    }
  }, {
    type: 'json'
  }).then(res => {
    if (res.code == 0) {
      ElMessage({
        type: 'success',
        message: '操作成功'
      });
      getUserList();
    } else {
      ElMessage({
        type: 'warning',
        message: res.msg
      });
      getUserList();
    }
  })
}
//删除用户
const deleteMenu = (rows) => {
  ElMessageBox.confirm(
    '确认删除此用户吗？',
    '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    beforeClose: (action, instance, done) => {
      if (action === 'confirm') {
        instance.confirmButtonLoading = true
        instance.confirmButtonText = '删除中...';
        proxy.$Request.post('/user/deleteUserByUserId/' + rows.userId, {
          // params: {
          //   userId: rows.userId
          // }
        }, {
          // type: 'json'
        }).then(res => {
          if (res.code == 0) {
            done()
            instance.confirmButtonLoading = false
          } else {
            instance.confirmButtonText = '确认'
            instance.confirmButtonLoading = false
            ElMessageBox.alert(res.msg, '提示', {
              confirmButtonText: '确认',
            })
          }
        })
      } else {
        done()
      }
    },
  }
  ).then(() => {
    getUserList()
    ElMessage({
      type: 'success',
      message: '已删除'
    });

  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消',
    })
  })
};
//获取用户列表
const getUserList = () => {
  menuLoading.value = true
  userList.splice(0, userList.length)
  proxy.$Request.get('/admin/payDetails/getPayDetailsList', {
    params: {
      page: page.value,
      limit: limit.value,
      userName: userNameT.value,
      orderId: phoneT.value,
      classify: classifyT.value,
      state: stateT.value,
      startTime: '',
      endTime: '',

    }
  }, {
    type: 'json'
  }).then(res => {
    // console.log(res)
    userList.splice(0, userList.length, ...res.data.records)
    total.value = res.data.total;
    menuLoading.value = false
  })
};
const clearBtn = () => {
  page.value = 1
  userNameT.value = ''
  phoneT.value = ''
  classifyT.value = ''
  stateT.value = ''
  getUserList()
}


</script>

<style lang="scss" scoped>
.menu {
  width: 100%;
  // height: 100%;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: var(--el-box-shadow-light);
  display: flow-root; // 创建块格式化上下文，防止外部浮动影响
}

.topBtn {
  width: 100%;
}

.menu-list {
  width: 100%;
}

.el-rows {
  margin-top: 20px;
}

span {
  font-size: 14px;
  color: #303133;
}
</style>
