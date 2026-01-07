// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** 用户登录 POST /api/user/login */
export async function registerUsingPost(
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
export async function getLoginUserInfoUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponse>('/api/user/loginUserInfo', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 用户注册 POST /api/user/register */
export async function registerUsingPost1(
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
