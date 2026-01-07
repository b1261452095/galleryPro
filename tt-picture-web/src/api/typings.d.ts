declare namespace API {
  type BaseResponse = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type helloUsingGETParams = {
    /** name */
    name: string
  }

  type test2UsingGETParams = {
    /** name */
    name: string
  }

  type testUsingGETParams = {
    /** name */
    name: string
  }

  type UserLoginRequest = {
    /** 密码 */
    password: string
    /** 账号 */
    userAccount: string
  }

  type UserRegisterRequest = {
    /** 密码 */
    password: string
    /** 确认密码 */
    surePassword: string
    /** 账号 */
    userAccount: string
  }
}
