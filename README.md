# 高校教务管理系统

基于 `Spring Boot + Thymeleaf + MySQL` 实现的 Java Web 教务管理系统，围绕学生学籍、课程选课、成绩评价、公告通知、请假审批等业务场景进行设计与实现，适合作为课程设计、毕业设计或 Java Web 综合项目参考。

## 项目特点

- 多角色权限控制：管理员、教师、学生
- 学籍信息管理：学生档案、状态管理、异动记录
- 教务管理：课程档案、排课信息、在线选课
- 成绩评价：成绩录入、加权计算、GPA 与排名统计
- 综合服务：公告通知、请假审批、首页数据看板
- 数据持久化：基于 MySQL + Spring Data JPA

## 技术栈

- Java 17
- Spring Boot 3
- Spring MVC
- Thymeleaf
- Spring Data JPA
- MySQL 8
- Maven

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

## 功能模块

### 1. 账户权限与安全模块

- 登录与注册
- 管理员 / 教师 / 学生三角色
- 基于会话的访问控制
- 密码使用 BCrypt 加密存储

### 2. 智能教务管理模块

- 课程档案管理
- 课程容量统计
- 排课信息维护
- 学生在线选课与冲突校验

### 3. 学籍信息数字化模块

- 学生基础档案管理
- 入学日期、班级、专业、照片等信息维护
- 学籍状态与异动记录

### 4. 成绩与评价量化模块

- 平时 / 期中 / 期末成绩录入
- 总评加权计算
- GPA 自动统计
- 班级排名分析

### 5. 综合互动服务模块

- 公告通知发布与阅读
- 请假申请与审批
- 首页统计卡片与可视化看板

## 本地运行

### 1. 启动 MySQL

```powershell
powershell -ExecutionPolicy Bypass -File "D:\java web\student-management-system\scripts\start-mysql.ps1"
```

### 2. 启动 Spring Boot 项目

```powershell
cd "D:\java web\student-management-system"
mvn spring-boot:run
```

如果 `8080` 端口被占用，可改用：

```powershell
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### 3. 访问地址

- 登录页：`http://localhost:8080/login`
- 首页：`http://localhost:8080/dashboard`

## 默认演示账号

- 管理员：`admin / admin123`
- 教师：`teacher / teacher123`
- 学生：`student / student123`

## 数据库配置

默认数据库名：

```text
student_management_system
```

`application.properties` 当前使用环境变量优先方式：

```properties
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:123456}
```

如需修改数据库账号密码，可直接调整本地环境变量或配置文件。

## 开源说明

本项目主要用于学习交流与课程设计展示，欢迎基于此继续扩展：

- 导出 PDF / Excel 报表
- 更完整的 RBAC 权限模型
- JWT 登录认证
- 分页查询与条件搜索
- Vue / React 前后端分离版本
