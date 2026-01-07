<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import type { TableColumnsType } from 'ant-design-vue'

interface UserType {
  id: number
  userAccount: string
  userName?: string
  userRole: string
  createTime: string
  updateTime: string
}

// 查询表单
const searchForm = reactive({
  userAccount: '',
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

// 表格列定义
const columns: TableColumnsType<UserType> = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
    width: 80,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    key: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    key: 'userName',
  },
  {
    title: '角色',
    dataIndex: 'userRole',
    key: 'userRole',
    customRender: ({ text }) => {
      const roleMap: Record<string, string> = {
        admin: '管理员',
        user: '普通用户',
      }
      return roleMap[text] || text
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
  },
]

// 加载用户列表
const loadUserList = async () => {
  loading.value = true
  try {
    // TODO: 替换为实际的API调用
    // const res = await getUserListUsingPost({
    //   ...searchForm,
    //   current: pagination.current,
    //   pageSize: pagination.pageSize,
    // })

    // 模拟数据
    await new Promise((resolve) => setTimeout(resolve, 500))
    const mockData: UserType[] = [
      {
        id: 1,
        userAccount: 'admin',
        userName: '管理员',
        userRole: 'admin',
        createTime: '2024-01-01 10:00:00',
        updateTime: '2024-01-01 10:00:00',
      },
      {
        id: 2,
        userAccount: 'user001',
        userName: '测试用户1',
        userRole: 'user',
        createTime: '2024-01-02 10:00:00',
        updateTime: '2024-01-02 10:00:00',
      },
    ]

    dataSource.value = mockData
    pagination.total = mockData.length
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
  searchForm.userAccount = ''
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

// 删除用户
const handleDelete = async () => {
  try {
    // TODO: 调用删除API
    // await deleteUserUsingPost({ id: record.id })
    message.success('删除成功')
    loadUserList()
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
    <a-card title="用户管理" :bordered="false">
      <!-- 查询表单 -->
      <a-form :model="searchForm" layout="inline" class="search-form">
        <a-form-item label="账号">
          <a-input
            v-model:value="searchForm.userAccount"
            placeholder="请输入账号"
            allow-clear
            style="width: 200px"
          />
        </a-form-item>
        <a-form-item label="用户名">
          <a-input
            v-model:value="searchForm.userName"
            placeholder="请输入用户名"
            allow-clear
            style="width: 200px"
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
            <a-button type="primary" @click="handleSearch">查询</a-button>
            <a-button @click="handleReset">重置</a-button>
          </a-space>
        </a-form-item>
      </a-form>

      <!-- 用户列表表格 -->
      <a-table
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        class="user-table"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column }">
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small">编辑</a-button>
              <a-popconfirm
                title="确定要删除该用户吗？"
                ok-text="确定"
                cancel-text="取消"
                @confirm="handleDelete"
              >
                <a-button type="link" danger size="small">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<style scoped>
.user-manage-page {
  padding: 24px;
}

.search-form {
  margin-bottom: 24px;
}

.user-table {
  margin-top: 16px;
}
</style>
