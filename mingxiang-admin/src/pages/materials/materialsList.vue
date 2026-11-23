<template>
  <div class="menu">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="分类管理" name="first">
        <div style="margin: 0 20px;text-align: right;">

          <el-button size="small" type="primary" plain @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="helpClassifyId" label="编号" width="100"></el-table-column>
          <el-table-column prop="helpClassifyName" label="名称"></el-table-column>
          <el-table-column prop="sort" label="排序" width="100"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="addChangeU(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)" style="margin: 5px;">删除</el-button>
            </template>
          </el-table-column>

        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="liaotianSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="文章管理" name="second">
        <div style="text-align: right;margin: 0 20px;">
          <div style="display: inline-block;margin-right: 10px;">
            <span>分类：</span>
            <el-select v-model="helpClassifyIdT" class="m-2" placeholder="请选择分类" size="small" style="width: 200px;"
              @change="handleSearchW()">
              <el-option v-for="(item, index) in liaotianData" :key="index" :label="item.helpClassifyName"
                :value="item.helpClassifyId" />
            </el-select>
          </div>


          <el-button size="small" type="primary" plain @click="handleSearchW()">刷新</el-button>
          <el-button size="small" type="primary" @click="addChangeW()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="wenzhangData">
          <el-table-column prop="helpWordId" label="编号" width="100"></el-table-column>
          <el-table-column prop="helpWordTitle" label="标题"></el-table-column>
          <el-table-column prop="helpWordContent" label="内容"></el-table-column>
          <el-table-column prop="sort" label="排序" width="100"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button size="small" type="primary" @click="addChangeW(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="danger" @click="handleDeleteW(scope.row)"
                style="margin: 5px;">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="liaotianSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <!-- 弹框 -->
    <!-- 审核 弹框 -->
    <el-dialog v-model="dialogFormVisibleU" :title="titles" width="80%" center>
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
        <el-radio-group v-model="state" class="ml-4">

          <el-radio :label="2">封禁账号</el-radio>
          <el-radio :label="3">驳回</el-radio>
          <el-radio :label="4">4通过但不做处理</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">审核说明：</span>
        <el-input style="width:50%;" v-model="description" type="textarea" rows="4" placeholder="请输入审核说明"></el-input>
      </div> -->
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
        <el-input style="width:50%;" v-model="helpClassifyName" type="text" placeholder="请输入名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 审核 弹框 -->
    <el-dialog v-model="dialogFormVisibleW" :title="titles" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
        <el-input style="width:50%;" v-model="helpWordTitle" type="text" placeholder="请输入标题"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
        <el-input style="width:50%;" v-model="helpWordContent" type="text" placeholder="请输入内容"></el-input>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleW = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToW" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  onMounted
} from 'vue';
import pagination from '@/components/pagination/pagination.vue'
import {
  ElMessage
} from 'element-plus';
const {
  proxy
} = getCurrentInstance();
const activeName = ref('first'); //默认选中
const liaotianData = reactive([]); //分类 集合
const wenzhangData = reactive([]); //文章 集合
const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数

const tableDataLoading = ref(false);//列表是否加载中
const dialogFormVisibleU = ref(false);
const routeUrl = ref('');
const titles = ref('添加');
const helpClassifyId = ref('');
const helpClassifyName = ref('');
const parentId = ref(0);
const sort = ref('');

const helpClassifyIdT = ref('');
const dialogFormVisibleW = ref(false);
const helpWordId = ref('');
const helpWordTitle = ref('');
const helpWordContent = ref('');


onMounted(() => {
  liaotianSelect();
});


// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '分类管理') {
    liaotianSelect();
  }
  if (event.srcElement.innerText == '文章管理') {
    helpClassifyIdT.value = liaotianData[0].helpClassifyId
    wenzhangSelect();
  }

}

