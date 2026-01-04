declare namespace API {
  type BaseResponseObject_ = {
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
}
