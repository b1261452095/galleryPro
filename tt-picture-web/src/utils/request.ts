import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { message } from 'ant-design-vue'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data

    // 根据后端返回的状态码判断
    if (response.status === 200) {
      return res
    } else {
      message.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  (error) => {
    console.error('响应错误:', error)

    if (error.response) {
      const { status, data } = error.response

      switch (status) {
        case 401:
          message.error('未授权，请重新登录')
          // 清除 token
          localStorage.removeItem('token')
          // 跳转到登录页
          window.location.href = '/login'
          break
        case 403:
          message.error('拒绝访问')
          break
        case 404:
          message.error('请求的资源不存在')
          break
        case 500:
          message.error('服务器错误')
          break
        default:
          message.error(data?.message || '请求失败')
      }
    } else if (error.request) {
      message.error('网络错误，请检查网络连接')
    } else {
      message.error('请求配置错误')
    }

    return Promise.reject(error)
  },
)

// 通用请求方法
export interface RequestConfig extends AxiosRequestConfig {
  showError?: boolean // 是否显示错误提示
}

export const request = <T = any>(config: RequestConfig): Promise<T> => {
  return service.request<any, T>(config)
}

// 便捷方法
export const get = <T = any>(url: string, config?: RequestConfig): Promise<T> => {
  return request<T>({ ...config, method: 'GET', url })
}

export const post = <T = any>(url: string, data?: any, config?: RequestConfig): Promise<T> => {
  return request<T>({ ...config, method: 'POST', url, data })
}

export const put = <T = any>(url: string, data?: any, config?: RequestConfig): Promise<T> => {
  return request<T>({ ...config, method: 'PUT', url, data })
}

export const del = <T = any>(url: string, config?: RequestConfig): Promise<T> => {
  return request<T>({ ...config, method: 'DELETE', url })
}

export default service
