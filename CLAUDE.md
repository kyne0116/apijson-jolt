# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a comprehensive APIJSON-based student-parent management system that demonstrates the full spectrum of zero-code CRUD operations, complex aggregation queries, and multi-table join operations using the APIJSON framework. The project showcases advanced features including real-time statistics, data analysis, and complete REST API testing.

## Quick Start

### One-Click Setup
```bash
# Linux/macOS
./start.sh

# Windows  
start.bat

# Manual setup
mvn compile exec:java -Dexec.mainClass="apijson.boot.StudentParentDatabaseInit"
mvn spring-boot:run
```

### Access Points
- **API Base**: http://localhost:8080
- **Demo Page**: http://localhost:8080/student-parent-demo.html
- **Documentation**: 运行指南.md

## Development Commands

### Database Operations
```bash
# Initialize Student-Parent tables and test data
mvn compile exec:java -Dexec.mainClass="apijson.boot.StudentParentDatabaseInit"

# Legacy APIJSON table setup (if needed)
mvn compile exec:java -Dexec.mainClass="apijson.boot.DatabaseTest"
mvn compile exec:java -Dexec.mainClass="apijson.boot.CreateAccessTables"
```

### Build and Run
```bash
# Start the application
mvn spring-boot:run

# Compile the project
mvn compile

# Package the application
mvn package
```

### Testing
```bash
# Run complete API test suite
mvn test -Dtest=StudentParentApiTest

# Run all tests
mvn test
```

## Implemented Features

### 1. Student Single-Table Operations
- **Basic CRUD**: Create, Read, Update, Delete student records
- **Conditional Queries**: Filter by ID, grade, age range
- **Aggregation Queries**: Count, average, min/max calculations
- **Grouping & Statistics**: Group by grade with detailed statistics
- **Pagination & Sorting**: Multi-field sorting with pagination support

### 2. Complex Aggregation Queries
- **Grade Statistics**: Student count per grade with average age
- **Age Distribution**: Age-based student distribution analysis
- **Gender Analysis**: Male/female ratio across different grades
- **Multi-dimensional Analytics**: Combined statistics with multiple grouping criteria

### 3. Parent Management
- **Full CRUD Operations**: Complete parent information management
- **Relationship Tracking**: Track various family relationships (父亲、母亲、爷爷、奶奶等)
- **Emergency Contacts**: Dedicated emergency contact management
- **Occupation Analysis**: Professional distribution statistics

### 4. Multi-Table Join Operations

#### Simple Joins
- **Student-Parent Lookup**: Find all parents for a specific student
- **Parent-Student Lookup**: Find student information for a parent
- **Emergency Contact Queries**: List all emergency contacts system-wide

#### Complex Joins
- **Grade-based Emergency Contacts**: Find emergency contacts for specific grades
- **Occupation Distribution**: Analyze parent occupations with age statistics
- **Multi-condition Joins**: Complex queries combining multiple criteria
- **Nested Relationship Queries**: Deep relationship analysis

### 5. Real-time Data Analytics
- **Gender Distribution**: Live gender ratio analysis
- **Grade Statistics**: Comprehensive grade-level analytics
- **Age Analytics**: Age distribution patterns
- **Parent Relationship Stats**: Family relationship distribution analysis

### 6. Advanced System Features
- **Interactive Demo**: Automated demonstration of all features
- **Custom JSON Queries**: Advanced search with raw JSON input
- **Data Export**: Simulated export functionality
- **Batch Operations**: Bulk data processing capabilities
- **Real-time API Display**: Live request/response monitoring

## Architecture Overview

### Enhanced Database Schema
- **Student Table**: Complete student information with indexes
- **Parent Table**: Comprehensive parent data with foreign key relationships
- **APIJSON Control Tables**: Access, Request, Function permission tables
- **Test Data**: Rich dataset with 10 students and 23 parents

### Core Framework Components
- **APIJSON 7.1.0**: Advanced zero-code CRUD capabilities
- **Spring Boot 3.2.5**: Modern web framework
- **MySQL 8.4**: Production-ready database setup
- **Bootstrap 5.1.3**: Responsive UI framework

### Key Enhanced Files

#### Backend Components
- `StudentParentDatabaseInit.java`: Complete database initialization
- `StudentParentApiTest.java`: Comprehensive API test suite (28 test cases)
- `student-parent-schema.sql`: Complete table schema with relationships
- `student-parent-data.sql`: Rich test dataset

#### Frontend Components  
- `student-parent-demo.html`: Full-featured demo interface
- `student-parent-demo.js`: Complete JavaScript implementation with 40+ functions
- Advanced UI with real-time statistics display

#### Configuration Files
- `student-parent-schema.sql`: Production-ready table structures
- `student-parent-data.sql`: Comprehensive test data
- Enhanced Access and Request configurations

### APIJSON Advanced Query Examples

#### Aggregation Queries
```json
{
  "Student[]": {
    "Student": {
      "@column": "grade, count(*):count, avg(age):avg_age",
      "@group": "grade",
      "@order": "count-"
    }
  }
}
```

#### Complex Joins
```json
{
  "Student": {
    "grade": "九年级"
  },
  "Parent[]": {
    "Parent": {
      "is_emergency_contact": 1,
      "@column": "name,phone,relationship"
    }
  }
}
```

#### Multi-condition Queries
```json
{
  "Parent[]": {
    "Parent": {
      "student_id{}": [1,2,3],
      "relationship": "父亲",
      "@order": "name+"
    }
  }
}
```

## Testing Coverage

The `StudentParentApiTest` provides comprehensive coverage:

1. **Student CRUD Tests** (7 tests)
   - Query all students
   - Query by ID and grade
   - Count operations
   - Create, update, delete operations

