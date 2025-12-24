import {} from '@umijs/openapi'

export default {
  requestLibPath: "import request from '@/utils/request'",
  // 修改为正确的 OpenAPI JSON 文档地址
  // 常见的地址有：
  // - /v2/api-docs (Swagger 2.0)
  // - /v3/api-docs (OpenAPI 3.0)
  // - /swagger/v2/api-docs
  schemaPath: 'http://localhost:8888/api/v3/api-docs',
  serversPath: './src',
}