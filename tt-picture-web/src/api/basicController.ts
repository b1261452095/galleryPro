// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** hello GET /api/hello222245 */
export async function helloUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.helloUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/hello222245', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** test GET /api/test */
export async function testUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.testUsingGET1Params,
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
export async function testDownloadUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.testDownloadUsingGET1Params,
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
export async function testUploadUsingPost1(
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

  return request<API.BaseResponse>('/api/test/upload', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  })
}

/** test2 GET /api/test2 */
export async function test2UsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.test2UsingGET1Params,
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
