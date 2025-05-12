<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import {ref} from 'vue'
import defaultAvatar from '@/assets/default.png'
import { useUserInfoStore } from '@/stores/userInfo'
import {useTokenStore} from '@/stores/token'
import {updateUserAvatarUrlService} from '@/api/user'

const userInfoStore = useUserInfoStore()
const tokenStore = useTokenStore()
const uploadRef = ref()


//用户头像地址 数据回显
const imgUrl= ref(userInfoStore.info.userPic)



// 上传前校验文件类型和大小
const beforeUpload = (file) => {
      const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        ElMessage.error('只能上传图片文件(JPEG/PNG/GIF)!');
      }
      if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB!');
      }
      return isImage && isLt2M; // 返回true才会继续上传
    }


// 图片上传成功回调函数
const handleUploadSuccess =  (result) => {
    imgUrl.value = result.data

}


// 更新头像
const updateAvatar = async () => {
    await updateUserAvatarUrlService(imgUrl.value)
    
    // 更新pinia中数据
    userInfoStore.info.userPic = imgUrl.value
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-upload 
                    ref="uploadRef"
                    class="avatar-uploader" 
                    :show-file-list="false"
                    :auto-upload="true"
                    action="/api/upload"
                    :headers="{'Authorization': tokenStore.token}"
                    :on-success="handleUploadSuccess"
                    :before-upload="beforeUpload"     
                    :name="file"
                    >
                    <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                    <img v-else :src="defaultAvatar" width="278" />
                </el-upload>
                <br />
                <el-button type="primary" :icon="Plus" size="large"  @click="uploadRef.$el.querySelector('input').click()">
                    选择图片
                </el-button>
                <el-button type="success" @click="updateAvatar" :icon="Upload" size="large">
                    上传头像
                </el-button>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep() {
        .avatar {
            width: 278px;
            height: 278px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}
</style>