---
title: "SpringBoot 基本原理--依赖传递与自动配置"
date: 2023-10-17T15:09:53+08: 00
draft: false
tags: ["SpringBoot"]
categories: ["JavaWeb"]
twemoji: true
lightgallery: true
---


## 1.SpringBoot 配置优先级
### 1.1 文件配置
SpringBoot 中支持三种格式的配置文件:
```cpp
// 1. application.properties 文件
server.port=8081
// 2. application.yml 文件
server:
   port: 8082
// 3. application.yaml 文件
server:
   port: 8083
```
虽然springboot支持多种格式配置文件(也即三个文件同时存在), 但是在项目开发时, 推荐统一使用一种格式的配置(**yml是主流**). 同时, 他们的生效优先级是: `properties > yml > yaml` 

### 1.2 系统属性和命令行参数
SpringBoot 除了支持配置文件属性配置，还支持Java系统属性和命令行参数的方式进行属性配置。

* Java系统属性
Java系统属性是Java虚拟机提供的配置信息, 可以通过`System.getProperty()`方法获取。
`-Dserver.port=9000`

* 命令行参数
`--server.port=10010`

例如, 执行
`java -Dserver.port=9000 -jar tlias-web-management-0.0.1-SNAPSHOT.jar --server.port=10010`

注意, 此处的jar包就是利用maven工具进行打包的mytlias项目: 它是一个独立的安装了java即可运行的 jar 包(Springboot项目进行打包时，需要引入插件 spring-boot-maven-plugin (基于官网骨架创建项目，会自动添加该插件))

优先级(低→高)
```yml
application.yaml（忽略）
application.yml
application.properties
java系统属性（-Dxxx=xxx）
命令行参数（--xxx=xxx）
```

## 2. bean 管理
### 2.1 获取 bean
默认情况下, Spring项目启动时, 会把bean都创建好放在IOC容器中, 如果想要主动获取这些bean, 可以通过如下方式:
方式|函数原型
:-|:-|
根据name获取bean|`Object getBean(String name)`
根据类型获取bean|`<T> T getBean(Class<T> requiredType)`
根据name获取bean（带类型转换）|`<T> T getBean(String name, Class<T> requiredType)`

上述所说的 【Spring项目启动时, 会把其中的bean都创建好】还会受到作用域及延迟初始化影响, 这里主要针对于默认的**单例非延迟加载的bean**而言。

### 2.2 作用域
Spring支持五种作用域，后三种在web环境才生效：

作用域|	说明
:-|:-
`singleton`|容器内同名称的 bean 只有一个实例（单例）（默认）
`prototype`	|每次使用该 bean 时会创建新的实例（非单例）
`request`|每个请求范围内会创建新的实例（web环境中，了解）
`session`|每个会话范围内会创建新的实例（web环境中，了解）
`application`|每个应用范围内会创建新的实例（web环境中，了解）

可以通过 `@Scope` 注解来进行配置作用域:
```java
@Scope("prototype")
@RestController
@RequestMapping("/depts")
public class DeptController {
   // some code
}
```
默认singleton的bean, 在容器启动时被创建，可以使用`@Lazy`注解来延迟初始化（延迟到第一次使用时）。
prototype的bean, 每一次使用该bean的时候都会创建一个新的实例。
实际开发当中, 绝大部分的Bean是单例的, 也就是说绝大部分Bean不需要配置scope属性。

### 2.3 管理第三方 Bean
无法给第三方类添加注解
```java
@Component
@Controller
@Service
@Repository
```
如果要管理的bean对象来自于第三方（不是自定义的），是无法用 `@Component` 及衍生注解声明bean的, 就需要用到 `@Bean` 注解。

若要管理的第三方bean对象, 建议**对这些bean进行集中分类配置**, 可以通过 `@Configuration` 注解声明一个配置类。
```java
// 连包带类创建 config.CommonConfig

@Configuration
public class CommonConfig {
   @Bean
   public SAXReader saxReader(){
      return new SAXReader();
   }
}
```
通过`@Bean`注解的`name`或`value`属性可以声明`bean`的名称, 如果不指定, **默认bean的名称就是方法名**. 如果第三方bean**需要依赖其它bean对象**, 直接在`bean`定义方法中设置形参即可, 容器会**根据类型自动装配**。

