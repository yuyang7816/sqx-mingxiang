<template>
  <div class="menu">
    <div>
      <div style="display: inline-block;margin: 5px;">
        <span>用户名称 ：</span>
        <el-input v-model="userNameT" placeholder="请输入用户名称" style="width: 150px;" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>手机号码 ：</span>
        <el-input v-model="phoneT" placeholder="请输入手机号码" style="width: 150px;" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>邀请码 ：</span>
        <el-input v-model="invitationCodeT" placeholder="请输入手机号码" style="width: 150px;" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>邀请人邀请码 ：</span>
        <el-input v-model="inviterCodeT" placeholder="请输入手机号码" style="width: 150px;" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>是否是会员 ：</span>
        <el-select v-model="isVip" class="m-2" placeholder="请选择是否是会员" style="width: 150px;" @change="getUserList">
          <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
        </el-select>
      </div>&nbsp;&nbsp;
      <el-button type="primary" size="small" @click="getUserList">查询</el-button>
      <el-button type="primary" size="small" plain @click="clearBtn()">重置</el-button>
    </div>

    <el-row class="el-rows">
      <el-col :span="24">
        <div class="menu-list">
          <el-table :data="userList" v-loading="menuLoading">
            <el-table-column prop="userId" align="center" label="编号" width="60" fixed="left" />
            <el-table-column label="用户名称" align="center" min-width="170" prop="userName" fixed="left" />
            <el-table-column prop="sex" label="性别">
              <template #default="scope">
                <span v-if="scope.row.sex == 1">男</span>
                <span v-else-if="scope.row.sex == 2">女</span>
                <span v-else>未设置</span>
              </template>
            </el-table-column>
            <el-table-column label="图像">
              <template #default="scope">
                <img v-if="scope.row.avatar == null" src="../../assets/logo.png" alt="" width="40" height="40">
                <img v-else :src="scope.row.avatar" alt="" width="40" height="40">
              </template>
            </el-table-column>
            <el-table-column prop="phone" label="手机号">
            </el-table-column>
            <el-table-column prop="balance" label="当前余额" width="80"></el-table-column>
            <!-- <el-table-column prop="isVip" label="是否为会员">
					</el-table-column> -->
            <el-table-column prop="isVip" label="是否为会员">
              <template #default="scope">
                <span v-if="scope.row.isVip == 1" style="color: #13ce66;">是</span>
                <span v-else>否</span>
              </template>
            </el-table-column>
            <el-table-column prop="platform" label="渠道来源"></el-table-column>
            <el-table-column prop="invitationCode" label="邀请码"></el-table-column>
            <el-table-column label="邀请人邀请码" prop="inviterCode">
              <!-- <template #default="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template> -->
            </el-table-column>
            <!-- <el-table-column prop="zhiRate" label="一级佣金比例" width="150">
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例" width="150">
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
					</el-table-column> -->
            <el-table-column prop="zhiFuBao" label="支付宝账号">
            </el-table-column>
            <el-table-column prop="wxImg" label="收款二维码" width="150">
              <template #default="scope">
                <div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
                  暂无收款二维码
                </div>
                <div v-else style="display:flex;flex-wrap: wrap;">
                  <div style="margin: 2px;">
                    <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                      <template #reference>
                        <img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.wxImg" alt="">
                      </template>
                      <template #default>
                        <img style="width: 400px; height: auto;" :src="scope.row.wxImg" alt="">
                      </template>
                    </el-popover>

                  </div>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
            <el-table-column prop="status" label="状态" fixed="right">
              <template #default="scope">
                <el-switch v-model="scope.row.status" class="ml-2"
                  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" @change="changeBtn(scope.row)"
                  :active-value="1" :inactive-value="2" />
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" min-width="120">
              <template #default="scope">
                <el-button type="primary" size="small" @click="updata(scope.row)" style="margin:3px">
                  用户详情
                </el-button>
                <el-button size="small" type="warning" @click="zsVipChangeU(scope.row)"
                  style="margin:3px">赠送会员</el-button>
                <el-button size="small" type="warning" plain @click="quxiaoChangeU(scope.row)" style="margin:3px"
                  v-if="scope.row.isVip == 1">取消会员</el-button>
                <el-button @click="deleteMenu(scope.row)" type="danger" size="small" style="margin:3px">
                  删除
                </el-button>
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
const phoneT = ref(''); //用户昵称
const invitationCodeT = ref('');
const inviterCodeT = ref('');
const isVip = ref('');
const statusData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '否',
    value: 0
  },
  {
    label: '是',
    value: 1
  },
]);
//当前页
const page = ref(1);
//每页条数
const limit = ref(10);
//总条数
const total = ref(0);

onMounted(() => {
  getUserList();
});
//跳转用户详情
const updata = (item) => {
  router.push({
    name: 'userDetail',
    query: {
      userId: item.userId,
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
// 重置
const clearBtn = () => {
  page.value = 1
  userNameT.value = ''
  phoneT.value = ''
  isVip.value = ''
  invitationCodeT.value = ''
  inviterCodeT.value = ''

  getUserList()
}
//获取用户列表
const getUserList = () => {
  menuLoading.value = true
  userList.splice(0, userList.length)
  proxy.$Request.get('/user/selectUserList', {
    params: {
      page: page.value,
      limit: limit.value,
      userName: userNameT.value,
      phone: phoneT.value,
      invitationCode: invitationCodeT.value,
      inviterCode: inviterCodeT.value,
      isVip: isVip.value,

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
//赠送会员
const zsVipChangeU = (rows) => {
  ElMessageBox.confirm('确定赠送会员?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      proxy.$Request.post('/admin/vipDetails/giveUserVip', {
        // params: {
        userId: rows.userId,
        num: 30,
        // }
      }, {
        type: 'form-data'
      }).then(res => {
        if (res.code == 0) {
          getUserList()
          ElMessage({
            type: 'success',
            message: '操作成功'
          });
          getUserList();
        } else {
          ElMessage({
            type: 'dange',
            message: '已取消',
          })
        }
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消',
      })
    })




  // ElMessageBox.confirm(
  //   '确认要赠送会员？',
  //   '提示', {
  //   confirmButtonText: '确认',
  //   cancelButtonText: '取消',
  //   type: 'warning',
  // }).then(() => {


  // })
};
const quxiaoChangeU = (rows) => {
  ElMessageBox.confirm(
    '确认要取消会员？',
    '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    proxy.$Request.post('/admin/vipDetails/cancelUserVip', {
      // params: {
      userId: rows.userId,
      // }
    }, {
      type: 'form-data'
    }).then(res => {
      if (res.code == 0) {
        ElMessage({
          type: 'success',
          message: '操作成功'
        });
        getUserList();
      } else {
        ElMessage({
          type: 'dange',
          message: '已取消',
        })
      }
    })

  })
};
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