2. **Aggregation Query Tests** (4 tests)  
   - Grade-based grouping statistics
   - Average age calculations
   - Pagination with sorting
   - Age range filtering

3. **Parent Management Tests** (2 tests)
   - Complete CRUD operations
   - Relationship-based queries

4. **Join Query Tests** (4 tests)
   - Simple student-parent joins
   - Complex multi-condition joins
   - Emergency contact analysis
   - Statistical join operations

5. **Data Cleanup Tests** (2 tests)
   - Proper test data cleanup
   - System state restoration

## Database Configuration

### Production Setup
- **Host**: localhost:30004
- **Database**: apijson
- **Username**: root  
- **Password**: Best@2008
- **Tables**: Student, Parent, Access, Request, Function

### Enhanced Schema Features
- Foreign key relationships with cascading operations
- Comprehensive indexes for performance
- Rich test data with realistic relationships
- Full APIJSON permission configuration

## Development Guidelines

### Adding New Features
1. Update database schema in `student-parent-schema.sql`
2. Add test data in `student-parent-data.sql`
3. Create corresponding test cases in `StudentParentApiTest.java`
4. Update frontend interface in `student-parent-demo.html/js`
5. Run full test suite to ensure compatibility

### APIJSON Best Practices
- Use meaningful table and column names
- Configure proper Access permissions
- Implement Request structure validation
- Test complex queries thoroughly
- Document JSON query patterns

### Performance Considerations
- Use indexes for frequently queried columns
- Limit result sets with @count parameter
- Use @column to select only needed fields
- Implement proper pagination for large datasets

## Troubleshooting

Common issues and solutions are documented in `运行指南.md`. For development issues:

1. Check database connectivity and permissions
2. Verify APIJSON Access table configurations  
3. Test API endpoints with simple queries first
4. Use browser developer tools for frontend debugging
5. Review application logs for backend issues

When modifying this advanced system, understand that it demonstrates the full power of APIJSON's zero-code approach while maintaining enterprise-level functionality and testing coverage.

## 📊 ECharts图表可视化系统

### 新增功能概览
基于ECharts和JOLT转换的数据可视化系统，展示APIJSON数据的图表化呈现能力。

### 核心文件
- **图表页面**: `src/main/resources/static/charts.html`
- **图表脚本**: `src/main/resources/static/js/charts.js`
- **JOLT控制器**: `src/main/java/apijson/boot/JoltController.java`

### 技术栈集成
```
APIJSON (数据源) → JOLT (数据转换) → ECharts (图表渲染)
```

### 图表类型
1. **柱状图**: 学生年级分布
   - 数据源: `Student[]` 按年级分组统计
   - JOLT转换: APIJSON格式 → ECharts柱状图格式
   - 接口: `POST /jolt/grade-distribution`

2. **饼图**: 学生性别分布
   - 数据源: `Student[]` 按性别分组统计
   - JOLT转换: APIJSON格式 → ECharts饼图格式
   - 接口: `POST /jolt/gender-distribution`

3. **折线图**: 学生年龄分布
   - 数据源: `Student[]` 按年龄分组统计
   - JOLT转换: APIJSON格式 → ECharts折线图格式
   - 接口: `POST /jolt/age-distribution`

### JOLT转换示例
```json
// 输入 (APIJSON格式)
{
  "Student[]": [
    {"grade": "七年级", "count": 3},
    {"grade": "八年级", "count": 2}
  ]
}

// JOLT转换规则
[{
  "operation": "shift",
  "spec": {
    "Student[]": {
      "*": {
        "grade": "categories[]",
        "count": "values[]"
      }
    }
  }
}]

// 输出 (ECharts格式)
{
  "categories": ["七年级", "八年级"],
  "values": [3, 2]
}
```

### API接口
- **通用转换**: `POST /jolt/transform`
- **年级分布**: `POST /jolt/grade-distribution`
- **性别分布**: `POST /jolt/gender-distribution`
- **年龄分布**: `POST /jolt/age-distribution`
- **转换规格**: `GET /jolt/spec/{type}`
- **系统信息**: `GET /jolt/info`
- **测试接口**: `GET /jolt/test`

### 访问方式
- **图表页面**: http://localhost:8080/charts.html
- **主页面链接**: 点击"ECharts图表展示"按钮

### 功能特点
- 🔄 **实时数据转换**: JOLT实时转换APIJSON数据格式
- 📊 **多种图表类型**: 柱状图、饼图、折线图
- 🎨 **响应式设计**: 自适应不同屏幕尺寸
- 🔧 **可视化配置**: 图表样式和交互效果
- 📱 **移动端友好**: Bootstrap 5响应式布局
- 🚀 **高性能渲染**: ECharts硬件加速渲染
- 🔍 **数据钻取**: 点击图表元素查看详细信息

### JOLT依赖
```xml
<dependency>
    <groupId>com.bazaarvoice.jolt</groupId>
    <artifactId>jolt-core</artifactId>
    <version>0.1.1</version>
</dependency>
<dependency>
    <groupId>com.bazaarvoice.jolt</groupId>
    <artifactId>json-utils</artifactId>
    <version>0.1.1</version>
</dependency>
```

### 开发指南
1. **添加新图表类型**:
   - 在`JoltController`中添加转换方法
   - 更新`charts.js`中的数据处理逻辑
   - 在`charts.html`中添加图表容器

2. **自定义JOLT转换**:
   - 使用标准JOLT操作: `shift`, `default`, `remove`
   - 避免使用复杂的条件操作
   - 在JavaScript中进行后处理

3. **ECharts配置**:
   - 使用响应式配置适配移动端
   - 添加交互事件增强用户体验
   - 配置主题和色彩以保持一致性