<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { UserOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()
const currentYear = new Date().getFullYear()

// 根据用户权限动态生成导航菜单
const navItems = computed(() => {
  const items = [{ path: '/', label: '首页' }]

  // 只有管理员才能看到用户管理菜单
  if (loginUserStore.isAdmin) {
    items.push({ path: '/admin/user', label: '用户管理' })
  }

  items.push(
    { path: '/gallery', label: '图片管理' },
    { path: '/upload', label: '上传' },
    { path: '/about', label: '关于' },
  )

  return items
})

const isActive = (path: string) => {
  return route.path === path
}

const navigateTo = (path: string) => {
  router.push(path)
}

const isLoggedIn = computed(() => loginUserStore.isLoggedIn)

const handleLogin = () => {
  router.push('/user/login')
}

const handleLogout = () => {
  loginUserStore.clearLoginUser()
  console.log('已退出登录')
}
</script>

<template>
  <div class="main-layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-container">
        <div class="logo">
          <h1>TT Picture</h1>
        </div>
        <nav class="nav">
          <a
            v-for="item in navItems"
            :key="item.path"
            :class="['nav-link', { active: isActive(item.path) }]"
            @click="navigateTo(item.path)"
          >
            {{ item.label }}
          </a>
        </nav>
        <div class="header-actions">
          <template v-if="isLoggedIn">
            <a-dropdown>
              <a-button>
                <UserOutlined />
                {{ loginUserStore.loginUser?.username }}
              </a-button>
              <template #overlay>
                <a-menu>
                  <a-menu-item key="profile">个人中心</a-menu-item>
                  <a-menu-item key="settings">设置</a-menu-item>
                  <a-menu-divider />
                  <a-menu-item key="logout" @click="handleLogout">退出登录</a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </template>
          <template v-else>
            <a-button type="primary" @click="handleLogin">登录</a-button>
          </template>
        </div>
      </div>
    </header>

    <!-- 主内容区域 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 底部信息 -->
    <footer class="footer">
      <div class="footer-container">
        <div class="footer-info">
          <p>&copy; {{ currentYear }} TT Picture. All rights reserved.</p>
        </div>
        <div class="footer-links">
          <a href="#" class="footer-link">隐私政策</a>
          <span class="separator">|</span>
          <a href="#" class="footer-link">使用条款</a>
          <span class="separator">|</span>
          <a href="#" class="footer-link">联系我们</a>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.main-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 顶部导航栏 */
.header {
  background-color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
  gap: 24px;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  color: #333;
  font-weight: 600;
}

.nav {
  display: flex;
  gap: 32px;
  flex: 1;
}

.nav-link {
  text-decoration: none;
  color: #666;
  font-size: 16px;
  transition: color 0.3s;
  position: relative;
  cursor: pointer;
}

.nav-link:hover {
  color: #1890ff;
}

.nav-link.active {
  color: #1890ff;
  font-weight: 500;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: -20px;
  left: 0;
  right: 0;
  height: 2px;
  background-color: #1890ff;
}

.header-actions {
  display: flex;
  align-items: center;
}

/* 主内容区域 */
.main-content {
  flex: 1;
  /* max-width: 1200px; */
  width: 100%;
  margin: 0 auto;
  padding: 24px 20px;
}

/* 底部信息 */
.footer {
  background-color: #f5f5f5;
  border-top: 1px solid #e8e8e8;
  margin-top: auto;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.footer-info p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 12px;
}

.footer-link {
  color: #666;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.footer-link:hover {
  color: #1890ff;
}

.separator {
  color: #d9d9d9;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-container {
    flex-wrap: wrap;
    height: auto;
    padding: 16px 20px;
  }

  .logo {
    order: 1;
  }

  .header-actions {
    order: 2;
  }

  .nav {
    order: 3;
    width: 100%;
    justify-content: center;
    gap: 20px;
    margin-top: 12px;
  }

  .nav-link {
    font-size: 14px;
  }

  .footer-container {
    flex-direction: column;
    text-align: center;
  }
}
</style>
