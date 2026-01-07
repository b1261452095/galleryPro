<script setup lang="ts">
import { onMounted } from 'vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

const loginUserStore = useLoginUserStore()

// 页面初始化时检查登录状态
onMounted(async () => {
  // 从后端获取当前登录用户信息
  await loginUserStore.fetchLoginUser()
})
</script>

<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style>
#app {
  min-height: 100vh;
}
</style>