## 3. SpringBoot: 起步依赖+自动配置
SpringBoot 方便好用易上手的基本原因就是它实现了优雅的自动依赖引入与自动配置.

**起步依赖**: 例如 web, 它将基本的 web 开发依赖都自动引入进来, 原理是 maven 的**依赖传递**, 例如包A依赖包B, 包B依赖包C, 则会将 A B C 都引入进来.

**自动配置**: SpringBoot的自动配置就是当spring容器启动后, 一些配置类、bean对象就自动存入到了IOC容器中, 不需要我们手动去声明, 从而简化了开发, 省去了繁琐的配置操作.

### 3.1 自动配置
通过 `案例1. 引入自定义依赖` 来一步步的说明 SpringBoot web 项目的 bean 自动配置的过程.

#### 3.1.1 自定义的演示工具类
创建一个包, 里面包含一些自定义工具类, 他们将交由`bean`容器管理, 在启动时自动创建其中的对象, 在需要使用时只需`@Autowired`自动注入即可.
创建模块`mytlias-utils`, 包结构及代码如下:
```java
// src/main/java/com.example/

// TokenParser.java
package com.exmaple;
import org.springframework.stereotype.Component;
@Component
public class TokenParser {
    public void parse() {
        System.out.println("TokenParser ... parse ...");
    }
}

// HeaderConfig
package com.exmaple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class HeaderConfig {
    @Bean
    public HeaderGenerator headerGenerator(){
        return new HeaderGenerator();
    }
    @Bean
    public HeaderParser headerParser(){
        return new HeaderParser();
    }
}

// HeaderGenerator
package com.exmaple;
public class HeaderGenerator {
    public void generate(){
        System.out.println("HeaderGenerator ... generate ...");
    }
}

// HeaderParser
package com.exmaple;
public class HeaderParser {
    public void parse(){
        System.out.println("HeaderParser ... parse ...");
    }
}
```
如上所示一共4个类, 其中 `TokenParser` 使用 `@Component` 注解注明为 bean, `HeaderGenerator` 和 `HeaderParser` 为两个普通工具类, 但使用 `@Bean` 的方式在 `HeaderConfig` 的配置类中配置为了 bean.

#### 3.1.2 引入 `mytlias-utils` 模块
复制一份可运行的 `mytlias` 工程, 在其中导入`mytlias-utils` 模块, 方式是在 pom.xml 中引入依赖:
```xml
<dependency>
   <groupId>com.example</groupId>
   <artifactId>mytlias-utils</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
```
maven 刷新后, 创建 Test 测试这些 bean 是否存在于 IOC 容器中.
```java
@SpringBootTest
public class AutoConfigTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Gson gson;

    @Test
    public void testGson(){
        System.out.println(gson.toJson(Result.success()));
    }

    @Test
    public void testTokenParser(){
        System.out.println(applicationContext.getBean(TokenParser.class));
    }

    @Test
    public void testHeaderGenerator(){
        System.out.println(applicationContext.getBean(HeaderGenerator.class));
    }

    @Test
    public void testHeaderParser(){
        System.out.println(applicationContext.getBean(HeaderParser.class));
    }
}
```
注意 Gson 和 ApplicationContext 的 bean 是 spring 项目自带的, 另一方面 HeaderConfig 本身作为配置类实质也是交由 IOC 容器管理.

运行 testTokenParser 等测试:
```log

org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.exmaple.TokenParser' available
```
发现运行报错, 引入了依赖, 但并没有查找到 Bean.
原因: 在启动类中, 注解 `@SpringBootApplication` 具有包扫描的作用, 同时, 它的扫描范围又仅限于当前包及其子包, 因此, 对于引入的 `mytlias-utils` 库, 其中的类(bean) 并未扫描到.

#### 3.1.3 SpringBoot 包扫描机制
以上, 为了使得 `mytlias-utils` 能被扫描到, 可以进行一些必要的工作:

