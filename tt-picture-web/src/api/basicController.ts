// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** hello GET /api/hello222245 */
export async function helloUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.helloUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseObject_>('/api/hello222245', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** test GET /api/test */
export async function testUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.testUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<string>('/api/test', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 测试下载文件 GET /api/test/download */
export async function testDownloadUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.testDownloadUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<any>('/api/test/download', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 测试上传文件 POST /api/test/upload */
export async function testUploadUsingPost(body: {}, file?: File, options?: { [key: string]: any }) {
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

  return request<API.BaseResponseString_>('/api/test/upload', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  })
}

/** test2 GET /api/test2 */
export async function test2UsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.test2UsingGETParams,
  options?: { [key: string]: any }
) {
  return request<string>('/api/test2', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
