<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import type { UploadProps, UploadFile } from 'ant-design-vue'
import {
  listPictureByPageSimpleUsingPost,
  deletePictureUsingDelete,
  uploadPictureUsingPost,
} from '@/api/wenjianjichucaozuo'

// 搜索表单
const searchForm = ref<API.PictureQueryRequest>({
  current: 1,
  pageSize: 12,
  name: '',
  category: '',
})

// 图片列表数据
const pictureList = ref<API.PictureVo[]>([])
const total = ref(0)
const loading = ref(false)

// 上传对话框
const uploadModalVisible = ref(false)
const uploadLoading = ref(false)
const fileList = ref<UploadFile[]>([])

// 打开上传对话框
const showUploadModal = () => {
  uploadModalVisible.value = true
  fileList.value = []
}

// 关闭上传对话框
const handleUploadCancel = () => {
  uploadModalVisible.value = false
  fileList.value = []
}

// 文件上传前的校验
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('只能上传图片文件！')
    return false
  }
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    message.error('图片大小不能超过 10MB！')
    return false
  }
  return false // 阻止自动上传，手动控制
}

// 文件列表变化
const handleFileChange: UploadProps['onChange'] = ({ fileList: newFileList }) => {
  fileList.value = newFileList
}

// 执行上传
const handleUploadSubmit = async () => {
  if (fileList.value.length === 0) {
    message.warning('请选择要上传的图片')
    return
  }

  uploadLoading.value = true
  let successCount = 0
  let failCount = 0

  for (const fileItem of fileList.value) {
    try {
      // 获取原始文件对象
      const file = fileItem.originFileObj as File
      if (!file) {
        failCount++
        message.error(`${fileItem.name} 文件对象为空`)
        continue
      }

      const res = await uploadPictureUsingPost({}, {}, file)
      if (res.code === 0) {
        successCount++
      } else {
        failCount++
        message.error(`${fileItem.name} 上传失败: ${res.message}`)
      }
    } catch (error: any) {
      failCount++
      const errorMsg = error?.response?.data?.message || error?.message || '上传失败'
      message.error(`${fileItem.name} 上传失败: ${errorMsg}`)
      console.error('上传错误:', error)
    }
  }

  uploadLoading.value = false

  if (successCount > 0) {
    message.success(`成功上传 ${successCount} 张图片`)
    handleUploadCancel()
    loadPictureList() // 刷新列表
  }
  if (failCount > 0) {
    message.error(`${failCount} 张图片上传失败`)
  }
}

