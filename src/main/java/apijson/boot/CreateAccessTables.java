package apijson.boot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * 创建APIJSON配置表工具
 */
public class CreateAccessTables {
    
    public static void main(String[] args) {
        System.out.println("开始创建APIJSON配置表...");
        
        String dbUrl = "jdbc:mysql://localhost:30004/apijson?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String dbUser = "root";
        String dbPass = "Best@2008";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                 Statement stmt = conn.createStatement()) {
                
                // 创建Access表（权限配置表）
                createAccessTable(stmt);
                
                // 创建Request表（请求验证配置表）
                createRequestTable(stmt);
                
                // 插入Student和Parent的配置数据
                insertStudentParentConfigs(stmt);
                
                // 验证配置
                validateConfigs(stmt);
                
                System.out.println("✓ APIJSON配置表创建完成");
                
            }
            
        } catch (Exception e) {
            System.err.println("✗ 创建配置表失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createAccessTable(Statement stmt) throws Exception {
        System.out.println("正在创建Access表...");
        
        stmt.executeUpdate("DROP TABLE IF EXISTS Access");
        stmt.executeUpdate(
            "CREATE TABLE `Access` (" +
            "`id` bigint(20) NOT NULL AUTO_INCREMENT," +
            "`debug` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否为 DEBUG 调试数据，只允许在开发环境使用：0-否，1-是'," +
            "`schema` varchar(100) DEFAULT NULL COMMENT '数据库名/模式'," +
            "`name` varchar(50) NOT NULL COMMENT '实际表名'," +
            "`alias` varchar(20) DEFAULT NULL COMMENT '外部调用的表别名'," +
            "`get` varchar(100) NOT NULL DEFAULT '[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]' COMMENT '允许 get 的角色列表'," +
            "`head` varchar(100) NOT NULL DEFAULT '[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]' COMMENT '允许 head 的角色列表'," +
            "`gets` varchar(100) NOT NULL DEFAULT '[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]' COMMENT '允许 gets 的角色列表'," +
            "`heads` varchar(100) NOT NULL DEFAULT '[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]' COMMENT '允许 heads 的角色列表'," +
            "`post` varchar(100) NOT NULL DEFAULT '[\"OWNER\", \"ADMIN\"]' COMMENT '允许 post 的角色列表'," +
            "`put` varchar(100) NOT NULL DEFAULT '[\"OWNER\", \"ADMIN\"]' COMMENT '允许 put 的角色列表'," +
            "`delete` varchar(100) NOT NULL DEFAULT '[\"OWNER\", \"ADMIN\"]' COMMENT '允许 delete 的角色列表'," +
            "`date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
            "`detail` varchar(1000) DEFAULT NULL COMMENT '详细描述'," +
            "PRIMARY KEY (`id`)," +
            "UNIQUE KEY `alias_UNIQUE` (`alias`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限配置表（必须）'"
        );
        
        System.out.println("✓ Access表创建成功");
    }
    
    private static void createRequestTable(Statement stmt) throws Exception {
        System.out.println("正在创建Request表...");
        
        stmt.executeUpdate("DROP TABLE IF EXISTS Request");
        stmt.executeUpdate(
            "CREATE TABLE `Request` (" +
            "`id` bigint(20) NOT NULL AUTO_INCREMENT," +
            "`debug` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否为 DEBUG 调试数据：0-否，1-是'," +
            "`version` tinyint(4) NOT NULL DEFAULT '1' COMMENT '接口版本号：1,2,3...'," +
            "`method` varchar(10) DEFAULT NULL COMMENT 'HTTP请求方法'," +
            "`tag` varchar(20) NOT NULL COMMENT '标签'," +
            "`structure` text NOT NULL COMMENT 'JSON结构'," +
            "`detail` varchar(10000) DEFAULT NULL COMMENT '详细说明'," +
            "`date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
            "PRIMARY KEY (`id`)," +
            "UNIQUE KEY `tag_version_method` (`tag`,`version`,`method`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请求验证配置表'"
        );
        
        System.out.println("✓ Request表创建成功");
    }
    
    private static void insertStudentParentConfigs(Statement stmt) throws Exception {
        System.out.println("正在插入Student和Parent配置...");
        
        // 插入Access配置
        stmt.executeUpdate(
            "INSERT INTO Access (debug, name, alias, `get`, head, gets, heads, post, put, `delete`, detail) VALUES " +
            "(1, 'Student', '学生', '[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]', '[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]', '[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]', '[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]', '[\"ADMIN\"]', '[\"ADMIN\", \"OWNER\"]', '[\"ADMIN\"]', '学生信息表权限配置')," +
            "(1, 'Parent', '家长', '[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]', '[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]', '[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]', '[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]', '[\"ADMIN\"]', '[\"ADMIN\", \"OWNER\"]', '[\"ADMIN\"]', '家长信息表权限配置')"
        );
        
        // 插入Request配置  
        stmt.executeUpdate(
            "INSERT INTO Request (debug, version, method, tag, structure, detail) VALUES " +
            // Student 表相关配置
            "(1, 1, 'GET', 'Student', '{\"Student\":{\"@column\":\"id,student_no,name,gender,age,grade,class_name,phone,email,address,status,create_time,update_time\"}}', '获取学生信息')," +
            "(1, 1, 'HEAD', 'Student', '{\"Student\":{\"@column\":\"id,name\"}}', '获取学生数量')," +
            "(1, 1, 'GETS', 'Student', '{\"Student\":{\"@column\":\"id,student_no,name,gender,age,grade,class_name,phone,email,address,status,create_time,update_time\"}}', '安全获取学生信息')," +
            "(1, 1, 'HEADS', 'Student', '{\"Student\":{\"@column\":\"id,name\"}}', '安全获取学生数量')," +
            "(1, 1, 'POST', 'Student', '{\"Student\":{\"student_no!\":\"\",\"name!\":\"\",\"gender\":\"\",\"age\":\"\",\"grade\":\"\",\"class_name\":\"\",\"phone\":\"\",\"email\":\"\",\"address\":\"\"}}', '新增学生')," +
            "(1, 1, 'PUT', 'Student', '{\"Student\":{\"id!\":0,\"student_no\":\"\",\"name\":\"\",\"gender\":\"\",\"age\":\"\",\"grade\":\"\",\"class_name\":\"\",\"phone\":\"\",\"email\":\"\",\"address\":\"\",\"status\":\"\"}}', '修改学生信息')," +
            "(1, 1, 'DELETE', 'Student', '{\"Student\":{\"id!\":0}}', '删除学生')," +
            
            // Parent 表相关配置  
            "(1, 1, 'GET', 'Parent', '{\"Parent\":{\"@column\":\"id,student_id,name,relationship,gender,age,phone,email,occupation,work_address,is_emergency_contact,status,create_time,update_time\"}}', '获取家长信息')," +
            "(1, 1, 'HEAD', 'Parent', '{\"Parent\":{\"@column\":\"id,name\"}}', '获取家长数量')," +
            "(1, 1, 'GETS', 'Parent', '{\"Parent\":{\"@column\":\"id,student_id,name,relationship,gender,age,phone,email,occupation,work_address,is_emergency_contact,status,create_time,update_time\"}}', '安全获取家长信息')," +
            "(1, 1, 'HEADS', 'Parent', '{\"Parent\":{\"@column\":\"id,name\"}}', '安全获取家长数量')," +
            "(1, 1, 'POST', 'Parent', '{\"Parent\":{\"student_id!\":0,\"name!\":\"\",\"relationship!\":\"\",\"gender\":\"\",\"age\":\"\",\"phone!\":\"\",\"email\":\"\",\"occupation\":\"\",\"work_address\":\"\",\"is_emergency_contact\":\"\"}}', '新增家长')," +
            "(1, 1, 'PUT', 'Parent', '{\"Parent\":{\"id!\":0,\"student_id\":\"\",\"name\":\"\",\"relationship\":\"\",\"gender\":\"\",\"age\":\"\",\"phone\":\"\",\"email\":\"\",\"occupation\":\"\",\"work_address\":\"\",\"is_emergency_contact\":\"\",\"status\":\"\"}}', '修改家长信息')," +
            "(1, 1, 'DELETE', 'Parent', '{\"Parent\":{\"id!\":0}}', '删除家长')"
        );
        
        System.out.println("✓ Student和Parent配置插入成功");
    }
    
    private static void validateConfigs(Statement stmt) throws Exception {
        // 验证Access配置
        ResultSet rs = stmt.executeQuery("SELECT name, alias FROM Access WHERE name IN ('Student', 'Parent')");
        System.out.println("Access配置:");
        while (rs.next()) {
            System.out.println("  - " + rs.getString("name") + " (" + rs.getString("alias") + ")");
        }
        
        // 验证Request配置
        rs = stmt.executeQuery("SELECT method, tag, detail FROM Request WHERE tag IN ('Student', 'Parent') ORDER BY tag, method");
        System.out.println("Request配置:");
        while (rs.next()) {
            System.out.println("  - " + rs.getString("method") + " " + rs.getString("tag") + ": " + rs.getString("detail"));
        }
    }
}