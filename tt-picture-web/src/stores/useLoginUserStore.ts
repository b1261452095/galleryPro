import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface LoginUser {
  id: string
  username: string
  email?: string
  avatar?: string
  token: string
}

export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<LoginUser | null>(null)

  // 计算属性：是否已登录
  const isLoggedIn = computed(() => !!loginUser.value)

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

  return {
    loginUser,
    isLoggedIn,
    setLoginUser,
    clearLoginUser,
    restoreLoginUser,
  }
})
