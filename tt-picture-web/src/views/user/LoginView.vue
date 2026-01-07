<script setup lang="ts">
import { reactive } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { registerUsingPost } from '@/api/yonghuguanli.ts' // Note: registerUsingPost is actually login

const router = useRouter()
const loginUserStore = useLoginUserStore()

const formState = reactive<API.UserLoginRequest>({
  userAccount: 'admin',
  password: 'admin123',
})

const handleSubmit = async () => {
  if (!formState.userAccount || !formState.password) {
    message.error('请输入账号和密码')
    return
  }

  try {
    const res = await registerUsingPost(formState)
    console.log('res:', res)
    if (res.code === 0) {
      await loginUserStore.fetchLoginUser()
      message.success('登录成功')
      router.push('/')
    } else {
      message.error('登录失败：' + (res.message || '未知错误'))
    }
  } catch (error) {
    message.error('登录请求失败：' + ((error as Error).message || '网络错误'))
  }
}
</script>

<template>
  <div class="login-page">
    <a-form :model="formState" name="login" class="login-form" @finish="handleSubmit">
      <div class="form-title">用户登录</div>

      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号!' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" size="large">
          <template #prefix>
            <UserOutlined />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item name="password" :rules="[{ required: true, message: '请输入密码!' }]">
        <a-input-password v-model:value="formState.password" placeholder="请输入密码" size="large">
          <template #prefix>
            <LockOutlined />
          </template>
        </a-input-password>
      </a-form-item>

      <div class="form-actions">
        <a-button type="primary" html-type="submit" class="submit-btn" size="large" block>
          登录
        </a-button>
        <div class="extra-links">
          <router-link to="/user/register">没有账号？去注册</router-link>
        </div>
      </div>
    </a-form>
  </div>
</template>

<style scoped>
.login-page {
  background: white;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.form-title {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 24px;
  color: #333;
}

.submit-btn {
  margin-bottom: 16px;
}

.extra-links {
  text-align: right;
  font-size: 14px;
}

.extra-links a {
  color: #1890ff;
}
</style>
