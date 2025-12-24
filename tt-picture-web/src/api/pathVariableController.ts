// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** 此处后端没有提供注释 GET /javabeat/${param0} */
export async function getRegExp(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRegExpParams,
  options?: { [key: string]: any }
) {
  const { regexp1: param0, ...queryParams } = params
  return request<string>(`/javabeat/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /user/${param0}/roles/${param1} */
export async function getLogin(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLoginParams,
  options?: { [key: string]: any }
) {
  const { userId: param0, roleId: param1, ...queryParams } = params
  return request<string>(`/user/${param0}/roles/${param1}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