* 方案一: `@ComponentScan` 组件扫描:
```java
@ComponentScan({"com.example", "com.itheima", "com.alibaba", ...})
@SpringBootApplication
public class SpringbootWebConfig2Application {
   // some code
}
```
对于所有的外部依赖, 需要写入所有的可能用到的包 ... ... 代码会十分臃肿.

> <font color="red">实际测试时, 会发现找不到 `com.example` 包, 此时测试仍然失败.</font>
> 问题解决了, 原因不是pom依赖问题, 只是由于包名拼写错误

* 方案二: `@Import` 导入. 使用`@Import`导入的类会被Spring加载到IOC容器中，导入形式主要有以下几种:
```log
导入 普通类
导入 配置类
导入 ImportSelector 接口实现类
```
演示:
```java
// SpringbootAutoconfigApplication 头部添加注解
@Import(TokenParser.class)     // 导入普通类并交给ioc管理
// com.exmaple.TokenParser@63d3c9dc

@Import(HeaderConfig.class)   // 导入配置类并交给ioc管理
// com.exmaple.HeaderGenerator@1a4cbcc6
// com.exmaple.HeaderParser@3d4818e8

```
对于 `ImportSelector`, 它的作用是将要导入的类放到一起:
```java
package com.example;

public class UtilsImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"com.example.HeaderConfig"};
    }
}
```
**注意检查包名是否有拼写错误**

* 方案三: 使用 `@EnableXxxx` 注解对`@Import`注解进行封装
实际上, 对于任何第三方的类, 只有第三方类本身才知道自己的包下有哪些类是需要交给 IOC容器 进行管理的, **因此需要一个自动配置的方式**, 第三方库必须指明哪些类需要成为bean, 指明的方式是`@EnableXxxx`, 这一注解由第三方库创建.

创建 `EnableHeaderConfig` 注解类.
```java
package com.example;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(UtilsImportSelector.class)
public @interface EnableHeaderConfig {
}
```
此时通过一个注解即可完成 bean 导入.
```java
@EnableHeaderConfig
@SpringBootApplication
public class SpringbootAutoconfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootAutoconfigApplication.class, args);
    }
}
```
测试通过.


#### 3.1.4 `@SpringBootConfiguration`
该注解标识在SpringBoot工程引导类上, 是SpringBoot中最最最重要的注解. 查看注解的源代码, 一步步理解项目启动时 bean 的加载过程.

##### a. `public @interface SpringBootApplication`
部分代码:
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    ...
```
**`@SpringBootConfiguration`** 注解类主要由三个部分组成：
`@SpringBootConfiguration`: 该注解与 `@Configuration` 注解作用相同, 用来声明当前也是一个配置类.
`@ComponentScan`: 组件扫描, 默认扫描当前引导类所在包及其子包.
`@EnableAutoConfiguration`: SpringBoot实现自动化配置的核心注解 (类比`@EnableHeaderConfig`).

##### b. `public @interface EnableAutoConfiguration`
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({AutoConfigurationImportSelector.class})
public @interface EnableAutoConfiguration {
   ...
```
可以看到有一句 `@Import({AutoConfigurationImportSelector.class})`, 这就类比 `@EnableHeaderConfig` 类中的 `@Import(UtilsImportSelector.class)`.
##### c. `AutoConfigurationImportSelector.class`
找到 `selectImports` 方法, 并进行查看:
```java
public String[] selectImports(AnnotationMetadata annotationMetadata) {
   if (!this.isEnabled(annotationMetadata)) {
      return NO_IMPORTS;
   } else {
      AutoConfigurationEntry autoConfigurationEntry = this.getAutoConfigurationEntry(annotationMetadata);
      return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
   }
}
```
继续找到 `getAutoConfigurationEntry()` 方法:
```java
protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
   if (!this.isEnabled(annotationMetadata)) {
      return EMPTY_ENTRY;
   } else {
      AnnotationAttributes attributes = this.getAttributes(annotationMetadata);
      List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
      configurations = this.removeDuplicates(configurations);
      Set<String> exclusions = this.getExclusions(annotationMetadata, attributes);
      this.checkExcludedClasses(configurations, exclusions);
      configurations.removeAll(exclusions);
      configurations = this.getConfigurationClassFilter().filter(configurations);
      this.fireAutoConfigurationImportEvents(configurations, exclusions);
      return new AutoConfigurationEntry(configurations, exclusions);
   }
}
```
代码的大致逻辑是, 先获取所有的配置项`List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);`, 接着排除掉一些后再把筛选后的 configurations 返回.

