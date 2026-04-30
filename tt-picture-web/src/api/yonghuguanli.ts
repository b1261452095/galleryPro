// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** 新增用户（仅管理员） POST /api/user/add */
export async function addUserUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addUserUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/user/add', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 删除用户（仅管理员） DELETE /api/user/delete */
export async function deleteUserUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserUsingDELETEParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/user/delete', {
    method: 'DELETE',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取用户列表（仅管理员） POST /api/user/list */
export async function getUserListUsingPost(
  body: API.UserQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageUserVo_>('/api/user/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 用户登录 POST /api/user/login */
export async function registerUsingPost(
  body: API.UserLoginRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLoginUserVo_>('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取登录的用户信息 GET /api/user/loginUserInfo */
export async function getLoginUserInfoUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginUserVo_>('/api/user/loginUserInfo', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 用户注册 POST /api/user/register */
export async function registerUsingPost1(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString_>('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 更新用户（仅管理员） PUT /api/user/update */
export async function updateUserUsingPut(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateUserUsingPUTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/user/update', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
