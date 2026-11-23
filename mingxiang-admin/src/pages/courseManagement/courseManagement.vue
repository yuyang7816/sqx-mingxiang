<template>
  <div class="menu">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="课程列表" name="first">
        <div>
          <div style="display: inline-block;margin: 5px;">
            <span>课程类型 ：</span>
            <el-input v-model="typeNameT" placeholder="请输入课程类型" style="width: 150px;" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>课程名称 ：</span>
            <el-input v-model="courseNameT" placeholder="请输入课程名称" style="width: 150px;" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>课程标题 ：</span>
            <el-input v-model="courseTitleT" placeholder="请输入课程标题" style="width: 150px;" />
          </div>
          <div style="display: inline-block;margin: 5px;">
            <span>导师标题 ：</span>
            <el-input v-model="mentorTitleT" placeholder="请输入导师标题" style="width: 150px;" />
          </div>
          <div style="display: inline-block;margin: 5px;">
            <span>导师名称 ：</span>
            <el-input v-model="mentorT" placeholder="请输入名称" style="width: 150px;" />
          </div>
          <div style="display: inline-block;margin: 5px;">
            <span>状态 ：</span>
            <el-select v-model="courseStatusT" class="m-2" placeholder="请选择状态" style="width: 150px;"
              @change="handleSearch">
              <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>&nbsp;&nbsp;
          <el-button size="small" type="primary" plain @click="handleSearch()">查询</el-button>
          <el-button size="small" type="primary" @click="clearSearch()">重置</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column fixed prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="typeName" label="课程类型"></el-table-column>
          <el-table-column prop="exploreParentName" label="探索一级分类"></el-table-column>
          <el-table-column prop="exploreName" label="探索二级分类"></el-table-column>
          <el-table-column prop="courseName" label="课程名称"></el-table-column>
          <el-table-column prop="cover" label="封面" width="100">
            <template #default="scope">
              <img :src="scope.row.cover" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="courseDesc" label="课程简介" width="250">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <div style="width: 400px;">{{ scope.row.courseDesc }}</div>
                <template #reference>
                  <div
                    style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
                    slot="reference">{{ scope.row.courseDesc }}</div>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="courseTitle" label="课程标题"></el-table-column>
          <el-table-column prop="mentorTitle" label="导师标题"></el-table-column>
          <el-table-column prop="mentor" label="导师名称"></el-table-column>
          <el-table-column prop="mentorPhoto" label="导师头像" width="100">
            <template #default="scope">
              <img :src="scope.row.mentorPhoto" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="mentorIntroduction" label="导师简介" width="250">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <div style="width: 400px;">{{ scope.row.mentorIntroduction }}</div>
                <template #reference>
                  <div
                    style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
                    slot="reference">{{ scope.row.mentorIntroduction }}</div>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="课程备注" width="250">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover">
                <div style="width: 300px;">{{ scope.row.remark }}</div>
                <template #reference>
                  <div
                    style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
                    slot="reference">{{ scope.row.remark }}</div>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="backgroundImage" label="背景图片" width="100">
            <template #default="scope">
              <img :src="scope.row.backgroundImage" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="labelName" label="标签" width="200">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover">
                <div style="width: 300px;">{{ scope.row.labelName }}</div>
                <template #reference>
                  <div
                    style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
                    slot="reference">{{ scope.row.labelName }}</div>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="courseStatus" label="状态">
            <template #default="scope">
              <span v-if="scope.row.courseStatus === 0">正常</span>
              <span v-if="scope.row.courseStatus == 1" style="color: #f56c6c;">下架</span>
              <el-switch v-model="scope.row.courseStatus" @click="changeState(scope.row)"
                :disabled="!isAuth('bannerList:update')" :active-value="0" :inactive-value="1" active-color="#13ce66"
                inactive-color="#ff4949">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="zhangjieChang(scope.row)"
                style="margin: 5px;">章节管理</el-button>
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
        <span style="width: 200px;display: inline-block;text-align: right;">课程类型 ：</span>
        <el-select v-model="typeId" class="m-2" placeholder="请选择课程类型" style="width: 50%;">
          <el-option v-for="(item, index) in typeList" :key="index" :label="item.typeName" :value="item.id" />
        </el-select>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">探索一级分类 ：</span>
        <el-select v-model="exploreId1" class="m-2" placeholder="请选择课程类型" style="width: 50%;" @change="exploreSearch">
          <el-option v-for="(item, index) in exploreList1" :key="index" :label="item.exploreName" :value="item.id" />
        </el-select>
      </div>
      <div style="margin-bottom: 10px;" v-if="exploreId1 != ''">
        <span style="width: 200px;display: inline-block;text-align: right;">探索二级分类 ：</span>
        <el-select v-model="exploreId" class="m-2" placeholder="请选择课程类型" style="width: 50%;">
          <el-option v-for="(item, index) in exploreList2" :key="index" :label="item.exploreName" :value="item.id" />
        </el-select>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">课程名称 ：</span>
        <el-input style="width:50%;" v-model="courseName" type="text" placeholder="请输入课程名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">课程简介 ：</span>
        <el-input style="width:50%;" v-model="courseDesc" type="textarea" :rows="4" placeholder="请输入课程简介"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">课程标题 ：</span>
        <el-input style="width:50%;" v-model="courseTitle" type="text" placeholder="请输入课程标题"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">导师标题 ：</span>
        <el-input style="width:50%;" v-model="mentorTitle" type="text" placeholder="请输入导师标题"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">导师 ：</span>
        <el-input style="width:50%;" v-model="mentor" type="text" placeholder="请输入导师"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">导师简介 ：</span>
        <el-input style="width:50%;" v-model="mentorIntroduction" type="textarea" :rows="4"
          placeholder="请输入导师简介"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">课程备注 ：</span>
        <el-input style="width:50%;" v-model="remark" type="textarea" :rows="4" placeholder="请输入课程备注"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">课程状态 ：</span>
        <el-radio-group v-model="courseStatus">
          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">下架</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">标签 ：</span>
        <el-checkbox-group v-model="labelIds" style="display: inline-block;" v-if="labelList.length > 0"
          @change="handleCheckedCitiesChange">
          <el-checkbox v-for="(item, index) in labelList" :key="index" :label="item.id">{{ item.labelName
            }}</el-checkbox>
        </el-checkbox-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">导师头像 ：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="mentorPhoto"
            style="width: 148px;height: 148px;line-height: 148px;text-align: center;"
            action="https://mingxiangadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess1">
            <img v-if="mentorPhoto" :src="mentorPhoto" class="avatar" style="width: 148px;height: 148px;" />
            <el-icon v-else style="font-size: 28px;color: #D9D9D9">
              <Plus />
            </el-icon>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">封面 ：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="cover"
            style="width: 148px;height: 148px;line-height: 148px;text-align: center;"
            action="https://mingxiangadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess2">
            <img v-if="cover" :src="cover" class="avatar" style="width: 148px;height: 148px;" />
            <el-icon v-else style="font-size: 28px;color: #D9D9D9">
              <Plus />
            </el-icon>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">背景图片 ：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="backgroundImage"
            style="width: 148px;height: 148px;line-height: 148px;text-align: center;"
            action="https://mingxiangadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess3">
            <img v-if="backgroundImage" :src="backgroundImage" class="avatar" style="width: 148px;height: 148px;" />
            <el-icon v-else style="font-size: 28px;color: #D9D9D9">
              <Plus />
            </el-icon>
          </el-upload>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 章节列表 弹框 -->
    <el-dialog v-model="dialogFormVisibleZj" title="章节列表" width="80%" center>
      <div style="margin: 0 20px;text-align: right;">
        <el-button size="small" type="primary" plain @click="handleSearchZ()">刷新</el-button>
        <el-button size="small" type="warning" plain @click="addChangeZ()">添加</el-button>
      </div>
      <el-table v-loading="tableDataLoading1" :data="zhangjieList">
        <el-table-column fixed prop="id" label="编号" width="100"></el-table-column>
        <el-table-column prop="chapterName" label="章节名称"></el-table-column>
        <el-table-column prop="chapterImg" label="章节图片" width="100">
          <template #default="scope">
            <img v-if="scope.row.chapterImg" :src="scope.row.chapterImg" width="60" height="60" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="chapterNum" label="章节序号"></el-table-column>
        <el-table-column prop="chapterUrl" label="章节链接" width="300">
          <template #default="scope">
            <audio id="audio" controls autoplay="autoplay" v-if="scope.row.chapterUrl" style="width: 200px;">
              <source :src="scope.row.chapterUrl" type="audio/mpeg">
            </audio>
          </template>
        </el-table-column>
        <el-table-column prop="chapterDuration" label="章节时长"></el-table-column>
        <!-- <el-table-column prop="chapterPrice" label="章节金额"></el-table-column> -->
        <el-table-column prop="chapterCharge" label="是否会员专享">
          <template #default="scope">
            <span v-if="scope.row.chapterCharge === 0">是</span>
            <span v-if="scope.row.chapterCharge == 1" style="color: #f56c6c;">否</span>
          </template>
        </el-table-column>
        <el-table-column prop="chapterStatus" label="状态">
          <template #default="scope">
            <span v-if="scope.row.chapterStatus === 0">正常</span>
            <span v-if="scope.row.chapterStatus == 1" style="color: #f56c6c;">下架</span>
            <el-switch v-model="scope.row.chapterStatus" @click="changeState(scope.row)"
              :disabled="!isAuth('bannerList:update')" :active-value="0" :inactive-value="1" active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="addChangeZ(scope.row)" style="margin: 5px;">修改</el-button>
            <el-button size="small" type="danger" @click="handleDeleteZ(scope.row)" style="margin: 5px;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="liaotianSelect()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 章节列表 弹框 -->
    <el-dialog v-model="dialogFormVisibleZjA" :title="titles" width="70%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">章节名称 ：</span>
        <el-input style="width:50%;" v-model="chapterName" type="text" placeholder="请输入章节名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">封面 ：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="cover"
            style="width: 148px;height: 148px;line-height: 148px;text-align: center;"
            action="https://mingxiangadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess2">
            <img v-if="cover" :src="cover" class="avatar" style="width: 148px;height: 148px;" />
            <el-icon v-else style="font-size: 28px;color: #D9D9D9">
              <Plus />
            </el-icon>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">章节序号 ：</span>
        <el-input style="width:50%;" v-model="chapterNum" type="text" placeholder="请输入章节序号"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">章节音频 ：</span>
        <el-input style="width:50%;" v-model="chapterUrl" type="text" placeholder="请输入章节链接"></el-input>
        <el-upload v-model="chapterUrl" style="display: inline-block;margin: 3px;"
          action="https://dzhtadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
          :on-success="handleAvatarSuccess4" :before-upload="beforeVideoUpload">
          <el-button size="small" type="primary" plain>上传音频</el-button>
        </el-upload>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">章节时长 ：</span>
        <el-input style="width:50%;" v-model="chapterDuration" type="text" placeholder="请输入章节时长"></el-input>
      </div>
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">章节金额 ：</span>
        <el-input style="width:50%;" v-model="chapterPrice" type="text" placeholder="请输入章节金额"></el-input>
      </div> -->
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否会员专享 ：</span>
        <el-radio-group v-model="chapterCharge">
          <el-radio :label="0">是</el-radio>
          <el-radio :label="1">否</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">状态 ：</span>
        <el-radio-group v-model="mentorIntroduction">
          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">下架</el-radio>
        </el-radio-group>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleZjA = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToZ" style="margin: 10px;">确认</el-button>
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
const page1 = ref(1);//当前页
const limit1 = ref(10);//每页条数
const total1 = ref(0);//总条数

