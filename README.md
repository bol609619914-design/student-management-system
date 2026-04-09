# 高校教务管理系统

基于 `Spring Boot + Thymeleaf + MySQL` 实现的高校教务管理系统，覆盖账户权限、学籍信息、课程选课、成绩评价、公告通知、请假审批等典型教务场景，适合作为 `Java Web` 课程设计、毕业设计或项目展示使用。

## 1. 项目简介

本项目采用 `Spring Boot` 单体架构开发，后端使用 `Spring MVC + Spring Data JPA`，前端使用 `Thymeleaf` 服务端模板渲染，数据库使用 `MySQL 8`。  
系统围绕高校教务核心流程展开，支持管理员、教师、学生三类角色协同使用。

## 2. 技术栈

- Java 17
- Spring Boot 3.3.5
- Spring MVC
- Spring Data JPA
- Thymeleaf
- MySQL 8
- Maven
- HTML / CSS

## 3. 功能模块

### 3.1 账户权限与安全模块
- 支持管理员、教师、学生三类角色
- 登录注册与会话认证
- 基于角色的菜单显示与访问控制
- 密码使用 BCrypt 加密存储
- 管理员可直接创建教师或学生账号

### 3.2 智能教务管理模块
- 课程档案管理
- 课程容量维护
- 排课信息维护
- 学生在线选课
- 排课冲突与选课冲突校验

### 3.3 学籍信息数字化模块
- 学生基础档案管理
- 学号、班级、专业、入学日期维护
- 学籍状态管理
- 学籍异动历史记录

### 3.4 成绩与评价量化模块
- 平时、期中、期末成绩录入
- 总评自动加权计算
- GPA 自动计算
- 学生排名分析
- 成绩单导出

### 3.5 综合互动服务模块
- 公告通知发布与查看
- 学生请假申请
- 教师 / 管理员在线审批
- 审批意见、审批人、审批时间记录
- 首页数据看板展示

## 4. 项目结构

```text
student-management-system
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com.example.studentms
│  │  │     ├─ config
│  │  │     ├─ controller
│  │  │     ├─ dto
│  │  │     ├─ model
│  │  │     ├─ repository
│  │  │     ├─ service
│  │  │     └─ StudentManagementSystemApplication.java
│  │  └─ resources
│  │     ├─ static
│  │     ├─ templates
│  │     └─ application.properties
├─ scripts
├─ sql
├─ pom.xml
├─ README.md
└─ CHANGELOG.md
```

## 5. IDEA + MySQL 部署方式

### 5.1 环境要求

- IntelliJ IDEA
- JDK 17
- Maven 3.9 及以上
- MySQL 8
- Navicat（可选，用于查看数据库）

### 5.2 第一步：导入数据库

项目已提供数据库导入文件：

- [student_management_system.sql](/D:/java%20web/student-management-system/sql/student_management_system.sql)

导入方式：

1. 打开 Navicat 并连接你的 MySQL
2. 新建数据库，名称建议为 `student_management_system`
3. 运行 `student_management_system.sql`

如果你想直接用命令行导入，也可以执行：

```bash
mysql -uroot -p < student_management_system.sql
```

### 5.3 第二步：导入 IDEA

1. 打开 IntelliJ IDEA
2. 选择 `Open`
3. 选择项目根目录 `student-management-system`
4. 等待 IDEA 自动识别为 Maven 项目并下载依赖

### 5.4 第三步：配置数据库连接

默认数据库配置在：

- [application.properties](/D:/java%20web/student-management-system/src/main/resources/application.properties)

默认连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_management_system?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:123456}
```

如果老师电脑上的 MySQL 用户名或密码不同，只需要修改：

- `spring.datasource.username`
- `spring.datasource.password`

### 5.5 第四步：启动项目

在 IDEA 中运行启动类：

- [StudentManagementSystemApplication.java](/D:/java%20web/student-management-system/src/main/java/com/example/studentms/StudentManagementSystemApplication.java)

也可以在终端中执行：

```bash
mvn spring-boot:run
```

如果 `8080` 端口被占用，可以改用：

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### 5.6 第五步：访问系统

- 登录页：`http://localhost:8080/login`
- 首页：`http://localhost:8080/dashboard`

如果改成 `8081`：

- 登录页：`http://localhost:8081/login`

## 6. 默认演示账号

- 管理员：`admin / admin123`
- 教师：`teacher / teacher123`
- 学生：`student / student123`

## 7. Navicat 连接参数

- 主机：`127.0.0.1`
- 端口：`3306`
- 用户名：`root`
- 密码：`123456`
- 数据库：`student_management_system`

## 8. 项目亮点

- 不只是基础学生 CRUD，而是扩展成较完整的教务业务系统
- 支持多角色权限与角色分流首页
- 支持课程冲突校验、成绩量化、请假审批闭环
- 首页带统计卡片、课程负载、成绩与公告数据展示
- 适合答辩演示、课程设计展示和 GitHub 作品集展示

## 9. 开源与说明

本项目主要用于学习交流、课程设计与项目展示，欢迎基于此继续完善和扩展。
