---
title: "Web前端(3)-Vue-简化开发的前端框架"
date: 2023-04-12T20:35:40+08:00
draft: false
tags: ["web前端", "Vue"]
categories: ["JavaWeb"]
twemoji: true
lightgallery: true
---

Vue 是一套前端框架，免除原生JavaScript中的DOM操作，简化书写. 基于**MVVM(Model-View-ViewModel)**思想，实现数据的**双向绑定**，将编程的关注点放在数据上。
* 官网:https://v2.cn.vueis.org/
* 框架:是一个半成品软件，是一套可重用的、通用的、软件基础代码模型。基于框架进行开发，更加快捷、更加高效。

## Vue快速入门
1. 新建HTML页面，引入Vue.js文件
   `<script src="js/vue.js"></script>`
2. 在JS代码区域，创建Vue核心对象，定义数据模型
   ```js
    <script>
        new Vue({
            el:"#app",
            data:{
                message: "Hello Vue!"
            }
        );
    </script>
   ```
3. 编写视图
   ```HTML
    <div id="app">
    <input type="text” V-model="message">
    {{message}}
    </div>
   ```
注意插值表达式: `{{表达式}}`
内容可以是: 变量, 三元运算符, 函数调用, 算术运算

## 常用指令
指令: HTML标签上带有 **`v-前缀`** 的特殊属性，不同指令具有不同含义。例如: v-if，v-for...常用指令
指令|作用
-:|:-
v-bind|为HTML标签绑定属性值，如设置href，css样式等
v-model|在表单元素上创建双向数据绑定
V-on|为HTML标签绑定事件
v-if|条件性的渲染某元素, 判定为true时渲染,否则不渲染
V-else-if|
V-else|
V-show|根据条件展示某元素，区别在于切换的是display属性的值
v-for| 列表渲染，遍历容器的元素或者对象的属性

### v-bind / v-model
* v-bind:
`<a v-bind:href="url">传智教育</a>`
or 只保留冒号
`<a :href="url">传智教育</a>`

* v-model
`<input type="text" v-model="url">`

* 对应Vue对象
```js
<script>
new Vue(){
    el: "#app",
    data: {
        url: "https://www.github.com"
    }
};
</script>
```
* view 片段
```html
<div id="app">
    <a v-bind:href="ur1">链接1</a>
    <a :href="ur1">链接2</a>
    <input type="text" v-model="url">
</div>
```
### v-on
v-on 为HTML标签绑定事件
`<input type="button" value="按钮" v-on:click="handle()">`
or
`<input type="button" value="按钮" @click="handle()">`
* js 代码片段
```js
<script>
    new Vue(){
        el:"#app",
        data: {
            //...
        }
        methods: {
            handle:function(){
                alert("我被点击了");
            }
        }
    }
<script>
```

### v-if / V-else-if / v-else / v-show

分别使用 if-else-if 和 show 完成以下逻辑:
在输入框输入一个代表年龄的正整数, 判断并显示年龄阶段, 例如 <=35: 青年人, <60: 中年人, 老年人;

* view snippet
```html
<input type="text" v-model: "age">
年龄{{age}},经判定为:
<span v-if: "age <= 35">年轻人</span>
<span v-else-if: "age < 60">中年人</span>
<span V-else>老年人</span>
```
### v-for


## 案例
通过Vue完成表格数据的渲染展示
```js
var users = [
    {name:"Tom", age: 20, gender: 1, score: 78},
    {name:"Rose", age: 18, gender: 2, score: 86},
    {name: "Jerry", age: 26, gender: 1, score: 90},
    {name: "Tony", age: 30, gender: 1, score: 52}
]
```
性别:
gender == 1: 男gender == 2:女
评价:
score >= 85: 优秀; score >= 60:及格; 否则: 不及格
不及格用红色渲染

编号|姓名|年龄|性别|成绩|评价
|:-|:-:|:-:|:-:|:-:|-:|
1|Tom|20|男|78|及格|
2|Rose|18|女|86|优秀|
3|Jerry|26|男|90|优秀|
4|Tony|30|男|52|不及格

a html table snippet example
```html
<!-- border="1" cellspacing="0" width="60%" -->
<table>
<tr>
<th>编号</th><th>姓名</th><th>年龄</th><th>性别</th><th>成绩</th><th>评价</th>
</tr>
<tr align="center">
<td>1</td>
<td>Itcast</td>
<td>18</td>
<td><span>男</span><span>女</span></td>
<td>90</td>
<td><span>优秀</span><span>及格</span><span style="color:red">不及格</span></td>
</tr>
</table>
```
## 生命周期
生命周期:指一个对象从创建到销毁的整个过程
生命周期的八个阶段:每触发一个生命周期事件，会自动执行一个**生命周期方法(钩子)**。

状态|阶段周期
:-|:-
beforeCreate|创建前
created|创建后
beforeMount|挂载前
mounted|挂载完成
beforeUpdate|更新前
updated|更新后
beforeDestroy|销毁前
destroyed|销毁后
```js
<script>
new Vue(){
    el:"#app"
    data: {
        // ...
    }
    mounted(): function(){
        console.log("Vue挂载完毕,发送请求获取数据");
    }
    // methods: 
}
</script>
```
* 回顾
  1. Vue是什么?
    Vue是一个基于MVVM模型的前端is框架
  2. Vue常用指令?
    v-bind、V-model、V-on、v-if、v-show、v-for
  3. Vue生命周期?
    mounted
