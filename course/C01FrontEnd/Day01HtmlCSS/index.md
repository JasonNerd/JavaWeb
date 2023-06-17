---
title: "Web前端(1)-HTML与CSS-网页的排版与样式"
date: 2023-04-06T15:26:52+08:00
draft: false
tags: ["web前端", "HTML", "CSS"]
categories: ["JavaWeb"]
twemoji: true
lightgallery: true
---

## Web 网站工作流程
浏览器 -> 前端服务器 (返回前端代码, 浏览器解释前端代码渲染出框架)
浏览器 -> 请求数据 (后端服务器--Java程序) --> 数据库服务器(返回对应数据)

## 初识 web 前端
* 文字 图片 音频 视频 超链接 ... ...
* 本质: 前端代码
* 由浏览器解析和渲染, 内核不同, 渲染效果不同? -- 需要一个标准！
* 标准: W3C
  * HTML: 结构-元素和内容
  * CSS: 网页表现(页面样式)
  * JS: 网页的行为(例如按钮)

## HTML与CSS入门
HTML: HyperText Markup Language, 超文本标记语言
1. 超文本: 不仅仅是文本
2. 标签: 预定义标签, 例如 `<video>` `<img>`等
CSS(Cascading Style Sheet): 层叠样式表
例如字体大小、颜色等等

### [示例1--焦点访谈：中国底气 新思想夯实大国粮仓](./cases/FocusInterview.html)
#### 标题
```html
<html>
    <head>
        <title>HTML 入门</title>
    </head>
    <body>
        <h1>这是一个标题</h1>
        <img src="../image/mouth.png"/>
    </body>
<html>
```
#### 正文排版
1. 图片`<img>`: src, height, weight
2. 超链接 `a`
3. 标题: `<h1>`至`<h6>`
4. 视频标签: `<video>`
    `src`: 规定视频的url
    `controls`: 显示播放控件
    `width`: 播放器的宽度
    `height`: 播放器的高度
5. 音频标签:`<audio>`
    `src`: 规定音频的url
    `controls`: 显示播放控件
6. 段落标签: `<p>`
7. 文本加粗标签: `<b>`, `<strong>`
8. CSS样式
    `line-height`: 设置行高
    `text-indent`:定义第一个行内容的缩进
    `text-align`:规定元素中的文本的水平对齐方式
9. **注意**
  * 在HTML中无论输入多少个空格，只会显示一个。可以使用空格占位符: &nbsp;
  * HTML标签不区分大小写
  * HTML标签属性值单双引号都可以
  * HTML语法
#### 整体布局
##### 盒子模型
* **盒子模型**：页面中所有的元素(标签)，都可以看做是一个 盒子，由盒子将页面中的元素包含在一个矩形区域内，通过盒子的视角更方便的进行页面布局
* **盒子模型组成**: 内容区域(**content**) 、内边距区域 (**padding**) 、边框区域 (**border**) 、外边距区域 (**margin**)
##### 布局标签
布局标签:实际开发网页中，会大量频繁的使用 div 和span 这两个没有语义的布局标签
* 标签: `<div>` `<span>`
* 特点:
  * div标签: 
    * 一行只显示一个(独占一行); 
    * 宽度默认是父元素的宽度，高度默认由内容撑开; 
    * 可以设置宽高 (width、height);
  * span标签: 
    * 一行可以显示多个; 
    * 宽度和高度默认由内容撑开; 
    * 不可以设置宽高(width、height);
* 示例:
```css
<style>
    div {
        width: 200px;
        height: 200px;
        box-sizing: border-box;         /* 指定width height为盒子的高宽 */
        background-color: aquamarine;   /* 背景色 */
        padding: 20px 20px 20px 20px;   /* 内边距，上右下左*/
        border: 10px solid  red;        /* 边框，宽度 线条类型 颜色*/
        margin:30px 30px 30px 30px;     /* 外边距，上右下左*/
    }
</style>
```

### [表格表单等网页元素]()
#### 表格
| 标签 |描述 | 属性/备注 |
|:--|:--:|:--:|
|`<table>`| 定义表格整体，可以包裹多个 `<tr>` | `border`: 规定表格边框的宽度; `width`: 规定表格的宽度; `cellspacing`: 规定单元之间的空间|
|`<tr>`| 表格的行，可以包裹多个 `<td>`| |
|`<td>`|表格单元格(普通)，可以包裹内容|如果是表头单元格，可以替换为 `<th>`|

#### 表单`<form>`
`<input>`: 定义表单项，通过type属性控制输入形式
`<select>`:定义下拉列表
`<textarea>`;定义文本域

1. form 表单属性:
    * action: 表单提交的url, 也即往何处提交数据,  如果不指定, 默认提交到当前页面
    * method: 提交方式
      * get: 在url后面拼接表单数据, 比如: `?username=Tom&age=12`, ur1长度有限制, 默认值.
      * post: 在消息体(请求体) 中传递, 参数大小无限制.

```html
<form action="" method="get">
    用户名: <input type="text" name="username">
    年龄: <input type="text" name="age">
    <input type="submit" value="提交">
</form>
```

注意: 表单项必须有name属性才可以提交

2. 表单标签-表单项

type取值|描述
:-|:-
text| 默认值，定义单行的输入字段
password| 定义密码字段
radio| 定义单选按钮
checkbox| 定义复选框
file| 定义文件上传按钮
date/time/datetime-local| 定义日期/时间/日期时间
number| 定义数字输入框
email| 定义邮件输入框
hidden| 定义隐藏域
submit / reset / button|定义提交按钮 /重置按钮 /可点击按钮

[示例文件-NavigateForms.html](./cases/NavigateForms.html)