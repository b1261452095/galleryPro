declare namespace API {
  type getLoginParams = {
    userId: string
    roleId: string
  }

  type getRegExpParams = {
    regexp1: string
  }

  type hello2Params = {
    name?: string
  }

  type hello3Params = {
    name?: string
  }

  type hello4Params = {
    name?: string
  }

  type hello5Params = {
    name?: string
  }

  type helloParams = {
    name?: string
  }

  type saveUser2Params = {
    u: User
  }

  type saveUser3Params = {
    u: User
  }

  type saveUser4Params = {
    u: User
  }

  type saveUser5Params = {
    u: User
  }

  type saveUserParams = {
    u: User
  }

  type User = {
    name?: string
    age?: number
  }
}
