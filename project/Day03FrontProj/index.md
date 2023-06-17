---
title: "第03节-前端工程化"
date: 2023-04-16T19:26:07+08:00
draft: false
tags: ["Ajax"]
categories: ["JavaWeb"]
twemoji: true
lightgallery: true
---

## 1. Ajax / Axios
### Ajax 基本语法
Ajax: Asynchronous JavaScript And XML，异步的JavaScript和XML。

* 主要功能:
    1. 数据交换: 通过Ajax可以给服务器发送请求，并获取服务器响应的数据
    2. 异步交互: 可以**在不重新加载整个页面的情况下，与服务器交换数据并更新部分网页的技术**，如: **搜索联想、用户名是否可用的校验**等等。

* 原生Ajax使用基本步骤:
  1. 准备数据地址: http://yapi.smart-xwork.cn/mock/169327/emp/list
  2. 创建XMLHttpRequest对象:用于和服务器交换数据
  3. 向服务器发送请求
  4. 获取服务器响应数据
![](./image/2023-04-17-14-30-55.png)

比较繁琐, Axios 对原生的Ajax进行了封装，简化书写，快速开发。
官网: https://www.axios-http.cn/

### Axios 入门
  1. 引入Axios的js文件:
   ```js
   <script src="js/axios-0.18.0.js"></script>
   ```
  2. 使用Axios发送请求，并获取响应结果
   ```js
   // 1. 使用 get 方法获取 url 数据, 传回的数据为 result. 
   axios({
        method: "get",
        url: "http://yapi.smart-xwork.cn/mock/169327/emp/list"
   }).then((result) => {
        console.log(result.data);
   }
   ```
   ```js
   // 2. 使用 post 方法获取 url 数据, 传回的数据为 result. 
   axios({
        method: "post",
        url: "http://yapi.smart-xwork.cn/mock/169327/emp/deleteByld",
        data: "id=1"
   }).then((result) => {
        console.log(result.data);
   }
   ```

* 继续简化: 使用别名
```js
axios.get(url [, config])
axios.delete(url [, config])
axios.post(url [, datal, config]])
axios.put(url [, data[, config]])
```
  * GET请求
  ```js
    axios.get("http://yapi.smart-xwork.cn/mock/169327/emp/list").then((result)=>{
        console.log(result.data);
    });
  ```
  * 发送POST请求
  ```js
  axios.post("http://vapi.smart-xwork.cn/mock/169327/emp/deleteByld", "id=1").then((result) => {
    consle.log(result.data);
  });
  ```
### 案例: 动态获取数据并展示表格
基于Vue及Axios完成数据的动态加载展示
1. 数据准备的url: http://yapi.smart-xwork.cn/mock/169327/emp/list
```json
{
    "data":[{
        "name":"Tom", 
        "age": 20, 
        "gender": 1, 
        "score": 78
    },{
        "name":"Rose", 
        "age": 18, 
        "gender": 2, 
        "score": 86
    },{
        "name": "Jerry", 
        "age": 26, 
        "gender": 1, 
        "score": 90
    },{
        "name": "Tony", 
        "age": 30, 
        "gender": 1, 
        "score": 52
    }]
}
```
2. 在页面加载完成后，自动发送异步请求，加载数据，渲染展示页面(性别:1代表男，2 代表女)。
```json
{"code":1,"message":"success","data":[{"id":1,"name":"谢逊","image":"https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/1.jpg","gender":1,"job":"班主任","entrydate":"2008-05-09","updatetime":"2022-10-01 12:00:00"},{"id":2,"name":"殷天正","image":"https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/2.jpg","gender":1,"job":"讲师","entrydate":"2012-05-09","updatetime":"2022-10-01 12:00:00"},{"id":3,"name":"韦一笑","image":"https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/3.jpg","gender":1,"job":"讲师","entrydate":"2020-05-09","updatetime":"2022-10-01 12:00:00"},{"id":4,"name":"黛绮丝","image":"https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/4.jpg","gender":2,"job":"讲师","entrydate":"2018-05-09","updatetime":"2022-10-01 12:00:00"}]}
```

