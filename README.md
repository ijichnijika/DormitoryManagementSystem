# 🏠 宿舍管理系统 (Dormitory Management System)

一个基于 Spring Boot 3 + Vue 3 的现代化宿舍管理系统，提供完整的宿舍信息管理、学生管理、卫生检查等功能。

## ✨ 功能特性

- 🔐 **用户认证与授权**：基于 JWT 的安全认证机制，支持多角色权限管理
- 👥 **用户管理**：完整的用户增删改查、角色分配、批量导入等功能
- 🏢 **宿舍管理**：楼栋、房间信息管理，支持级联选择
- 🧹 **卫生检查**：卫生检查记录管理，支持查询和统计
- 📋 **班级管理**：班级信息维护
- 📊 **数据统计**：多维度数据统计与报表
- 📱 **响应式设计**：支持桌面端和移动端访问

## 🛠️ 技术栈

### 后端
- **框架**：Spring Boot 3.5.8
- **Java 版本**：JDK 17
- **安全**：Spring Security + JWT
- **数据库**：MySQL + MyBatis
- **缓存**：Redis
- **API 文档**：Knife4j (Swagger)
- **工具库**：Hutool、Lombok、PageHelper

### 前端
- **框架**：Vue 3
- **构建工具**：Vite
- **HTTP 客户端**：Axios
- **UI 组件**：Element Plus
- **路由**：Vue Router
- **状态管理**：Pinia

## �️ 数据库设计

### 数据库名称
`dormitory_db` (字符集：utf8mb4)

### 系统域 - 基础配置表

#### 1. sys_class - 班级信息表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| class_name | VARCHAR(64) | 班级名称（唯一） |
| counselor_name | VARCHAR(32) | 辅导员姓名 |
| created_at | DATETIME | 创建时间 |

#### 2. sys_building - 宿舍楼信息表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| building_name | VARCHAR(32) | 楼宇名称 |
| manager_name | VARCHAR(32) | 宿管姓名 |
| created_at | DATETIME | 创建时间 |
| deleted_at | DATETIME | 删除时间（软删除） |

#### 3. sys_room - 宿舍房间表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| building_id | BIGINT | 所属楼宇ID |
| room_number | VARCHAR(16) | 房间号 |
| capacity | INT | 床位容量（默认4） |
| gender | TINYINT | 性别限制：1-男寝，2-女寝 |
| created_at | DATETIME | 创建时间 |

**索引**：building_id + room_number 唯一索引

#### 4. sys_user - 系统用户表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| username | VARCHAR(32) | 账号（学号/工号，唯一） |
| password | VARCHAR(128) | 加密密码 |
| real_name | VARCHAR(32) | 真实姓名 |
| phone | VARCHAR(11) | 手机号 |
| email | VARCHAR(64) | 邮箱 |
| role | VARCHAR(128) | 角色（多角色逗号分隔） |
| class_id | BIGINT | 所属班级ID |
| room_id | BIGINT | 所属宿舍ID |
| status | TINYINT | 状态：1-正常，0-禁用 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |
| deleted_at | DATETIME | 删除时间（软删除） |

### 业务域 - 核心业务表

#### 5. biz_inspection - 卫生检查记录表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| room_id | BIGINT | 被检查宿舍ID |
| inspector_id | BIGINT | 检查员ID |
| modifier_id | BIGINT | 最后修改人ID |
| total_score | INT | 总分（0-100） |
| remarks | TEXT | 备注说明 |
| evidence_imgs | TEXT | 图片路径（逗号分隔） |
| check_date | DATE | 检查日期 |
| created_at | DATETIME | 录入时间 |
| updated_at | DATETIME | 最后修改时间 |

**约束**：total_score 取值范围 0-100

#### 6. biz_application - 检查员权限申请表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| applicant_id | BIGINT | 申请人ID |
| application_reason | VARCHAR(500) | 申请理由 |
| status | TINYINT | 状态：0-待审核，1-已通过，2-已驳回 |
| reviewer_id | BIGINT | 审核人ID |
| review_comment | TEXT | 审核意见 |
| review_time | DATETIME | 审核时间 |
| apply_time | DATETIME | 申请提交时间 |
| updated_at | DATETIME | 最后更新时间 |

