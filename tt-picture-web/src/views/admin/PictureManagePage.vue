<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import type { UploadProps, UploadFile } from 'ant-design-vue'
import {
  listPictureByPageSimpleUsingPost2,
  deletePictureUsingDelete2,
  uploadPictureUsingPost2,
  updatePictureUsingPut1,
} from '@/api/tupianguanli'

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

// 上传表单数据
const uploadForm = ref({
  id: undefined as number | undefined,
  name: '',
  introduction: '',
  category: '',
  tags: [] as string[],
})

// 是否为编辑模式
const isEditMode = ref(false)

// 标签输入
const tagInputValue = ref('')

// 图片详情对话框
const detailModalVisible = ref(false)
const currentPicture = ref<API.PictureVo | null>(null)

// 表格列定义
const columns = [
  {
    title: '序号',
    key: 'index',
    width: 80,
    align: 'center' as const,
  },
  {
    title: '图片',
    key: 'preview',
    width: 120,
    align: 'center' as const,
  },
  {
    title: '名称',
    key: 'name',
    dataIndex: 'name',
    width: 150,
    ellipsis: true,
  },
  {
    title: '简介',
    key: 'introduction',
    dataIndex: 'introduction',
    width: 200,
    ellipsis: true,
  },
  {
    title: '类型',
    key: 'category',
    dataIndex: 'category',
    width: 100,
    align: 'center' as const,
  },
  {
    title: '标签',
    key: 'tags',
    dataIndex: 'tags',
    width: 150,
  },
  {
    title: '图片信息',
    key: 'pictureInfo',
    width: 180,
  },
  {
    title: '用户id',
    key: 'userId',
    dataIndex: 'userId',
    width: 100,
    align: 'center' as const,
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '编辑时间',
    key: 'editTime',
    dataIndex: 'editTime',
    width: 180,
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    align: 'center' as const,
    fixed: 'right' as const,
  },
]

// 打开上传对话框
const showUploadModal = () => {
  isEditMode.value = false
  uploadModalVisible.value = true
  fileList.value = []
  uploadForm.value = {
    id: undefined,
    name: '',
    introduction: '',
    category: '',
    tags: [],
  }
  tagInputValue.value = ''
}

// 打开编辑对话框
const showEditModal = (picture: API.PictureVo) => {
  isEditMode.value = true
  uploadModalVisible.value = true
  fileList.value = []
  uploadForm.value = {
    id: picture.id,
    name: picture.name || '',
    introduction: picture.introduction || '',
    category: picture.category || '',
    tags: picture.tags || [],
  }
  tagInputValue.value = ''
}

// 关闭上传对话框
const handleUploadCancel = () => {
  uploadModalVisible.value = false
  isEditMode.value = false
  fileList.value = []
  uploadForm.value = {
    id: undefined,
    name: '',
    introduction: '',
    category: '',
    tags: [],
  }
  tagInputValue.value = ''
}

// 添加标签
const handleAddTag = () => {
  const tag = tagInputValue.value.trim()
  if (tag && !uploadForm.value.tags.includes(tag)) {
    uploadForm.value.tags.push(tag)
    tagInputValue.value = ''
  }
}

// 删除标签
const handleRemoveTag = (tag: string) => {
  uploadForm.value.tags = uploadForm.value.tags.filter((t) => t !== tag)
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
  // 编辑模式且没有选择新文件时,只更新信息
  if (isEditMode.value && fileList.value.length === 0) {
    await handleEditSubmit()
    return
  }

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

      const res = await uploadPictureUsingPost2(
        {},
        {
          id: uploadForm.value.id,
          name: uploadForm.value.name,
          introduction: uploadForm.value.introduction,
          category: uploadForm.value.category,
          tags: uploadForm.value.tags,
        },
        file,
      )
      if (res.code === 0) {
        successCount++
      } else {
        failCount++
        message.error(`${fileItem.name} 上传失败: ${res.message}`)
      }
    } catch (error: unknown) {
      failCount++
      const err = error as { response?: { data?: { message?: string } }; message?: string }
      const errorMsg = err?.response?.data?.message || err?.message || '上传失败'
      message.error(`${fileItem.name} 上传失败: ${errorMsg}`)
      console.error('上传错误:', error)
    }
  }

  uploadLoading.value = false

  if (successCount > 0) {
    message.success(
      isEditMode.value ? `成功更新 ${successCount} 张图片` : `成功上传 ${successCount} 张图片`,
    )
    handleUploadCancel()
    loadPictureList() // 刷新列表
  }
  if (failCount > 0) {
    message.error(`${failCount} 张图片${isEditMode.value ? '更新' : '上传'}失败`)
  }
}

