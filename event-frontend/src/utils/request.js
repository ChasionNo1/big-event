//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';
import { ElMessage } from 'element-plus'
import { useTokenStore } from '@/stores/token';
import router from '@/router';
//定义一个变量,记录公共的前缀  ,  baseURL
const baseURL = 'http://localhost:8080';
const instance = axios.create({baseURL})


// 添加请求拦截器
instance.interceptors.request.use(
    (config) => {
        const tokenStore = useTokenStore()
        // 请求前的回调
        // 添加token
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token
        }

        return config
    },

    (err) => {
        // 请求错误的回调
        Promise.reject(err)
    }

)

//添加响应拦截器
instance.interceptors.response.use(
    result => {
        if (result.data.code === 0) {
            // 判断一下弹窗情况，这里只要发送一次请求，请求成功就会弹窗
            // 判断这个message是否为null，为null就不弹
            if ( result.data.message) {
                ElMessage({ message: result.data.message, type: 'success' })
            }
            return result.data;
        } else{
             // 操作失败
            ElMessage.error(result.data.message ? result.data.message : '操作失败')
            return Promise.reject(result.data);//异步的状态转化成失败的状态
        }
       

    },
    err => {
        // 判断响应状态码，如果为401，则证明未登录，提示请登录，并跳转到登录页面
        if (err.response.status === 401){
            ElMessage.error('请先登录')
            // 跳转到登录页面
            router.push('/login')
        }
        // ElMessage.error('服务异常')
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default instance;