### 数据库脚本位置
完整的建表脚本位于：[src/main/resources/sql/schema.sql](src/main/resources/sql/schema.sql)

## �📦 项目结构

```
DormitoryManagementSystem/
├── src/
│   ├── main/
│   │   ├── java/com/nijika/
│   │   │   ├── config/          # 配置类（安全、跨域、Knife4j等）
│   │   │   ├── controller/      # 控制器层
│   │   │   ├── entity/          # 实体类
│   │   │   ├── mapper/          # MyBatis Mapper
│   │   │   ├── service/         # 业务逻辑层
│   │   │   ├── dto/             # 数据传输对象
│   │   │   ├── vo/              # 视图对象
│   │   │   ├── utils/           # 工具类
│   │   │   └── exception/       # 异常处理
│   │   ├── resources/
│   │   │   ├── mapper/          # MyBatis XML 映射文件
│   │   │   ├── front-end/       # Vue 3 前端项目
│   │   │   └── application.yml  # Spring Boot 配置
│   │   └── webapp/
│   └── test/                    # 测试代码
├── pom.xml                      # Maven 配置
└── README.md                    # 项目说明
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+ (前端开发)

### 后端启动

1. **克隆项目**
```bash
git clone https://github.com/ijichnijika/DormitoryManagementSystem.git
cd DormitoryManagementSystem
```

2. **配置数据库**
- 创建数据库：
  ```sql
  CREATE DATABASE dormitory_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```
- 导入数据库脚本：`mysql -u root -p dormitory_db < src/main/resources/sql/schema.sql`
- 修改 `src/main/resources/application.yml` 中的数据库配置

3. **配置 Redis**
- 确保 Redis 服务运行
- 修改 `application.yml` 中的 Redis 配置

4. **启动项目**
```bash
mvn clean install
mvn spring-boot:run
```

5. **访问 API 文档**
- Knife4j: http://localhost:8080/doc.html

### 前端启动

1. **进入前端目录**
```bash
cd src/main/resources/front-end
```

2. **安装依赖**
```bash
npm install
```

3. **启动开发服务器**
```bash
npm run dev
```

4. **访问应用**
- 前端地址: http://localhost:5173

### 构建生产版本

**后端打包**
```bash
mvn clean package
java -jar target/nijika-0.0.1-SNAPSHOT.jar
```

**前端打包**
```bash
cd src/main/resources/front-end
npm run build
```

## 🔑 默认账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 系统管理员 |
| 老师 | T001 | 123456 | 老师 |
| 学生 | 20230102 | 123456 | 普通学生 |
| 检察员 | 20230101 | 123456 | 检察员 |


> **注意**：首次使用请及时修改默认密码！

## 📝 核心功能说明

### 用户管理
- 支持多角色：超级管理员、老师、学生
- 用户信息的增删改查
- 角色权限分配
- 批量导入用户

### 宿舍管理
- 楼栋管理：楼栋信息维护
- 房间管理：房间分配、容量管理
- 级联选择：楼栋-房间联动

### 卫生检查
- 检查记录录入
- 检查历史查询
- 个人检查记录
- 统计分析

## 🔧 配置说明

### 主要配置项 (`application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dormitory_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  
  redis:
    host: localhost
    port: 6379

server:
  port: 8080

jwt:
  secret: your_jwt_secret_key
  expiration: 86400000  # 24小时
```

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 开源协议

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 📧 联系方式

- 项目地址：https://github.com/ijichnijika/DormitoryManagementSystem
- 作者：ijichnijika（xyf）

## 🙏 致谢

感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [MyBatis](https://mybatis.org/)
- [Knife4j](https://doc.xiaominfo.com/)
- [Element Plus](https://element-plus.org/)

---

⭐ 如果这个项目对你有帮助，请给个 Star 吧！
