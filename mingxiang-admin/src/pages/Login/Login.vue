<template>
  <!-- <img src="../../assets/login-imgs/login-bg.png"
    style="position: absolute;z-index: -1;bottom: 0;width: 100%;height: 100vh;" /> -->
  <div class="login-container">

    <div class="login-box">
      <div class="login-title">省钱兄冥想助眠系统</div>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="账号"
            style="height: 36px;line-height: 36px;margin-bottom: 4px" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" show-password
            style="height: 36px;line-height: 36px;margin-bottom: 4px" />
        </el-form-item>
        <el-form-item prop="code">
          <el-row :gutter="10">
            <el-col :span="16">
              <el-input @keydown.enter.prevent="handleLogin" v-model="loginForm.captcha" placeholder="验证码"
                style="height: 36px;line-height: 36px;margin-bottom: 4px" />
            </el-col>
            <el-col :span="8">
              <div class="captcha-container" @click="refreshCaptcha">
                <img :src="captchaUrl" alt="验证码" class="captcha-image" />
              </div>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%;margin-top: 22px;height: 36px;line-height: 36px;"
            @click="handleLogin" :loading="loading">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {
  ref,
  onMounted,
  getCurrentInstance
} from 'vue'
import {
  ElMessage
} from 'element-plus'
import {
  useRouter
} from 'vue-router'
import {
  useUserStore
} from '@/stores/user'
import {
  getUUID
} from '/comment/utils.js' // 正确导入方式
import {
  ROOTPATH
} from '/comment/httpUrl'
const {
  proxy
} = getCurrentInstance();
const loginFormRef = ref()
const loading = ref(false)
const captchaUrl = ref('');
const captchaKey = ref(''); //获取验证码的标识

//登录表单
const loginForm = ref({
  username: '',
  password: '',
  captcha: '',
  uuid: '',
});

const rules = {
  username: [{
    required: true,
    message: '请输入用户名',
    trigger: 'blur'
  }],
  password: [{
    required: true,
    message: '请输入密码',
    trigger: 'blur'
  }],
  captcha: [{
    required: true,
    message: '请输入验证码',
    trigger: 'blur'
  }]
}

const router = useRouter()
const userStore = useUserStore()

// 生成验证码URL（这里使用一个在线服务作为示例）
const generateCaptchaUrl = () => {
  try {
    // 生成唯一的验证码标识符
    captchaKey.value = getUUID()
    loginForm.value.uuid = captchaKey.value
    // 获取验证码图片的 URL
    const url = `${ROOTPATH}/captcha.jpg?uuid=${captchaKey.value}`
    return url
  } catch (error) {
    ElMessage.error('获取验证码失败')
    return ''
  }
}

// 刷新验证码
const refreshCaptcha = () => {
  captchaUrl.value = generateCaptchaUrl()
}
// 页面加载时获取验证码
onMounted(() => {
  refreshCaptcha()
})
//登录
const handleLogin = async () => {
  try {
    await loginFormRef.value.validate()
    loading.value = true
    proxy.$Request.post('/sys/login', loginForm.value, {
      type: 'json'
    }).then(res => {
      if (res.code == 0) {
        userStore.setToken(res.token); //全局存储登录凭证
        userStoreBtn()
        router.push('/');
        loading.value = false;
      } else {
        ElMessage.error(res.msg)
        loading.value = false;
        //登录失败刷新验证码
        refreshCaptcha();
      }
    })
  } catch (error) {
    console.log('表单验证失败:', error)
    // 验证失败时刷新验证码
    refreshCaptcha()
  }
}
// 获取用户信息
const userStoreBtn = async () => {
  console.log('1111')
  proxy.$Request.get('/sys/user/info', {
  }).then((res) => {
    userStore.setUserInfo(res.data.user)
    console.log(res)
  })
}

</script>

<style lang="scss" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
  background: url('../../assets/login-imgs/login_bg.jpg');
  background-size: cover;

  .login-box {
    width: 400px;
    background: white;
    border-radius: 6px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px 30px;

    .login-title {
      text-align: center;
      margin-bottom: 20px;
      margin-top: 10px;
      font-size: 20px;
      color: #333;
      font-weight: 600;
    }

    .captcha-container {
      height: 36px;
      cursor: pointer;

      .captcha-image {
        width: 100%;
        height: 100%;
        // object-fit: cover;
        // border-radius: 4px;
      }
    }
  }
}
</style>
