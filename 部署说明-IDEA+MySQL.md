# IDEA + MySQL 部署说明

这是提供给老师或同学的快速部署说明。

## 一、准备环境

需要先安装：

- IntelliJ IDEA
- JDK 17
- Maven
- MySQL 8
- Navicat（可选）

## 二、导入数据库

项目附带数据库导入文件：

- `sql/student_management_system.sql`

导入方法：

1. 打开 Navicat，连接 MySQL
2. 新建或准备数据库 `student_management_system`
3. 执行 `student_management_system.sql`

## 三、导入 IDEA

1. 打开 IDEA
2. 选择 `Open`
3. 打开项目根目录 `student-management-system`
4. 等待 Maven 依赖下载完成

## 四、检查数据库配置

数据库配置文件：

- `src/main/resources/application.properties`

默认配置：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_management_system?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123456
```

如果电脑上的数据库账号密码不同，请自行修改。

## 五、启动项目

在 IDEA 中运行：

- `StudentManagementSystemApplication.java`

启动成功后访问：

- `http://localhost:8080/login`

如果端口冲突，可改用：

- `http://localhost:8081/login`

## 六、默认账号

- 管理员：`admin / admin123`
- 教师：`teacher / teacher123`
- 学生：`student / student123`