const tableDataLoading = ref(false);//列表是否加载中
const dialogFormVisibleU = ref(false);
const routeUrl = ref('');
const titles = ref('添加');

const classify = ref('');
const id = ref('');
const courseId = ref('');
// 课程
const typeId = ref('');//课程类型id
const courseName = ref('');//课程名称
const courseDesc = ref('');//课程简介
const courseStatus = ref('');//课程状态：0正常 1下架
const courseTitle = ref('');//课程标题
const mentorTitle = ref('');//导师标题
const mentor = ref('');//导师
const mentorPhoto = ref('');//导师头像
const mentorIntroduction = ref('');//导师简介
const cover = ref('');//封面
const remark = ref('');//课程备注
const backgroundImage = ref('');//背景图片
const labelIds = ref([]);//标签id集合
const labelList = reactive([]);//标签集合
const typeList = reactive([]);//课程类型集合
const exploreList1 = reactive([]);
const exploreList2 = reactive([]);
const exploreId = ref('');
const exploreId1 = ref('');
const dialogFormVisibleZj = ref(false);
const dialogFormVisibleZjA = ref(false);
const tableDataLoading1 = ref(false);
const zhangjieList = reactive([]);

const typeNameT = ref('');
const courseNameT = ref('');
const courseTitleT = ref('');
const mentorTitleT = ref('');
const mentorT = ref('');
const courseStatusT = ref('');
const statusData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '正常',
    value: 0
  },
  {
    label: '下架',
    value: 1
  },
  // {
  //   label: '拒绝转账',
  //   value: 2
  // }
])
// 章节
const chapterId = ref('');
const chapterName = ref(''); //章节名称
const chapterNum = ref(''); //章节序号
const chapterUrl = ref(''); //章节链接
const chapterDuration = ref(''); //章节时长
const chapterPrice = ref(''); //章节金额
const chapterCharge = ref(''); //是否收费 0 免费 1 收费
const chapterStatus = ref(''); //状态 0 正常 1 下架


