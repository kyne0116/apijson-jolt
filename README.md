# APIJSON学生家长管理系统

<div align="center">

![APIJSON Logo](https://github.com/Tencent/APIJSON/raw/master/assets/logo.png)

**基于APIJSON框架的零代码CRUD演示系统**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![APIJSON](https://img.shields.io/badge/APIJSON-7.1.0-blue.svg)](https://github.com/Tencent/APIJSON)
[![MySQL](https://img.shields.io/badge/MySQL-8.4.3-orange.svg)](https://dev.mysql.com/downloads/)
[![ECharts](https://img.shields.io/badge/ECharts-5.4.2-purple.svg)](https://echarts.apache.org/)
[![JOLT](https://img.shields.io/badge/JOLT-0.1.1-yellow.svg)](https://github.com/bazaarvoice/jolt)
[![License](https://img.shields.io/badge/License-Apache%202.0-red.svg)](http://www.apache.org/licenses/LICENSE-2.0)

</div>

## 📋 项目概述

本项目是一个基于**APIJSON框架**的学生家长管理系统，展示了如何使用APIJSON实现**零代码CRUD操作**。通过简单的JSON配置，即可实现完整的增删改查功能，支持复杂的关联查询和权限控制。

**🆕 重大更新**: 
- 🎯 **完整数据流演示**: 实现了从数据库→APIJSON扁平化→JOLT转换→ECharts图表的完整数据链路
- 📊 **零代码可视化**: 通过配置实现数据可视化，无需编写图表代码
- 🔄 **实时数据转换**: 集成JOLT引擎，支持复杂的JSON数据结构转换
- 📈 **多图表支持**: 提供柱状图、饼图、折线图等多种ECharts图表类型

## ✨ 主要特性

- 🚀 **零代码开发** - 无需编写任何业务代码，通过JSON配置实现CRUD
- 🔐 **权限控制** - 基于角色的访问控制，支持表级和字段级权限
- 🔗 **关联查询** - 支持多表JOIN查询、嵌套对象查询、数组关联查询
- ✅ **请求验证** - 自动参数验证，SQL注入防护，数据格式校验
- 🎯 **RESTful API** - 标准的HTTP API接口，支持JSON数据格式
- 📱 **Web演示界面** - 完整的前端演示页面，展示所有功能特性
- 📊 **数据可视化** - 基于ECharts的图表展示，支持柱状图、饼图、折线图
- 🔄 **JOLT数据转换** - JSON到JSON的数据格式转换，支持复杂的数据结构变换

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | JDK 17 | 运行环境 |
| Spring Boot | 3.2.5 | Web框架 |
| APIJSON | 7.1.0 | 核心框架 |
| MySQL | 8.4.3 | 数据库 |
| Maven | 3.9+ | 构建工具 |
| Bootstrap | 5.1.3 | 前端UI框架 |
| ECharts | 5.4.2 | 图表可视化库 |
| JOLT | 0.1.1 | JSON转换引擎 |

## 🗄️ 数据库设计

### 表结构关系图

```
┌─────────────────┐         ┌─────────────────┐
│     Student     │         │     Parent      │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄───────┐│ id (PK)         │
│ student_no      │        ││ student_id (FK) │
│ name            │        └┤ name            │
│ gender          │         │ relationship    │
│ age             │         │ phone           │
│ grade           │         │ email           │
│ class_name      │         │ occupation      │
│ phone           │         │ ...             │
│ email           │         └─────────────────┘
│ address         │
│ status          │
│ create_time     │
│ update_time     │
└─────────────────┘
```

### 核心表说明

#### Student表（学生信息）
- **主键**: `id` - 学生唯一标识
- **业务字段**: 学号、姓名、性别、年龄、年级、班级等
- **关联**: 一个学生可以有多个家长

#### Parent表（家长信息）
- **主键**: `id` - 家长唯一标识
- **外键**: `student_id` - 关联学生表
- **业务字段**: 姓名、关系、联系方式、职业等

## 🚀 快速开始

### 环境准备

1. **Java环境**: JDK 17+
2. **数据库**: MySQL 8.0+
3. **构建工具**: Maven 3.6+

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd apijson-jolt
```

2. **启动MySQL服务**
```bash
# 确保MySQL服务运行在localhost:30004
# 用户名: root
# 密码: Best@2008
# 数据库: apijson
```

3. **创建数据库表**
```bash
# 运行数据库初始化工具
mvn compile exec:java -Dexec.mainClass="apijson.boot.DatabaseTest"
mvn compile exec:java -Dexec.mainClass="apijson.boot.CreateAccessTables"
```

4. **启动应用**
```bash
mvn spring-boot:run
```

5. **访问应用**
- **API服务**: http://localhost:8080
- **数据管理页面**: http://localhost:8080/student-parent-demo.html
- **图表可视化页面**: http://localhost:8080/charts.html

## 🌐 系统页面

### 📊 数据管理页面
**访问地址**: [http://localhost:8080/student-parent-demo.html](http://localhost:8080/student-parent-demo.html)

这是主要的数据管理界面，提供完整的CRUD操作功能：

#### 核心功能
- **学生管理**: 增删改查学生信息，支持条件查询和统计
- **家长管理**: 管理学生家长信息，支持关系设定
- **关联查询**: 演示多表联合查询功能
- **数据统计**: 实时数据分析和统计展示
- **API演示**: 实时显示API请求和响应内容

### 📈 图表可视化页面  
**访问地址**: [http://localhost:8080/charts.html](http://localhost:8080/charts.html)

这是新增的数据可视化界面，展示APIJSON + JOLT + ECharts的完整集成：

#### 图表类型
- **📊 柱状图**: 学生年级分布统计
- **🥧 饼图**: 学生性别比例分析  
- **📈 折线图**: 学生年龄分布趋势
- **📋 数据概览**: 关键指标实时统计

#### 技术特点
- **实时数据**: 从APIJSON API实时获取数据
- **JOLT转换**: 自动将APIJSON格式转换为ECharts格式
- **响应式设计**: 支持各种屏幕尺寸
- **交互功能**: 点击图表元素查看详细信息

#### 数据流程
```
MySQL数据库 → APIJSON查询 → JOLT数据转换 → ECharts图表渲染
```

## 🎯 API接口说明

### 基础端点

| 方法 | 端点 | 说明 |
|------|------|------|
| POST | `/get` | 查询数据 |
| POST | `/post` | 创建数据 |
| POST | `/put` | 更新数据 |
| POST | `/delete` | 删除数据 |
| POST | `/head` | 统计数量 |

### 🎯 完整数据流演示端点 (新增)

| 方法 | 端点 | 说明 |
|------|------|------|
| GET | `/dataflow/status` | 数据流系统状态检查 |
| GET | `/dataflow/grade-distribution` | 年级分布完整数据流演示 |
| GET | `/dataflow/gender-distribution` | 性别分布完整数据流演示 |
| GET | `/dataflow/age-distribution` | 年龄分布完整数据流演示 |
| POST | `/dataflow/test-jolt-transform` | JOLT转换功能测试 |

### 数据流示例
```bash
# 获取年级分布的完整数据流演示
curl http://localhost:8080/dataflow/grade-distribution

# 返回完整的数据流步骤：
{
  "success": true,
  "step1_raw_data": [{"grade":"七年级","count":1}],
  "step2_apijson_format": {"Student[]":[{"grade":"七年级","count":1}]},
  "step3_jolt_spec": [{"operation":"shift","spec":{"Student\\[\\]":{"*":{"grade":"categories[]","count":"values[]"}}}}],
  "step3_echarts_data": {"categories":["七年级"],"values":[1]},
  "step4_echarts_config": {...},
  "dataflow": "数据库 → APIJSON格式 → JOLT转换 → ECharts配置"
}
```

### 使用示例

#### 1. 查询所有学生
```bash
curl -X POST http://localhost:8080/get \
  -H "Content-Type: application/json" \
  -d '{"Student":{}}'
```

#### 2. 按条件查询学生
```bash
curl -X POST http://localhost:8080/get \
  -H "Content-Type: application/json" \
  -d '{
    "Student[]": {
      "Student": {
        "grade": "九年级",
        "@order": "age-"
      }
    }
  }'
```

#### 3. 联表查询：学生及其家长
```bash
curl -X POST http://localhost:8080/get \
  -H "Content-Type: application/json" \
  -d '{
    "Student": {"id": 1},
    "Parent[]": {
      "Parent": {"student_id": 1}
    }
  }'
```

#### 5. JOLT数据转换示例
```bash
# 将年级分布数据转换为ECharts格式
curl -X POST http://localhost:8080/jolt/grade-distribution \
  -H "Content-Type: application/json" \
  -d '{
    "Student[]": [
      {"grade": "七年级", "count": 3},
      {"grade": "八年级", "count": 2}
    ]
  }'

# 返回结果：
{
  "success": true,
  "data": {
    "categories": ["七年级", "八年级"],
    "values": [3, 2]
  }
}
```

#### 6. 获取JOLT服务信息
```bash
curl http://localhost:8080/jolt/info
```

## 🎨 Web演示界面

### 主要功能页面

#### 📊 数据管理界面
访问 [http://localhost:8080/student-parent-demo.html](http://localhost:8080/student-parent-demo.html) 体验完整的CRUD功能演示。

#### 📈 图表可视化界面  
访问 [http://localhost:8080/charts.html](http://localhost:8080/charts.html) 体验ECharts数据可视化功能。

**页面间导航**: 两个页面提供相互跳转链接，方便在数据管理和图表展示之间切换。

### 界面功能特性

#### 📊 学生管理模块
- ✅ 查询所有学生信息
- ✅ 按ID/年级条件查询  
- ✅ 学生数量统计
- ✅ 添加新学生
- ✅ 编辑学生信息
- ✅ 删除学生记录

#### 👨‍👩‍👧‍👦 家长管理模块
- ✅ 查询所有家长信息
- ✅ 按学生ID查询家长
- ✅ 查询紧急联系人
- ✅ 添加新家长
- ✅ 编辑家长信息
- ✅ 删除家长记录

#### 📊 数据可视化模块 (新增)
- ✅ 学生年级分布柱状图
- ✅ 学生性别分布饼图  
- ✅ 学生年龄分布折线图
- ✅ 关键指标实时统计
- ✅ JOLT数据转换演示
- ✅ 图表交互和数据钻取
- ✅ 响应式图表设计

#### 🔄 JOLT转换功能
- ✅ APIJSON到ECharts数据格式转换
- ✅ 多种转换规则支持
- ✅ 转换过程可视化演示
- ✅ 错误处理和回退机制

## 📁 项目结构

```
apijson-jolt/
├── src/main/java/apijson/boot/
│   ├── DemoApplication.java         # Spring Boot启动类
│   ├── DemoController.java          # API控制器
│   ├── DataFlowDemoController.java  # 数据流演示控制器 (新增)
│   ├── DatabaseInitializer.java     # 数据库初始化器 (新增)
│   ├── DemoSQLConfig.java          # 数据库配置
│   ├── DemoParser.java             # APIJSON解析器
│   ├── DemoVerifier.java           # 权限验证器
│   ├── jolt/
│   │   └── JoltController.java     # JOLT转换控制器 (新增)
│   ├── DatabaseTest.java           # 数据库测试工具
│   └── CreateAccessTables.java     # 配置表创建工具
├── src/main/resources/
│   ├── application.properties       # 应用配置
│   ├── static/
│   │   ├── student-parent-demo.html # 数据管理演示页面
│   │   ├── charts.html             # 图表可视化页面 (新增)
│   │   └── js/
│   │       ├── student-parent-demo.js # 数据管理脚本
│   │       └── charts.js           # 图表功能脚本 (新增)
├── pom.xml                         # Maven配置
├── README.md                       # 项目说明
└── .gitignore                      # Git忽略文件
```

## 🔧 配置说明

### 数据库配置
```properties
# MySQL数据库配置
spring.datasource.url=jdbc:mysql://localhost:30004/apijson?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=Best@2008

# APIJSON配置
apijson.datasource.url=jdbc:mysql://localhost:30004/apijson?useSSL=false&serverTimezone=UTC
apijson.datasource.username=root
apijson.datasource.password=Best@2008
apijson.datasource.schema=apijson
```

### 权限配置
系统通过`Access`表配置API访问权限：
- **UNKNOWN**: 未登录用户
- **LOGIN**: 已登录用户  
- **ADMIN**: 管理员用户

### 请求验证配置
系统通过`Request`表配置API请求结构验证，确保数据安全性。

## 📊 测试数据

系统自动初始化以下测试数据：

### 学生数据（5条记录）
| ID | 学号 | 姓名 | 年龄 | 年级 | 班级 |
|----|------|------|------|------|------|
| 1 | S2024001 | 张小明 | 15 | 九年级 | 九年级(1)班 |
| 2 | S2024002 | 李小红 | 14 | 八年级 | 八年级(2)班 |
| 3 | S2024003 | 王小强 | 16 | 九年级 | 九年级(2)班 |
| 4 | S2024004 | 刘小美 | 13 | 七年级 | 七年级(1)班 |
| 5 | S2024005 | 陈小华 | 15 | 九年级 | 九年级(1)班 |

### 家长数据（9条记录）
包含不同学生的父亲、母亲等角色，建立完整的外键关联关系。

## 🌟 APIJSON核心特性展示

### 1. 零代码CRUD
```json
// 无需编写Controller代码，直接通过JSON实现CRUD
{
  "Student": {
    "name": "新学生",
    "age": 16,
    "grade": "十年级"
  }
}
```

### 2. 动态查询
```json
// 支持复杂的动态查询条件
{
  "Student[]": {
    "Student": {
      "age{}": [14, 16],
      "grade": "九年级",
      "@column": "id,name,age",
      "@order": "age+",
      "@count": 10,
      "@page": 0
    }
  }
}
```

### 3. 关联查询
```json
// 一次请求获取学生及其所有家长信息
{
  "Student": {
    "id": 1
  },
  "Parent[]": {
    "Parent": {
      "student_id": 1,
      "@order": "relationship+"
    }
  }
}
```

### 4. 字段控制
```json
// 精确控制返回字段，减少数据传输
{
  "Student": {
    "@column": "id,name,grade,age",
    "grade": "九年级"
  }
}
```

### 5. 权限控制
```sql
-- 通过Access表配置不同角色的访问权限
INSERT INTO Access (name, get, post, put, delete) VALUES 
('Student', '["UNKNOWN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]');
```

## 🧪 功能测试

### 自动化测试
```bash
# 运行数据库连接测试
mvn compile exec:java -Dexec.mainClass="apijson.boot.DatabaseTest"
```

### 手动测试
1. **数据管理功能**: 访问 http://localhost:8080/student-parent-demo.html
   - 测试各项CRUD功能
   - 验证关联查询功能
   - 检查API请求响应格式

2. **图表可视化功能**: 访问 http://localhost:8080/charts.html
   - 测试图表数据加载
   - 验证JOLT数据转换
   - 检查图表交互功能

3. **完整数据流演示**: 访问数据流API端点
   - 测试数据库到图表的完整流程
   - 验证每个步骤的数据转换
   - 检查APIJSON→JOLT→ECharts集成

## ❓ 常见问题

### Q1: 应用启动失败怎么办？
**A**: 检查以下几点：
- 确认MySQL服务正常运行且端口为30004
- 验证数据库用户名密码是否正确
- 确保已创建apijson数据库

### Q2: 演示页面无法访问？
**A**: 检查：
- Spring Boot应用是否成功启动
- 数据管理页面访问地址：http://localhost:8080/student-parent-demo.html
- 图表页面访问地址：http://localhost:8080/charts.html
- 浏览器控制台是否有JavaScript错误

### Q3: 图表无法显示数据？
**A**: 检查：
- 数据库是否有学生数据
- JOLT转换服务是否正常运行
- 浏览器网络面板是否有API请求错误
- ECharts库是否正确加载

### Q4: JOLT转换失败？
**A**: 检查：
- JOLT依赖是否正确添加到pom.xml
- APIJSON数据格式是否正确
- 查看后端日志中的JOLT转换错误信息

### Q5: API请求返回权限错误？
**A**: 检查：
- Access表是否正确配置
- 请求的表名和字段是否存在
- 用户角色权限是否足够

### Q6: 如何添加新的表？
**A**: 按以下步骤：
1. 创建数据库表
2. 在Access表中添加权限配置  
3. 在Request表中添加请求验证配置
4. 重启应用使配置生效

### Q7: 如何添加新的图表类型？
**A**: 按以下步骤：
1. 在JoltController中添加新的转换方法
2. 在charts.js中添加对应的数据处理逻辑
3. 在charts.html中添加图表容器和配置

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交变更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开Pull Request

## 📄 许可证

本项目基于 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 开源协议。

## 🔗 相关链接

- [APIJSON官方文档](https://github.com/Tencent/APIJSON)
- [APIJSON在线测试](http://apijson.cn/api)
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [ECharts官方文档](https://echarts.apache.org/)
- [JOLT转换文档](https://github.com/bazaarvoice/jolt)

---

<div align="center">

**⭐ 如果这个项目对您有帮助，请给个Star支持一下！**

**🎯 体验地址**
- [数据管理页面](http://localhost:8080/student-parent-demo.html)
- [图表可视化页面](http://localhost:8080/charts.html)

Made with ❤️ using APIJSON + JOLT + ECharts

</div>