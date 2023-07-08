---
title: "C02BackEnd-Day07MyBatis"
date: 2023-07-06T09:34:00+08:00
draft: fals
tags: ["MyBatis"]
categories: ["JavaWeb", "数据库"]
twemoji: true
lightgallery: true
---

## MyBatis 入门
### 基本介绍
* MyBatis是一款优秀的 持久层 框架，用于简化JDBC的开发
* MyBatis本是 Apache的一个开源项目iBatis,2010年这个项目由apache移到了google code,并且改名为MyBatis。2013年11月迁移到Github。
* 官网: https://mybatis.org/mybatis-3/zh/index.html

### 使用MyBatis
创建 Springboot 项目
勾选 MyBatis framework 和 Mysql connector 依赖
准备数据库和数据库表
```sql
create table user(
    id int unsigned primary key auto_increment comment 'ID',
    name varchar(100) comment '姓名',
    age tinyint unsigned comment '年龄',
    gender tinyint unsigned comment '性别, 1:男, 2:女',
    phone varchar(11) comment '手机号'
) comment '用户表';

insert into user(id, name, age, gender, phone) VALUES (null,'白眉鹰王',55,'1','18800000000');
insert into user(id, name, age, gender, phone) VALUES (null,'金毛狮王',45,'1','18800000001');
insert into user(id, name, age, gender, phone) VALUES (null,'青翼蝠王',38,'1','18800000002');
insert into user(id, name, age, gender, phone) VALUES (null,'紫衫龙王',42,'2','18800000003');
insert into user(id, name, age, gender, phone) VALUES (null,'光明左使',37,'1','18800000004');
insert into user(id, name, age, gender, phone) VALUES (null,'光明右使',48,'1','18800000005');

```
书写数据接收实体
```Java
public class User {
    private Integer id;
    private String name;
    private short age;
    private short gender;
    private String phone;
    // getter setter constructor toString
}
```
配置连接信息
```yaml
# 数据库连接信息 - 四要素
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.url=jdbc:mysql://localhost:3306/db_mybatis
spring.datasource.username=root
spring.datasource.password=root226
```
编写 Mapper 类, 定义 Select 函数
```Java
@Mapper
public interface UserMapper {
    // 在运行时，会自动生成资接口的实现类对象(代理对象)，并且将该对象交给TOC容器营理
    @Select("select * from user")
    public List<User> listAll();
}
```
编写Test类, 测试 MyBatis 是否正常工作

### JDBC介绍
**Java DataBase Connectivity**, 是使用Java语言操作关系型数据库的一套API. 它**只提供一些接口, 也即sun公司官方定义的一套操作所有关系型数据库的规范, 各个数据库厂商(例如MySQL, Oracle, SqlServer)去实现这套接口, 提供数据库驱动jar包**。我们可以使用这套接口（JDBC）编程，真正执行的代码是驱动jar包中的实现类。

JDBC的所有操作均在Java代码中完成, 第一是注册驱动, 例如MySQL, 二是配置连接属性, 包括url/name/password等, 三是连接数据库, 四是单独配置属性和SQL语句并完成数据操作, 最后释放资源. 如图所示, 有三大缺点:
![](./image/2023-07-08-10-20-52.png)
一是数据库连接属性与数据处理相互独立, 然而它们均放在了代码中, 在数据库连接属性改变时, 还需要修改代码, 难以维护.
二是数据处理过程过于繁琐.
三是每次要与数据库交互都需要连接和释放, 影响性能.

在 MyBatis 框架下, 有如下处理:
一是数据库连接的四要素被放在配置文件, 是全局属性, 当数据库更换时只需要更改配置, 同时, 数据库连接(con, cursor)交由**数据库连接池**管理, 可重用, 效率高;
二是定义了**数据实体**, 通过数据实体的各个字段接收查询返回的数据;
三是实现了 Mapper 注解, 在**Mapper接口**中定义函数并与SQL操作进行绑定, 另外Mapper接口遵循**依赖注入**机制, 在IOC容器中会存在一个Mapper接口实现类的对象, 因此在其它地方的Java代码中定义完成后即可使用其中的函数。


### 数据库连接池
数据库连接池是个容器, 负责分配、管理数据库连接(Connection), 它允许应用程序**重复使用一个现有的数据库连接，而不是再重新建立一个**, 释放空闲时间超过最大空闲时间的连接(也即线程申请了数据库连接但未在使用), 来避免因为没有释放连接而引起的**数据库连接遗漏**
* 资源重用
* 提升系统响应速度
* 避免数据库连接遗漏

标准接口：DataSource
官方(sun)提供的数据库连接池接口, 第三方组织可以实现此接口以构造适合特性的数据库连接池, 常见的有C3P0, DBCP, Druid, Hikari(Springboot default). 后两个使用居多, 其中Druid连接池是阿里巴巴开源的数据库连接池项目, 功能强大, 性能优秀, 是Java语言最好的数据库连接池之一.
更换连接池只需要更改pom.xml
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.8</version>
</dependency>
```
### Lombook
在定义数据实体时, 除了自己定义属性字段, 其他的getter, setter, constructor, toString等等都还要自己去写, 虽然可以去生成, 但这些重复性的代码显得臃肿.

`Lombok` 是一个实用的Java类库, 能通过**注解**的形式自动生成 constructor, getter/setter, equals, hashcode, toString 等方法，并可以自动化生成日志变量，简化java开发、提高效率。

引入依赖:
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

注解| 作用
:-|:-|
@Getter/@Setter	|为所有的属性提供get/set方法
@ToString	|会给类自动生成易阅读的 toString 方法
@EqualsAndHashCode	|根据类所拥有的非静态字段自动重写 equals 方法和 hashCode 方法
@Data	|提供了更综合的生成代码功能（@Getter + @Setter + @ToString + @EqualsAndHashCode）
@NoArgsConstructor	|为实体类生成无参的构造器方法
@AllArgsConstructor	|为实体类生成除了static修饰的字段之外带有各参数的构造器方法。

## mybatis 基础
CURD操作 



 