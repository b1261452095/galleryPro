<script setup lang="ts"></script>

<template>
  <div class="home-page">
    <a-space direction="vertical" :size="32" style="width: 100%">
      <!-- 欢迎区域 -->
      <div class="welcome-section">
        <h1>欢迎来到 TT Picture</h1>
        <p class="subtitle">专业的图片管理和分享平台</p>
      </div>

      <!-- 功能卡片 -->
      <a-row :gutter="[24, 24]">
        <a-col :xs="24" :sm="8">
          <a-card hoverable class="feature-card">
            <div class="card-content">
              <div class="card-icon" style="color: #1890ff">📷</div>
              <h3>图片管理</h3>
              <p>轻松管理您的所有图片资源</p>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="8">
          <a-card hoverable class="feature-card">
            <div class="card-content">
              <div class="card-icon" style="color: #52c41a">⬆️</div>
              <h3>快速上传</h3>
              <p>支持批量上传和拖拽上传</p>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="8">
          <a-card hoverable class="feature-card">
            <div class="card-content">
              <div class="card-icon" style="color: #722ed1">☁️</div>
              <h3>云端存储</h3>
              <p>安全可靠的云端存储服务</p>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </a-space>
  </div>
</template>

<style scoped>
.home-page {
  padding: 48px 0;
}

.welcome-section {
  text-align: center;
  margin-bottom: 24px;
}

.welcome-section h1 {
  font-size: 36px;
  color: #333;
  margin-bottom: 16px;
  font-weight: 600;
}

.subtitle {
  font-size: 18px;
  color: #666;
  margin: 0;
}

.feature-card {
  height: 100%;
  transition: transform 0.3s, box-shadow 0.3s;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-content {
  text-align: center;
  padding: 24px 0;
}

.card-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.card-content h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 12px;
  font-weight: 500;
}

.card-content p {
  font-size: 14px;
  color: #666;
  margin: 0;
}
</style>
