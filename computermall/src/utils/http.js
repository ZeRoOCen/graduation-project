// import Vue from 'vue'
import axios from 'axios'
// import store from '@/store'
// import api_admin_modules from '@/api/admin/admin_index'//  @/：代表src路径下的内容；api_admin_modules为api/admin下index.js中导出的模块(export default api_admin_modules)
import modules from '@/api'
import { baseURL } from '@/config'  // baseURL在config下的index.js中导出的模块(module.exports = config)

// axios.create根据指定配置创建一个新的 axios，也就是每个新的axios都有自己的配置
// 新创建的axios没有取消请求和批量发请求的方法，其它所有语法都是一政的
const instance = axios.create({
  baseURL, // baseURL = 'http://localhost:9000'
  timeout: 30000 //单独对此请求设置网络超时，超时时间为30秒
})

const http = {}

let service = {
}

modules.forEach((item) => {
    service = { ...service, ...item }
})

for (const key in service) {
  const api = service[key]
  http[key] = async function (
    { url, ...params }, 
    isFormData = false, 
    config = {} ){
    let newParams = {}
    if (params && isFormData) {
      newParams = new FormData()
      for (const i in params) {
        newParams.append(i, params[i])
      }
    } else {
      newParams = params
    }
    let response = {}
    //观察是否需要取消封装put请求，因为会导致一些无法解决的问题 api.method === 'put'。目前的解决方法是展示不适用put请求方式
    if (api.method === 'put' || api.method === 'post' || api.method === 'patch') {
      try {
        if (api.isDycRouter) {
          response = await instance[api.method](url, newParams, config)
        } else {
          response = await instance[api.method](api.url, newParams, config)
        }
      } catch (err) {
        response = err
      }
    } else if (api.method === 'delete' || api.method === 'get') {
      config.params = newParams
      try {
        if (api.isDycRouter) {
          response = await instance[api.method](url, config)
        } else {
          response = await instance[api.method](api.url, config)
        }
      } catch (err) {
        response = err
      }
    }
    return response
  }
}

instance.interceptors.request.use(config => {
  // 这里是前置拦截器, 可做数据处理
  return config
}, error => {
   // 这里是错误回调
  return Promise.reject(error)
})

instance.interceptors.response.use(response => {
  // 这里是后置拦截器，做全局请求异常捕获
  return response.data
}, error => {
  // 这里是错误回调
  return Promise.reject(error)
})

export default http
