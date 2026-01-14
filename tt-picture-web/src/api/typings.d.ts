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

  type BaseResponsePagePicture_ = {
    code?: number
    data?: PagePicture_
    message?: string
  }

  type BaseResponsePagePictureVo_ = {
    code?: number
    data?: PagePictureVo_
    message?: string
  }

  type BaseResponsePageUserVo_ = {
    code?: number
    data?: PageUserVo_
    message?: string
  }

  type BaseResponsePictureVo_ = {
    code?: number
    data?: PictureVo
    message?: string
  }

  type BaseResponseString_ = {
    code?: number
    data?: string
    message?: string
  }

  type DeleteRequest = {
    id?: number
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

  type PagePicture_ = {
    current?: number
    pages?: number
    records?: Picture[]
    size?: number
    total?: number
  }

  type PagePictureVo_ = {
    current?: number
    pages?: number
    records?: PictureVo[]
    size?: number
    total?: number
  }

  type PageUserVo_ = {
    current?: number
    pages?: number
    records?: UserVo[]
    size?: number
    total?: number
  }

  type Picture = {
    category?: string
    createTime?: string
    editTime?: string
    id?: number
    introduction?: string
    isdelete?: number
    name?: string
    picFormat?: string
    picHeight?: number
    picScale?: number
    picSize?: number
    picWidth?: number
    tags?: string
    updateTime?: string
    url?: string
    userId?: number
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

  type PictureVo = {
    category?: string
    createTime?: string
    editTime?: string
    id?: number
    introduction?: string
    name?: string
    picFormat?: string
    picHeight?: number
    picScale?: number
    picSize?: number
    picWidth?: number
    tags?: string[]
    updateTime?: string
    url?: string
    user?: UserVo
    userId?: number
  }

  type test2UsingGETParams = {
    /** name */
    name: string
  }

  type testDownloadUsingGETParams = {
    /** filepath */
    filepath?: string
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

  type uploadPictureUsingPOSTParams = {
    id?: number
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
