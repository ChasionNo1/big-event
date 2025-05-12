<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import {userRegisterService, userLoginService} from '@/api/user'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/token'
//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)
// 表单引用
const form = ref(null)
// 定义数据模型
const registerData = ref({
    username:'',
    password:'',
    rePassword:''
})
const router = useRouter()
const tokenStore = useTokenStore()
// 校验密码的函数
const checkPassword = (rules, value, callback) => {
    if (value === '') {
        callback(new Error('请再次确认密码'))
    }else if (value !== registerData.value.password) {
        callback(new Error('请确保两次输入的密码一样'))
    }else{
        callback()
    }
}

// 定义表单校验规则
const rules = {
    username:[
        {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'

        },
        {
            min: 5, max:16, message:'请输入5-16位非空字符', trigger: 'blur'
        }
    ],
    password:[
        {
            required: true,
            message: '请输入密码',
            trigger: 'blur'

        },
        {
            min: 5, max:16, message:'请输入5-16位非空字符', trigger: 'blur'
        }
    ],
    rePassword:[{validator:checkPassword, trigger:'blur'}]
}

// 调用后台接口，完成注册
const register = async () => {
    try {
    // 触发表单校验
    await form.value.validate()
    
    // 校验通过，发送请求
    await userRegisterService(registerData.value)
  } catch (error) {
    // 校验失败，不发送请求
    // ElMessage.error('请正确填写表单信息')
  }
}
// 绑定数据，复用注册表单的数据模型
// 表单数据校验
// 登录函数
const login = async () => {
    try {
         // 触发表单校验
        await form.value.validate()
        let result = await userLoginService(registerData.value)
        // 把得到的token存放到pinia中
        // console.log(result.data)
        tokenStore.setToken(result.data)
        router.push('/')
    } catch (error) {
       // 校验失败，不发送请求
        // ElMessage.error('请正确填写表单信息') 
        // ElMessage.error(error) 
    }
}
// 定义函数，清空数据模型的数据
const clearRegisterData = () => {
    registerData.value = {
        username:'',
        password:'',
        rePassword:''
    }
}
</script>

<template>
  <el-row class="login-page">
    <el-col :span="12" class="bg"></el-col>
    <el-col :span="6" :offset="3" class="form">
      <!-- 注册表单 -->
      <el-form
        ref="form"
        size="large"
        autocomplete="off"
        v-if="isRegister"
        :model="registerData"
        :rules="rules"
      >
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            v-model="registerData.username"
            :prefix-icon="User"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerData.password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="rePassword">
          <el-input
            v-model="registerData.rePassword"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入再次密码"
          ></el-input>
        </el-form-item>
        <!-- 注册按钮 -->
        <el-form-item>
          <el-button
            class="button"
            type="primary"
            auto-insert-space
            @click="register"
          >
            注册
          </el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = false;clearRegisterData()">
            ← 返回
          </el-link>
        </el-form-item>
      </el-form>
      <!-- 登录表单 -->
      <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model="registerData.username" :prefix-icon="User" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
           v-model="registerData.password"
            name="password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox>记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button @click="login" class="button" type="primary" auto-insert-space 
            >登录</el-button
          >
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = true; clearRegisterData()">
            注册 →
          </el-link>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
  height: 100vh;
  background-color: #fff;

  .bg {
    background: url("@/assets/logo2.png") no-repeat 60% center / 240px auto,
      url("@/assets/login_bg.jpg") no-repeat center / cover;
    border-radius: 0 20px 20px 0;
  }

  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;

    .title {
      margin: 0 auto;
    }

    .button {
      width: 100%;
    }

    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}
</style>
