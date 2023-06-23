
---
title: "JavaWeb-C02BackEnd-Day05Http"
date: 2023-06-17T15:23:47+08:00
draft: false
tags: ["Web后端"]
categories: ["JavaWeb"]
twemoji: true
lightgallery: true
---

# http 请求数据格式
请求行: 请求方式, 资源路径, 协议(HTTP/1.1)
请求头: 格式为(key: value)对, 例如浏览器版本, 浏览器接受的资源类型等等
请求体: POST请求, 存放请求参数
# http 响应数据
响应行: 状态码(1xx)

状态码| 描述
|:-:|:-|
1xx | 响应中-临时状态码，表示请求已经接收，告诉客户端应该继续请求或者如果它已经完成则忽略它。
2xx | 成功-表示请求已经被成功接收，处理已完成。
3xx | 重定向-重定向到其他地方;让客户端再发起一次请求以完成整个处理
4xx | 客户端错误-处理发生错误，责任在客户端。如: 请求了不存在的资源、客户端未被授权、禁止访问等。
5xx | 服务器错误-处理发生错误，责任在服务端。如: 程序抛出异常等。

响应头: 格式为(key: value)对, 例如响应内容类型, 长度, 压缩算法
响应体: POST请求, 存放请求参数

响应码描述表(常见)
状态码 |英文描述| 解释
|:-:|:-|:-|
200| OK | 客户端请求成功，即处理成功，这是我们最想看到的状态码
302| Found | 指示所请求的资源已移动到由Location响应头给定的 URL，浏览器会自动重新访问到这个页面告诉客户端
304| Not Modified | 请求的资源至上次取得后，服务端并未更改，使用本地缓存，隐式重定向
400| Bad Request| 客户端请求有语法错误，不能被服务器所理解
403| Forbidden|服务器收到请求，但是拒绝提供服务，比如:没有权限访问相关资源
404| Not Found|请求资源不存在，一般是URL输入有误，或者网站资源被删除
405| Method Not Allowed|请求方式有误，比如应该用GET请求方式的资源，用了POST
428| Precondition Required|服务器要求有条件的请求，告诉客户端要想访问该资源，必须携带特定的请求头
429| Too Many Requests|指示用户在给定时间内发送了太多请求 ("限速”) ，配合 Retry-After(多长时间后可以请求)响应头一起使用
431| Request Header Fie1ds Too Large|请求头太大，服务器拒绝处理，可以在减少请求头域的大小后重新提交
500| Internal Server Error|服务器发生不可预期的错误。服务器出异常了，赶紧看日志去吧
503| service Unavailable|服务器尚未准备好处理请求，服务器刚刚启动，还未初始化好

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
controller service dao
## 解耦合
java bean, ioc/di