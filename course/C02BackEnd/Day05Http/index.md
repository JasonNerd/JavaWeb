# 请求数据
1. get/post, 简单直接参数
2. get/post, 简单实体参数
3. get/post, 复杂实体参数
4. post, json参数
5. get/post, 数组/集合参数
6. get, 路径参数

# 响应数据
返回字符串
返回对象实体
返回对象实体的集合
`@RestController` = `@Controller` + `@ResponseBody`, 返回值均统一封装为了json
## 统一响应结果
均封装为 Result 对象: Result(code, msg, data)

# 分层解耦--三层架构
## 分层
