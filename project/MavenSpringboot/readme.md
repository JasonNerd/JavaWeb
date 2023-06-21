# EmplistA
在前面的前端学习中, 我们学习了 vue 框架 和 axios 技术, 并在离线(也即使用vue.js而非vue脚手架)的情况下通过向测试服务器(Yapi)请求数据得以将网页的表格完整渲染和展示.

在本模块中, 我们尝试将前端的vue, axios技术结合进来, 使用离线的 xml 员工数据, 通过本地的 springboot 后台服务将网页表格渲染起来, 主要的工作在于如何将二者结合以及后端需要做的工作

## steps
* 在 pom.xml 引入 dom4j 依赖 (2.1.3)
* 编写 XMLParseUtils.java, 解析 emp.xml 为 Emp 实体(列表)
* 完成 EmplistApplication.java, 将请求的数据处理好返回给前端, 首次运行注意还需要编写 Emp 实体类, 响应实体类(统一的Result)
* 引入 html, css, js 等静态文件, 利用 axios, vue 进行数据请求和响应数据的展示


# EmplistB
在EmplistB中, 我们成功利用 springboot 使本机作为Web服务器 并响应前端的数据请求. 那么, 从后端代码编写来看, 它还不够优雅, 主要原因是他把所有的操作例如接受请求、查询原始数据、处理得到响应数据以及返回响应数据都放在了一起，**不便于扩展和维护。**

**想一想计算机网络的分层协议**, 这里我们借鉴这一思想, 将功能进行分层, 交给不同的包进行处理, 通常的, 我们有:
`controller`: `控制层`, 接收前端发送的请求，对请求进行处理，并响应数据
`service`: `业务逻辑层`, 处理具体的业务逻辑
`dao`: 数据访问层(Data Access 0bject)(`持久层`)，负责数据访问操作，包括数据的增、删、改、查

## steps
在 EmplistA 的基础上, 先注释(或删除)掉原先的接收、处理、响应的代码;
接着将代码实际执行的功能进行划分
* 增加 `dao` 包, 编写 `empDao` 接口, 该接口具有一个方法, 它返回查询得到的 empList.
* 增加 `dao.impl` 包, 将原 EmplistA 关于访问 xml 获得原始数据的 代码作为 `empDao` 的一个实现
* 增加 `service` 包, 编写 `empService` 接口, 该接口具有一个方法, 它返回经过处理的作为响应数据(的一部分)的 empList.
* 增加 `service.impl` 包, 将原 EmplistA 关于 将原emp列表的数字转义 代码作为 `empService` 的一个实现
* 在新的 `EmplistController` 中, 明确请求路径、请求参数, 调用 `empService` 获取得到处理数据, 封装为 `Result` 返回响应数据.
* 访问网页, 查看表格是否正常显示

# EmplistB2
在 EmplistB 的介绍中, 我们清楚的理解了分层的思想和一个简单的案例实现, 然而, 一个值得注意的细节是:

`empService`为了使用 `empDao` 的数据查询功能, 需要实例化 `empDao` 的一个具体实现, 例如 `empDaoA`.

`EmplistController`为了使用`empService`的服务以获取相应具体数据, 需要实例化 `empService` 的一个具体实现, 例如 `empServiceA`.

因此, 这三层之间具有两层的耦合关系, 假设`empService`需要使用另一个`empDao`实体, 例如`empDaoB`, 那么需要对`empService`的代码进行修改, 同样 `EmplistController` 需要其他服务时, 也需要修改自己的代码.

## Java Bean
* **控制反转: `Inversion of Control`**, 简称 `IC`。对象的创建控制权由程序自身转移到**外部(容器)**，这种思想称为控制反转.
* **依赖注入: `Dependency Injection`**，简称`DI`。容器为应用程序提供运行时所依赖的资源，称之为依赖注入。
* **Bean对象**: IOC容器中创建、管理的对象，称之为bean。

## 解耦合
上面提到的思想, 也即借助于媒介使得对象创建交由媒介进行, 实现了解耦合.具体的我们通过 **注解: `@Component`, `@Autowired`** 来完成这一功能
* `EmplistController` 需要 `empService` 对象, 因此在声明语句前加上 `@Autowired`, 表示请 IOC容器提供这个bean对象;

* `empService` 作为一个元素被 `EmplistController` 使用, 因此在类创建语句前添加注解 `@Component`, 表示将自己对象的创建交由IOC容器管理; 另一方面它还使用了 `empDao` 对象, 这需要IOC容器提供, 因此在声明语句前加上 `@Autowired`;

* `empDao` 作为一个元素被 `empService` 使用, 因此在类创建语句前添加注解 `@Component`, 表示将自己对象的创建交由IOC容器管理;

### @Component
`@Component` 是声明bean的基础注解, 鉴于分层思想的考虑, 我们把不同层bean的注解进行了重命名以优化代码可读性:
`@Controller`, 标注在控制器类上
`@Service`, 标注在业务类上
`@Repository`, 标注在数据访问类上(由于与mybatis整合，用的少)

值得注意的是, Javabean 具有包扫描机制, 对于在 Application 之外的bean, 无法直接简单的通过注解引入, 因此, 需要养成良好编程习惯, 在主程序所在的包内创建子包(**@SpringBootApplication具有包扫描作用，默认扫描当前包及其子包**)

### @Autowired
`@Autowired` 会自动为当前声明的变量分配对象实体, 该对象从JavaBean容器获得, 然而, 当具有多种同类型的bean时, 这将产生冲突. 解决方案是:

若不改变需要依赖注入的类, 那么对于这多个同类型的bean
1. 只在一种bean的类前加@Component系列的注解
2. 所有的bean都加@Component系列的注解, 表示可以交由容器保管, 然而, 只在其中一个类加 **`@primary`** 注解, 表示优先注入该bean的实现

继续思考, 如果出现多对多的情况(例如有多个 controller, 它们使用不尽相同的service), 以上方法又变的不适用了, 这里解决方案是:

**所有的bean都加`@Component`系列的注解, 表示可以交由容器保管, 并且, 在需要bean注入的类中, 加入 `@Resource(name="xxxx")` 注解指定bean**