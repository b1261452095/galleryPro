declare namespace API {
  type addUserUsingPOSTParams = {
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type BaseResponseBoolean_ = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseLoginUserVo_ = {
    code?: number
    data?: LoginUserVo
    message?: string
  }

  type BaseResponseLong_ = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseObject_ = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type BaseResponsePageUserVo_ = {
    code?: number
    data?: PageUserVo_
    message?: string
  }

  type BaseResponseString_ = {
    code?: number
    data?: string
    message?: string
  }

  type deleteUserUsingDELETEParams = {
    id?: number
  }

  type helloUsingGETParams = {
    /** name */
    name: string
  }

  type LoginUserVo = {
    createTime?: string
    id?: number
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type PageUserVo_ = {
    current?: number
    pages?: number
    records?: UserVo[]
    size?: number
    total?: number
  }

  type test2UsingGETParams = {
    /** name */
    name: string
  }

  type testUsingGETParams = {
    /** name */
    name: string
  }

  type updateUserUsingPUTParams = {
    id?: number
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type UserLoginRequest = {
    /** 密码 */
    password: string
    /** 账号 */
    userAccount: string
  }

  type UserQueryRequest = {
    current?: number
    id?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type UserRegisterRequest = {
    /** 密码 */
    password: string
    /** 确认密码 */
    surePassword: string
    /** 账号 */
    userAccount: string
  }

  type UserVo = {
    createTime?: string
    id?: number
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }
}
