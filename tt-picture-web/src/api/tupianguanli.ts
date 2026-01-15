// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** 管理员查询图片列表(有敏感信息) POST /api/picture/admin/list */
export async function listPictureByPageUsingPost2(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/picture/admin/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除图片 DELETE /api/picture/delete */
export async function deletePictureUsingDelete2(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/picture/delete', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 用户查询图片列表(无敏感信息) POST /api/picture/list */
export async function listPictureByPageSimpleUsingPost2(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/picture/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取图片列表(有敏感信息) POST /api/picture/list/page */
export async function listPictureByPageUsingPost1(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/picture/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取图片列表(无敏感信息) POST /api/picture/list/page/simple */
export async function listPictureByPageSimpleUsingPost1(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/picture/list/page/simple', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除图片 DELETE /api/picture/picture/delete */
export async function deletePictureUsingDelete1(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/picture/picture/delete', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 修改图片信息 PUT /api/picture/picture/update */
export async function updatePictureUsingPut(
  body: API.PictureUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/picture/picture/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 上传图片（可重复） POST /api/picture/picture/upload */
export async function uploadPictureUsingPost1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.uploadPictureUsingPOST1Params,
  body: {},
  file?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData()

  if (file) {
    formData.append('file', file)
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele]

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''))
        } else {
          formData.append(ele, new Blob([JSON.stringify(item)], { type: 'application/json' }))
        }
      } else {
        formData.append(ele, item)
      }
    }
  })

  return request<API.BaseResponse>('/api/picture/picture/upload', {
    method: 'POST',
    params: {
      ...params,
    },
    data: formData,
    requestType: 'form',
    ...(options || {}),
  })
}

/** 测试下载文件 GET /api/picture/test/download */
export async function testDownloadUsingGet2(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.testDownloadUsingGET2Params,
  options?: { [key: string]: any }
) {
  return request<any>('/api/picture/test/download', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 测试上传文件 POST /api/picture/test/upload */
export async function testUploadUsingPost2(
  body: {},
  file?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData()

  if (file) {
    formData.append('file', file)
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele]

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''))
        } else {
          formData.append(ele, new Blob([JSON.stringify(item)], { type: 'application/json' }))
        }
      } else {
        formData.append(ele, item)
      }
    }
  })

  return request<API.BaseResponse>('/api/picture/test/upload', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  })
}

/** 编辑图片信息 PUT /api/picture/update */
export async function updatePictureUsingPut1(
  body: API.PictureUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/picture/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 上传图片 支持批量上传,最大10MB POST /api/picture/upload */
export async function uploadPictureUsingPost2(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.uploadPictureUsingPOST2Params,
  body: {},
  file?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData()

  if (file) {
    formData.append('file', file)
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele]

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''))
        } else {
          formData.append(ele, new Blob([JSON.stringify(item)], { type: 'application/json' }))
        }
      } else {
        formData.append(ele, item)
      }
    }
  })

  return request<API.BaseResponse>('/api/picture/upload', {
    method: 'POST',
    params: {
      ...params,
    },
    data: formData,
    requestType: 'form',
    ...(options || {}),
  })
}