继续找到 `getCandidateConfigurations` 方法:
```java
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
   List<String> configurations = ImportCandidates.load(AutoConfiguration.class, this.getBeanClassLoader()).getCandidates();
   Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports. " +
            "If you are using a custom packaging, make sure that file is correct.");
   return configurations;
}
```
可以看出, configurations 是从 config文件 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports.` 中读取出来的.

那么我们是否能够找到并查看这个文件呢? 是可以的.

先找到 `external libraries`, 接着找到一个叫 `org.springframework.boot:spring-boot-autoconfigure:3.x.x` 的文件夹. 可以看到里面确实包含了这样一个文件. 其中的内容都是一些配置类的全名:
```java
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
...
```

### 3.2 条件注册
可以按照一定的条件决定是否注册为IOC容器的bean
作用：按照一定的条件进行判断，在满足给定条件后才会注册对应的bean对象到Spring IOC容器中。
位置：方法、类
举例如下:
名称| 作用
:-|:-
`@Conditional`| 本身是一个父注解，派生出大量的子注解：
`@ConditionalOnClass`| 判断环境中是否有对应字节码文件，才注册bean到IOC容器。
`@ConditionalOnMissingBean`| 判断环境中没有对应的bean（类型 或 名称） ，才注册bean到IOC容器。
`@ConditionalOnProperty`| 判断配置文件中有对应属性和值，才注册bean到IOC容器。

其他的 Conditional 注解示例:
![](image/2023-10-17-16-28-32.png)

## 4. 案例: 自定义 starter
在实际开发中，经常会定义一些公共组件，提供给各个项目团队使用。而在SpringBoot的项目中，一般会将这些公共组件封装为SpringBoot 的 starter。

先看通常的 starter 定义, 例如 `org.mybatis.spring.boot: mybatis-spring-boot-starter`, 主要的是有一个文件 pom.xml, 它定义了需要的依赖, 另外与之配对的就是自动配置类: `org.mybatis.spring.boot: mybatis-spring-boot-autoconfigure`, 它里面存放着 `.import` 文件.

一般而言, `spring-boot-starter` 在前面的是官方, 在后面的第三方.

需求: 自定义 `aliyun-oss-spring-boot-starter`, 完成阿里云OSS操作工具类 `AliyunOSSUtils` 的自动配置.
目标: 引入起步依赖引入之后, 要想使用阿里云OSS, 注入 `AliyunOSSUtils` 直接使用即可.
* 创建 `aliyun-oss-spring-boot-starter` 模块
* 创建 `aliyun-oss-spring-boot-autoconfigure` 模块，在starter中引入该模块
* 在 `aliyun-oss-spring-boot-autoconfigure` 模块中的定义自动配置功能，并定义自动配置文件 `META-INF/spring/xxxx.imports`

### 4.1 原始 AliyunOSSUtils 的用法过程
0. 引入依赖:
```xml
<dependency>
   <groupId>com.aliyun.oss</groupId>
   <artifactId>aliyun-sdk-oss</artifactId>
   <version>3.15.1</version>
</dependency>
```
1. 在配置文件配置连接四要素, 包括认证ID&Key和访问地址&文件夹.
```yml
aliyun:
  oss:
    endpoint: https://oss-cn-beijing.aliyuncs.com/
    accessKeyId: xxx
    accessKeySecret: xxx
    bucketName: rainbow-tlias
```
2. 通过 `@Value(${property-name})` 将值注入到本地变量
3. 编写文件上传逻辑.
4. 加入 `@Component` 注解交由 bean 管理.
5. 在使用时进入注入.

### 4.2 starter 配置.
分别创建 `aliyun-oss-spring-boot-starter` 模块 和 `aliyun-oss-spring-boot-autoconfigure` 模块, 在 starter 中引入 autoconfigure 依赖, starter 只保留 pom 文件, autoconfigure 中保留源代码文件夹以及pom文件.

`pom.xml(starter)` 参考
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.5</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-oss-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.aliyun.oss</groupId>
            <artifactId>aliyun-oss-spring-boot-autoconfigure</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```
