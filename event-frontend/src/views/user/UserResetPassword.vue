<script setup>
import { ref } from 'vue'
import { useUserInfoStore } from '@/stores/userInfo'
import {updateUserPasswordService} from '@/api/user'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import {useTokenStore} from '@/stores/token'


const tokenStore = useTokenStore()
const router = useRouter()
const userInfoStore = useUserInfoStore()
const psdInfo = ref({
    'oldPassword':'',
    'newPassword':'',
    'rePassword':''
})
const form = ref(null)
const checkPassword = (rules, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== psdInfo.value.newPassword) {
        callback(new Error('两次输入密码不一致'))
    } else {
        callback()
    }
}

const rules = {
    oldPassword:[
        {
            required: true,
            message: '请输入密码',
            trigger: 'blur'

        },
        {
            min: 5, max:16, message:'请输入5-16位非空字符', trigger: 'blur'
        }
    ],
    newPassword:[
        {
            required: true,
            message: '请输入密码',
            trigger: 'blur'

        },
        {
            min: 5, max:16, message:'请输入5-16位非空字符', trigger: 'blur'
        }
    ],
    rePassword: [
    // 基础规则：必填、长度
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '请输入5-16位非空字符', trigger: 'blur' },
    // 自定义规则：一致性校验
    { validator: checkPassword, trigger: 'blur' }
  ]
}
const updateUserPsd = async () => {
    try {
        await form.value.validate()
        let result = await updateUserPasswordService(psdInfo.value)
    ElMessage.success(result.message ? result.message : '修改成功')
    // 退出登录
    // 清空数据
    tokenStore.removeToken()
    userInfoStore.removeInfo()

    // 跳转到登录页面
    router.push('/login')

    }catch (error){

    }
   
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>修改密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form ref="form" :model="psdInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="原密码" prop="oldPassword">
                        <el-input type="password" v-model="psdInfo.oldPassword"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input type="password" v-model="psdInfo.newPassword"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="rePassword">
                        <el-input type="password" v-model="psdInfo.rePassword"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserPsd">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>