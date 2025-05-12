import request from '@/utils/request'

// 提供调用注册接口的函数
export const userRegisterService = (registerData) => {
    // 借助urlSearchParams完成传递
    const params = new URLSearchParams();
    for (let key in registerData) {
        params.append(key, registerData[key])
    }
    return request.post('/user/register', params)
}

// 提供调用登录接口的函数
export const userLoginService = (loginData) => {
    // 借助urlSearchParams完成传递
    const params = new URLSearchParams();
    for (let key in loginData) {
        params.append(key, loginData[key])
    }
    return request.post('/user/login', params)
}

// 获取用户详细信息
export const userInfoService = () => {
    // 在请求头添加token，这个在拦截器中已经添加了
    return request.get('/user/userInfo')

}

// 修改用户信息
export const updateUserInfoService = (info) => {
    return request.put('/user/update', info)
}

// 更新用户头像地址
export const updateUserAvatarUrlService = (url) => {
    const params = new URLSearchParams()
    params.append('avatarUrl', url)
    return request.patch('/user/updateAvatar', params)
}

// 更新密码 
export const updateUserPasswordService = (data) => {
    return request.patch('/user/updatePwd', data)
}