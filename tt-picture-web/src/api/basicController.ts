// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** 此处后端没有提供注释 GET /hello */
export async function hello(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.helloParams,
  options?: { [key: string]: any }
) {
  return request<string>('/hello', {
    method: 'GET',
    params: {
      // name has a default value: unknown user
      name: 'unknown user',
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /hello */
export async function hello3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.hello3Params,
  options?: { [key: string]: any }
) {
  return request<string>('/hello', {
    method: 'PUT',
    params: {
      // name has a default value: unknown user
      name: 'unknown user',
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /hello */
export async function hello2(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.hello2Params,
  options?: { [key: string]: any }
) {
  return request<string>('/hello', {
    method: 'POST',
    params: {
      // name has a default value: unknown user
      name: 'unknown user',
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /hello */
export async function hello5(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.hello5Params,
  options?: { [key: string]: any }
) {
  return request<string>('/hello', {
    method: 'DELETE',
    params: {
      // name has a default value: unknown user
      name: 'unknown user',
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PATCH /hello */
export async function hello4(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.hello4Params,
  options?: { [key: string]: any }
) {
  return request<string>('/hello', {
    method: 'PATCH',
    params: {
      // name has a default value: unknown user
      name: 'unknown user',
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /save_user */
export async function saveUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.saveUserParams,
  options?: { [key: string]: any }
) {
  return request<string>('/save_user', {
    method: 'GET',
    params: {
      ...params,
      u: undefined,
      ...params['u'],
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /save_user */
export async function saveUser3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.saveUser3Params,
  options?: { [key: string]: any }
) {
  return request<string>('/save_user', {
    method: 'PUT',
    params: {
      ...params,
      u: undefined,
      ...params['u'],
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /save_user */
export async function saveUser2(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.saveUser2Params,
  options?: { [key: string]: any }
) {
  return request<string>('/save_user', {
    method: 'POST',
    params: {
      ...params,
      u: undefined,
      ...params['u'],
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /save_user */
export async function saveUser5(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.saveUser5Params,
  options?: { [key: string]: any }
) {
  return request<string>('/save_user', {
    method: 'DELETE',
    params: {
      ...params,
      u: undefined,
      ...params['u'],
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PATCH /save_user */
export async function saveUser4(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.saveUser4Params,
  options?: { [key: string]: any }
) {
  return request<string>('/save_user', {
    method: 'PATCH',
    params: {
      ...params,
      u: undefined,
      ...params['u'],
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /user */
export async function user(options?: { [key: string]: any }) {
  return request<API.User>('/user', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /user */
export async function user3(options?: { [key: string]: any }) {
  return request<API.User>('/user', {
    method: 'PUT',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /user */
export async function user2(options?: { [key: string]: any }) {
  return request<API.User>('/user', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /user */
export async function user5(options?: { [key: string]: any }) {
  return request<API.User>('/user', {
    method: 'DELETE',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PATCH /user */
export async function user4(options?: { [key: string]: any }) {
  return request<API.User>('/user', {
    method: 'PATCH',
    ...(options || {}),
  })
}
