<template>
  <div class="menu">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="场景配置列表" name="first">
        <div style="margin: 0 20px;text-align: right;">
          <el-button size="small" type="primary" plain @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column fixed prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="effectsName" label="名称"></el-table-column>
          <el-table-column prop="bgImg" label="图片">
            <template #default="scope">
              <img v-if="scope.row.bgImg" :src="scope.row.bgImg" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="filePath" label="音频链接" width="300">
            <template #default="scope">
              <audio id="audio" controls autoplay="autoplay" v-if="scope.row.filePath" style="width: 200px;">
                <source :src="scope.row.filePath" type="audio/mpeg">
              </audio>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
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
    </el-tabs>
    <!-- 弹框 -->
    <!-- 添加/修改 弹框 -->
    <el-dialog v-model="dialogFormVisibleU" :title="titles" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">图片 ：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="imageUrl"
            style="width: 148px;height: 148px;line-height: 148px;text-align: center;"
            action="https://mingxiangadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess1">
            <img v-if="imageUrl" :src="imageUrl" class="avatar" style="width: 148px;height: 148px;" />
            <el-icon v-else style="font-size: 28px;color: #D9D9D9">
              <Plus />
            </el-icon>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">音频 ：</span>
        <el-input style="width:50%;" v-model="filePath" type="text" placeholder="请输入音频链接"></el-input>
        <el-upload v-model="filePath" style="display: inline-block;margin: 3px;"
          action="https://dzhtadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
          :on-success="handleAvatarSuccess4" :before-upload="beforeVideoUpload">
          <el-button size="small" type="primary" plain>上传音频</el-button>
        </el-upload>
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
  ElMessage,
  ElNotification
} from 'element-plus';
const {
  proxy
} = getCurrentInstance();
const activeName = ref('first'); //默认选中
const liaotianData = reactive([]); //数据 集合
const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数

const tableDataLoading = ref(false);//列表是否加载中
const dialogFormVisibleU = ref(false);
const routeUrl = ref('');
const titles = ref('添加');

const classify = ref('');
const id = ref('');
const name = ref('');
const imageUrl = ref('');
const sort = ref('');
const filePath = ref(''); //音乐链接


onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '场景配置列表') {
    classify.value = 1
    liaotianSelect();
  }
}

//分类数据 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.get('/soundEffects/getSoundEffectsList', {
    params: {
      pageNum: page.value,
      pageSize: limit.value,
      effectsName: '',
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    liaotianData.splice(0, liaotianData.length, ...res.data.list)
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
    id.value = row.id
    name.value = row.effectsName
    imageUrl.value = row.bgImg
    sort.value = row.sort
    filePath.value = row.filePath

  } else {
    titles.value = '添加'
    id.value = ''
    name.value = ''
    imageUrl.value = ''
    sort.value = ''
    filePath.value = ''
  }
  dialogFormVisibleU.value = true
}
const handleAvatarSuccess4 = (file) => {
  filePath.value = file.data
}
const beforeVideoUpload = (file) => {
  var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
  const extension = testmsg === 'mp3'
  if (!extension) {
    ElMessage({
      message: "上传文件只能是mp3格式！",
      type: 'error',
      onClose: () => {
      }
    })

  }
  return extension;
}
const StairNoticeToU = () => {
  if (name.value == '' || name.value == null) {
    ElNotification({
      message: '名称不能为空',
      type: 'warning'
    })

    return
  }
  if (imageUrl.value == '' || imageUrl.value == null) {
    ElNotification({
      message: '图片不能为空',
      type: 'warning'
    })

    return
  }
  if (filePath.value == '' || filePath.value == null) {
    ElNotification({
      message: '音频不能为空',
      type: 'warning'
    })

    return
  }
  if (sort.value == '' || sort.value == null) {
    ElNotification({
      message: '排序不能为空',
      type: 'warning'
    })

    return
  }

  if (titles.value == '添加') {
    routeUrl.value = '/soundEffects/saveInfo'
  } else {
    routeUrl.value = '/soundEffects/edit'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    effectsName: name.value,
    bgImg: imageUrl.value,
    sort: sort.value,
    filePath: filePath.value,
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
/** 删除 */
const handleDelete = (data) => {
  proxy.$Request.post('/soundEffects/delete', {
    id: data.id,
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

function handleAvatarSuccess1(file) {
  imageUrl.value = file.data
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
