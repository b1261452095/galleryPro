<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import type { TableColumnsType } from 'ant-design-vue'
import {
  SearchOutlined,
  ReloadOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  UserOutlined,
  CrownOutlined,
  PictureOutlined,
} from '@ant-design/icons-vue'
import {
  getUserListUsingPost,
  deleteUserUsingDelete,
  addUserUsingPost,
  updateUserUsingPut,
} from '@/api/yonghuguanli'

interface UserType extends API.UserVo {
  id: number
}

// 查询表单
const searchForm = reactive({
  userName: '',
  userRole: undefined as string | undefined,
})

// 表格数据
const dataSource = ref<UserType[]>([])
const loading = ref(false)
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
})

// 弹窗相关
const modalVisible = ref(false)
const modalTitle = ref('新增用户')
const isEdit = ref(false)
const formRef = ref()

// 表单数据
const formData = reactive({
  id: undefined as number | undefined,
  userAccount: '',
  userName: '',
  userAvatar: '',
  userProfile: '',
  userRole: 'user',
})

// 表单验证规则
const formRules = {
  userAccount: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  userRole: [{ required: true, message: '请选择角色', trigger: 'change' }],
}

// 表格列定义
const columns: TableColumnsType<UserType> = [
  {
    title: '序号',
    key: 'index',
    width: 70,
    align: 'center',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    key: 'userAccount',
    width: 140,
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    key: 'userName',
    width: 120,
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    key: 'userAvatar',
    width: 80,
    align: 'center',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    key: 'userProfile',
    ellipsis: true,
    minWidth: 200,
  },
  {
    title: '角色',
    dataIndex: 'userRole',
    key: 'userRole',
    width: 110,
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 180,
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    align: 'center',
    fixed: 'right',
  },
]

