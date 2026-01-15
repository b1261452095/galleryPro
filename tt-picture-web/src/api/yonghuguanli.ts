// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** 新增用户（仅管理员） POST /api/user/add */
export async function addUserUsingPost1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addUserUsingPOST1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/user/add', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 删除用户（仅管理员） DELETE /api/user/delete */
export async function deleteUserUsingDelete1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserUsingDELETE1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/user/delete', {
    method: 'DELETE',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取用户列表（仅管理员） POST /api/user/list */
export async function getUserListUsingPost1(
  body: API.UserQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/user/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 用户登录 POST /api/user/login */
export async function registerUsingPost2(
  body: API.UserLoginRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取登录的用户信息 GET /api/user/loginUserInfo */
export async function getLoginUserInfoUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponse>('/api/user/loginUserInfo', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 用户注册 POST /api/user/register */
export async function registerUsingPost3(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 更新用户（仅管理员） PUT /api/user/update */
export async function updateUserUsingPut1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateUserUsingPUT1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/api/user/update', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