onMounted(() => {
  liaotianSelect();
});


// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '课程列表') {
    classify.value = ''
    liaotianSelect();
  }


}

//数据 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.get('/courseInfo/getCourseInfoAllList', {
    params: {
      pageNum: page.value,
      pageSize: limit.value,
      typeName: typeNameT.value,
      courseName: courseNameT.value,
      courseTitle: courseTitleT.value,
      mentorTitle: mentorTitleT.value,
      mentor: mentorT.value,
      courseStatus: courseStatusT.value,

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
// 刷新
const handleSearch = () => {
  page.value = 1
  liaotianSelect()
}
// 重置
const clearSearch = () => {
  page.value = 1
  typeNameT.value = ''
  courseNameT.value = ''
  courseTitleT.value = ''
  mentorTitleT.value = ''
  mentorT.value = ''
  courseStatusT.value = ''
  liaotianSelect()
}

// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    id.value = row.id
    typeId.value = row.typeId
    courseName.value = row.courseName
    courseDesc.value = row.courseDesc
    courseStatus.value = row.courseStatus
    courseTitle.value = row.courseTitle
    mentorTitle.value = row.mentorTitle
    mentor.value = row.mentor
    mentorPhoto.value = row.mentorPhoto
    mentorIntroduction.value = row.mentorIntroduction
    cover.value = row.cover
    remark.value = row.remark
    backgroundImage.value = row.backgroundImage
    exploreId.value = row.exploreId
    exploreId1.value = row.exploreParentId

    if (row.labelIds) {
      var arrs = row.labelIds.split(',')
      var arr1 = []
      for (var i in arrs) {
        var nums = Number(arrs[i])
        arr1.push(nums)
      }
      labelIds.value = arr1
    } else {
      labelIds.value = []
    }


  } else {
    titles.value = '添加'
    id.value = ''
    typeId.value = ''//课程类型id
    courseName.value = ''//课程名称
    courseDesc.value = ''//课程简介
    courseStatus.value = 0//课程状态：0正常 1下架
    courseTitle.value = ''//课程标题
    mentorTitle.value = ''//导师标题
    mentor.value = ''//导师
    mentorPhoto.value = ''//导师头像
    mentorIntroduction.value = ''//导师简介
    cover.value = ''//封面
    remark.value = ''//课程备注
    backgroundImage.value = ''//背景图片
    labelIds.value = [] //标签id集合
    exploreId.value = ''
    exploreId1.value = ''
  }
  biaoqianSelect()
  dialogFormVisibleU.value = true
}
// 点击探索分类
const exploreSearch = () => {
  for (var i in exploreList1) {
    if (exploreId1.value == exploreList1[i].id) {
      exploreList2.splice(0, exploreList2.length, ...exploreList1[i].children)
    }
  }

}
const StairNoticeToU = () => {
  if (courseName.value == '' || courseName.value == null) {
    ElNotification({
      message: '课程名称不能为空',
      type: 'warning'
    })

    return
  }



  if (titles.value == '添加') {
    routeUrl.value = '/courseInfo/saveInfo'
  } else {
    routeUrl.value = '/courseInfo/edit'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    typeId: typeId.value,
    courseName: courseName.value,
    courseDesc: courseDesc.value,
    courseStatus: courseStatus.value,
    courseTitle: courseTitle.value,
    mentorTitle: mentorTitle.value,
    mentor: mentor.value,
    mentorPhoto: mentorPhoto.value,
    mentorIntroduction: mentorIntroduction.value,
    cover: cover.value,
    remark: remark.value,
    backgroundImage: backgroundImage.value,
    labelIds: labelIds.value.toString(),
    exploreId: exploreId.value,
    exploreParentId: exploreId1.value,
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
  proxy.$Request.post('/courseInfo/delete', {
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

/** 修改状态*/
const changeState = (data) => {
  proxy.$Request.post('/banner/updateBannerStateById', {
    id: data.id,
  }, {
    // type: 'json'
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
          liaotianSelect()
        }
      })
    }
  })
}
const handleAvatarSuccess1 = (file) => {
  mentorPhoto.value = file.data
}
const handleAvatarSuccess2 = (file) => {
  cover.value = file.data
}
const handleAvatarSuccess3 = (file) => {
  backgroundImage.value = file.data
}
const handleAvatarSuccess4 = (file) => {
  chapterUrl.value = file.data
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
//类型 标签 数据 列表
const biaoqianSelect = () => {
  // 类型
  typeList.splice(0, typeList.length)
  proxy.$Request.get('/courseType/selectCourseTypeList', {
    params: {
      pageNum: 1,
      pageSize: 50,
      parentId: 0,
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    typeList.splice(0, typeList.length, ...res.data.list)
  })

  // 标签
  labelList.splice(0, labelList.length)
  proxy.$Request.get('/courseLabel/selectByCourseLabelList', {
    params: {
      pageNum: 1,
      pageSize: 50,
      meditationName: '',
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    labelList.splice(0, labelList.length, ...res.data.list)
  })
  // 探索分类
  exploreList1.splice(0, exploreList1.length)
  proxy.$Request.get('/courseExplore/selectCourseExploreList', {
    params: {
      pageNum: 1,
      pageSize: 50,
      parentId: 0,
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    exploreList1.splice(0, exploreList1.length, ...res.data.list)
    for (var i in exploreList1) {
      if (exploreId1.value == exploreList1[i].id) {
        exploreList2.splice(0, exploreList2.length, ...exploreList1[i].children)
      }
    }
  })


}
// 多选效果
const handleCheckAllChange = (val) => {
  labelIds.value = val ? labelList : []
  isIndeterminate.value = false
}
const handleCheckedCitiesChange = (value) => {
  console.log(value)
}
// 章节列表 弹框
const zhangjieChang = (row) => {
  courseId.value = row.id
  zhangjieSelect()
  dialogFormVisibleZj.value = true

}
// 获取 章节 数据
const zhangjieSelect = () => {
  tableDataLoading1.value = true;
  zhangjieList.splice(0, zhangjieList.length)
  proxy.$Request.get('/chapterInfo/selectChapterInfoList', {
    params: {
      pageNum: page1.value,
      pageSize: limit1.value,
      courseId: courseId.value,
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading1.value = false;
    zhangjieList.splice(0, zhangjieList.length, ...res.data.list)
    total1.value = res.data.total;
  })

}
// 刷新 分类
const handleSearchZ = () => {
  page.value = 1
  zhangjieSelect()
}
// 添加/修改 分类 弹框
const addChangeZ = (row) => {
  if (row) {
    titles.value = '修改'
    id.value = row.id
    chapterName.value = row.chapterName //章节名称
    cover.value = row.chapterImg
    chapterNum.value = row.chapterNum //章节序号
    chapterUrl.value = row.chapterUrl //章节链接
    chapterDuration.value = row.chapterDuration //章节时长
    chapterPrice.value = row.chapterPrice //章节金额
    chapterCharge.value = row.chapterCharge //是否收费 0 免费 1 收费
    chapterStatus.value = row.chapterStatus //状态 0 正常 1 下架


  } else {
    titles.value = '添加'
    id.value = ''
    chapterName.value = '' //章节名称
    cover.value = ''
    chapterNum.value = '' //章节序号
    chapterUrl.value = '' //章节链接
    chapterDuration.value = '' //章节时长
    chapterPrice.value = '' //章节金额
    chapterCharge.value = 0 //是否收费 0 免费 1 收费
    chapterStatus.value = 0 //状态 0 正常 1 下架
  }
  dialogFormVisibleZjA.value = true
}
const StairNoticeToZ = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/chapterInfo/saveInfo'
  } else {
    routeUrl.value = '/chapterInfo/edit'
  }
  proxy.$Request.post(routeUrl.value, {
    courseId: courseId.value,
    id: id.value,
    chapterName: chapterName.value,
    chapterNum: chapterNum.value,
    chapterUrl: chapterUrl.value,
    chapterDuration: chapterDuration.value,
    chapterPrice: chapterPrice.value,
    chapterCharge: chapterCharge.value,
    chapterStatus: chapterStatus.value,
    chapterImg: cover.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleZjA.value = false
          zhangjieSelect()
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
const handleDeleteZ = (data) => {
  proxy.$Request.post('/chapterInfo/delete', {
    id: data.id,
  }, {
    type: 'form-data'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          zhangjieSelect()
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

span {
  font-size: 14px;
  color: #303133;
}
</style>
