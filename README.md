# 高校教务管理系统

基于 `Spring Boot + Thymeleaf + MySQL` 实现的高校教务管理系统，覆盖学生学籍、课程选课、成绩评价、公告通知、请假审批与首页数据看板等典型教务场景，适合作为 Java Web 课程设计、毕业设计或综合项目展示。

## 项目简介

本项目采用 `Spring Boot` 单体式架构开发，后端使用 `Spring MVC + Spring Data JPA`，前端使用 `Thymeleaf` 服务端模板渲染，数据库使用 `MySQL`。系统围绕高校常见教务业务流程展开，实现了从账号认证、学籍维护、课程管理到成绩统计、公告通知和请假审批的一体化功能。

## 功能模块

### 1. 账户权限与安全模块

- 支持管理员、教师、学生三种角色
- 登录注册与会话认证
- 基于角色的菜单显示与访问控制
- 密码使用 BCrypt 加密存储

### 2. 智能教务管理模块

- 课程档案管理
- 课程容量管理
- 排课信息维护
- 学生在线选课
- 课程时间冲突校验

### 3. 学籍信息数字化模块

- 学生档案管理
- 学号、专业、班级、入学日期维护
- 学籍状态管理
- 学籍异动历史记录

### 4. 成绩与评价量化模块

- 平时成绩、期中成绩、期末成绩录入
- 总评加权计算
- GPA 自动统计
- 班级排名分析

### 5. 综合互动服务模块

- 公告通知发布与阅读
- 学生请假申请
- 教师 / 管理员审批
- 首页统计卡片与数据看板

## 技术栈

- Java 17
- Spring Boot 3
- Spring MVC
- Thymeleaf
- Spring Data JPA
- MySQL 8
- Maven
- HTML / CSS

## 系统架构

项目采用典型的 MVC 三层结构：

- `Controller`：处理请求分发与页面跳转
- `Service`：处理业务逻辑
- `Repository`：负责数据库访问
- `Model`：实体类映射数据库表
- `Templates`：Thymeleaf 页面模板

整体属于：

- 单体架构
- MVC 分层架构
- 服务端渲染架构
- 基于角色的权限控制系统

## 项目结构

```text
student-management-system
├─ src
│  └─ main
│     ├─ java
│     │  └─ com.example.studentms
│     │     ├─ config
│     │     ├─ controller
│     │     ├─ dto
│     │     ├─ model
│     │     ├─ repository
│     │     ├─ service
│     │     └─ StudentManagementSystemApplication.java
│     └─ resources
│        ├─ static
│        ├─ templates
│        └─ application.properties
├─ scripts
│  ├─ start-mysql.ps1
│  └─ stop-mysql.ps1
├─ demo-seed.sql
├─ pom.xml
└─ README.md
```

## 项目亮点

- 不只是基础学生 CRUD，而是扩展成较完整的教务业务系统
- 同时覆盖学籍、课程、成绩、公告、请假五大模块
- 支持角色权限控制与动态菜单渲染
- 支持成绩加权、GPA 统计、排名分析等量化能力
- 首页集成统计卡片、进度条和业务看板，更适合项目展示

## 运行环境

- JDK 17
- Maven 3.9+
- MySQL 8

## 本地运行

### 1. 启动 MySQL

```powershell
powershell -ExecutionPolicy Bypass -File "D:\java web\student-management-system\scripts\start-mysql.ps1"
```

### 2. 启动项目

```powershell
cd "D:\java web\student-management-system"
mvn spring-boot:run
```

如果 `8080` 端口被占用，可以改为：

```powershell
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### 3. 访问系统

- 登录页：`http://localhost:8080/login`
- 首页：`http://localhost:8080/dashboard`

## 默认演示账号

- 管理员：`admin / admin123`
- 教师：`teacher / teacher123`
- 学生：`student / student123`

## 数据库说明

默认数据库名：

```text
student_management_system
```

当前数据库连接配置采用环境变量优先方式：

```properties
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:123456}
```

如需调整数据库账号密码，可修改本地环境变量或 `application.properties`。

## 页面展示建议

如果你准备把仓库做成作品集，建议补充以下页面截图：

- 登录页
- 首页总览
- 学籍信息页
- 课程与选课页
- 成绩评价页
- 公告通知页

## 后续可扩展方向

- 接入 JWT 认证
- 导出 Excel / PDF 报表
- 增加分页与条件搜索
- 增加头像上传与文件存储
- 改造成 Vue / React 前后端分离版本

## 适用场景

- Java Web 课程设计
- Spring Boot 实训项目
- 毕业设计展示
- 个人作品集仓库

## 开源说明

本项目主要用于学习交流与项目展示，欢迎基于此继续完善和扩展。
