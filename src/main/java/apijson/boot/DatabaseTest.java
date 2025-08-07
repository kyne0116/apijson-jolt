package apijson.boot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * 数据库连接和表创建测试
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("开始测试数据库连接和创建表...");
        
        String dbUrl = "jdbc:mysql://localhost:30004/apijson?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String dbUser = "root";
        String dbPass = "Best@2008";
        
        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL驱动加载成功");
            
            // 建立连接
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
                System.out.println("✓ 数据库连接成功");
                
                try (Statement stmt = conn.createStatement()) {
                    // 检查MySQL版本
                    ResultSet rs = stmt.executeQuery("SELECT VERSION() as version");
                    if (rs.next()) {
                        System.out.println("✓ MySQL版本: " + rs.getString("version"));
                    }
                    
                    // 检查Student和Parent表是否存在
                    rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'apijson' AND TABLE_NAME IN ('Student', 'Parent')");
                    int tableCount = 0;
                    while (rs.next()) {
                        System.out.println("✓ 找到表: " + rs.getString("TABLE_NAME"));
                        tableCount++;
                    }
                    
                    if (tableCount < 2) {
                        System.out.println("! 正在创建Student和Parent表...");
                        createTables(stmt);
                        System.out.println("✓ 表创建完成");
                    }
                    
                    // 验证表创建成功
                    rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Student");
                    if (rs.next()) {
                        System.out.println("✓ Student表记录数: " + rs.getInt("count"));
                    }
                    
                    rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Parent");
                    if (rs.next()) {
                        System.out.println("✓ Parent表记录数: " + rs.getInt("count"));
                    }
                }
            }
            
            System.out.println("✓ 数据库测试完成");
            
        } catch (Exception e) {
            System.err.println("✗ 数据库操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTables(Statement stmt) throws Exception {
        // 创建Student表
        stmt.executeUpdate("DROP TABLE IF EXISTS Student");
        stmt.executeUpdate(
            "CREATE TABLE Student (" +
            "id BIGINT NOT NULL AUTO_INCREMENT COMMENT '学生ID，主键'," +
            "student_no VARCHAR(20) NOT NULL COMMENT '学号，唯一'," +
            "name VARCHAR(50) NOT NULL COMMENT '学生姓名'," +
            "gender TINYINT DEFAULT 0 COMMENT '性别：0-男，1-女'," +
            "age INT DEFAULT 0 COMMENT '年龄'," +
            "grade VARCHAR(20) COMMENT '年级'," +
            "class_name VARCHAR(30) COMMENT '班级'," +
            "phone VARCHAR(15) COMMENT '学生手机号'," +
            "email VARCHAR(100) COMMENT '邮箱'," +
            "address TEXT COMMENT '家庭住址'," +
            "status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用'," +
            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
            "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
            "PRIMARY KEY (id)," +
            "UNIQUE KEY uk_student_no (student_no)," +
            "KEY idx_name (name)," +
            "KEY idx_grade_class (grade, class_name)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表'"
        );
        
        // 创建Parent表
        stmt.executeUpdate("DROP TABLE IF EXISTS Parent");
        stmt.executeUpdate(
            "CREATE TABLE Parent (" +
            "id BIGINT NOT NULL AUTO_INCREMENT COMMENT '家长ID，主键'," +
            "student_id BIGINT NOT NULL COMMENT '学生ID，关联Student表'," +
            "name VARCHAR(50) NOT NULL COMMENT '家长姓名'," +
            "relationship VARCHAR(10) NOT NULL COMMENT '关系：父亲、母亲、爷爷、奶奶等'," +
            "gender TINYINT DEFAULT 0 COMMENT '性别：0-男，1-女'," +
            "age INT DEFAULT 0 COMMENT '年龄'," +
            "phone VARCHAR(15) NOT NULL COMMENT '联系电话'," +
            "email VARCHAR(100) COMMENT '邮箱'," +
            "occupation VARCHAR(50) COMMENT '职业'," +
            "work_address TEXT COMMENT '工作地址'," +
            "is_emergency_contact TINYINT DEFAULT 0 COMMENT '是否紧急联系人：0-否，1-是'," +
            "status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用'," +
            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
            "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
            "PRIMARY KEY (id)," +
            "KEY idx_student_id (student_id)," +
            "KEY idx_phone (phone)," +
            "KEY idx_name (name)," +
            "CONSTRAINT fk_parent_student FOREIGN KEY (student_id) REFERENCES Student (id) ON DELETE CASCADE" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长信息表'"
        );
        
        // 插入测试数据
        insertTestData(stmt);
    }
    
    private static void insertTestData(Statement stmt) throws Exception {
        // 插入学生数据
        stmt.executeUpdate("INSERT INTO Student (student_no, name, gender, age, grade, class_name, phone, email, address) VALUES " +
            "('S2024001', '张小明', 0, 15, '九年级', '九年级(1)班', '13800001001', 'zhangxm@student.com', '北京市朝阳区某某小区1号楼')," +
            "('S2024002', '李小红', 1, 14, '八年级', '八年级(2)班', '13800001002', 'lixh@student.com', '北京市海淀区某某花园2号楼')," +
            "('S2024003', '王小强', 0, 16, '九年级', '九年级(2)班', '13800001003', 'wangxq@student.com', '北京市西城区某某路88号')," +
            "('S2024004', '刘小美', 1, 13, '七年级', '七年级(1)班', '13800001004', 'liuxm@student.com', '北京市东城区某某街道99号')," +
            "('S2024005', '陈小华', 0, 15, '九年级', '九年级(1)班', '13800001005', 'chenxh@student.com', '北京市丰台区某某社区5号楼')"
        );
        
        // 插入家长数据
        stmt.executeUpdate("INSERT INTO Parent (student_id, name, relationship, gender, age, phone, email, occupation, work_address, is_emergency_contact) VALUES " +
            "(1, '张大明', '父亲', 0, 42, '13900001001', 'zhangdm@parent.com', '软件工程师', '北京市朝阳区中关村软件园', 1)," +
            "(1, '李淑华', '母亲', 1, 38, '13900001002', 'lish@parent.com', '会计师', '北京市朝阳区金融街', 0)," +
            "(2, '李大强', '父亲', 0, 45, '13900001003', 'lidq@parent.com', '医生', '北京市海淀区某某医院', 1)," +
            "(2, '王秀英', '母亲', 1, 41, '13900001004', 'wangxy@parent.com', '教师', '北京市海淀区某某中学', 0)," +
            "(3, '王建国', '父亲', 0, 48, '13900001005', 'wangjg@parent.com', '企业家', '北京市西城区某某大厦', 1)," +
            "(4, '刘志刚', '父亲', 0, 40, '13900001006', 'liuzg@parent.com', '司机', '北京市东城区', 0)," +
            "(4, '赵美丽', '母亲', 1, 37, '13900001007', 'zhaoml@parent.com', '护士', '北京市东城区某某医院', 1)," +
            "(5, '陈国富', '父亲', 0, 43, '13900001008', 'chengf@parent.com', '销售经理', '北京市丰台区某某公司', 1)," +
            "(5, '孙丽娟', '母亲', 1, 39, '13900001009', 'sunlj@parent.com', '设计师', '北京市丰台区某某设计公司', 0)"
        );
    }
}