//分类 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/helpClassify/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    helpClassifyName: '',
    parentId: 0,

    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    liaotianData.splice(0, liaotianData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 刷新 分类
const handleSearch = () => {
  page.value = 1
  liaotianSelect()
}


// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    helpClassifyId.value = row.helpClassifyId
    helpClassifyName.value = row.helpClassifyName
    parentId.value = row.parentId
    sort.value = row.sort
  } else {
    titles.value = '添加'
    helpClassifyId.value = ''
    helpClassifyName.value = ''
    parentId.value = 0
    sort.value = ''
  }
  dialogFormVisibleU.value = true
}
const StairNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/helpClassify/save'
  } else {
    routeUrl.value = '/admin/helpClassify/update'
  }
  proxy.$Request.post(routeUrl.value, {
    helpClassifyId: helpClassifyId.value,
    helpClassifyName: helpClassifyName.value,
    parentId: parentId.value,
    sort: sort.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleU.value = false
          liaotianSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
        }
      })
    }
  })
}
/** 删除 分类*/
const handleDelete = (data) => {
  proxy.$Request.post('/admin/helpClassify/delete', {
    helpClassifyId: data.helpClassifyId,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          liaotianSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
        }
      })
    }
  })
}
// 文章 列表
const wenzhangSelect = () => {
  tableDataLoading.value = true
  wenzhangData.splice(0, wenzhangData.length)
  proxy.$Request.post('/admin/helpWord/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    helpClassifyId: helpClassifyIdT.value,
    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    wenzhangData.splice(0, wenzhangData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 刷新 文章
const handleSearchW = () => {
  page.value = 1
  wenzhangSelect()
}
// 添加/修改 文章 弹框
const addChangeW = (row) => {
  if (row) {
    titles.value = '修改'
    helpClassifyId.value = row.helpClassifyId
    helpWordId.value = row.helpWordId
    helpWordTitle.value = row.helpWordTitle
    helpWordContent.value = row.helpWordContent
    sort.value = row.sort

  } else {
    titles.value = '添加'
    helpClassifyId.value = helpClassifyIdT.value
    helpWordId.value = ''
    helpWordTitle.value = ''
    helpWordContent.value = ''
    sort.value = ''

  }
  dialogFormVisibleW.value = true
}
const StairNoticeToW = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/helpWord/save'
  } else {
    routeUrl.value = '/admin/helpWord/update'
  }
  proxy.$Request.post(routeUrl.value, {
    helpWordId: helpWordId.value,
    helpClassifyId: helpClassifyId.value,
    helpWordTitle: helpWordTitle.value,
    helpWordContent: helpWordContent.value,
    sort: sort.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleW.value = false
          wenzhangSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
        }
      })
    }
  })
}
/** 删除 文章*/
const handleDeleteW = (data) => {
  proxy.$Request.post('/admin/helpWord/delete', {
    helpWordId: data.helpWordId,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          liaotianSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
        }
      })
    }
  })
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

.el-rows {
  margin-top: 20px;
}

span {
  color: #8c939d;
}

.main-view {
  display: flex;
  flex-direction: column;
  width: 100%;

  .container {
    // background-color: white;
    width: 100%;
    // min-height: 800px;
    height: fit-content;
    border-radius: 5px;
    padding: 30px;
    box-sizing: border-box;

    >* {
      margin: 0 0 30px 0;

      &:last-child {
        margin: 0;
      }
    }
  }
}

.detailtabel h2 {
  margin-top: 0;
}

.detailtabel .table_main table {
  width: 100%;
}

.detailtabel .table_main {
  border: 1px solid #e8e8e8;
}

.detailtabel table tr {
  border-bottom: 1px solid #e8e8e8;
}

.detailtabel table tr th {
  background-color: #fafafa;
  padding: 16px 24px;
  border-right: 1px solid #e8e8e8;
  border-bottom: 1px solid #e8e8e8;
}

.detailtabel table tr td {
  padding: 16px 24px;
  border-right: 1px solid #e8e8e8;
  border-bottom: 1px solid #e8e8e8;
}

.detailtabel table th {
  color: rgba(0, 0, 0, 0.85);
  font-weight: 400;
  font-size: 14px;
  line-height: 1.5;
}

.border-rt {
  border-right: none !important;
}

.border-bt {
  border-bottom: none !important;
}

.el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-color: #409EFF;
  width: 148px;
  height: 148px;
}

.el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 148px;
  height: 148px;
  line-height: 148px;
  text-align: center;
}

.avatar {
  width: 148px;
  height: 148px;
  display: block;
}
</style>
