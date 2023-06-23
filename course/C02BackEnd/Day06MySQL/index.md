---
title: "JavaWeb-C02BackEnd-Day06MySQL"
date: 2023-06-21T19:30:15+08:00
draft: false
tags: ["MySQL"]
categories: ["JavaWeb", "数据库"]
twemoji: true
lightgallery: true
---

## MySQL数据模型
database table record

## SQL 分类

简称| 全称 | 描述
|:-|:-|:-|
DDL | Data Definition Language | 数据定义语言，用来定义数据库对象(数据库，表，字段)
DML | Data Manipulation Language | 数据操作语言，用来对数据库表中的数据进行增删改
DQL | Data Query Language | 数据查询语言，用来查询数据库中表的记录
DCL | Data Control Language | 数据控制语言，用来创建数据库用户、控制数据库的访问权限

## DDL(Data Definition Language)
### database
```SQL
create database if not exists student_info;
show databases;
use database student_info;
select database();  --查看 当前数据库
drop database if exists student_info;
```
### table

约束是作用于表中字段上的规则, 用于限制存储在表中的数据, 保证数据库中数据的正确性、有效性和完整性。

约束| 描述 | 关键字
:-|:-:|:-|
非空约束 | 限制该字段值不能为null | not null
唯一约束 | 保证字段的所有数据都是唯一、不重复的 | unique
主键约束 | 主键是一行数据的唯一标识，要求非空且唯一 | primary key
默认约束 | 保存数据时，如果未指定该字段值，则采用默认值 | default
外键约束 | 让两张表的数据建立连接，保证数据的一致性和完整性 | foreign key

那么假如要建立这样一张表, `(id, username, name, age, gender)`, 要求id为主键, username唯一且非空. 则有:
```sql
create table if not exists tb_user{
    id int primary key auto increment comment '用户ID, 唯一标识',
    username varchar(20) unique not null comment '用户名, 非空且唯一',
    name varchar(10) comment '姓名',
    age int comment '年龄',
    gender char(1) default '男' comment '性别'
} comment '用户表';

```
在员工管理系统中, 有这样一个表格(`tb_emp`), **请在 IDEA 中实现它**

字段名称 | 必填/选填 | 类型 | 长度限制 | 输入限制 | 是否唯一
:-|:-|:-|:-|:-|:-|
用户名|必填|输入框|2-20|数字、字母|是
员工姓名|必填|输入框|2-10|汉字|否
性别|选填|下拉框|-|选择 男/女|否
图像|选填|图片上传|2M|图片大小不能超过2M|否
职位|选填|下拉框|-|选择 班主任/讲师/学工主管/教研主管|否
入职日期|选填|日期选择组件|-|格式为:xxxx-xx-xx|否
归属部门|选填|下拉框|-|选择该员工所属的部门，关联到部门表|否

添加员工时，会给员工设置一个默认的密码 123456，添加完成后，员工就可以通过该空码登录该后台管理系统了

添加一条记录时, 还会保存它的 添加时间(或称为 入职时间)

更新一条记录时, 还会保存它的 更新时间

## DML(Data Manipulation Language)
添加数据 (INSERT)
修改数据 (UPDATE)
删除数据 (DELETE)

1. 向 `tb_emp` 表插入几条数据
   `insert into tb_emp(username,name, gender,create time,update time) values ('wuji','张无忌',1,now() ,now());`
2. 更新其中的一条数据
   `update tb_emp set name = '张三', update_time = now() where id=1;`
3. 删除表格数据
   `delete from tb_emp where xxx;`


## DQL (Data Query Language)
* 基本查询 (`select attrs from table`)
* 条件查询 (`where condition`)
* 分组查询 (`group by some attr`)
* 排序查询 (`order by some attr`)
* 分页查询 (`limit start_index, record counts`)

### 条件查询
1. 查询姓名为 景天 的员工信息
2. 查询 没有分配职位 的员工信息
3. 否询 入职日期 在2000-01-01(包含) 到2010-01-01(包含)之的员工信息
4. 查询 姓名 为两个字的员工信息
5. 查询 职位是 2 (讲师)，3 (学工主管)，4 (教研主管) 的员工信息
6. 查询 姓张的员工信息
   `select * from tb_emp where name like '张%'`


### 统计
1. 统计 `tb_emp` 记录条数:
   `select count(*) from tb_emp;`
2. 统计该企业最早入职的员工
   `select min(entrydate) from tb_emp;`
3. 统计该企业最迟入职的员工 - max
4. 统计该企业员工 ID 的平均值 - avg
5. 统计该企业员工 ID 之和 - sum

### 分组、排序、分页
1. 先查询入职时间在2015-01-01(包含)以前的员工, 并对结果根职位分组, 获取员工数量大于等于2的职位:
   `select job,count(*) from tb_emp where entrydate <= '2015-01-01' group by job having count(*) >= 2;`
2. 根据入职时间，对员工进行升序排序
3. 根据入职时间，对员工进行降序排序
   `select * from tb_emp order by entrydate desc;`
4.  根据 入职时间 对公司的员工进行 升序排序 ， 入职时间相同 ， 再按照 更新时间 进行降序排序
    `select * from tb_emp order by entrydate ,update_time desc;`
5. 假设每页展示10条数据, 现在要查询第5页的数据
    `select * from tb_emp limit 40, 10`


## 案例：根据页面原型分析需求完成员工信息查询
1. 页面一: 条件查询
根据输入的 员工姓名、员工性别、入职时间 搜索满足条件的员工信息。
其中 员工姓名 支持模糊匹配; 性别 进行精确查询，入职时间 进行范围查询
支持分页查询
并对查询的结果，根据最后修改时间进行倒序排序.
```sql
select *
from tb_emp
where name like '%文%'
and gender = 1
and entrydate between '2000-01-01' and '2015-12-31!
order by update time desc
limit 0,10;
```

2. 统计分析
性别分布
```sql
select if(gender = 1，'男性员工', '女性员工') 性别, count(*) from tb _emp group by gender;
```
职位分布
```sql
select case job 
when 1 then '班主任'
when 2 then '讲师' 
when 3 then '学工上管'
when 4 then '教研上管'
else '未分配职位' end ,
count(*)
from tbemp group by job;
```