// 加载图片列表
const loadPictureList = async () => {
  loading.value = true
  try {
    const res = await listPictureByPageSimpleUsingPost(searchForm.value)
    if (res.code === 0 && res.data) {
      pictureList.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      message.error(res.message || '加载失败')
    }
  } catch (error) {
    message.error('加载图片列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  searchForm.value.current = 1
  loadPictureList()
}

// 重置搜索
const handleReset = () => {
  searchForm.value = {
    current: 1,
    pageSize: 12,
    name: '',
    category: '',
  }
  loadPictureList()
}

// 分页变化
const handlePageChange = (page: number, pageSize: number) => {
  searchForm.value.current = page
  searchForm.value.pageSize = pageSize
  loadPictureList()
}

// 删除图片
const handleDelete = async (id: number) => {
  try {
    const res = await deletePictureUsingDelete({ pictureId: id })
    if (res.code === 0) {
      message.success('删除成功')
      loadPictureList()
    } else {
      message.error(res.message || '删除失败')
    }
  } catch (error) {
    message.error('删除失败')
    console.error(error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadPictureList()
})
</script>

<template>
  <div class="gallery-page">
    <div class="page-header">
      <div class="header-left">
        <h1>图片管理</h1>
        <p>浏览和管理所有图片</p>
      </div>
      <div class="header-right">
        <a-button type="primary" size="large" @click="showUploadModal">
          <template #icon>
            <span>📤</span>
          </template>
          上传图片
        </a-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="图片名称">
          <a-input
            v-model:value="searchForm.name"
            placeholder="请输入图片名称"
            style="width: 200px"
            @pressEnter="handleSearch"
          />
        </a-form-item>
        <a-form-item label="分类">
          <a-input
            v-model:value="searchForm.category"
            placeholder="请输入分类"
            style="width: 200px"
            @pressEnter="handleSearch"
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" @click="handleSearch">搜索</a-button>
            <a-button @click="handleReset">重置</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>

    <!-- 图片网格 -->
    <a-spin :spinning="loading">
      <div v-if="pictureList.length > 0" class="picture-grid">
        <div v-for="picture in pictureList" :key="picture.id" class="picture-card">
          <div class="picture-image">
            <img :src="picture.url" :alt="picture.name" />
          </div>
          <div class="picture-info">
            <div class="picture-title">{{ picture.name }}</div>
            <div class="picture-meta">
              <span v-if="picture.category" class="category">{{ picture.category }}</span>
              <span class="size">{{ formatFileSize(picture.picSize) }}</span>
            </div>
            <div v-if="picture.user" class="picture-user">
              <a-avatar :size="24" :src="picture.user.userAvatar">
                {{ picture.user.userName?.[0] }}
              </a-avatar>
              <span class="username">{{ picture.user.userName }}</span>
            </div>
            <div class="picture-actions">
              <a-button type="link" size="small" @click="() => window.open(picture.url)">
                查看
              </a-button>
              <a-popconfirm
                title="确定要删除这张图片吗？"
                ok-text="确定"
                cancel-text="取消"
                @confirm="handleDelete(picture.id!)"
              >
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </div>
          </div>
        </div>
      </div>
      <a-empty v-else description="暂无图片" />
    </a-spin>

    <!-- 分页 -->
    <div v-if="total > 0" class="pagination">
      <a-pagination
        v-model:current="searchForm.current"
        v-model:page-size="searchForm.pageSize"
        :total="total"
        :show-total="(total: number) => `共 ${total} 张图片`"
        :page-size-options="['12', '24', '48', '96']"
        show-size-changer
        show-quick-jumper
        @change="handlePageChange"
      />
    </div>

    <!-- 上传对话框 -->
    <a-modal
      v-model:open="uploadModalVisible"
      title="上传图片"
      :confirm-loading="uploadLoading"
      @ok="handleUploadSubmit"
      @cancel="handleUploadCancel"
    >
      <a-upload-dragger
        v-model:file-list="fileList"
        name="file"
        :multiple="true"
        :before-upload="beforeUpload"
        :max-count="10"
        accept="image/*"
        @change="handleFileChange"
      >
        <p class="ant-upload-drag-icon">📷</p>
        <p class="ant-upload-text">点击或拖拽图片到此区域上传</p>
        <p class="ant-upload-hint">支持单次上传多张图片，每张图片不超过 10MB</p>
      </a-upload-dragger>
    </a-modal>
  </div>
</template>

<script lang="ts">
// 格式化文件大小
function formatFileSize(bytes?: number): string {
  if (!bytes) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i]
}

// 导出供模板使用
export { formatFileSize }
</script>

<style scoped>
.gallery-page {
  padding: 24px 0;
}

.page-header {
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.header-left h1 {
  font-size: 28px;
  color: #333;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.header-left p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.search-bar {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.picture-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.picture-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.picture-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.picture-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
}

.picture-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.picture-card:hover .picture-image img {
  transform: scale(1.05);
}

.picture-info {
  padding: 16px;
}

.picture-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.picture-meta {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 12px;
}

.category {
  background: #e6f7ff;
  color: #1890ff;
  padding: 2px 8px;
  border-radius: 4px;
}

.size {
  color: #999;
}

.picture-user {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.username {
  font-size: 14px;
  color: #666;
}

.picture-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 24px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-right {
    width: 100%;
  }

  .header-right .ant-btn {
    width: 100%;
  }

  .picture-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 16px;
  }

  .search-bar :deep(.ant-form) {
    flex-direction: column;
    align-items: stretch;
  }

  .search-bar :deep(.ant-form-item) {
    margin-right: 0;
    margin-bottom: 12px;
  }

  .search-bar :deep(.ant-input) {
    width: 100% !important;
  }
}
</style>
