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
