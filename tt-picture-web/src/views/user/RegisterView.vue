<script setup lang="ts">
import { reactive } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { registerUsingPost1 } from '@/api/yonghuguanli.ts' // Note: registerUsingPost1 is register

const router = useRouter()

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  password: '',
  surePassword: '',
})

const handleSubmit = async () => {
  if (!formState.userAccount || !formState.password || !formState.surePassword) {
    message.error('请填写完整信息')
    return
  }
  if (formState.password !== formState.surePassword) {
    message.error('两次输入的密码不一致')
    return
  }

  try {
    const res = await registerUsingPost1(formState)
    if (res.data?.code === 0) {
      message.success('注册成功')
      router.push('/user/login')
    } else {
      message.error('注册失败：' + (res.data?.message || '未知错误'))
    }
  } catch (error) {
    message.error('注册请求失败：' + ((error as Error).message || '网络错误'))
  }
}
</script>

<template>
  <div class="register-page">
    <a-form
      :model="formState"
      name="register"
      class="register-form"
      @finish="handleSubmit"
    >
      <div class="form-title">用户注册</div>

      <a-form-item
        name="userAccount"
        :rules="[{ required: true, message: '请输入账号!' }]"
      >
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" size="large">
          <template #prefix>
            <UserOutlined />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item
        name="password"
        :rules="[{ required: true, message: '请输入密码!' }, { min: 8, message: '密码不能少于8位' }]"
      >
        <a-input-password v-model:value="formState.password" placeholder="请输入密码" size="large">
          <template #prefix>
            <LockOutlined />
          </template>
        </a-input-password>
      </a-form-item>

      <a-form-item
        name="surePassword"
        :rules="[{ required: true, message: '请确认密码!' }, { min: 8, message: '密码不能少于8位' }]"
      >
        <a-input-password v-model:value="formState.surePassword" placeholder="请确认密码" size="large">
          <template #prefix>
            <LockOutlined />
          </template>
        </a-input-password>
      </a-form-item>

      <div class="form-actions">
        <a-button type="primary" html-type="submit" class="submit-btn" size="large" block>
          注册
        </a-button>
        <div class="extra-links">
          <router-link to="/user/login">已有账号？去登录</router-link>
        </div>
      </div>
    </a-form>
  </div>
</template>

<style scoped>
.register-page {
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
