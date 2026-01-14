import { createRouter, createWebHistory } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('../layouts/MainLayout.vue'),
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('../views/HomePage.vue'),
        },
        {
          path: 'upload',
          name: 'upload',
          component: () => import('../views/UploadPage.vue'),
          meta: { requiresAuth: true }, // 需要登录
        },
        {
          path: 'about',
          name: 'about',
          component: () => import('../views/AboutPage.vue'),
        },
        {
          path: 'admin/user',
          name: 'admin_user',
          component: () => import('../views/admin/UserManagePage.vue'),
          meta: { requiresAuth: true, requiresAdmin: true }, // 需要登录且需要管理员权限
        },
        {
          path: 'admin/picture',
          name: 'admin_picture',
          component: () => import('../views/admin/PictureManagePage.vue'),
          meta: { requiresAuth: true, requiresAdmin: true }, // 需要登录且需要管理员权限
        },
      ],
    },
    {
      path: '/user',
      component: () => import('../layouts/UserLayout.vue'),
      children: [
        {
          path: 'login',
          name: 'user_login',
          component: () => import('../views/user/LoginView.vue'),
        },
        {
          path: 'register',
          name: 'user_register',
          component: () => import('../views/user/RegisterView.vue'),
        },
      ],
    },
  ],
})

// 路由守卫：检查需要登录的页面
router.beforeEach((to, _from, next) => {
  const loginUserStore = useLoginUserStore()

  // 如果页面需要登录且用户未登录
  if (to.meta.requiresAuth && !loginUserStore.isLoggedIn) {
    // 跳转到登录页，并记录原目标页面
    next({ name: 'user_login', query: { redirect: to.fullPath } })
    return
  }

  // 如果页面需要管理员权限且用户不是管理员
  if (to.meta.requiresAdmin && !loginUserStore.isAdmin) {
    // 跳转到首页，提示无权限
    next({ name: 'home' })
    return
  }

  next()
})

export default router
