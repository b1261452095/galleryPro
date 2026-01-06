declare namespace API {
  type BaseResponseLoginUserVo_ = {
    code?: number
    data?: LoginUserVo
    message?: string
  }

  type BaseResponseObject_ = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type BaseResponseString_ = {
    code?: number
    data?: string
    message?: string
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

  type Pinyin__ = {
    /** 密码 */
    password: string
    /** 确认密码 */
    surePassword: string
    /** 账号 */
    userAccount: string
  }

  type Pinyin_2 = {
    /** 密码 */
    password: string
    /** 账号 */
    userAccount: string
  }

  type test2UsingGETParams = {
    /** name */
    name: string
  }

  type testUsingGETParams = {
    /** name */
    name: string
  }
}