// 仅更新图片信息(不上传新文件)
const handleEditSubmit = async () => {
  if (!uploadForm.value.id) {
    message.error('图片ID不存在')
    return
  }

  uploadLoading.value = true
  try {
    // 使用专门的更新接口
    const res = await updatePictureUsingPut1({
      id: uploadForm.value.id,
      name: uploadForm.value.name,
      introduction: uploadForm.value.introduction,
      category: uploadForm.value.category,
      tags: uploadForm.value.tags,
    })

    if (res.code === 0) {
      message.success('更新成功')
      handleUploadCancel()
      loadPictureList()
    } else {
      message.error(res.message || '更新失败')
    }
  } catch (error: unknown) {
    const err = error as { response?: { data?: { message?: string } }; message?: string }
    const errorMsg = err?.response?.data?.message || err?.message || '更新失败'
    message.error(errorMsg)
    console.error('更新错误:', error)
  } finally {
    uploadLoading.value = false
  }
}

// 加载图片列表
const loadPictureList = async () => {
  loading.value = true
  try {
    const res = await listPictureByPageSimpleUsingPost2(searchForm.value)
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
    const res = await deletePictureUsingDelete2({ id })
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

// 查看图片
const handleView = (picture: API.PictureVo) => {
  currentPicture.value = picture
  detailModalVisible.value = true
}

// 关闭详情对话框
const handleDetailCancel = () => {
  detailModalVisible.value = false
  currentPicture.value = null
}

// 下载图片
const handleDownload = () => {
  if (!currentPicture.value?.url) return
  const link = document.createElement('a')
  link.href = currentPicture.value.url
  link.download = currentPicture.value.name || '图片'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  message.success('开始下载')
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
        <a-button type="primary" size="large" @click="showUploadModal"> 上传图片 </a-button>
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

    <!-- 图片表格 -->
    <div class="table-container">
      <a-table
        :columns="columns"
        :data-source="pictureList"
        :loading="loading"
        :pagination="false"
        :row-key="(record: API.PictureVo) => record.id"
        :scroll="{ x: 1600 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'preview'">
            <div class="preview-image">
              <img :src="record.url" :alt="record.name" />
            </div>
          </template>
          <template v-else-if="column.key === 'name'">
            <div class="name-cell">{{ record.name }}</div>
          </template>
          <template v-else-if="column.key === 'index'">
            {{ (searchForm.current! - 1) * searchForm.pageSize! + pictureList.indexOf(record) + 1 }}
          </template>
          <template v-else-if="column.key === 'introduction'">
            <div class="intro-cell">{{ record.introduction || '-' }}</div>
          </template>
          <template v-else-if="column.key === 'category'">
            <a-tag v-if="record.category" color="blue">{{ record.category }}</a-tag>
            <span v-else class="text-gray">-</span>
          </template>
          <template v-else-if="column.key === 'tags'">
            <div class="tags-cell">
              <a-tag v-for="tag in record.tags" :key="tag" color="cyan">{{ tag }}</a-tag>
              <span v-if="!record.tags || record.tags.length === 0" class="text-gray">-</span>
            </div>
          </template>
          <template v-else-if="column.key === 'pictureInfo'">
            <div class="picture-info-cell">
              <div>格式: {{ record.picFormat || '-' }}</div>
              <div>
                宽高:
                {{
                  record.picWidth && record.picHeight
                    ? `${record.picWidth} × ${record.picHeight}`
                    : '-'
                }}
              </div>
              <div>宽高比: {{ record.picScale ? record.picScale.toFixed(2) : '-' }}</div>
              <div>大小: {{ formatFileSize(record.picSize) }}</div>
            </div>
          </template>
          <template v-else-if="column.key === 'userId'">
            {{ record.userId || '-' }}
          </template>
          <template v-else-if="column.key === 'createTime'">
            {{ formatDateTime(record.createTime) }}
          </template>
          <template v-else-if="column.key === 'editTime'">
            {{ formatDateTime(record.editTime) }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleView(record)">查看</a-button>
              <a-button type="link" size="small" @click="showEditModal(record)">编辑</a-button>
              <a-popconfirm
                title="确定要删除这张图片吗？"
                ok-text="确定"
                cancel-text="取消"
                @confirm="handleDelete(record.id!)"
              >
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </div>

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

    <!-- 上传/编辑对话框 -->
    <a-modal
      v-model:open="uploadModalVisible"
      :title="isEditMode ? '编辑图片' : '上传图片'"
      :confirm-loading="uploadLoading"
      @ok="handleUploadSubmit"
      @cancel="handleUploadCancel"
    >
      <div class="upload-form">
        <a-upload-dragger
          v-if="!isEditMode"
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

        <a-alert
          v-else
          message="编辑模式"
          description="当前为编辑模式,只会更新图片信息,不会替换图片文件"
          type="info"
          show-icon
          style="margin-bottom: 16px"
        />

        <a-divider>图片信息</a-divider>

        <a-form layout="vertical">
          <a-form-item label="名称">
            <a-input
              v-model:value="uploadForm.name"
              placeholder="请输入图片名称"
              :maxlength="100"
            />
          </a-form-item>

          <a-form-item label="简介">
            <a-textarea
              v-model:value="uploadForm.introduction"
              placeholder="请输入图片简介"
              :rows="3"
              :maxlength="200"
              show-count
            />
          </a-form-item>

          <a-form-item label="分类">
            <a-input
              v-model:value="uploadForm.category"
              placeholder="请输入分类，如：素材、资料等"
              :maxlength="50"
            />
          </a-form-item>

          <a-form-item label="标签">
            <div class="tag-input-container">
              <div v-if="uploadForm.tags.length > 0" class="tags-display">
                <a-tag
                  v-for="tag in uploadForm.tags"
                  :key="tag"
                  closable
                  color="blue"
                  @close="handleRemoveTag(tag)"
                >
                  {{ tag }}
                </a-tag>
              </div>
              <a-input-group compact class="tag-input-group">
                <a-input
                  v-model:value="tagInputValue"
                  placeholder="输入标签后按回车或点击添加"
                  style="width: calc(100% - 80px)"
                  :maxlength="20"
                  @pressEnter="handleAddTag"
                />
                <a-button type="primary" @click="handleAddTag">添加</a-button>
              </a-input-group>
            </div>
          </a-form-item>
        </a-form>
      </div>
    </a-modal>

    <!-- 图片详情对话框 -->
    <a-modal
      v-model:open="detailModalVisible"
      title="图片详情"
      width="900px"
      :footer="null"
      @cancel="handleDetailCancel"
    >
      <div v-if="currentPicture" class="picture-detail">
        <div class="detail-left">
          <div class="detail-image-container">
            <img :src="currentPicture.url" :alt="currentPicture.name" class="detail-image" />
          </div>
        </div>
        <div class="detail-right">
          <a-descriptions :column="1" bordered size="small">
            <a-descriptions-item label="名称">
              {{ currentPicture.name || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="简介">
              {{ currentPicture.introduction || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="分类">
              <a-tag v-if="currentPicture.category" color="blue">
                {{ currentPicture.category }}
              </a-tag>
              <span v-else>-</span>
            </a-descriptions-item>
            <a-descriptions-item label="标签">
              <div v-if="currentPicture.tags && currentPicture.tags.length > 0">
                <a-tag v-for="tag in currentPicture.tags" :key="tag" color="cyan">
                  {{ tag }}
                </a-tag>
              </div>
              <span v-else>-</span>
            </a-descriptions-item>
            <a-descriptions-item label="格式">
              {{ currentPicture.picFormat || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="尺寸">
              {{
                currentPicture.picWidth && currentPicture.picHeight
                  ? `${currentPicture.picWidth} × ${currentPicture.picHeight}`
                  : '-'
              }}
            </a-descriptions-item>
            <a-descriptions-item label="宽高比">
              {{ currentPicture.picScale ? currentPicture.picScale.toFixed(2) : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="大小">
              {{ formatFileSize(currentPicture.picSize) }}
            </a-descriptions-item>
            <a-descriptions-item label="上传者">
              {{ currentPicture.userId || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="创建时间">
              {{ formatDateTime(currentPicture.createTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="编辑时间">
              {{ formatDateTime(currentPicture.editTime) }}
            </a-descriptions-item>
          </a-descriptions>
          <div class="detail-actions">
            <a-button type="primary" size="large" block @click="handleDownload">
              下载图片
            </a-button>
          </div>
        </div>
      </div>
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

// 格式化日期时间
function formatDateTime(dateTime?: string): string {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

// 导出供模板使用
export { formatFileSize, formatDateTime }
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

.table-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.preview-image {
  width: 80px;
  height: 80px;
  overflow: hidden;
  border-radius: 6px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.preview-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.preview-image:hover img {
  transform: scale(1.1);
}

.name-cell {
  font-weight: 500;
  color: #333;
}

.intro-cell {
  color: #666;
  font-size: 13px;
  line-height: 1.5;
}

.tags-cell {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.picture-info-cell {
  font-size: 12px;
  color: #666;
  line-height: 1.6;
}

.picture-info-cell > div {
  margin-bottom: 2px;
}

.user-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.username {
  font-size: 14px;
  color: #666;
}

.text-gray {
  color: #999;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 24px 0;
}

/* 上传表单样式 */
.upload-form {
  max-height: 70vh;
  overflow-y: auto;
}

.tag-input-container {
  width: 100%;
}

.tags-display {
  margin-bottom: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-input-group {
  display: flex;
  width: 100%;
}

/* 图片详情样式 */
.picture-detail {
  display: flex;
  gap: 24px;
  min-height: 500px;
}

.detail-left {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 8px;
  padding: 20px;
}

.detail-image-container {
  max-width: 100%;
  max-height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.detail-image {
  max-width: 100%;
  max-height: 600px;
  object-fit: contain;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.detail-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-actions {
  margin-top: auto;
  padding-top: 16px;
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

  .table-container {
    padding: 12px;
  }
}
</style>