autoconfigure 模块需要实现 AliyunOSSUtils 的功能, 也即上传文件并返回文件的公开资源路径(url). 同时还需要配置自动封装bean的文件. 更确切的说, starter 进行了依赖管理, autoconfigure 则实现了自动配置.

#### 4.3 autoconfigure 配置
先把 `AliOSSUtils.java` 和 `AliOSSProperties.java` 复制到源代码目录下, 将四要素配置到 `application..yml` 中, 接着由于需要自动注入, 则将这两个类的 @Component 注解均删去.

对于 AliOSSProperties, 也把 lombok 删除并实现 getter 和 setter.

随后实现配置类 `AliOSSConfiguration.java`, 首先需要一个 `@Configuration` 说明它是配置类, 接着实现一个 `@Bean` 方法, 它返回一个 `AliOSSUtils` 对象, 该对象将交给 SpringBoot IOC容器 管理.

然而, 一个问题在于, AliOSSProperties 不是 bean, 因而 AliOSSUtils 也不可以 Autowired. 这里就要使用另一个注解 `@EnableConfigurationProperties(AliOSSProperties.class)`, 它能够使 configuration-properties 类成为 bean.同时为 AliOSSUtils 的 properties 参数设置 getter setter. 为 properties 赋值.

**`AliOSSConfiguration`**
```java
package com.aliyun.oss;

@EnableConfigurationProperties(AliOSSProperties.class)
@Configuration
public class AliOSSConfiguration {
    @Bean
    public AliOSSUtils aliOSSUtils(AliOSSProperties properties){
        AliOSSUtils aliOSSUtils = new AliOSSUtils();
        aliOSSUtils.setProperties(properties);
        return aliOSSUtils;
    }
}
```
**`AliOSSProperties`**
```java
package com.aliyun.oss;

@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOSSProperties {
    private String endpoint;        // 服务器域名
    private String accessKeyId;     // 访问ID
    private String accessKeySecret; // 访问密钥
    private String bucketName;      // 数据桶名
    ...  // getter setter
}
```
**`AliOSSUtils`**
```java
package com.aliyun.oss;

public class AliOSSUtils {
    private AliOSSProperties properties;
    ...  // getter setter
    public String upload(MultipartFile file) throws IOException {
      // some code
    }
```
比较绕, 但总体来说, 通过 `EnableConfigurationProperties` 使得 AliOSSProperties 生效, 成为一个 bean. 随后在 `AliOSSConfiguration` 中又通过 `Bean` 方法, 将 AliOSSUtils 注册成为一个 bean. 注意注册的过程中使用到了 AliOSSProperties 对象.

### 4.4 编写自动配置文件
在 resource 文件夹下, 新建 `META-INF/spring/` 文件夹, 接着新建 `org.springframework.boot.autoconfigure.AutoConfiguration.imports.` 文件. 在里面写入要自动创建的 bean: `com.aliyun.oss.AliOSSConfiguration`. (依据AliOSSConfiguration, 紧接着 AliOSSUtils AliOSSProperties 也成为 bean)

### 4.5 测试

创建一个测试工程, 引入 `aliyun-oss-spring-boot-starter`, 在测试类中测试 AliOSSConfiguration 等的可用性.
```java
@SpringBootTest
class AliyunOssTestApplicationTests {
    @Autowired
    AliOSSProperties p;
    @Autowired
    private ApplicationContext context;
    @Test
    void testAliConfig() {
        System.out.println(context.getBean(AliOSSConfiguration.class));
    }
    @Test
    void testAliUtil() {
        System.out.println(context.getBean(AliOSSUtils.class));
    }
    @Test
    void testAliProp() {
        System.out.println(context.getBean(AliOSSProperties.class));
        System.out.println(p.getEndpoint());
    }
}
```
全部测试成功!

### 4.6 工程重组
使用重建好的 starter 工程, 将原有的删除(包括两个java文件, yml的配置, pom中的依赖). 随后引入 starter 依赖, maven 刷新, 惊讶的发现其他的代码无需变动.


