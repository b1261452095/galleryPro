import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getLoginUserInfoUsingGet } from '@/api/yonghuguanli'

export interface LoginUser {
  id: string
  username: string
  email?: string
  avatar?: string
  token: string
  userRole?: string // 用户角色：admin 或 user
}

/**
 * 存储登录信息的状态
 */
export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<LoginUser | null>(null)

  // 计算属性：是否已登录
  const isLoggedIn = computed(() => !!loginUser.value)

  // 计算属性：是否是管理员
  const isAdmin = computed(() => loginUser.value?.userRole === 'admin')

  // 设置登录用户信息
  const setLoginUser = (user: LoginUser) => {
    loginUser.value = user
    // 保存到 localStorage
    localStorage.setItem('loginUser', JSON.stringify(user))
    localStorage.setItem('token', user.token)
  }

  // 清除登录用户信息
  const clearLoginUser = () => {
    loginUser.value = null
    localStorage.removeItem('loginUser')
    localStorage.removeItem('token')
  }

  // 从 localStorage 恢复登录状态
  const restoreLoginUser = () => {
    const userStr = localStorage.getItem('loginUser')
    if (userStr) {
      try {
        loginUser.value = JSON.parse(userStr)
      } catch (error) {
        console.error('恢复登录状态失败:', error)
        clearLoginUser()
      }
    }
  }

  // 从后端获取登录用户信息
  const fetchLoginUser = async () => {
    try {
      const res = await getLoginUserInfoUsingGet()
      if (res.code === 0 && res.data) {
        const userData = res.data as API.LoginUserVo
        // 将后端数据转换为前端 LoginUser 格式
        const user: LoginUser = {
          id: String(userData.id || ''),
          username: userData.userName || userData.userAccount || '',
          email: '',
          avatar: userData.userAvatar,
          token: localStorage.getItem('token') || '',
          userRole: userData.userRole,
        }
        setLoginUser(user)
      }
    } catch {
      // 获取失败，可能是未登录状态
    }
  }

  return {
    loginUser,
    isLoggedIn,
    isAdmin,
    setLoginUser,
    clearLoginUser,
    restoreLoginUser,
    fetchLoginUser,
  }
})