## 2. 前端工程化(前后端分离开发)
### 通过 yapi 模拟数据服务器
YApi 是高效、易用、功能强大的 api 管理平台，旨在为开发、产品、测试人员提供更优雅的接口管理服务
http://yapi.smart-xwork.cn/

### vue脚手架
![](./image/2023-04-17-19-35-54.png)
介绍: Vue-cli 是Vue官方提供的一个脚手架，用于快速生成一个 Vue 的项目模板

Vue-cli提供了如下功能:
   * 统一的目录结构
   * 本地调试
   * 热部署
   * 单元测试
   * 集成打包上线
   * 依赖环境: Node.JS

安装 nodeJS 后,
管理员命令行运行:
```
npm config set prefix "[安装路径]"
// 安装路径 替换为自己的安装目录
npm install -g @vue/cli
npm set registry https://registry.npm.taobao.org
vue -V
```

这样就安装好了 vue-cli 工具

### 创建 vue 项目
在工程文件夹下管理员命令行运行
`vue create vue-hello`
`cd vue-hello`
`npm run serve`
或者通过图形化界面
`vue ui`

#### 目录结构
![](./image/2023-04-17-19-51-13.png)

Vue 的 组文件以 .vue 结尾, 每个组件由三个部分组成:
template, script, style
```vue
<template>
  <div id="app">
    <h1>{{ message }}</h1>
  </div>
</template>

<script>
export default {
  data(){
    return {
      message: "Hello Vue!"
    }
  },
  methods: {
    // 
  }
}
</script>

<style>

</style>

```

## Element
Element:是饿了么团队研发的，一套为开发者、设计师和产品经理准备的基于 Vue 2.0 的桌面端组件库

组件: 组成网页的部件，例如 超链接、按钮、图片、表格、表单、分页条等等。

官网: https://element.eleme.cn/#/zh-CNListener

使用:
* 安装ElementUI组件库(在当前工程的目录下)，在命令行执行指令
`npm i element-ui -S`
* 引入ElementUI组件库
`import ElementUI from 'element-ui'`;
`import 'element-ui/lib/theme-chalk/index.css'`;
`Vue.use(ElementuI);` <-- main.js
* 访问官网，复制组件代码，调整

### [Container 布局容器](https://element.eleme.cn/)
用于布局的容器组件，方便快速搭建页面的基本结构：

`<el-container>`：外层容器。当子元素中包含 `<el-header>` 或 `<el-footer>` 时，全部子元素会垂直上下排列，否则会水平左右排列。
`<el-header>`：顶栏容器。
`<el-aside>`：侧边栏容器。
`<el-main>`：主要区域容器。
`<el-footer>`：底栏容器。


![](./image/2023-04-17-21-01-25.png)

1. 布局容器
2. 元素构成
   1. 侧边下拉菜单
   2. 头部导航栏
   3. 主要区域容器
      1. 顶部搜索栏表单
      2. 表格
      3. 翻页

### 暂存


Day2-chapter01-Vue快速入门
file:///D:/User/mrrai/project/JavaLearning/HeiMa/WebbingTechniques/S01FrontEnd/Day02Vue/html/D201Vue%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8.html

vue-hello
http://localhost:8080/

基本用例 | Axios 中文文档 | Axios 中文网
https://www.axios-http.cn/docs/example

emplist-YApi-高效、易用、功能强大的可视化接口管理平台
http://yapi.smart-xwork.cn/project/227281/interface/api/3186516

API — Vue.js
https://v2.cn.vuejs.org/v2/api/#v-for

组件 | Element
https://element.eleme.cn/#/zh-CN/component/table

note
file:///C:/Users/mrrai/AppData/Local/Temp/mume2023317-12808-1et52ft.wglek.html

Day04-01. maven-课程介绍_哔哩哔哩_bilibili
https://www.bilibili.com/video/BV1m84y1w7Tb?p=50