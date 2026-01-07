<script setup lang="ts">
const currentYear = new Date().getFullYear()
</script>

<template>
  <div class="user-layout">
    <div class="container">
      <div class="header">
        <div class="title">
          <img src="/favicon.ico" alt="logo" class="logo" />
          <span class="title-text">TT Picture</span>
        </div>
        <div class="desc">TT Picture - 你的云端相册</div>
      </div>
      <div class="content">
        <router-view />
      </div>
      <div class="footer">
        <p>&copy; {{ currentYear }} TT Picture. All rights reserved.</p>
        <div class="links">
          <a href="#">隐私政策</a>
          <a href="#">使用条款</a>
          <a href="#">联系我们</a>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.container {
  width: 100%;
  max-width: 400px;
}

.header {
  text-align: center;
  margin-bottom: 32px;
}

.title {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 8px;
}

.logo {
  height: 44px;
  margin-right: 12px;
}

.title-text {
  font-size: 32px;
  font-weight: 600;
  color: #333;
}

.desc {
  font-size: 14px;
  color: #888;
}

.content {
  margin-bottom: 24px;
}

.footer {
  text-align: center;
}

.footer p {
  color: #888;
  font-size: 12px;
  margin-bottom: 8px;
}

.links {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.links a {
  color: #888;
  font-size: 12px;
  text-decoration: none;
  transition: color 0.3s;
}

.links a:hover {
  color: #1890ff;
}
</style>
