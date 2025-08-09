package apijson.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 数据库初始化器 - 在应用启动时自动创建表和数据
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("📊 开始初始化数据库...");
        
        try {
            // 创建学生表
            createStudentTable();
            
            // 创建家长表
            createParentTable();
            
            // 创建APIJSON配置表
            createApijsonTables();
            
            // 插入测试数据
            insertTestData();
            
            System.out.println("✅ 数据库初始化完成！");
            System.out.println("📋 学生数据管理页面: http://localhost:8080/student-parent-demo.html");
            System.out.println("📊 ECharts图表页面: http://localhost:8080/charts.html");
            
        } catch (Exception e) {
            System.err.println("❌ 数据库初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createStudentTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Student (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                student_no VARCHAR(20) UNIQUE,
                name VARCHAR(50) NOT NULL,
                gender TINYINT DEFAULT 0,
                age INT DEFAULT 0,
                grade VARCHAR(20),
                class_name VARCHAR(50),
                phone VARCHAR(20),
                email VARCHAR(100),
                address TEXT,
                status TINYINT DEFAULT 1,
                create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
            """;
        jdbcTemplate.execute(sql);
        System.out.println("✓ Student表创建成功");
    }
    
    private void createParentTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Parent (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                student_id BIGINT NOT NULL,
                name VARCHAR(50) NOT NULL,
                relationship VARCHAR(10) NOT NULL,
                gender TINYINT DEFAULT 0,
                age INT,
                phone VARCHAR(20) NOT NULL,
                email VARCHAR(100),
                occupation VARCHAR(100),
                work_address TEXT,
                is_emergency_contact TINYINT DEFAULT 0,
                create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
            """;
        jdbcTemplate.execute(sql);
        System.out.println("✓ Parent表创建成功");
    }
    
    private void createApijsonTables() {
        // Access权限表
        String accessSql = """
            CREATE TABLE IF NOT EXISTS Access (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                debug TINYINT DEFAULT 0 NOT NULL,
                name VARCHAR(50) NOT NULL,
                alias VARCHAR(20),
                `get` TEXT,
                head TEXT,
                gets TEXT,
                heads TEXT,
                post TEXT,
                put TEXT,
                `delete` TEXT,
                date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                detail TEXT
            )
            """;
        jdbcTemplate.execute(accessSql);
        
        // Request验证表
        String requestSql = """
            CREATE TABLE IF NOT EXISTS Request (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                debug TINYINT DEFAULT 0 NOT NULL,
                version TINYINT DEFAULT 1 NOT NULL,
                method VARCHAR(10) DEFAULT 'GETS' NOT NULL,
                tag VARCHAR(20) NOT NULL,
                structure TEXT NOT NULL,
                detail TEXT,
                date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        jdbcTemplate.execute(requestSql);
        System.out.println("✓ APIJSON配置表创建成功");
    }
    
    private void insertTestData() {
        // 检查是否已有学生数据
        Integer studentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Student", Integer.class);
        boolean hasStudentData = studentCount > 0;
        
        if (hasStudentData) {
            System.out.println("⚠️ 数据已存在，跳过数据插入");
        }
        
        // 插入学生测试数据（仅当没有数据时）
        if (!hasStudentData) {
            String[] studentSqls = {
                "INSERT INTO Student (student_no, name, gender, age, grade, class_name, phone, email, address, status) VALUES ('S2024001', '张小明', 0, 15, '九年级', '九年级(1)班', '13800001001', 'zhangxm@email.com', '北京市朝阳区建国路1号', 1)",
                "INSERT INTO Student (student_no, name, gender, age, grade, class_name, phone, email, address, status) VALUES ('S2024002', '李小红', 1, 14, '八年级', '八年级(2)班', '13800001002', 'lixh@email.com', '上海市浦东新区陆家嘴路2号', 1)",
                "INSERT INTO Student (student_no, name, gender, age, grade, class_name, phone, email, address, status) VALUES ('S2024003', '王小强', 0, 16, '九年级', '九年级(2)班', '13800001003', 'wangxq@email.com', '广州市天河区珠江新城3号', 1)",
                "INSERT INTO Student (student_no, name, gender, age, grade, class_name, phone, email, address, status) VALUES ('S2024004', '刘小美', 1, 13, '七年级', '七年级(1)班', '13800001004', 'liuxm@email.com', '深圳市南山区科技园4号', 1)",
                "INSERT INTO Student (student_no, name, gender, age, grade, class_name, phone, email, address, status) VALUES ('S2024005', '陈小华', 0, 15, '九年级', '九年级(1)班', '13800001005', 'chenxh@email.com', '杭州市西湖区文三路5号', 1)"
            };
            
            for (String sql : studentSqls) {
                jdbcTemplate.execute(sql);
            }
            System.out.println("✓ 学生测试数据插入成功");
        }
        
        // 插入家长测试数据（仅当没有数据时）
        if (!hasStudentData) {
            String[] parentSqls = {
                "INSERT INTO Parent (student_id, name, relationship, gender, age, phone, email, occupation, work_address, is_emergency_contact) VALUES (1, '张大强', '父亲', 0, 42, '13900001001', 'zhangdq@email.com', '软件工程师', '北京市海淀区中关村大街10号', 1)",
                "INSERT INTO Parent (student_id, name, relationship, gender, age, phone, email, occupation, work_address, is_emergency_contact) VALUES (1, '王美丽', '母亲', 1, 40, '13900001002', 'wangml@email.com', '会计师', '北京市朝阳区国贸大厦20层', 0)",
                "INSERT INTO Parent (student_id, name, relationship, gender, age, phone, email, occupation, work_address, is_emergency_contact) VALUES (2, '李建国', '父亲', 0, 45, '13900001003', 'lijg@email.com', '医生', '上海市黄浦区人民医院', 1)"
            };
            
            for (String sql : parentSqls) {
                jdbcTemplate.execute(sql);
            }
            System.out.println("✓ 家长测试数据插入成功");
        }
        
        // 强制重新设置APIJSON权限配置 - 确保UNKNOWN用户能访问
        // 先删除现有权限配置
        jdbcTemplate.execute("DELETE FROM Access WHERE name IN ('Student', 'Parent')");
        
        // 重新插入正确的权限配置
        String[] accessSqls = {
            "INSERT INTO Access (name, `get`, head, gets, heads, post, put, `delete`, detail) VALUES ('Student', '[\"UNKNOWN\", \"LOGIN\", \"ADMIN\"]', '[\"UNKNOWN\", \"LOGIN\", \"ADMIN\"]', '[\"UNKNOWN\", \"LOGIN\", \"ADMIN\"]', '[\"UNKNOWN\", \"LOGIN\", \"ADMIN\"]', '[\"ADMIN\"]', '[\"ADMIN\"]', '[\"ADMIN\"]', '学生表的访问权限配置')",
            "INSERT INTO Access (name, `get`, head, gets, heads, post, put, `delete`, detail) VALUES ('Parent', '[\"UNKNOWN\", \"LOGIN\", \"ADMIN\"]', '[\"UNKNOWN\", \"LOGIN\", \"ADMIN\"]', '[\"UNKNOWN\", \"LOGIN\", \"ADMIN\"]', '[\"UNKNOWN\", \"LOGIN\", \"ADMIN\"]', '[\"ADMIN\"]', '[\"ADMIN\"]', '[\"ADMIN\"]', '家长表的访问权限配置')"
        };
        
        for (String sql : accessSqls) {
            jdbcTemplate.execute(sql);
        }
        System.out.println("✓ APIJSON权限配置强制更新完成");
    }
}