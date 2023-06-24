---
title: "JavaWeb-C02BackEnd-Day06MySQL(2)"
date: 2023-06-23T16:50:36+08:00
draft: false
tags: ["MySQL"]
categories: ["JavaWeb", "数据库"]
twemoji: true
lightgallery: true
---

## 1. 多表设计
一对多
多对多
### 1.1 一对多
例如 部门信息表(tb_dept) 和 员工信息表(tb_emp), 部门信息表存放着部门名称(name)和id, 员工需要添加所属部门(dept_id)的信息。

可以分析到, **一个部门**下会有**多个员工**, 而员工只属于某一个部门(暂不考虑身兼数职的情况)。因此两表构成 **一(部门表)对多(员工表)** 的关系, 为了将两表关联上, **需要在数据库表中多的一方，添加字段，来关联一的一方的主键**。

例如, 在 tb_emp 中添加字段 dept_id 表示员工的归属部门, 同时称部门表为父表而员工表为子表。
#### 1.1.1 外键约束
部门数据可以直接删除，然而还有部分员工归属于该部门下，此时就出现了数据的不完整、不一致问题。

在数据库层面操作添加物理外键约束
```sql
alter table  表名  add constraint  外键名称  foreign key (外键字段名) references  主表(字段名);
```
这样在删除某一部门时, 会首先检查员工信息表, 若发现该部门下还有员工, 则这一删除操作不会执行.

然而, 其缺点在于:
* 影响增、删、改的效率（需要检查外键关系）。
* 仅用于单节点数据库，不适用与分布式、集群场景。
* 容易引发数据库的死锁问题，消耗性能。

因此, 通常在业务层逻辑中，解决外键关联, 也即通过java代码实现.

### 1.2 多对多
例如选课关系, 一名学生可以选修多门课程, 一门课程也会有多名学生来听. 因此这构成多对多关系.
此时, 为了将两表关联起来, 需要新建一个 学生选课表, 一条记录表示某一学生学习了某一门课程。

### 1.3 设计案例
苍穹外卖后台管理系统
本次只分析 分类管理(category), 菜品管理(dish), 套餐管理(setmeal) 三个表.
* category
包括**菜品分类**、**套餐分类**
* dish
**菜品名称**, 2-20, 汉字、字母大小写、阿拉伯数字, 不可重复
**菜品分类**, 必填, 下拉框
**价格**, 必填, 输入框, 数字，可以有小数点后2位小数
* setmeal
**套餐名称**, **套餐分类**, **套餐价格**, **套餐菜品**
#### 关系分析
一个菜品只对应一个菜品分类, 一个菜品分类下可以有多种菜品;
一个套餐只对应一个套餐分类, 一个套餐分类下可以有多种套餐;
一个套餐可包含多种菜品, 一个菜品可位于多种套餐下.
因此:
```cmd
category: dish = 1: n;
category: setmeal = 1: n;
setmeal: dish = m: n;
setmeal_dish: record the relation between setmeal and dish, note that copies is recorded too;
```

## 2. 多表查询
多表查询:指从多张表中查询数据
笛卡尔积:笛卡尔乘积是指在数学中，两个集合(A集合和B集合)的所有组合情况
分类
* 连接查询
  * 内连接:相当于查询A、B交集部分数据
  * 外连接
    * 左外连接:查询左表所有数据(包括两张表交集部分数据)
    * 右外连接:查询右表所有数据(包括两张表交集部分数据)
* 子查询

### 2.1 连接
1. 查询员工的姓名及所属的部门名称(隐式内连接实现)
```sql
select 字段列表 from 表1,表2 where 条件 ...;
```
2. 查询员工表**所有员工**的姓名，和对应的部门名称 (左外连接)
```sql
select 字段列表 from 表1 left [outer] join 表2 on 连接条件 ...;
```
3.  查询部门表 所有部门的名名称，和对应的员工名称 (右外连接)

### 2.2 子查询
* SQL语句中嵌套select语句，称为嵌套查询，又称子查询。

* 形式: `select * from tl where column1 = ( select column1 from t2 ... );`

* 子查询外部的语句可以是insert / update / delete / select 的任何一个，最常见的是 select。

#### 2.2.1 分类
标量子查询:子查询返回的结果为单个值(数字、字符串、日期等)
列子查询:子查询返回的结果为一列
行子查询:子查询返回的结果为一行
表子查询:子查询返回的结果为多行多列

#### 2.2.2 案例
4. 查询 教研部 的所有员工信息 (标量子查询)

5. 查询 教研部 和 咨询部 的所有员工信息 (列子查询)

6. 查询与 韦一笑 的入职日期 及 职位都相同的员工信息 (行子查询)

7. 查询入职日期是 2006-01-01 之后的员工信息及其部门名称 (表子查询)

## 3. 附录
```sql
-- 1
select tb emp.name,tb dept.name from th emp,tb dept where tb emp.dept id = th dept.id;

-- 2
select e.name, d.name from tb emp e left join th dept d on e.dept id = d.id;

--4
select * from tb_emp where dept_id =(select id from tb_dept where name = '教研部';

--5
select * from tb_emp where dept_id in (select id from tb_dept where name = '教研部' or name = '咨询部');

--6
select * from th_emp where (entrydate,job)=(select entrydate, job from tb_emp where name = '韦一笑');

--7
select e.* , d.name from (select * from tb_emp where entrydate > '2006-01-01') e , tb_dept d where e.dept id = d.id;
```