// 加载用户列表
const loadUserList = async () => {
  loading.value = true
  try {
    const queryParams: API.UserQueryRequest = {
      current: pagination.current,
      pageSize: pagination.pageSize,
    }

    if (searchForm.userName) {
      queryParams.userName = searchForm.userName
    }
    if (searchForm.userRole) {
      queryParams.userRole = searchForm.userRole
    }

    const res = await getUserListUsingPost(queryParams)

    if (res.code === 0 && res.data) {
      dataSource.value = (res.data.records || []) as UserType[]
      pagination.total = res.data.total || 0
    } else {
      message.error(res.message || '加载用户列表失败')
    }
  } catch (error) {
    message.error('加载用户列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 查询
const handleSearch = () => {
  pagination.current = 1
  loadUserList()
}

// 重置
const handleReset = () => {
  searchForm.userName = ''
  searchForm.userRole = undefined
  pagination.current = 1
  loadUserList()
}

// 表格分页变化
const handleTableChange = (pag: { current: number; pageSize: number }) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  loadUserList()
}

// 打开新增弹窗
const handleAdd = () => {
  isEdit.value = false
  modalTitle.value = '新增用户'
  formData.id = undefined
  formData.userAccount = ''
  formData.userName = ''
  formData.userAvatar = ''
  formData.userProfile = ''
  formData.userRole = 'user'
  modalVisible.value = true
}

// 打开编辑弹窗
const handleEdit = (record: UserType) => {
  isEdit.value = true
  modalTitle.value = '编辑用户'
  formData.id = record.id
  formData.userAccount = record.userAccount || ''
  formData.userName = record.userName || ''
  formData.userAvatar = record.userAvatar || ''
  formData.userProfile = record.userProfile || ''
  formData.userRole = record.userRole || 'user'
  modalVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value?.validate()

    if (isEdit.value) {
      // 编辑
      const res = await updateUserUsingPut({
        id: formData.id,
        userName: formData.userName,
        userAvatar: formData.userAvatar,
        userProfile: formData.userProfile,
        userRole: formData.userRole,
      })
      if (res.code === 0) {
        message.success('更新成功')
        modalVisible.value = false
        loadUserList()
      } else {
        message.error(res.message || '更新失败')
      }
    } else {
      // 新增
      const res = await addUserUsingPost({
        userAccount: formData.userAccount,
        userName: formData.userName,
        userAvatar: formData.userAvatar,
        userProfile: formData.userProfile,
        userRole: formData.userRole,
      })
      if (res.code === 0) {
        message.success('新增成功')
        modalVisible.value = false
        loadUserList()
      } else {
        message.error(res.message || '新增失败')
      }
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 取消
const handleCancel = () => {
  modalVisible.value = false
  formRef.value?.resetFields()
}

// 删除用户
const handleDelete = async (userId: number) => {
  try {
    const res = await deleteUserUsingDelete({ id: userId })
    if (res.code === 0) {
      message.success('删除成功')
      loadUserList()
    } else {
      message.error(res.message || '删除失败')
    }
  } catch {
    message.error('删除失败')
  }
}

onMounted(() => {
  loadUserList()
})
</script>

<template>
  <div class="user-manage-page">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <p class="page-description">管理系统用户信息，包括新增、编辑、删除等操作</p>
    </div>

    <a-card :bordered="false" class="search-card">
      <!-- 查询表单 -->
      <a-form :model="searchForm" layout="inline" class="search-form">
        <a-form-item label="用户名">
          <a-input
            v-model:value="searchForm.userName"
            placeholder="请输入用户名"
            allow-clear
            style="width: 200px"
            @press-enter="handleSearch"
          />
        </a-form-item>
        <a-form-item label="角色">
          <a-select
            v-model:value="searchForm.userRole"
            placeholder="请选择角色"
            allow-clear
            style="width: 150px"
          >
            <a-select-option value="admin">管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" @click="handleSearch">
              <template #icon>
                <search-outlined />
              </template>
              查询
            </a-button>
            <a-button @click="handleReset">
              <template #icon>
                <reload-outlined />
              </template>
              重置
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>

      <div class="table-toolbar">
        <a-button type="primary" @click="handleAdd">
          <template #icon>
            <plus-outlined />
          </template>
          新增用户
        </a-button>
      </div>

      <!-- 用户列表表格 -->
      <a-table
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="{
          current: pagination.current,
          pageSize: pagination.pageSize,
          total: pagination.total,
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (total: number) => `共 ${total} 条记录`,
        }"
        row-key="id"
        class="user-table"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record, index }">
          <template v-if="column.key === 'index'">
            <span class="index-cell">
              {{ (pagination.current - 1) * pagination.pageSize + index + 1 }}
            </span>
          </template>
          <template v-else-if="column.key === 'userAccount'">
            <span class="account-cell">{{ record.userAccount }}</span>
          </template>
          <template v-else-if="column.key === 'userAvatar'">
            <div class="avatar-cell">
              <a-avatar v-if="record.userAvatar" :src="record.userAvatar" :size="40" />
              <a-avatar v-else :size="40" style="background-color: #1890ff">
                {{ record.userName?.charAt(0)?.toUpperCase() || 'U' }}
              </a-avatar>
            </div>
          </template>
          <template v-else-if="column.key === 'userName'">
            <span class="name-cell">{{ record.userName || '-' }}</span>
          </template>
          <template v-else-if="column.key === 'userProfile'">
            <a-tooltip v-if="record.userProfile" :title="record.userProfile">
              <span class="profile-cell">{{ record.userProfile }}</span>
            </a-tooltip>
            <span v-else class="empty-text">-</span>
          </template>
          <template v-else-if="column.key === 'userRole'">
            <a-tag v-if="record.userRole === 'admin'" color="red" class="role-tag">
              <template #icon>
                <crown-outlined />
              </template>
              管理员
            </a-tag>
            <a-tag v-else color="blue" class="role-tag">
              <template #icon>
                <user-outlined />
              </template>
              普通用户
            </a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space :size="8">
              <a-button type="link" size="small" class="action-btn" @click="handleEdit(record)">
                <template #icon>
                  <edit-outlined />
                </template>
                编辑
              </a-button>
              <a-popconfirm
                title="确定要删除该用户吗？"
                ok-text="确定"
                cancel-text="取消"
                @confirm="handleDelete(record.id)"
              >
                <a-button type="link" danger size="small" class="action-btn">
                  <template #icon>
                    <delete-outlined />
                  </template>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      :width="600"
      :destroy-on-close="true"
      @ok="handleSubmit"
      @cancel="handleCancel"
    >
      <a-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        class="user-form"
      >
        <a-form-item label="账号" name="userAccount">
          <a-input
            v-model:value="formData.userAccount"
            placeholder="请输入账号"
            :disabled="isEdit"
            :prefix="isEdit ? undefined : ''"
          >
            <template v-if="!isEdit" #prefix>
              <user-outlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="用户名" name="userName">
          <a-input v-model:value="formData.userName" placeholder="请输入用户名">
            <template #prefix>
              <user-outlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="头像" name="userAvatar">
          <a-input v-model:value="formData.userAvatar" placeholder="请输入头像URL">
            <template #prefix>
              <picture-outlined />
            </template>
          </a-input>
          <div v-if="formData.userAvatar" class="avatar-preview">
            <a-avatar :src="formData.userAvatar" :size="64" />
          </div>
        </a-form-item>
        <a-form-item label="简介" name="userProfile">
          <a-textarea
            v-model:value="formData.userProfile"
            placeholder="请输入简介"
            :rows="3"
            :maxlength="200"
            show-count
          />
        </a-form-item>
        <a-form-item label="角色" name="userRole">
          <a-select v-model:value="formData.userRole" placeholder="请选择角色">
            <a-select-option value="admin"> <crown-outlined /> 管理员 </a-select-option>
            <a-select-option value="user"> <user-outlined /> 普通用户 </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped>
.user-manage-page {
  padding: 24px;
  background: #f0f2f5;
  min-height: calc(100vh - 64px);
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #262626;
  margin: 0 0 8px 0;
}

.page-description {
  font-size: 14px;
  color: #8c8c8c;
  margin: 0;
}

.search-card {
  border-radius: 8px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
}

.search-form {
  margin-bottom: 16px;
}

.search-form :deep(.ant-form-item) {
  margin-bottom: 16px;
}

.table-toolbar {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-end;
}

.user-table {
  background: #fff;
}

.user-table :deep(.ant-table) {
  font-size: 14px;
}

.user-table :deep(.ant-table-container) {
  width: 100%;
}

.user-table :deep(.ant-table-thead > tr > th) {
  background: #fafafa;
  font-weight: 600;
  color: #262626;
  padding: 12px 16px;
}

.user-table :deep(.ant-table-tbody > tr > td) {
  padding: 12px 16px;
}

.user-table :deep(.ant-table-tbody > tr:hover > td) {
  background: #f5f5f5;
}

.index-cell {
  color: #8c8c8c;
  font-weight: 500;
}

.account-cell {
  color: #1890ff;
  font-weight: 500;
}

.avatar-cell {
  display: flex;
  align-items: center;
  justify-content: center;
}

.name-cell {
  font-weight: 500;
  color: #262626;
}

.profile-cell {
  display: inline-block;
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #595959;
}

.empty-text {
  color: #bfbfbf;
}

.role-tag {
  font-weight: 500;
  border-radius: 4px;
  padding: 2px 8px;
}

.action-btn {
  padding: 0;
  height: auto;
  line-height: 1;
}

.action-btn:hover {
  transform: translateY(-1px);
  transition: all 0.2s;
}

.user-form :deep(.ant-form-item) {
  margin-bottom: 20px;
}

.avatar-preview {
  margin-top: 12px;
  padding: 12px;
  background: #fafafa;
  border-radius: 8px;
  display: flex;
  justify-content: center;
}

:deep(.ant-modal-header) {
  border-bottom: 1px solid #f0f0f0;
  padding: 16px 24px;
}

:deep(.ant-modal-body) {
  padding: 24px;
}

:deep(.ant-modal-footer) {
  border-top: 1px solid #f0f0f0;
  padding: 12px 16px;
}

:deep(.ant-btn-primary) {
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.045);
}

:deep(.ant-btn-primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
  transition: all 0.3s;
}

:deep(.ant-pagination) {
  margin-top: 16px;
}
</style>
