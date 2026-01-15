declare namespace API {
  type addUserUsingPOST1Params = {
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type BaseResponse = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type DeleteRequest = {
    id?: number
  }

  type deleteUserUsingDELETE1Params = {
    id?: number
  }

  type helloUsingGET1Params = {
    /** name */
    name: string
  }

  type PictureQueryRequest = {
    category?: string
    current?: number
    id?: number
    introduction?: string
    name?: string
    pageSize?: number
    picFormat?: string
    picHeight?: number
    picScale?: number
    picSize?: number
    picWidth?: number
    sortField?: string
    sortOrder?: string
    tags?: string[]
    url?: string
    userId?: number
  }

  type PictureUpdateRequest = {
    category?: string
    id?: number
    introduction?: string
    name?: string
    tags?: string[]
  }

  type PictureVo = {
    id?: number
    url?: string
    name?: string
    introduction?: string
    category?: string
    tags?: string[]
    picSize?: number
    picWidth?: number
    picHeight?: number
    picScale?: number
    picFormat?: string
    userId?: number
    createTime?: string
    editTime?: string
    updateTime?: string
    user?: any
  }

  type test2UsingGET1Params = {
    /** name */
    name: string
  }

  type testDownloadUsingGET1Params = {
    /** param0 */
    param0?: string
  }

  type testDownloadUsingGET2Params = {
    /** filepath */
    filepath?: string
  }

  type testUsingGET1Params = {
    /** name */
    name: string
  }

  type updateUserUsingPUT1Params = {
    id?: number
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type uploadPictureUsingPOST1Params = {
    category?: string
    id?: number
    introduction?: string
    name?: string
    tags?: string[]
  }

  type uploadPictureUsingPOST2Params = {
    category?: string
    id?: number
    introduction?: string
    name?: string
    tags?: string[]
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
}
